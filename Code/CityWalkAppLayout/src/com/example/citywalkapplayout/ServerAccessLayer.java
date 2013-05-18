package com.example.citywalkapplayout;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Array;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import android.widget.TextView;

public class ServerAccessLayer {

	public Tour getTour(String tourId, JSONObject tourDatabaseObject) {

		// List<String> categories = getCategoriesList(tourId);
		List<String> categories = new ArrayList<String>();
		List<Notes> noteList = getNotesList(tourId);
		List<tourLocations> tourLocations = getTourLocations(tourId);
		// List<String> categories = null;
		// List<Notes> noteList = null;
		// List<tourLocations> tourLocations = null;
		// JSONObject tourDatabaseObject = getTourFromDatabase(tourId);
		int id = 0;
		String dateAdded = null;
		String comments = null;
		String title = null;
		String description = null;
		int duration = 0;
		int views = 0;
		double rating = 0;
		String imageUrl = null;
		String noteOrder = null;
		String categoriesNotSplit = null;
		try {
			id = Integer.parseInt(tourDatabaseObject.getString("id"));
			dateAdded = tourDatabaseObject.getString("dateAdded");
			comments = tourDatabaseObject.getString("comments");
			title = tourDatabaseObject.getString("title");
			duration = Integer.parseInt(tourDatabaseObject
					.getString("duration"));
			views = Integer.parseInt(tourDatabaseObject.getString("views"));
			rating = Double.parseDouble(tourDatabaseObject.getString("rating"));
			description = tourDatabaseObject.getString("description");
			imageUrl = tourDatabaseObject.getString("imageUrl");
			noteOrder = tourDatabaseObject.getString("noteOrder");
			categoriesNotSplit = tourDatabaseObject.getString("categories");
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String categoriesArray[] = categoriesNotSplit.split(";");
		int categoriesLength = categoriesNotSplit.split(";").length;

		for (int i = 0; i < categoriesLength; i++) {
			categories.add(categoriesArray[i]);
		}

		Tour tour = new Tour();

		tour.setdateAdded(dateAdded);
		tour.setDuration(duration);
		tour.setId(id);
		tour.setNotes(noteList);
		tour.setTourLocations(tourLocations);
		tour.setRating(rating);
		tour.setTitle(title);
		tour.setViews(views);
		tour.setDescription(description);
		tour.setCategories(categories);
		tour.setImageUrl(imageUrl);
		tour.setNoteOrder(noteOrder);

		return tour;
	}

	public List<String> getCategoriesList(String tourId) {

		String notes;
		String query = "select * from tourCategories where tourId=" + tourId;
		JSONArray finalResult = sendQuery(query);

		List<String> categoryList = new ArrayList<String>();
		for (int i = 0; i < finalResult.length(); i++) {
			JSONObject jsonNote;
			String category = null;
			try {
				jsonNote = finalResult.getJSONObject(i);
				category = jsonNote.getString("tourCategory");
			} catch (JSONException e) {
				e.printStackTrace();
			}
			categoryList.add(category);
		}

		return categoryList;
	}

	public List<Notes> getNotesList(String tourId) {

		String notes;
		String query = "select * from notes where tourId=" + tourId;
		// tour = sendQuery(query);

		// JSONTokener tokener = new JSONTokener(tour);
		JSONArray finalResult = sendQuery(query);

		List<Notes> noteList = new ArrayList<Notes>();
		for (int i = 0; i < finalResult.length(); i++) {
			try {
				JSONObject jsonNote = finalResult.getJSONObject(i);
				if (jsonNote.getString("noteType").equals("POI")) {
					String title = jsonNote.getString("noteTitle");
					String description = jsonNote.getString("notesDescription");
					String location = jsonNote.getString("location");
					String link = jsonNote.getString("link");
					String imageUrl = jsonNote.getString("noteImageUrl");
					int id = jsonNote.getInt("noteId");

					POI poi = new POI();
					poi.setTitle(title);
					poi.setDescription(description);
					poi.setLocation(location);
					poi.setLink(link);
					poi.setImageUrl(imageUrl);
					poi.setId(id);

					noteList.add(poi);
				} else if (jsonNote.getString("noteType").equals("TourNotes")) {
					String title = jsonNote.getString("noteTitle");
					String description = jsonNote.getString("notesDescription");
					String location = jsonNote.getString("location");
					String imageUrl = jsonNote.getString("noteImageUrl");

					TourNotes tourNote = new TourNotes();
					tourNote.setTitle(title);
					tourNote.setDescription(description);
					tourNote.setLocation(location);
					tourNote.setImageUrl(imageUrl);
					int id = jsonNote.getInt("noteId");
					tourNote.setId(id);

					noteList.add(tourNote);
				} else {

				}
				System.out.println(finalResult.getString(i));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		return noteList;

	}

	public List<tourLocations> getTourLocations(String tourId) {

		String notes;
		String query = "select * from tourLocationPoints where tourId="
				+ tourId;
		JSONArray finalResult = sendQuery(query);

		List<tourLocations> locationPoints = new ArrayList<tourLocations>();
		for (int i = 0; i < finalResult.length(); i++) {
			try {
				JSONObject jsonNote = finalResult.getJSONObject(i);
				String location = jsonNote.getString("location");
				String locationIndexString = jsonNote
						.getString("locationIndex");
				int locationIndex = Integer.parseInt(locationIndexString);
				tourLocations locations = new tourLocations();
				locations.setLcation(location);
				locations.setLocationIndex(locationIndex);
				locationPoints.add(locations);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		return locationPoints;

	}

	public JSONObject getTourFromDatabase(String tourId) {
		String tour;
		String query = "select * from tour where id=" + tourId;

		// tour = sendQuery(query);

		// JSONTokener tokener = new JSONTokener(tour);
		JSONArray finalResult = sendQuery(query);
		JSONObject jsonNote = null;
		try {
			// finalResult = new JSONArray(tokener);
			jsonNote = finalResult.getJSONObject(0);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return jsonNote;

	}

	public JSONArray sendQuery(String query) {

		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(
				"http://192.168.2.11/cityWalkWebServer.php");
		HttpResponse response = null;
		String responseString = null;
		String url = "jdbc:mysql://cbech.com:3306/";
		String dbName = "CityWalkdb";
		String driver = "com.mysql.jdbc.Driver";
		String userName = "AMN";
		String password = "MYsqlAMN";
		JSONArray elements = new JSONArray();
		try {
			Class.forName(driver).newInstance();
			Connection conn = DriverManager.getConnection(url + dbName,
					userName, password);
			Statement st = conn.createStatement();
			ResultSet res = st.executeQuery(query);
			int i = 1;
			elements = new JSONArray();
			while (res.next()) {

				int attributesNumber = res.getMetaData().getColumnCount();

				JSONObject json = new JSONObject();
				for (int j = 1; j <= attributesNumber; j++) {
					String columnName = res.getMetaData().getColumnName(j)
							.toString();
					String columnValue = res.getObject(j).toString();
					json.put(columnName, columnValue);

				}
				elements.put(json);
			}

			System.out.println("ads");

		} catch (Exception E) {
			System.out.println("error");
		}

		/*
		 * try { // Add your data List<NameValuePair> nameValuePairs = new
		 * ArrayList<NameValuePair>(2); nameValuePairs.add(new
		 * BasicNameValuePair("fname", query)); httppost.setEntity(new
		 * UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
		 * 
		 * // Execute HTTP Post Request response = httpclient.execute(httppost);
		 * //view.setText(response.toString());
		 * System.out.println("response is: " + response.toString()); HttpEntity
		 * entity = response.getEntity(); responseString =
		 * EntityUtils.toString(entity); } catch (ClientProtocolException e) {
		 * System.out.println("in catch client protocol exception"); // TODO
		 * Auto-generated catch block } catch (IOException e) { // TODO
		 * Auto-generated catch block
		 * System.out.println("in catch IOException"); }
		 */

		return elements;

	}

	public List<Tour> getSortedTour(String orderAttribute) {

		String tour;

		String query = "select * from tour ORDER BY " + orderAttribute
				+ " desc";

		// tour = sendQuery(query);

		// JSONTokener tokener = new JSONTokener(tour);
		JSONArray finalResult = sendQuery(query);
		JSONObject jsonNote = null;
		try {
			// finalResult = new JSONArray(tokener);
			jsonNote = finalResult.getJSONObject(0);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<Tour> tourList = new ArrayList<Tour>();
		// For every tour in the list
		for (int i = 0; i < finalResult.length(); i++) {
			List<Notes> noteList = new ArrayList<Notes>();
			List<tourLocations> tourLocations = new ArrayList<tourLocations>();
			List<String> categories = new ArrayList<String>();
			Tour tour2 = new Tour();
			try {
				JSONObject jsonNote2 = finalResult.getJSONObject(i);
				String tourId = jsonNote2.getString("id");
				// String query2 = "select * from tour";
				String query2 = "SELECT * FROM citywalkdb.tour as tour, citywalkdb.tourlocationpoints as locationPoints, citywalkdb.notes as notes WHERE tour.id = "
						+ tourId
						+ " and locationPoints.tourId = "
						+ tourId
						+ " and notes.tourId = " + tourId;
				JSONArray finalResult2 = sendQuery(query2);
				JSONObject jsonNote3 = null;
				// For every row in the result
				for (int j = 0; j < finalResult2.length(); j++) {
					// finalResult = new JSONArray(tokener);
					jsonNote3 = finalResult2.getJSONObject(j);
					System.out.println("asd");
					if (jsonNote3.getString("noteType").equals("POI")) {
						String title = jsonNote3.getString("noteTitle");
						String description = jsonNote3
								.getString("notesDescription");
						String location = jsonNote3.getString("location");
						String link = jsonNote3.getString("link");
						String imageUrl = jsonNote3.getString("noteImageUrl");
						int id = jsonNote3.getInt("noteId");

						POI poi = new POI();
						poi.setTitle(title);
						poi.setDescription(description);
						poi.setLocation(location);
						poi.setLink(link);
						poi.setImageUrl(imageUrl);
						poi.setId(id);

						noteList.add(poi);
					} else if (jsonNote3.getString("noteType").equals(
							"TourNotes")) {
						String title = jsonNote3.getString("noteTitle");
						String description = jsonNote3
								.getString("notesDescription");
						String location = jsonNote3.getString("location");
						String imageUrl = jsonNote3.getString("noteImageUrl");

						TourNotes tourNote = new TourNotes();
						tourNote.setTitle(title);
						tourNote.setDescription(description);
						tourNote.setLocation(location);
						tourNote.setImageUrl(imageUrl);
						int id = jsonNote3.getInt("noteId");
						tourNote.setId(id);

						noteList.add(tourNote);
					}

					String location = jsonNote3.getString("location");
					String locationIndexString = jsonNote3
							.getString("locationIndex");
					int locationIndex = Integer.parseInt(locationIndexString);
					tourLocations locations = new tourLocations();
					locations.setLcation(location);
					locations.setLocationIndex(locationIndex);
					tourLocations.add(locations);

					int id = 0;
					String dateAdded = null;
					String comments = null;
					String title = null;
					String description = null;
					int duration = 0;
					int views = 0;
					double rating = 0;
					String imageUrl = null;
					String noteOrder = null;
					String categoriesNotSplit = null;
					try {
						id = Integer.parseInt(jsonNote3.getString("id"));
						dateAdded = jsonNote3.getString("dateAdded");
						comments = jsonNote3.getString("comments");
						title = jsonNote3.getString("title");
						duration = Integer.parseInt(jsonNote3
								.getString("duration"));
						views = Integer.parseInt(jsonNote3.getString("views"));
						rating = Double.parseDouble(jsonNote3
								.getString("rating"));
						description = jsonNote3.getString("description");
						imageUrl = jsonNote3.getString("imageUrl");
						noteOrder = jsonNote3.getString("noteOrder");
						categoriesNotSplit = jsonNote3.getString("categories");
					} catch (NumberFormatException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					String categoriesArray[] = categoriesNotSplit.split(";");
					int categoriesLength = categoriesNotSplit.split(";").length;

					for (int k = 0; k < categoriesLength; k++) {
						categories.add(categoriesArray[k]);
					}

					tour2.setdateAdded(dateAdded);
					tour2.setDuration(duration);
					tour2.setId(id);
					tour2.setNotes(noteList);
					tour2.setTourLocations(tourLocations);
					tour2.setRating(rating);
					tour2.setTitle(title);
					tour2.setViews(views);
					tour2.setDescription(description);
					tour2.setCategories(categories);
					tour2.setImageUrl(imageUrl);
					tour2.setNoteOrder(noteOrder);
				}

				Set<tourLocations> s = new LinkedHashSet<tourLocations>(
						tourLocations);
				Set<Notes> s2 = new LinkedHashSet<Notes>(noteList);
				List<Notes> noteList2 = new ArrayList<Notes>(s2);
				List<tourLocations> tourLocations2 = new ArrayList<tourLocations>(
						s);
				tour2.setNotes(noteList2);
				tour2.setTourLocations(tourLocations2);

			} catch (JSONException e) {
				e.printStackTrace();
			}

			tourList.add(tour2);
			System.out.println("sd");

		}
		System.out.println("asd");
		return tourList;
		// return null;
	}
}
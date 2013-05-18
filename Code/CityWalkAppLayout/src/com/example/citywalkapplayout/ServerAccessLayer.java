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
import java.util.List;

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
	 
	public Tour getTour(String tourId){
		List<String> categories = getCategoriesList(tourId);
		List<Notes> noteList = getNotesList(tourId);
		List<tourLocations> tourLocations = getTourLocations(tourId);
		JSONObject tourDatabaseObject = getTourFromDatabase(tourId);
		int id = 0;
		String dateAdded = null;
		String comments = null;
		String title = null;
		String description = null;
		int duration = 0;
		int views = 0;
		double rating = 0;
		String imageUrl = null;
		try {
			id = Integer.parseInt(tourDatabaseObject.getString("id"));
			dateAdded = tourDatabaseObject.getString("dateAdded");
			comments = tourDatabaseObject.getString("comments");
			title = tourDatabaseObject.getString("title");
			duration = Integer.parseInt(tourDatabaseObject.getString("duration"));
			views = Integer.parseInt(tourDatabaseObject.getString("views"));
			rating = Double.parseDouble(tourDatabaseObject.getString("rating"));
			description = tourDatabaseObject.getString("description");
			imageUrl = tourDatabaseObject.getString("imageUrl");
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
		
		return tour;
	}
	
	public List<String> getCategoriesList(String tourId){
		
		String notes;
		String query = "select * from tourCategories where tourId=" + tourId;
		JSONArray finalResult = sendQuery(query);
		
		List<String> categoryList = new ArrayList<String>();
		for (int i=0; i<finalResult.length(); i++) {
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
	
	
	
	
	public List<Notes> getNotesList(String tourId){
		
		String notes;
		String query = "select * from notes where tourId=" + tourId;
		//tour = sendQuery(query);
		
				//JSONTokener tokener = new JSONTokener(tour);
				JSONArray finalResult = sendQuery(query);
		
		List<Notes> noteList = new ArrayList<Notes>();
		for (int i=0; i<finalResult.length(); i++) {
		    try {
		    	JSONObject jsonNote = finalResult.getJSONObject(i);
		    	if(jsonNote.getString("noteType").equals("POI")){
		    		String title = jsonNote.getString("title");
		    		String description = jsonNote.getString("description");
		    		String location = jsonNote.getString("location");
		    		String link = jsonNote.getString("link");
		    		String imageUrl = jsonNote.getString("imageUrl");
		    		
		    		POI poi = new POI();
		    		poi.setTitle(title);
		    		poi.setDescription(description);
		    		poi.setLocation(location);
		    		poi.setLink(link);
		    		poi.setImageUrl(imageUrl);
		    		
		    		noteList.add(poi);
		    	}else if(jsonNote.getString("noteType").equals("TourNotes")){
		    		String title = jsonNote.getString("title");
		    		String description = jsonNote.getString("description");
		    		String location = jsonNote.getString("location");
		    		String imageUrl = jsonNote.getString("imageUrl");
		    		
		    		TourNotes tourNote = new TourNotes();
		    		tourNote.setTitle(title);
		    		tourNote.setDescription(description);
		    		tourNote.setLocation(location);
		    		tourNote.setImageUrl(imageUrl);
		    		
		    		noteList.add(tourNote);
		    	}else{
		    		
		    	}
				System.out.println(finalResult.getString(i));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		return noteList;
		
	}
	

public List<tourLocations> getTourLocations(String tourId){
		
		String notes;
		String query = "select * from tourLocationPoints where tourId=" + tourId;
		JSONArray finalResult = sendQuery(query);
		
		List<tourLocations> locationPoints = new ArrayList<tourLocations>();
		for (int i=0; i<finalResult.length(); i++) {
		    try {
		    	JSONObject jsonNote = finalResult.getJSONObject(i);
		    	String location = jsonNote.getString("location");
		    	String locationIndexString = jsonNote.getString("locationIndex");
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
	
	
	public JSONObject getTourFromDatabase(String tourId){
		
		String tour;
		
		String query = "select * from tour where id=" + tourId;
		
		//tour = sendQuery(query);
		
				//JSONTokener tokener = new JSONTokener(tour);
				JSONArray finalResult = sendQuery(query);
				JSONObject jsonNote = null;
				try {
					//finalResult = new JSONArray(tokener);
					jsonNote = finalResult.getJSONObject(0);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		
		return jsonNote;
		
	}
	
	public JSONArray sendQuery(String query){
		
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost("http://192.168.2.11/cityWalkWebServer.php");
		HttpResponse response = null;
		String responseString =  null;
		String url = "jdbc:mysql://cbech.com:3306/";
        String dbName = "CityWalkdb";
        String driver = "com.mysql.jdbc.Driver";
        String userName = "AMN";
        String password = "MYsqlAMN";
        JSONArray elements = new JSONArray();
        try {
        Class.forName(driver).newInstance();
        Connection conn = DriverManager.getConnection(url+dbName,userName,password);
        Statement st = conn.createStatement();
        ResultSet res = st.executeQuery(query); 
        int i = 1;
        elements = new JSONArray();
        while (res.next()) {
        	
        	int attributesNumber = res.getMetaData().getColumnCount();
        	
        	JSONObject json = new JSONObject();
        	for (int j = 1; j <= attributesNumber; j++){
        		String columnName = res.getMetaData().getColumnName(j).toString();
        		String columnValue = res.getObject(j).toString();
        		json.put(columnName, columnValue);
        		
        		
        	}
        	elements.put(json);
         }
		
        System.out.println("ads");
		
        }
        catch(Exception E){
        	System.out.println("error");
        }
		
		
		
		/*
		try {
		    // Add your data
		    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		    nameValuePairs.add(new BasicNameValuePair("fname", query));
		    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
		    
		    // Execute HTTP Post Request
		    response = httpclient.execute(httppost);
		    //view.setText(response.toString());	
		    System.out.println("response is: " + response.toString());
		    HttpEntity entity = response.getEntity();
		    responseString = EntityUtils.toString(entity);
		} catch (ClientProtocolException e) {
			System.out.println("in catch client protocol exception");
		    // TODO Auto-generated catch block
		} catch (IOException e) {
		    // TODO Auto-generated catch block
			System.out.println("in catch IOException");
		}
		*/
        
        return elements;
		
		
	}
	
	public List<Tour> getSortedTour(String orderAttribute){
		
		String tour;
		
		String query = "select * from tour ORDER BY " + orderAttribute;
		
		//tour = sendQuery(query);
		
		//JSONTokener tokener = new JSONTokener(tour);
		JSONArray finalResult = sendQuery(query);
		JSONObject jsonNote = null;
		try {
			//finalResult = new JSONArray(tokener);
			jsonNote = finalResult.getJSONObject(0);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<Tour> tourList = new ArrayList<Tour>();
		List<tourLocations> locationPoints = new ArrayList<tourLocations>();
		for (int i=0; i<finalResult.length(); i++) {
		    try {
		    	JSONObject jsonNote2 = finalResult.getJSONObject(i);
		    	String tourId = jsonNote2.getString("id");
		    	Tour tour1 = new Tour();
		    	tour1 = getTour(tourId);
		    	tourList.add(tour1);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		return tourList;
		//return null;
	}
}

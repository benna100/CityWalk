package com.example.citywalkapplayout;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
		
		
		
		List<Notes> noteList = getNotesList(tourId);
		JSONObject tourDatabaseObject = getTourFromDatabase(tourId);
		int id = 0;
		String dateAdded = null;
		String comments = null;
		String title = null;
		int duration = 0;
		int views = 0;
		double rating = 0;
		try {
			id = Integer.parseInt(tourDatabaseObject.getString("id"));
			dateAdded = tourDatabaseObject.getString("dateAdded");
			comments = tourDatabaseObject.getString("comments");
			title = tourDatabaseObject.getString("title");
			duration = Integer.parseInt(tourDatabaseObject.getString("duration"));
			views = Integer.parseInt(tourDatabaseObject.getString("views"));
			rating = Double.parseDouble(tourDatabaseObject.getString("views"));
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
		tour.setRating(rating);
		tour.setTitle(title);
		tour.setViews(views);
		
		return tour;
	}
	
	public List<Notes> getNotesList(String tourId){
		
		String notes;
		
		String query = "select * from notes where tourId=" + tourId;
		
		notes = sendQuery(query);
		
		JSONTokener tokener = new JSONTokener(notes);
		JSONArray finalResult = null;
		try {
			finalResult = new JSONArray(tokener);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		List<Notes> noteList = new ArrayList<Notes>();
		for (int i=0; i<finalResult.length(); i++) {
		    try {
		    	
		    	
		    	JSONObject jsonNote = finalResult.getJSONObject(i);
		    	if(jsonNote.getString("noteType").equals("POI")){
		    		String title = jsonNote.getString("title");
		    		String description = jsonNote.getString("description");
		    		String location = jsonNote.getString("location");
		    		String link = jsonNote.getString("link");
		    		
		    		POI poi = new POI();
		    		poi.setTitle(title);
		    		poi.setDescription(description);
		    		poi.setLocation(location);
		    		poi.setLink(link);
		    		
		    		noteList.add(poi);
		    	}else if(jsonNote.getString("noteType").equals("TourNotes")){
		    		String title = jsonNote.getString("title");
		    		String description = jsonNote.getString("description");
		    		String location = jsonNote.getString("location");
		    		
		    		TourNotes tourNote = new TourNotes();
		    		tourNote.setTitle(title);
		    		tourNote.setDescription(description);
		    		tourNote.setLocation(location);
		    		
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
	
	public JSONObject getTourFromDatabase(String tourId){
		
		String tour;
		
		String query = "select * from tour where id=" + tourId;
		
		tour = sendQuery(query);
		
		JSONTokener tokener = new JSONTokener(tour);
		JSONArray finalResult = null;
		JSONObject jsonNote = null;
		try {
			finalResult = new JSONArray(tokener);
			jsonNote = finalResult.getJSONObject(0);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return jsonNote;
		
	}
	
	public String sendQuery(String query){
		
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost("http://10.16.161.57/cityWalkWebServer.php/");
		HttpResponse response = null;
		String responseString =  null;
		try {
			
			System.out.println("In try");
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
		
		return responseString;
		
	}
}

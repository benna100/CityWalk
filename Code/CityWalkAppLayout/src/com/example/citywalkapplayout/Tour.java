package com.example.citywalkapplayout;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

public class Tour {
	String comments;
	/*LocalDate dateAdded;*/
	String dateAdded;
	List<Notes> notesList;
	List<tourLocations> tourLocations;
	int id;
	String title;
	int duration;
	int views;
	double rating;
	int img;
	int distance;
	
	/*
	Tour(List<Notes> notes, LocalDate date, int idInput){
		dateAdded = date;
		notesList = notes;
		id = idInput;
	}
	*/
	
	/*
	public void setDateAdded(){
		LocalDate dateToday = new LocalDate();
		this.dateAdded = LocalDate.now();
	}
	*/
	public int getDistance(){
		return distance;
	}
	
	public void setDistance(int i){
		distance = i;
	}
	
	public int getImg(){
		return img;
	}
	
	public void setImg(int i){
		img = i;
	}
	
	public void setId(int id){
		this.id = id;
	}
	public int getId(){
		return this.id;
	}
	
	public void setdateAdded(String dateAdded){
		this.dateAdded = dateAdded;
	}
	public String getDateAdded(){
		return this.dateAdded;
	}
	
	public void setNotes(List<Notes> noteList){
		this.notesList = noteList;
	}
	public List<Notes> getNoteList(){
		return this.notesList;
	}
	
	public void setTourLocations(List<tourLocations> locations){
		this.tourLocations = locations;
	}
	public List<tourLocations> getTourLocations(){
		return this.tourLocations;
	}
	
	public void setTitle(String title){
		this.title = title;
	}
	public String getTitle(){
		return this.title;
	}
	
	public void setDuration(int duration){
		this.duration = duration;
	}
	public int getDuration(){
		return this.duration;
	}
	
	public void setViews(int views){
		this.views = views;
	}
	public int getViews(){
		return this.views;
	}
	
	public void setRating(double rating){
		this.rating = rating;
	}
	public double getRating(){
		return this.rating;
	}
}



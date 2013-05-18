package com.example.citywalkapplayout;

import com.google.android.gms.maps.model.LatLng;

public class TourNotes extends Notes {
	
	public void setTitle(String title){
		this.noteTitle = title;
	}
	public String getTitle(){
		return this.noteTitle;
	}
	
	public void setDescription(String description){
		this.description = description;
	}
	public String getDescription(){
		return this.description;
	}
	
	public void setLocation(String location){
		this.location = location;
	}
	public String getLocation(){
		return this.location;
	}
	
	public void setImageUrl(String url){
			this.imageUrl = url;
			}
			public String getImageUrl(){
				return this.imageUrl;
			}
	
}

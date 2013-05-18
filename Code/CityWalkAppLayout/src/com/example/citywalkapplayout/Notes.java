package com.example.citywalkapplayout;

import com.google.android.gms.maps.model.LatLng;

public abstract class Notes {
	String noteTitle;
	String description;
	String location;
	String imageUrl;
	
	abstract void setTitle(String title);
	abstract String getTitle();
	
	abstract void setDescription(String description);
	abstract String getDescription();
	
	abstract void setLocation(String location);
	abstract String getLocation();
	
	abstract void setImageUrl(String url);
	abstract String getImageUrl();
	
}

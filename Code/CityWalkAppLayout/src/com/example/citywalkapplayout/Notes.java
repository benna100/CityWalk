package com.example.citywalkapplayout;


public abstract class Notes {
	String noteTitle;
	String description;
	String location;
	String imageUrl;
	int id;
	
	abstract void setTitle(String title);
	abstract String getTitle();
	
	abstract void setDescription(String description);
	abstract String getDescription();
	
	abstract void setLocation(String location);
	abstract String getLocation();
	
	abstract void setImageUrl(String url);
	abstract String getImageUrl();
	
	abstract void setId(int id);
	abstract int getId();
	
}

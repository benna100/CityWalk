package com.example.citywalkapplayout;

public class PreviewTour {
	
	private String title;
	private String time;
	private String dist;
	private int img;
	
	public PreviewTour(String title, String time, String dist, int img){
		this.title = title;
		this.time = time;
		this.dist = dist;
		this.img = img;
	}
	
	public String getTitle(){return title;}
	public String getTime(){return time;}
	public String getDist(){return dist;}
	public int getImg(){return img;}

}

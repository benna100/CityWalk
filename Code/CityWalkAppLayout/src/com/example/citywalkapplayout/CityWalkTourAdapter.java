package com.example.citywalkapplayout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CityWalkTourAdapter extends ArrayAdapter<PreviewTour>  {
	
	private final Context context;
	private final PreviewTour[] tours;

	public CityWalkTourAdapter(Context context, PreviewTour[] tours) {
		super(context, R.layout.list_2line, tours);
		this.context = context;
		this.tours = tours;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.list_2line, parent, false);
		TextView time1 = (TextView) rowView.findViewById(R.id.time1);
		TextView time2 = (TextView) rowView.findViewById(R.id.time2);
		TextView title1 = (TextView) rowView.findViewById(R.id.title1);
		TextView title2 = (TextView) rowView.findViewById(R.id.title2);
		TextView dist1 = (TextView) rowView.findViewById(R.id.dist1);
		TextView dist2 = (TextView) rowView.findViewById(R.id.dist2);
		ImageView icon1 = (ImageView) rowView.findViewById(R.id.icon1);
		ImageView icon2 = (ImageView) rowView.findViewById(R.id.icon2);
		ImageView img1 = (ImageView) rowView.findViewById(R.id.ImageView04);
		ImageView img2 = (ImageView) rowView.findViewById(R.id.ImageView05);
		
		if(position%2==1){return inflater.inflate(R.layout.empty, parent, false);}
		
		PreviewTour tour1 = tours[position];
		
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(50,50);
		
		title1.setText(tour1.getTitle());
		time1.setText(tour1.getTime());
		dist1.setText(tour1.getDist());
		icon1.setLayoutParams(layoutParams);
		icon1.setImageResource(tour1.getImg());
		
		if(position+1 < tours.length) {
			PreviewTour tour2 = tours[position+1];
			
			title2.setText(tour2.getTitle());
			time2.setText(tour2.getTime());
			dist2.setText(tour2.getDist());
			icon2.setLayoutParams(layoutParams);
			icon2.setImageResource(tour2.getImg());
			
			img1.setImageResource(R.drawable.stopwatch);
			img2.setImageResource(R.drawable.checkeredflag);
		}
		
		return rowView;
		
	}	

}

package com.example.citywalkapplayout;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

public class CityWalkTourAdapter extends ArrayAdapter<Tour> {

	private final Context context;
	private final List<Tour> tours;

	public CityWalkTourAdapter(Context context, List<Tour> tours) {
		super(context, R.layout.list_2line, tours);
		this.context = context;
		this.tours = tours;
	}

	@SuppressWarnings("deprecation")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.list_2line, parent, false);
		TextView views1 = (TextView) rowView.findViewById(R.id.views1);
		TextView views2 = (TextView) rowView.findViewById(R.id.views2);
		TextView title1 = (TextView) rowView.findViewById(R.id.title1);
		TextView title2 = (TextView) rowView.findViewById(R.id.title2);
		ImageView icon1 = (ImageView) rowView.findViewById(R.id.icon1);
		ImageView icon2 = (ImageView) rowView.findViewById(R.id.icon2);
		ImageView img2 = (ImageView) rowView.findViewById(R.id.ImageView05);
		
		TextView id1 = (TextView) rowView.findViewById(R.id.tourid1);
		TextView id2 = (TextView) rowView.findViewById(R.id.tourid2);
		
		GridLayout grid1 = (GridLayout) rowView.findViewById(R.id.grid1);
		GridLayout grid2 = (GridLayout) rowView.findViewById(R.id.grid2);
		grid1.setMinimumWidth(rowView.getWidth()/2);
		grid2.setMinimumWidth(rowView.getWidth()/2);
		
		RatingBar bar1 = (RatingBar) rowView.findViewById(R.id.ratingBar1);
		RatingBar bar2 = (RatingBar) rowView.findViewById(R.id.ratingBar2);
		bar1.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				return true;
			}
		});
		
		bar2.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				return true;
			}
		});

		if (position % 2 == 1) {
			return inflater.inflate(R.layout.empty, parent, false);
		}

		Tour tour1 = tours.get(position);

		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				50, 50);

		title1.setText(tour1.getTitle());
		views1.setText(tour1.getViews() + " views");
		icon1.setLayoutParams(layoutParams);
		bar1.setRating((float) tour1.getRating());
		id1.setText(tour1.getId()+"");

		Drawable urlImage = null;
		try {
			urlImage = drawableFromUrl(tour1.getImageUrl());

			icon1.setBackgroundDrawable(urlImage);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("un here");
		}

		if (position + 1 < tours.size()) {
			Tour tour2 = tours.get(position + 1);

			title2.setText(tour2.getTitle());
			views2.setText(tour2.getViews() + " views");
			icon2.setLayoutParams(layoutParams);
			bar2.setVisibility(View.VISIBLE);
			bar2.setRating((float) tour2.getRating());
			img2.setImageResource(R.drawable.viewsicon);

			id2.setText(tour2.getId()+"");
			
			try {
				urlImage = drawableFromUrl(tour2.getImageUrl());

				icon2.setBackgroundDrawable(urlImage);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("un here");
			}
		}

		return rowView;

	}

	public static Drawable drawableFromUrl(String url) throws IOException {
		Bitmap x;

		HttpURLConnection connection = (HttpURLConnection) new URL(url)
				.openConnection();
		connection.connect();
		InputStream input = connection.getInputStream();

		x = BitmapFactory.decodeStream(input);
		return new BitmapDrawable(x);
	}

}

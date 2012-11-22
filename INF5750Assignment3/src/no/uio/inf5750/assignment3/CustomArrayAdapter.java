package no.uio.inf5750.assignment3;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomArrayAdapter extends ArrayAdapter<String>{
	
	private Context mContext;
	private String[] mValues;
	private Drawable[] mImages;
	
	public CustomArrayAdapter(Context context, int textViewResourceId,
			String[] objects, Drawable[] images) {
		super(context, textViewResourceId, objects);
		this.mContext = context;
		this.mValues = objects;
		this.mImages = images;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.rowlayout, parent, false);
		TextView textView = (TextView) rowView.findViewById(R.id.rowlayout_textview);
		ImageView imageView = (ImageView) rowView.findViewById(R.id.rowlayout_imageview);

		textView.setText(mValues[position]);
		imageView.setImageDrawable(mImages[position]);
		
		return rowView;
	}	

}

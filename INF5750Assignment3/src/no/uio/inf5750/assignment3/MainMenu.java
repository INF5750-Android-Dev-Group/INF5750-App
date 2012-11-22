package no.uio.inf5750.assignment3;

import no.uio.inf5750.assignment3.dashboard.DashboardActivity;
import no.uio.inf5750.assignment3.interpretation.InterpretationActivity;
import no.uio.inf5750.assignment3.messaging.MessagingActivity;
import no.uio.inf5750.assignment3.util.UpdateDaemon;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class MainMenu extends Activity{
	
	private ListView mListView;
	private Context mContext;
	private CustomArrayAdapter mArrayAdapter;
	private String[] mMenuItems;
	private Drawable[] mIcons;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mainmenu);
		
		setGraphicalElements();
		updateList();
		
	}
	
	public void updateList()
	{//Gets number of messages and displays them in the listview
    	final Thread setUpdatedListThread = new Thread()
    	{
    		public void run()
    		{
	    		int unread = UpdateDaemon.getDaemon().getUnreadMessages();
	    		mMenuItems[1] += " ("+unread+")";
	    		mArrayAdapter = new CustomArrayAdapter(mContext, R.layout.rowlayout, mMenuItems, mIcons);
	        	mListView.setAdapter(mArrayAdapter);
    		}
    	};
    	
    	Thread updateDaemonThread = new Thread()
		{
			public void run()
			{
				UpdateDaemon.getDaemon().update();
				runOnUiThread(setUpdatedListThread);
			}
		};
		updateDaemonThread.start();	
	}
	
	public void setGraphicalElements()
	{
		mContext = this;
		mListView = (ListView) findViewById(R.id.mainmenu_listview);
		mMenuItems = new String[3];
		mMenuItems[0] = "Dashboard";
		mMenuItems[1] = "Messaging";
		mMenuItems[2] = "Interpretations";
		Resources res = getResources();
		mIcons = new Drawable[mMenuItems.length];
		mIcons[0] = res.getDrawable(R.drawable.ic_menu_gallery);
		mIcons[1] = res.getDrawable(R.drawable.ic_menu_compose);
		mIcons[2] = res.getDrawable(R.drawable.ic_menu_friendslist);
		mArrayAdapter = new CustomArrayAdapter(mContext, R.layout.rowlayout, mMenuItems, mIcons);
    	mListView.setAdapter(mArrayAdapter);
    	
    	mListView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                int position, long id) {
            	launchActivity(position);    
            }
          });
	}
	
	public void launchActivity(int id)
	{
		Intent intent = null;
		switch(id)
		{
		
			case 0:
				intent = new Intent(mContext, DashboardActivity.class);
				break;
				
			case 1:
				intent = new Intent(mContext, MessagingActivity.class);
				break;
				
			case 2:
				intent = new Intent(mContext, InterpretationActivity.class);
				break;
				
			default:
				intent = new Intent(mContext, DashboardActivity.class);
				break;
		}
		startActivity(intent);
	}

}

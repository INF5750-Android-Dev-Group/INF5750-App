package no.uio.inf5750.assignment3;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

public class UpdateDaemon {
	private static UpdateDaemon mDaemon;
	private JSONObject json;
	private int mUnread;
	
	static {
		mDaemon = new UpdateDaemon();
	}
	
	public static UpdateDaemon getDaemon() {
		return mDaemon;
	}
	
	private UpdateDaemon() {
		json = null;
	}
	
	public void update() {
		String jsonContent = ConnectionManager.getConnectionManager().doRequest("http://apps.dhis2.org/dev/api/currentUser/inbox");
		mUnread = 0;
		try {
			json = new JSONObject(jsonContent);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			json = null;
			e.printStackTrace();
			return;
		}
		int numMsg = getNumberOfMessages();
		for (int i = 0; i < numMsg; i++) {
			if (!getBooleanProperty("messageConversations", i, "read")) {
				mUnread++;
			}
		}
	}
	
	public int getUnreadMessages() {
		if (json == null) {
			update();
		}
		return mUnread;
	}
	
	private Object getProperty(String array, int id, String property) {
		
		if (json == null) {
			update();
		}
		Object ret;
		try {
			ret = json.getJSONArray(array).getJSONObject(id).get(property);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			ret = null;
			e.printStackTrace();
		}
		return ret;
	}
	
	public Boolean getBooleanProperty(String array, int id, String property) {
		return (Boolean) getProperty(array, id, property);
	}
	
	public String getStringProperty(String array, int id, String property) {
		return (String) getProperty(array, id, property);
	}
	
	public String getMessageName(int id) {
		return getStringProperty("messageConversations", id, "name");
	}
	
	// requires updating once the latest version of the json-stream is out
	public String getMessageTitle(int id) {
		return getStringProperty("messageConversations", id, "name");
	}
	
	public String getMessageUrl(int id) {
		if (json == null) {
			update();
		}
		return "";
	}
	
	public String getMessageUid(int id) {
		return getStringProperty("messageConversations", id, "id");
	}
	
	public int getNumberOfMessages() {
		if (json == null) {
			update();
		}
		if (json == null) {
			return 0;
		} else {
			int ret;
			try {
				ret = json.getJSONArray("messageConversations").length();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				ret = 0;
				e.printStackTrace();
			}
			return ret;
		}
	}
	
}

package no.uio.inf5750.assignment3.util;


public class Message extends ConversationBase {

	public String mTitle = "";
	public String mId = "";
	public String mLastUpdated = "";
	public boolean mRead;
	
	public String getConversationUrl() {
		return getConversationBaseUrl() + mId;
	}
	
	public static String getConversationUrl(String id) {
		return getConversationBaseUrl() + id;
	}
	
	private static String getConversationBaseUrl() {
		return ConnectionManager.getConnectionManager().getSite() + "messageConversations/";
	}

}

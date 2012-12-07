package no.uio.inf5750.assignment3.util;


public class Message extends ConversationBase {

	public String mTitle = "";
	public String mId = "";
	public String mLastUpdated = "";
	public boolean mRead;
	
	public String getConversationUrl() {
		return ConnectionManager.getConnectionManager().getSite() + "messageConversations/" + mId;
	}

}

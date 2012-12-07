package no.uio.inf5750.assignment3.util;

/**
 * Used for showing message-conversations
 * @author Jan Anders Bremer 
 */

public class Message extends ConversationBase {

	public String mTitle = "";
	public String mId = "";
	public String mLastUpdated = "";
	public boolean mRead;
	
	/**
	 * @return The conversation URL
	 */
	public String getConversationUrl() {
		return getConversationUrl(mId);
	}
	
	/**
	 * @param id ID of the conversation
	 * @return The conversation URL
	 */
	public static String getConversationUrl(String id) {
		return getConversationBaseUrl() + id;
	}
	
	
	/**
	 * @return The prefix of the conversation URL
	 */
	private static String getConversationBaseUrl() {
		return ConnectionManager.getConnectionManager().getSite() + "messageConversations/";
	}

}

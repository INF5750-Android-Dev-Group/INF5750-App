package no.uio.inf5750.assignment3;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

import android.util.Log;

public class UpdateDaemon {
	private static UpdateDaemon mDaemon;
	private NodeList mMessages, mInterpretations;
	private int mUnread;
	
	static {
		mDaemon = new UpdateDaemon();
	}
	
	public static UpdateDaemon getDaemon() {
		return mDaemon;
	}
	
	private UpdateDaemon() {
		
	}
	
	public void update() {
		updateMessages();
		updateInterpretations();
	}
	
	public void updateInterpretations() {
		String messageList = ConnectionManager.getConnectionManager().doRequest("http://apps.dhis2.org/dev/api/interpretations.xml");
		Document doc = Util.getDomElement(messageList);
		NodeList list = doc.getChildNodes();
		NodeList metadata = list.item(0).getChildNodes();
		mInterpretations = null;
		for (int i = 0; i < metadata.getLength(); i++) {
			if (metadata.item(i).getNodeName().equals("interpretations")) {
				mInterpretations = metadata.item(i).getChildNodes();
			}
		}
	}
	
	public void updateMessages() {
		
		String messageList = ConnectionManager.getConnectionManager().doRequest("http://apps.dhis2.org/dev/api/messageConversations.xml");
		Document doc = Util.getDomElement(messageList);
		if(doc==null)
			return;
		NodeList list = doc.getChildNodes();
		NodeList metadata = list.item(0).getChildNodes();
		mMessages = null;
		mUnread = 0;
		for (int i = 0; i < metadata.getLength(); i++) {
			if (metadata.item(i).getNodeName().equals("messageConversations")) {
				mMessages = metadata.item(i).getChildNodes();
				for (int j = 0; j < mMessages.getLength(); j++) {
					NamedNodeMap map = mMessages.item(j).getAttributes();
					if ( map.getNamedItem("read").getNodeValue().equals("false") ) {
						mUnread += 1;
					}
				}
			}
		}
	}
	
	public NodeList getMessages() {
		if (mMessages == null) {
			updateMessages();
		}
		return mMessages;
	}
	
	public NodeList getInterpretations() {
		if (mInterpretations == null) {
			updateInterpretations();
		}
		return mInterpretations;
	}
	
	public int getUnreadMessages() {
		if (mMessages == null) {
			updateMessages();
		}
		return mUnread;
	}
	
}

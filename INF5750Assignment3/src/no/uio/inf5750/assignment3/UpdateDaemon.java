package no.uio.inf5750.assignment3;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

public class UpdateDaemon {
	private static UpdateDaemon mDaemon;
	private NodeList mMessages;
	private int mUnread;
	
	static {
		mDaemon = new UpdateDaemon();
	}
	
	public static UpdateDaemon getUpdateDaemon() {
		return mDaemon;
	}
	
	private UpdateDaemon() {
		
	}
	
	public void update() {
		
		String messageList = ConnectionManager.getConnectionManager().doRequest("http://apps.dhis2.org/dev/api/messageConversations.xml");
		Document doc = Util.getDomElement(messageList);
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
		return mMessages;
	}
	
	public int getUnreadMessages() {
		if (mMessages == null) {
			update();
		}
		return mUnread;
	}
	
}

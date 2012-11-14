package no.uio.inf5750.assignment3;

import java.util.HashMap;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

public class UpdateDaemon {
	private static UpdateDaemon mDaemon;
	private NodeList mMessageList, mInterpretationList;
	private HashMap<String, NodeList> mMessages, mInterpretations;
	private int mUnread;
	
	static {
		mDaemon = new UpdateDaemon();
	}
	
	public static UpdateDaemon getDaemon() {
		return mDaemon;
	}
	
	private UpdateDaemon() {
		mMessages = new HashMap<String, NodeList>();
		mInterpretations = new HashMap<String, NodeList>();
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
		mInterpretationList = null;
		for (int i = 0; i < metadata.getLength(); i++) {
			if (metadata.item(i).getNodeName().equals("interpretations")) {
				mInterpretationList = metadata.item(i).getChildNodes();
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
		mMessageList = null;
		mUnread = 0;
		for (int i = 0; i < metadata.getLength(); i++) {
			if (metadata.item(i).getNodeName().equals("messageConversations")) {
				mMessageList = metadata.item(i).getChildNodes();
				for (int j = 0; j < mMessageList.getLength(); j++) {
					NamedNodeMap map = mMessageList.item(j).getAttributes();
					if ( map.getNamedItem("read").getNodeValue().equals("false") ) {
						mUnread += 1;
					}
				}
				break;
			}
		}
	}
	
	public NodeList getMessageList() {
		if (mMessageList == null) {
			updateMessages();
		}
		return mMessageList;
	}
	
	public NodeList getInterpretationList() {
		if (mInterpretationList == null) {
			updateInterpretations();
		}
		return mInterpretationList;
	}
	
	public int getUnreadMessages() {
		if (mMessageList == null) {
			updateMessages();
		}
		return mUnread;
	}
	
	public NodeList getMessageContent(String id) {
		NodeList ret = mMessages.get(id);
		if (ret == null) {
			
		}
		return ret;
	}
	
	public String getMessageName(int id) {
		return getMessageList().item(id).getAttributes().getNamedItem("name").getNodeValue();
	}
	
	public String getMessageUrl(int id) {
		return getMessageList().item(id).getAttributes().getNamedItem("href").getNodeValue();
	}
	
	public int getNumberOfMessages() {
		if (mMessageList == null) {
			updateMessages();
		}
		if (mMessageList == null) {
			return 0;
		} else {
			return getMessageList().getLength();
		}
	}
	
}

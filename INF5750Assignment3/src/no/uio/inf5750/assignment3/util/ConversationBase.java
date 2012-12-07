package no.uio.inf5750.assignment3.util;

import java.util.LinkedList;

/**
 * Conversation base class
 * @author Jan Anders Bremer
 *
 */

public abstract class ConversationBase {
	public User mUser = null;
	public LinkedList<Comment> mCommentThread = new LinkedList<Comment>();
	public String mId = "";
	public String mLastUpdated = "";
	public String mText = "";
}

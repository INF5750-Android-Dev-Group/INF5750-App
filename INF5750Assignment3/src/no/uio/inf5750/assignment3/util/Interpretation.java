package no.uio.inf5750.assignment3.util;

import java.util.LinkedList;

public class Interpretation extends ConversationBase {
	public LinkedList<InterpretationInfoNode> mInfo = new LinkedList<InterpretationInfoNode>();
	public String mCreated = "";
	
	public String getInterpretationUrl() {
		return getInterpretationBaseUrl() + mId;
	}
	
	public static String getInterpretationUrl(String id) {
		return getInterpretationBaseUrl() + id;
	}
	
	private static String getInterpretationBaseUrl() {
		return ConnectionManager.getConnectionManager().getSite() + "interpretations/";
	}
}

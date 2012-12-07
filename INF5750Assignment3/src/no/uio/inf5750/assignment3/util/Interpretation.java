package no.uio.inf5750.assignment3.util;

import java.util.LinkedList;

/**
 * Class used for storing interpretation-data
 * @author Jan Anders Bremer
 *
 */
public class Interpretation extends ConversationBase {
	public LinkedList<InterpretationInfoNode> mInfo = new LinkedList<InterpretationInfoNode>();
	public String mCreated = "";
	
	/**
	 * @return URL of the interpretation
	 */
	public String getInterpretationUrl() {
		return getInterpretationUrl(mId);
	}
	
	/**
	 * @param id of the interpretation
	 * @return URL of the interpretation
	 */
	public static String getInterpretationUrl(String id) {
		return getInterpretationBaseUrl() + id + getInterpretationPostfixUrl();
	}
	
	/**
	 * @return URL prefix
	 */
	private static String getInterpretationBaseUrl() {
		return ConnectionManager.getConnectionManager().getSite() + "interpretations/";
	}
	
	/**
	 * @return URL postfix
	 */
	private static String getInterpretationPostfixUrl() {
		return "/comment/";
	}
	
}

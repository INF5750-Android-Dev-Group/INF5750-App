package no.uio.inf5750.assignment3.util;

/**
 * Used for storing information about interpretation-posts
 * @author Jan Anders Bremer
 *
 */
public class InterpretationInfoNode {
	public String mName = "";
	public String mLastUpdated = "";
	public String mId = "";
	public boolean mChart = false;
	
	/**
	 * @return The URL where the imagedata is located (if this is a chart)
	 */
	public String getImageUrl() {
		return ConnectionManager.getConnectionManager().getSite() + "charts/" + mId + "/data";
	}
}

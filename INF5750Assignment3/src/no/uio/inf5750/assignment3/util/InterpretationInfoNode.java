package no.uio.inf5750.assignment3.util;

public class InterpretationInfoNode {
	public String mName = "";
	public String mLastUpdated = "";
	public String mId = "";
	public boolean mChart = false;
	
	String getImageUrl() {
		return ConnectionManager.getConnectionManager().getSite() + "charts/" + mId + "/data";
	}
	
	//TODO
	//Lag getChart/Image
}

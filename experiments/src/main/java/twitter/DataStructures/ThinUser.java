package twitter.DataStructures;

import java.io.Serializable;
import java.util.ArrayList;

public class ThinUser implements Serializable {
	private String userName;
	private ArrayList<ThinTweet> outgoingTweets;
	private ArrayList<ReTweet> outgoingReTweets;

	public ThinUser(String userName) {
		this.userName = userName;
		outgoingTweets = new ArrayList<ThinTweet>();
		outgoingReTweets = new ArrayList<ReTweet>();
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public ArrayList<ThinTweet> getOutgoingTweets() {
		return outgoingTweets;
	}

	public void setOutgoingTweets(ArrayList<ThinTweet> outgoingTweets) {
		this.outgoingTweets = outgoingTweets;
	}

	public ArrayList<ReTweet> getOutgoingReTweets() {
		return outgoingReTweets;
	}

	public void setOutgoingReTweets(ArrayList<ReTweet> outgoingReTweets) {
		this.outgoingReTweets = outgoingReTweets;
	}
}

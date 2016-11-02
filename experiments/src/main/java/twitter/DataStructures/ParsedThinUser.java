package twitter.DataStructures;

import java.io.Serializable;
import java.util.ArrayList;

public class ParsedThinUser implements Serializable{
	private String userName;
	private ArrayList<ParsedTweet> outgoingTweets;
	private ArrayList<ParsedRetweet> outgoingReTweets;

	public ParsedThinUser(String userName) {
		this.userName = userName;
		outgoingTweets = new ArrayList<ParsedTweet>();
		outgoingReTweets = new ArrayList<ParsedRetweet>();
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public ArrayList<ParsedTweet> getOutgoingTweets() {
		return outgoingTweets;
	}

	public void setOutgoingTweets(ArrayList<ParsedTweet> outgoingTweets) {
		this.outgoingTweets = outgoingTweets;
	}

	public ArrayList<ParsedRetweet> getOutgoingReTweets() {
		return outgoingReTweets;
	}

	public void setOutgoingReTweets(ArrayList<ParsedRetweet> outgoingReTweets) {
		this.outgoingReTweets = outgoingReTweets;
	}
}

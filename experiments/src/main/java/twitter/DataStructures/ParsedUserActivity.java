package twitter.DataStructures;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class ParsedUserActivity implements Serializable{
	 private String userName;
	    private HashMap<String,ParsedThinUser> followees;
	    private HashMap<String,ParsedThinUser> followers;
	    private ArrayList<ParsedTweet> outgoingTweets;
	    private ArrayList<ParsedRetweet> outgoingReTweets;

	    public ParsedUserActivity(String uName, HashMap<String,ParsedThinUser> followees,HashMap<String,ParsedThinUser> followers) {
	        this.followees = followees;
	        this.followers = followers;
	        userName = uName;
	    }

		public String getUserName() {
			return userName;
		}

		public HashMap<String, ParsedThinUser> getFollowees() {
			return followees;
		}

		public HashMap<String, ParsedThinUser> getFollowers() {
			return followers;
		}

		public ArrayList<ParsedTweet> getOutgoingTweets() {
			return outgoingTweets;
		}

		public ArrayList<ParsedRetweet> getOutgoingReTweets() {
			return outgoingReTweets;
		}

		public void setOutgoingTweets(ArrayList<ParsedTweet> outgoingTweets) {
			this.outgoingTweets = outgoingTweets;
		}

		public void setOutgoingReTweets(ArrayList<ParsedRetweet> outgoingReTweets) {
			this.outgoingReTweets = outgoingReTweets;
		}

}

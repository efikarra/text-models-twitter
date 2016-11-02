package twitter.DataStructures;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import twitter.Utilities.Constants;

public class ParsedRetweet extends ParsedTweet implements Serializable{
	List<String> retweetedUsers=new ArrayList<String>();
    public ParsedRetweet(ThinTweet tweet,String oldText,List<String> retweetedUsers) {
    	super(tweet,oldText);
        this.retweetedUsers=retweetedUsers;
    }
    public int compareTo(Object o) {
    	ParsedTweet otherTTweet = (ParsedTweet) o;
        return new Long(tweet.getTimeStamp()).compareTo(otherTTweet.getTweet().getTimeStamp());
    }

    @Override
	public String toString() {
		String tweet=Constants.TIMESTAMP_PREFIX+this.tweet.getTimeStamp();
		tweet+="\n";
		tweet+=Constants.TWEET_PREFIX+this.tweet.getText();
		return tweet;
	}
	public List<String> getRetweetedUsers() {
		return retweetedUsers;
	}

	public void setRetweetedUsers(List<String> retweetedUsers) {
		this.retweetedUsers = retweetedUsers;
	}
}

package twitter.DataStructures;

import java.io.Serializable;

public class ParsedTweet implements Comparable,Serializable{

	protected String oldText;
	protected ThinTweet tweet;
	
	public int compareTo(Object o) {
		ParsedTweet otherTTweet = (ParsedTweet) o;
		return tweet.compareTo(otherTTweet.getTweet());
	}

	public ParsedTweet(ThinTweet tweet,String oldText) {
		this.oldText=oldText;
		this.tweet=tweet;
	}
	public String getOldText() {
		return oldText;
	}
	public void setOldText(String oldText) {
		this.oldText = oldText;
	}
	public ThinTweet getTweet() {
		return tweet;
	}
	public void setTweet(ThinTweet tweet) {
		this.tweet = tweet;
	}
	
}

package twitter.DataStructures;

import java.io.Serializable;

public class TweetEvent implements Serializable,Comparable {

	private ThinTweet thinTweet;
	private String user;
	private boolean isRetweeted;
	private String oldText;
	private int eventId;
	public int compareTo(Object o) {
		TweetEvent otherTTweet = (TweetEvent) o;
        return new Long(thinTweet.getTimeStamp()).compareTo(otherTTweet.getThinTweet().getTimeStamp());
    }
	public TweetEvent(ThinTweet tweet, boolean isRetweeted,String oldText,String user) {
		this.user=user;
		this.thinTweet = tweet;
		this.isRetweeted = isRetweeted;
		this.oldText=oldText;
	}
	public TweetEvent(ThinTweet tweet, boolean isRetweeted) {
		this(tweet,isRetweeted,null,null);
		
	}public TweetEvent(String oldText,ThinTweet tweet, boolean isRetweeted) {
		this(tweet,isRetweeted,oldText,null);
	}
	
	public TweetEvent(ThinTweet tweet, boolean isRetweeted,String user) {
		this(tweet,isRetweeted,null,user);
	}
	public ThinTweet getThinTweet() {
		return thinTweet;
	}

	public void setThinTweet(ThinTweet tweet) {
		this.thinTweet = tweet;
	}

	public boolean isRetweeted() {
		return isRetweeted;
	}

	public void setRetweeted(boolean isRetweet) {
		this.isRetweeted = isRetweet;
	}
	public String getOldText() {
		return oldText;
	}
	public void setOldText(String oldText) {
		this.oldText = oldText;
	}

	public int getEventId() {
		return eventId;
	}

	public void setEventId(int eventId) {
		this.eventId = eventId;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}


}

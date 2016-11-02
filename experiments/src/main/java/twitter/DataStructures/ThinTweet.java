package twitter.DataStructures;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

import twitter.Utilities.Constants;

public class ThinTweet implements Comparable, Serializable {
	static final long serialVersionUID = 111333L;
	protected final long timeStamp;
	protected final String text;
	public ThinTweet() {
		timeStamp=0;
		text=null;
	}
	public ThinTweet(long timeStamp, String text) {
		this.timeStamp = timeStamp;
		this.text = text;
	}

	public int compareTo(Object o) {
		ThinTweet otherTTweet = (ThinTweet) o;
		return new Long(this.timeStamp).compareTo(otherTTweet.getTimeStamp());
	}

	public String getText() {
		return text;
	}

	public long getTimeStamp() {
		return timeStamp;
	}

	@Override
	public String toString() {
		String tweet=Constants.TIMESTAMP_PREFIX+this.timeStamp;
		tweet+="\n";
		tweet+=Constants.TWEET_PREFIX+this.text;
		return tweet;
	}

	public static void main(String[] args) throws Exception {
		ThinTweet tt1 = new ThinTweet(99, null);
		ThinTweet tt2 = new ThinTweet(100, null);
		ArrayList<ThinTweet> tweets = new ArrayList<ThinTweet>();
		tweets.add(tt1);
		tweets.add(tt2);
		Collections.sort(tweets);
		for (ThinTweet tweet : tweets) {
			System.out.println(tweet.getTimeStamp());
		}
	}
}
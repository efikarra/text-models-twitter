package twitter.DataStructures;

import java.io.Serializable;

public class BTMTweet implements Serializable {
	private TweetEvent tweetEvent;
	private Integer[] ids;

	public BTMTweet(TweetEvent tweetEvent, Integer[] ids) {
		this.tweetEvent = tweetEvent;
		this.ids = ids;
	}

	public TweetEvent getTweetEvent() {
		return tweetEvent;
	}

	public void setTweetEvent(TweetEvent tweetEvent) {
		this.tweetEvent = tweetEvent;
	}

	public Integer[] getIds() {
		return ids;
	}

	public void setIds(Integer[] ids) {
		this.ids = ids;
	}
}

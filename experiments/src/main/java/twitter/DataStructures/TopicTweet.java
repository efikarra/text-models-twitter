package twitter.DataStructures;

public class TopicTweet {
	 private TweetEvent tweetEvent;
	 private double[] vector;
	 
	 public TopicTweet(TweetEvent tweetEvent,double[] vector,int eventId) {
		this.tweetEvent=tweetEvent;
		this.vector=vector;
	}
	public TweetEvent getTweetEvent() {
		return tweetEvent;
	}
	public double[] getVector() {
		return vector;
	}
}

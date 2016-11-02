package twitter.DataStructures;

import java.io.Serializable;
import java.util.List;

public class UserData implements Serializable{
	private List<TweetEvent> trainTweets;
	private List<TweetEvent> testTweets;
	private String userName;
	public UserData() {
		this(null,null,null);
	}
	public UserData(String userName) {
		this(userName,null,null);
	}
	public UserData(List<TweetEvent> documents) {
		this.trainTweets = documents;
	}
	public UserData(String userName,List<TweetEvent> trainTweets,List<TweetEvent> testTweets) {
		this.trainTweets = trainTweets;
		this.testTweets = testTweets;
		this.userName=userName;
	}
	public List<TweetEvent> getTrainTweets() {
		return trainTweets;
	}
	public void setTrainTweets(List<TweetEvent> trainTweets) {
		this.trainTweets = trainTweets;
	}
	public List<TweetEvent> getTestTweets() {
		return testTweets;
	}
	public void setTestTweets(List<TweetEvent> testTweets) {
		this.testTweets = testTweets;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String screenName) {
		this.userName = screenName;
	}
	
}

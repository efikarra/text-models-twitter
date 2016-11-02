package twitter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import twitter.DataStructures.ParsedRetweet;
import twitter.DataStructures.ParsedThinUser;
import twitter.DataStructures.ParsedTweet;
import twitter.DataStructures.ReTweet;
import twitter.DataStructures.ThinTweet;
import twitter.DataStructures.ThinUser;
import twitter.DataStructures.TweetEvent;
import twitter.DataStructures.UserActivity;
import twitter.DataStructures.UserData;
import twitter.Utilities.Constants;
import twitter.Utilities.Twitter.TwitterUtilities;
import twitter.modelUtilities.ModelsUtilities;

public class TrainDataCreator {
	private UserActivity userActivity;
	private final UserData userData;
	Set<String> hashedTweets = new HashSet<String>();
	private long refTime;
	public TrainDataCreator(UserActivity userActivity,UserData userData,long refTime) {
		this.userActivity = userActivity;
		this.userData = userData;
		this.refTime = refTime;
	}

	public void postedTweetsRC(double trainPercent) {
		List<TweetEvent> trainTweets = new ArrayList<TweetEvent>();

		Map<String, ThinUser> reciprocalConnections = ModelsUtilities.getReciprocalConnections(userActivity);
		// System.out.println("reciprocal
		// connections"+reciprocalConnections.size());
		List<ThinUser> rconnections = new ArrayList<ThinUser>(reciprocalConnections.values());

		trainTweets.addAll(usersTweets(rconnections));
		trainTweets.addAll(usersReTweets(rconnections));

		cleanTrainTweets(trainTweets);
		trainTweets.addAll(userTweets());
		// System.out.println("after: " + trainTweets.size());
		userData.setTrainTweets(trainTweets);
	}
	public void postedReTweetsRC(double trainPercent) {
		List<TweetEvent> trainTweets = new ArrayList<TweetEvent>();

		Map<String, ThinUser> reciprocalConnections = ModelsUtilities.getReciprocalConnections(userActivity);
		// System.out.println("reciprocal
		// connections"+reciprocalConnections.size());
		List<ThinUser> rconnections = new ArrayList<ThinUser>(reciprocalConnections.values());

		trainTweets.addAll(usersTweets(rconnections));
		trainTweets.addAll(usersReTweets(rconnections));

		cleanTrainTweets(trainTweets);
		trainTweets.addAll(userReTweets());
		// System.out.println("after: " + trainTweets.size());
		userData.setTrainTweets(trainTweets);
		// printUserDataStatistics();
	}

	public void followeesFollowersTweetsRT(double trainPercent) {
		List<TweetEvent> trainTweets = new ArrayList<TweetEvent>();
		trainTweets.addAll(followersTweetsRT());
		trainTweets.addAll(matchedRetweetsUniform(trainPercent));
		userData.setTrainTweets(trainTweets);
		//printUserDataStatistics();
	}

	public void postedReTweetsFolloweesTweetsRT(double trainPercent) {
		List<TweetEvent> trainTweets = new ArrayList<TweetEvent>();
		trainTweets.addAll(userReTweets());
	
		trainTweets.addAll(matchedRetweetsUniform(trainPercent));
		
		cleanTrainTweets(trainTweets);
		userData.setTrainTweets(trainTweets);
		//printUserDataStatistics();
	}

	public void postedTweetsFolloweesTweetsRT(double trainPercent) {
		List<TweetEvent> trainTweets = new ArrayList<TweetEvent>();
		trainTweets.addAll(userTweets());
		trainTweets.addAll(matchedRetweetsUniform(trainPercent));
		userData.setTrainTweets(trainTweets);
	}

	public void postedTweetsFollowersTweetsRT(double trainPercent) {
		List<TweetEvent> trainTweets = new ArrayList<TweetEvent>();

		trainTweets.addAll(followersTweetsRT());
		trainTweets.addAll(userTweets());

		userData.setTrainTweets(trainTweets);
	}

	public void RCTweetsRetweets(double trainPercent) {

		List<TweetEvent> trainTweets = new ArrayList<TweetEvent>();

		Map<String, ThinUser> reciprocalConnections = ModelsUtilities.getReciprocalConnections(userActivity);
		// System.out.println("reciprocal
		// connections"+reciprocalConnections.size());
		List<ThinUser> rconnections = new ArrayList<ThinUser>(reciprocalConnections.values());

		trainTweets.addAll(usersTweets(rconnections));
		trainTweets.addAll(usersReTweets(rconnections));

		cleanTrainTweets(trainTweets);
		// System.out.println("after: " + trainTweets.size());
		userData.setTrainTweets(trainTweets);
		// printUserDataStatistics();
	}

	private List<TweetEvent> usersReTweets(List<ThinUser> users) {
		List<TweetEvent> tweets = new ArrayList<TweetEvent>();
		for (ThinUser friend : users) {
			List<ReTweet> retweets = friend.getOutgoingReTweets();
			for (ThinTweet retweet : retweets){
				if (retweet.getTimeStamp() < refTime)
				tweets.add(new TweetEvent(retweet, false,friend.getUserName()));
			}
		}
		return tweets;
	}

	private List<TweetEvent> usersTweets(List<ThinUser> users) {
		List<TweetEvent> etweets = new ArrayList<TweetEvent>();
		for (ThinUser friend : users) {
			// List<ThinTweet> tweets = friend.getOutgoingTweets();
			// for (ThinTweet tweet : tweets)
			// trainTweets.add(new TweetEvent(tweet, false));
			List<ThinTweet> tweets = friend.getOutgoingTweets();
			for (ThinTweet tweet : tweets){
				if (tweet.getTimeStamp() < refTime)
					etweets.add(new TweetEvent(tweet, false,friend.getUserName()));
			}
		}
		return etweets;

	}

	public void postedTweetsRetweets(double trainPercent) {

		List<TweetEvent> trainTweets = new ArrayList<TweetEvent>();
		trainTweets.addAll(userReTweets());
		cleanTrainTweets(trainTweets);
		// System.out.println("after: " + trainTweets.size());
		trainTweets.addAll(userTweets());
		userData.setTrainTweets(trainTweets);
		// printUserDataStatistics();
	}

	public void postedTweets(double trainPercent) {
		userData.setTrainTweets(userTweets());
		// printUserDataStatistics();
	}

	public void postedReTweets(double trainPercent) {
		List<TweetEvent> trainTweets = new ArrayList<TweetEvent>();
		trainTweets.addAll(userReTweets());

		cleanTrainTweets(trainTweets);
		userData.setTrainTweets(trainTweets);
		// printUserDataStatistics();
	}

	public void postedReTweetsFollowersTweetsRT(double trainPercent) {
		List<TweetEvent> trainTweets = new ArrayList<TweetEvent>();
		trainTweets.addAll(userReTweets());
		cleanTrainTweets(trainTweets);
		trainTweets.addAll(followersTweetsRT());
		userData.setTrainTweets(trainTweets);
	}

	private void cleanTrainTweets(List<TweetEvent> trainTweets) {
		Set<String> hashedTestTweets = new HashSet<String>();

		for (TweetEvent tevent : userData.getTestTweets()) {
			if (tevent.isRetweeted())
				hashedTestTweets.add(TwitterUtilities.getOriginalTweetFromRetweet(tevent.getThinTweet().getText()));
		}
		// System.out.println("hashedTestTweets: " + hashedTestTweets.size());
		// System.out.println("before: " + trainTweets.size());
		Iterator<TweetEvent> iterator = trainTweets.iterator();
		while (iterator.hasNext()) {
			TweetEvent tevent = iterator.next();
			if (hashedTestTweets
					.contains(TwitterUtilities.getOriginalTweetFromRetweet(tevent.getThinTweet().getText())))
				iterator.remove();
		}
	}

	public List<TweetEvent> userTweets() {
		List<TweetEvent> tweetsEvents = new ArrayList<TweetEvent>();
		List<ThinTweet> tweets = userActivity.getOutgoingTweets();
		Collections.sort(tweets);
		for (ThinTweet tweet : tweets){
			if (tweet.getTimeStamp() < refTime)
				tweetsEvents.add(new TweetEvent(tweet, false,userActivity.getUserName()));
		}
		return tweetsEvents;
	}

	public List<TweetEvent> userReTweets() {
		List<ReTweet> retweets = userActivity.getOutgoingReTweets();
		List<TweetEvent> retweetsEvents = new ArrayList<TweetEvent>();
		Collections.sort(retweets);
		for (ReTweet retweet : retweets){
			if (retweet.getTimeStamp() < refTime)
				retweetsEvents.add(new TweetEvent(retweet, false,userActivity.getUserName()));
		}
		return retweetsEvents;
	}

	private List<TweetEvent> followersTweetsRT() {
		List<TweetEvent> follTweets = new ArrayList<TweetEvent>();
		HashMap<String, ThinUser> followers = userActivity.getFollowers();
		for (Map.Entry<String, ThinUser> entry : followers.entrySet()) {

			List<ThinTweet> inTweets = entry.getValue().getOutgoingTweets();
			for (ThinTweet intweet : inTweets) {
				if (intweet.getTimeStamp() < refTime)
					follTweets.add(new TweetEvent(intweet, false,entry.getKey()));
			}

			List<ReTweet> inReTweets = entry.getValue().getOutgoingReTweets();
			for (ThinTweet inretweet : inReTweets) {
				if (inretweet.getTimeStamp() < refTime)
					follTweets.add(new TweetEvent(inretweet, false,entry.getKey()));
			}
		}
		return follTweets;
	}

	public void followersTweetsRetweets(double trainPercent) {
		userData.setTrainTweets(followersTweetsRT());
	}

	private List<TweetEvent> matchRetweeted(boolean addRetweets) {
		List<TweetEvent> tempTweets = new ArrayList<TweetEvent>();
		List<ReTweet> outRetweets = userActivity.getOutgoingReTweets();
		for (int i = 0; i < outRetweets.size(); i++) {
			String originalTweet = TwitterUtilities.getOriginalTweetFromRetweet(outRetweets.get(i).getText());
			hashedTweets.add(originalTweet);
			// if addRetweets, we also add user retweets for the user model
			// training.
			if (addRetweets)
				tempTweets.add(new TweetEvent(outRetweets.get(i), false));
		}

		HashMap<String, ThinUser> followees = userActivity.getFollowees();
		for (Map.Entry<String, ThinUser> entry : followees.entrySet()) {

			List<ThinTweet> inTweets = entry.getValue().getOutgoingTweets();
			for (ThinTweet intweet : inTweets) {
				if (hashedTweets.contains(intweet.getText()))
					tempTweets.add(new TweetEvent(intweet, true));
				else
					tempTweets.add(new TweetEvent(intweet, false));
			}

			List<ReTweet> inReTweets = entry.getValue().getOutgoingReTweets();
			for (ThinTweet inretweet : inReTweets) {
				String originalTweet = TwitterUtilities.getOriginalTweetFromRetweet(inretweet.getText());
				if (hashedTweets.contains(originalTweet))
					tempTweets.add(new TweetEvent(inretweet, true));
				else
					tempTweets.add(new TweetEvent(inretweet, false));
			}
		}
		return tempTweets;
	}

	public void followeesTweetsRetweets(double trainPercent) {
		userData.setTrainTweets(matchedRetweetsUniform(trainPercent));
	}

	private List<TweetEvent> matchedRetweetsUniform(double trainPercent) {
		List<TweetEvent> tweetsMatched = new ArrayList<TweetEvent>();
		List<TweetEvent> tweetsNotMatched = new ArrayList<TweetEvent>();
		List<TweetEvent> tweetsTemp = new ArrayList<TweetEvent>();

		List<ReTweet> outRetweets = userActivity.getOutgoingReTweets();
		for (int i = 0; i < outRetweets.size(); i++) {

			String originalTweet = TwitterUtilities.getOriginalTweetFromRetweet(outRetweets.get(i).getText());
			hashedTweets.add(originalTweet);
		}
		HashMap<String, ThinUser> followees = userActivity.getFollowees();
		for (Map.Entry<String, ThinUser> entry : followees.entrySet()) {
			// Set<String> hashedtemp = new HashSet<String>();

			List<ThinTweet> inTweets = entry.getValue().getOutgoingTweets();
			// for (ThinTweet tevent : inTweets) {
			// hashedtemp.add(tevent.getText());
			// }

			for (ThinTweet intweet : inTweets) {
				if (hashedTweets.contains(intweet.getText()))
					tweetsMatched.add(new TweetEvent(intweet, true,entry.getKey()));
				else
					tweetsNotMatched.add(new TweetEvent(intweet, false,entry.getKey()));
			}

			List<ReTweet> inReTweets = entry.getValue().getOutgoingReTweets();
			// for (ReTweet tevent : inReTweets) {
			// hashedtemp.add(TwitterUtilities.getOriginalTweetFromRetweet(tevent.getText()));
			// }
			// dupls += inTweets.size() + inReTweets.size() - hashedtemp.size();
			for (ReTweet inretweet : inReTweets) {
				String originalTweet = TwitterUtilities.getOriginalTweetFromRetweet(inretweet.getText());
				if (hashedTweets.contains(originalTweet))
					tweetsMatched.add(new TweetEvent(inretweet, true,entry.getKey()));
				else
					tweetsNotMatched.add(new TweetEvent(inretweet, false,entry.getKey()));
			}
		}
		for(TweetEvent tweet:tweetsMatched){
			if(tweet.getThinTweet().getTimeStamp()<refTime)
				tweetsTemp.add(tweet);
		}
		for(TweetEvent tweet:tweetsNotMatched){
			if(tweet.getThinTweet().getTimeStamp()<refTime)
				tweetsTemp.add(tweet);
		}
		return tweetsTemp;
	}
	public UserData getUserData() {
		return userData;
	}

	public long getRefTime() {
		return refTime;
	}

	public void setRefTime(long refTime) {
		this.refTime = refTime;
	}
}

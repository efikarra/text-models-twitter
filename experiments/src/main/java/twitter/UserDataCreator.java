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

import twitter.DataStructures.ReTweet;
import twitter.DataStructures.ThinTweet;
import twitter.DataStructures.ThinUser;
import twitter.DataStructures.TweetEvent;
import twitter.DataStructures.UserActivity;
import twitter.DataStructures.UserData;
import twitter.Utilities.Constants;
import twitter.Utilities.Twitter.TwitterUtilities;
import twitter.modelUtilities.ModelsUtilities;

public class UserDataCreator {
	private UserActivity userActivity;
	private final UserData userData;
	Set<String> hashedTweets = new HashSet<String>();

	public UserDataCreator(UserActivity userActivity,UserData userData) {
		this.userActivity = userActivity;
		this.userData = userData;
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
	public void getTestTweets(double trainPercent) {
		List<TweetEvent> tempTweets = matchedRetweetsUniform(trainPercent);
		int trainSize = (int) Math.ceil((double) trainPercent * (double) tempTweets.size());
		userData.setTestTweets(tempTweets.subList(trainSize, tempTweets.size()));
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
		List<TweetEvent> tempTweets = matchedRetweetsUniform(trainPercent);
		//System.out.println(tempTweets.size());
		int trainSize = (int) Math.ceil((double) trainPercent * (double) tempTweets.size());
		trainTweets.addAll(tempTweets.subList(0, trainSize));
		userData.setTrainTweets(trainTweets);
		//printUserDataStatistics();
	}

	public void postedReTweetsFolloweesTweetsRT(double trainPercent) {
		List<TweetEvent> trainTweets = new ArrayList<TweetEvent>();
		trainTweets.addAll(userReTweets());
	
		List<TweetEvent> tempTweets = matchedRetweetsUniform(trainPercent);
		
		cleanTrainTweets(trainTweets);
		userData.setTrainTweets(trainTweets);
		//printUserDataStatistics();
	}

	public void postedTweetsFolloweesTweetsRT(double trainPercent) {
		List<TweetEvent> trainTweets = new ArrayList<TweetEvent>();
		trainTweets.addAll(userTweets());
		List<TweetEvent> tempTweets = matchedRetweetsUniform(trainPercent);
		//System.out.println(tempTweets.size());
		int trainSize = (int) Math.ceil((double) trainPercent * (double) tempTweets.size());
		trainTweets.addAll(tempTweets.subList(0, trainSize));
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
			for (ThinTweet retweet : retweets)
				tweets.add(new TweetEvent(retweet, false));
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
			for (ThinTweet tweet : tweets)
				etweets.add(new TweetEvent(tweet, false));
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
		userData.setTrainTweets(userReTweets());
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
		for (ThinTweet tweet : tweets)
			tweetsEvents.add(new TweetEvent(tweet, false));
		return tweetsEvents;
	}

	public List<TweetEvent> userReTweets() {
		List<ReTweet> retweets = userActivity.getOutgoingReTweets();
		List<TweetEvent> retweetsEvents = new ArrayList<TweetEvent>();
		Collections.sort(retweets);
		for (ReTweet retweet : retweets)
			retweetsEvents.add(new TweetEvent(retweet, false));
		return retweetsEvents;
	}

	private List<TweetEvent> followersTweetsRT() {
		List<TweetEvent> follTweets = new ArrayList<TweetEvent>();
		HashMap<String, ThinUser> followers = userActivity.getFollowers();
		for (Map.Entry<String, ThinUser> entry : followers.entrySet()) {

			List<ThinTweet> inTweets = entry.getValue().getOutgoingTweets();
			for (ThinTweet intweet : inTweets) {
				follTweets.add(new TweetEvent(intweet, false));
			}

			List<ReTweet> inReTweets = entry.getValue().getOutgoingReTweets();
			for (ThinTweet inretweet : inReTweets) {
				follTweets.add(new TweetEvent(inretweet, false));
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

		List<TweetEvent> tempTweets = matchedRetweetsUniform(trainPercent);
		//System.out.println(tempTweets.size());
		int trainSize = (int) Math.ceil((double) trainPercent * (double) tempTweets.size());
		userData.setTrainTweets(tempTweets.subList(0, trainSize));
	}

	private List<TweetEvent> matchedRetweetsUniform(double trainPercent, long lastDate) {
		List<TweetEvent> tweetsMatched = new ArrayList<TweetEvent>();
		List<TweetEvent> tweetsNotMatched = new ArrayList<TweetEvent>();
		List<TweetEvent> tweetsTemp = new ArrayList<TweetEvent>();

		List<ReTweet> outRetweets = userActivity.getOutgoingReTweets();
		for (int i = 0; i < outRetweets.size(); i++) {
			String originalTweet = TwitterUtilities.getOriginalTweetFromRetweet(outRetweets.get(i).getText());
			hashedTweets.add(originalTweet);
		}
		int dupls = 0;
		HashMap<String, ThinUser> followees = userActivity.getFollowees();
		for (Map.Entry<String, ThinUser> entry : followees.entrySet()) {
			// Set<String> hashedtemp = new HashSet<String>();

			List<ThinTweet> inTweets = entry.getValue().getOutgoingTweets();
			// for (ThinTweet tevent : inTweets) {
			// hashedtemp.add(tevent.getText());
			// }

			for (ThinTweet intweet : inTweets) {
				if (intweet.getTimeStamp() < lastDate)
					continue;
				if (hashedTweets.contains(intweet.getText()))
					tweetsMatched.add(new TweetEvent(intweet, true));
				else
					tweetsNotMatched.add(new TweetEvent(intweet, false));
			}

			List<ReTweet> inReTweets = entry.getValue().getOutgoingReTweets();
			// for (ReTweet tevent : inReTweets) {
			// hashedtemp.add(TwitterUtilities.getOriginalTweetFromRetweet(tevent.getText()));
			// }
			// dupls += inTweets.size() + inReTweets.size() - hashedtemp.size();
			for (ThinTweet inretweet : inReTweets) {
				if (inretweet.getTimeStamp() < lastDate)
					continue;
				String originalTweet = TwitterUtilities.getOriginalTweetFromRetweet(inretweet.getText());
				if (hashedTweets.contains(originalTweet))
					tweetsMatched.add(new TweetEvent(inretweet, true));
				else
					tweetsNotMatched.add(new TweetEvent(inretweet, false));
			}
		}
		// System.out.println("dupls" + dupls);
		// System.out.println("test dataset size is: " + testTweets.size());
		Collections.sort(tweetsMatched);
		Collections.sort(tweetsNotMatched);

		int group = (int) Math.ceil((double) tweetsNotMatched.size() / (double) tweetsMatched.size());
		int j = 0;
		int k = 0;
		int groupsize = 0;

		int negSize = (int) Math.ceil((double) trainPercent * (double) tweetsNotMatched.size());
		int posSize = (int) Math.ceil((double) trainPercent * (double) tweetsMatched.size());
		tweetsTemp.addAll(tweetsNotMatched.subList(0, negSize));
		tweetsTemp.addAll(tweetsMatched.subList(0, posSize));

		tweetsTemp.addAll(tweetsMatched.subList(posSize, tweetsMatched.size()));
		tweetsTemp.addAll(tweetsNotMatched.subList(negSize, tweetsNotMatched.size()));
		return tweetsTemp;
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
		int dupls = 0;
		HashMap<String, ThinUser> followees = userActivity.getFollowees();
		for (Map.Entry<String, ThinUser> entry : followees.entrySet()) {
			// Set<String> hashedtemp = new HashSet<String>();

			List<ThinTweet> inTweets = entry.getValue().getOutgoingTweets();
			// for (ThinTweet tevent : inTweets) {
			// hashedtemp.add(tevent.getText());
			// }

			for (ThinTweet intweet : inTweets) {
				if (hashedTweets.contains(intweet.getText()))
					tweetsMatched.add(new TweetEvent(intweet, true));
				else
					tweetsNotMatched.add(new TweetEvent(intweet, false));
			}

			List<ReTweet> inReTweets = entry.getValue().getOutgoingReTweets();
			// for (ReTweet tevent : inReTweets) {
			// hashedtemp.add(TwitterUtilities.getOriginalTweetFromRetweet(tevent.getText()));
			// }
			// dupls += inTweets.size() + inReTweets.size() - hashedtemp.size();
			for (ThinTweet inretweet : inReTweets) {
				String originalTweet = TwitterUtilities.getOriginalTweetFromRetweet(inretweet.getText());
				if (hashedTweets.contains(originalTweet))
					tweetsMatched.add(new TweetEvent(inretweet, true));
				else
					tweetsNotMatched.add(new TweetEvent(inretweet, false));
			}
		}
		// System.out.println("dupls" + dupls);
		// System.out.println("test dataset size is: " + testTweets.size());
		Collections.sort(tweetsMatched);
		Collections.sort(tweetsNotMatched);
		// System.out.println(Constants.DEFAULT_DATE_FORMAT.format(new
		// Date(tweetsNotMatched.get(0).getThinTweet().getTimeStamp())));
		// System.out.println(Constants.DEFAULT_DATE_FORMAT.format(new
		// Date(tweetsNotMatched.get(tweetsNotMatched.size()-1).getThinTweet().getTimeStamp())));
		//
		// System.out.println(Constants.DEFAULT_DATE_FORMAT.format(new
		// Date(tweetsMatched.get(0).getThinTweet().getTimeStamp())));
		// System.out.println(Constants.DEFAULT_DATE_FORMAT.format(new
		// Date(tweetsMatched.get(tweetsMatched.size()-1).getThinTweet().getTimeStamp())));
		//

		int group = (int) Math.ceil((double) tweetsNotMatched.size() / (double) tweetsMatched.size());
		int j = 0;
		int k = 0;
		int groupsize = 0;
		// while (k < tweetsNotMatched.size()) {
		// if (groupsize == group) {
		// tweetsTemp.add(tweetsMatched.get(j));
		// j++;
		// groupsize = 0;
		// } else {
		// groupsize++;
		// tweetsTemp.add(tweetsNotMatched.get(k));
		// k++;
		// }
		// }
		// tweetsTemp.addAll(tweetsMatched.subList(j, tweetsMatched.size()));
		// System.out.println("all tweets: " + allTweets);
		// System.out.println("all positives: " + tweetsMatched.size());
		// System.out.println("all negatives: " + tweetsNotMatched.size());
		int negSize = (int) Math.ceil((double) trainPercent * (double) tweetsNotMatched.size());
		int posSize = (int) Math.ceil((double) trainPercent * (double) tweetsMatched.size());
		tweetsTemp.addAll(tweetsNotMatched.subList(0, negSize));
		tweetsTemp.addAll(tweetsMatched.subList(0, posSize));

		tweetsTemp.addAll(tweetsMatched.subList(posSize, tweetsMatched.size()));
		tweetsTemp.addAll(tweetsNotMatched.subList(negSize, tweetsNotMatched.size()));
		// System.out.println("all positives: " + tweetsMatched.size());
		// System.out.println("all negatives: " + tweetsNotMatched.size());
		return tweetsTemp;
	}

	private List<TweetEvent> matchedRetweetsUniform2() {
		List<TweetEvent> tweetsMatched = new ArrayList<TweetEvent>();
		List<TweetEvent> tweetsNotMatched = new ArrayList<TweetEvent>();
		List<TweetEvent> tweetsTemp = new ArrayList<TweetEvent>();
		List<ReTweet> outRetweets = userActivity.getOutgoingReTweets();
		for (int i = 0; i < outRetweets.size(); i++) {
			String originalTweet = TwitterUtilities.getOriginalTweetFromRetweet(outRetweets.get(i).getText());
			hashedTweets.add(originalTweet);
		}

		HashMap<String, ThinUser> followees = userActivity.getFollowees();
		int dupls = 0;
		for (Map.Entry<String, ThinUser> entry : followees.entrySet()) {

			List<ThinTweet> inTweets = entry.getValue().getOutgoingTweets();

			for (ThinTweet intweet : inTweets) {
				if (hashedTweets.contains(intweet.getText()))
					tweetsMatched.add(new TweetEvent(intweet, true));
				else
					tweetsNotMatched.add(new TweetEvent(intweet, false));
			}

			List<ReTweet> inReTweets = entry.getValue().getOutgoingReTweets();
			for (ThinTweet inretweet : inReTweets) {
				String originalTweet = TwitterUtilities.getOriginalTweetFromRetweet(inretweet.getText());
				if (hashedTweets.contains(originalTweet))
					tweetsMatched.add(new TweetEvent(inretweet, true));
				else
					tweetsNotMatched.add(new TweetEvent(inretweet, false));
			}
		}
		//System.out.println("dupls" + dupls);
		// System.out.println("test dataset size is: " + testTweets.size());
		Collections.sort(tweetsMatched);
		Collections.sort(tweetsNotMatched);
		int group = (int) Math.ceil((double) tweetsNotMatched.size() / (double) tweetsMatched.size());
		int j = 0;
		int k = 0;
		int groupsize = 0;
		while (k < tweetsNotMatched.size()) {
			if (groupsize == group) {
				tweetsTemp.add(tweetsMatched.get(j));
				j++;
				groupsize = 0;
			} else {
				groupsize++;
				tweetsTemp.add(tweetsNotMatched.get(k));
				k++;
			}
		}
		//System.out.println("all positives: " + tweetsMatched.size());
		//System.out.println("all negatives: " + tweetsNotMatched.size());
		tweetsTemp.addAll(tweetsMatched.subList(j, tweetsMatched.size()));

		return tweetsTemp;
	}

	public UserData getUserData() {
		return userData;
	}
}

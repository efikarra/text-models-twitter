package twitter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import twitter.DataStructures.Twitter.ParsedRetweet;
import twitter.DataStructures.Twitter.ParsedThinUser;
import twitter.DataStructures.Twitter.ParsedTweet;
import twitter.DataStructures.Twitter.ParsedUserActivity;
import twitter.DataStructures.Twitter.TweetEvent;
import twitter.DataStructures.Twitter.UserData;
import twitter.Utilities.Twitter.TwitterUtilities;
import twitter.modelUtilities.ModelsUtilities;

public class TrainPDataCreator {
	private ParsedUserActivity userActivity;
	private final UserData userData;
	Set<String> hashedTweets = new HashSet<String>();
	private long refTime;

	public TrainPDataCreator(ParsedUserActivity userActivity, UserData userData, long refTime) {
		this.userActivity = userActivity;
		this.userData = userData;
		this.refTime = refTime;
	}

	public void postedTweetsRC(double trainPercent) {
		List<TweetEvent> trainTweets = new ArrayList<TweetEvent>();
		Map<String, ParsedThinUser> reciprocalConnections = ModelsUtilities.getReciprocalConnections(userActivity);
		trainTweets.addAll(matchedUsersTweetsRT(trainPercent,reciprocalConnections));
		trainTweets.addAll(userTweets());
		
		userData.setTrainTweets(trainTweets);
	}

	private List<TweetEvent> convertToTweetEvent(List<ParsedTweet> trainTweets) {
		List<TweetEvent> tweets = new ArrayList<TweetEvent>();
		for (ParsedTweet tweet : trainTweets)
			tweets.add(new TweetEvent(tweet.getTweet(), true,tweet.getOldText()));
		return tweets;
	}

	public void postedReTweetsRC(double trainPercent) {
		List<TweetEvent> trainTweets = new ArrayList<TweetEvent>();
		Map<String, ParsedThinUser> reciprocalConnections = ModelsUtilities.getReciprocalConnections(userActivity);
		trainTweets.addAll(matchedUsersTweetsRT(trainPercent,reciprocalConnections));

		trainTweets.addAll(userReTweets());
		cleanEventTrainTweets(trainTweets);
		userData.setTrainTweets(trainTweets);
	}

	public void followeesFollowersTweetsRT(double trainPercent) {
		List<TweetEvent> trainTweets = new ArrayList<TweetEvent>();
		trainTweets.addAll(followersTweetsRT());
		//List<TweetEvent> trainTweets2 = convertToTweetEvent(trainTweets);
		trainTweets.addAll(matchedRetweetsUniform(trainPercent));
		userData.setTrainTweets(trainTweets);
	}

	public void postedReTweetsFolloweesTweetsRT(double trainPercent) {
		List<TweetEvent> trainTweets = new ArrayList<TweetEvent>();
		trainTweets.addAll(userReTweets());
		cleanEventTrainTweets(trainTweets);
		trainTweets.addAll(matchedRetweetsUniform(trainPercent));
		userData.setTrainTweets(trainTweets);
		// printUserDataStatistics();
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
		Map<String, ParsedThinUser> reciprocalConnections = ModelsUtilities.getReciprocalConnections(userActivity);
		trainTweets.addAll(matchedUsersTweetsRT(trainPercent,reciprocalConnections));
		cleanEventTrainTweets(trainTweets);
		userData.setTrainTweets(trainTweets);
	}

	private List<ParsedRetweet> usersReTweets(List<ParsedThinUser> users) {
		List<ParsedRetweet> tweets = new ArrayList<ParsedRetweet>();
		for (ParsedThinUser friend : users) {
			List<ParsedRetweet> retweets = friend.getOutgoingReTweets();
			for (ParsedRetweet retweet : retweets) {
				if (retweet.getTweet().getTimeStamp() < refTime)
					tweets.add(retweet);
			}
		}
		return tweets;
	}

	private List<ParsedTweet> usersTweets(List<ParsedThinUser> users) {
		List<ParsedTweet> etweets = new ArrayList<ParsedTweet>();
		for (ParsedThinUser friend : users) {
			// List<ThinTweet> tweets = friend.getOutgoingTweets();
			// for (ThinTweet tweet : tweets)
			// trainTweets.add(new TweetEvent(tweet, false));
			List<ParsedTweet> tweets = friend.getOutgoingTweets();
			for (ParsedTweet tweet : tweets)
				if (tweet.getTweet().getTimeStamp() < refTime)
					etweets.add(tweet);
		}
		return etweets;

	}

	public void postedTweetsRetweets(double trainPercent) {

		List<TweetEvent> trainTweets = new ArrayList<TweetEvent>();
		trainTweets.addAll(userReTweets());
		cleanEventTrainTweets(trainTweets);
		trainTweets.addAll(userTweets());
		userData.setTrainTweets(trainTweets);
	}

	public void postedTweets(double trainPercent) {
		userData.setTrainTweets(userTweets());
	}

	public void postedReTweets(double trainPercent) {
		List<TweetEvent> trainTweets = new ArrayList<TweetEvent>();
		trainTweets.addAll(userReTweets());

		cleanEventTrainTweets(trainTweets);
		userData.setTrainTweets(trainTweets);
	}

	public void postedReTweetsFollowersTweetsRT(double trainPercent) {
		List<TweetEvent> trainTweets = new ArrayList<TweetEvent>();
		trainTweets.addAll(userReTweets());
		cleanEventTrainTweets(trainTweets);
		trainTweets.addAll(followersTweetsRT());
		userData.setTrainTweets(trainTweets);
	}

	private void cleanTrainTweets(List<ParsedTweet> trainTweets) {
		Set<String> hashedTestTweets = new HashSet<String>();

		for (TweetEvent tevent : userData.getTestTweets()) {
			if (tevent.isRetweeted())
				hashedTestTweets.add(TwitterUtilities.getOriginalTweetFromRetweet(tevent.getOldText()));
		}
		Iterator<ParsedTweet> iterator = trainTweets.iterator();
		while (iterator.hasNext()) {
			ParsedTweet tevent = iterator.next();
			if (hashedTestTweets.contains(TwitterUtilities.getOriginalTweetFromRetweet(tevent.getOldText())))
				iterator.remove();
		}
	}
	private void cleanEventTrainTweets(List<TweetEvent> trainTweets) {
		Set<String> hashedTestTweets = new HashSet<String>();

		for (TweetEvent tevent : userData.getTestTweets()) {
			if (tevent.isRetweeted())
				hashedTestTweets.add(TwitterUtilities.getOriginalTweetFromRetweet(tevent.getOldText()));
		}
		Iterator<TweetEvent> iterator = trainTweets.iterator();
		while (iterator.hasNext()) {
			TweetEvent tevent = iterator.next();
			if (hashedTestTweets.contains(TwitterUtilities.getOriginalTweetFromRetweet(tevent.getOldText())))
				iterator.remove();
		}
	}

	public List<TweetEvent> userTweets() {
		List<TweetEvent> tweetsEvents = new ArrayList<TweetEvent>();
		List<ParsedTweet> tweets = userActivity.getOutgoingTweets();
		for (ParsedTweet tweet : tweets) {
			if (tweet.getTweet().getTimeStamp() < refTime)
				tweetsEvents.add(new TweetEvent(tweet.getTweet(), true,tweet.getOldText(),userActivity.getUserName()));
		}
		return tweetsEvents;
	}

	public List<TweetEvent> userReTweets() {
		List<ParsedRetweet> retweets = userActivity.getOutgoingReTweets();
		List<TweetEvent> retweetsEvents = new ArrayList<TweetEvent>();
		for (ParsedRetweet retweet : retweets) {
			if (retweet.getTweet().getTimeStamp() < refTime)
				retweetsEvents.add(new TweetEvent(retweet.getTweet(), true,retweet.getOldText(),userActivity.getUserName()));
		}
		return retweetsEvents;
	}

	public List<ParsedTweet> userPTweets() {
		List<ParsedTweet> tweets = userActivity.getOutgoingTweets();
		List<ParsedTweet> trainTweets = new ArrayList<ParsedTweet>();
		for (ParsedTweet tweet : tweets) {
			if (tweet.getTweet().getTimeStamp() < refTime)
				trainTweets.add(tweet);
		}
		return trainTweets;
	}

	public List<ParsedRetweet> userPReTweets() {
		List<ParsedRetweet> tweets = userActivity.getOutgoingReTweets();
		List<ParsedRetweet> trainTweets = new ArrayList<ParsedRetweet>();
		for (ParsedRetweet tweet : tweets) {
			if (tweet.getTweet().getTimeStamp() < refTime)
				trainTweets.add(tweet);
		}
		return trainTweets;
	}

	private List<ParsedTweet> followersTweetsRTP() {
		List<ParsedTweet> follTweets = new ArrayList<ParsedTweet>();
		HashMap<String, ParsedThinUser> followers = userActivity.getFollowers();
		for (Map.Entry<String, ParsedThinUser> entry : followers.entrySet()) {

			List<ParsedTweet> inTweets = entry.getValue().getOutgoingTweets();
			for (ParsedTweet intweet : inTweets) {
				if (intweet.getTweet().getTimeStamp() < refTime)
					follTweets.add(intweet);
			}

			List<ParsedRetweet> inReTweets = entry.getValue().getOutgoingReTweets();
			for (ParsedRetweet inretweet : inReTweets) {
				if (inretweet.getTweet().getTimeStamp() < refTime)
					follTweets.add(inretweet);

			}
		}
		return follTweets;
	}
	private List<TweetEvent> followersTweetsRT() {
		List<TweetEvent> follTweets = new ArrayList<TweetEvent>();
		HashMap<String, ParsedThinUser> followers = userActivity.getFollowers();
		for (Map.Entry<String, ParsedThinUser> entry : followers.entrySet()) {

			List<ParsedTweet> inTweets = entry.getValue().getOutgoingTweets();
			for (ParsedTweet intweet : inTweets) {
				if (intweet.getTweet().getTimeStamp() < refTime)
					follTweets.add(new TweetEvent(intweet.getTweet(), true,intweet.getOldText(),entry.getKey()));
			}

			List<ParsedRetweet> inReTweets = entry.getValue().getOutgoingReTweets();
			for (ParsedRetweet inretweet : inReTweets) {
				if (inretweet.getTweet().getTimeStamp() < refTime)
					follTweets.add(new TweetEvent(inretweet.getTweet(), true,inretweet.getOldText(),entry.getKey()));

			}
		}
		return follTweets;
	}
	public void followersTweetsRetweets(double trainPercent) {
		userData.setTrainTweets(followersTweetsRT());
	}

	public void followeesTweetsRetweets(double trainPercent) {
		userData.setTrainTweets(matchedRetweetsUniform(trainPercent));
	}

	private List<TweetEvent> matchedUsersTweetsRT(double trainPercent, Map<String, ParsedThinUser> users) {
		List<TweetEvent> tweetsMatched = new ArrayList<TweetEvent>();
		List<TweetEvent> tweetsNotMatched = new ArrayList<TweetEvent>();
		List<TweetEvent> tweetsTemp = new ArrayList<TweetEvent>();

		List<ParsedRetweet> outRetweets = userActivity.getOutgoingReTweets();
		for (int i = 0; i < outRetweets.size(); i++) {

			String originalTweet = TwitterUtilities.getOriginalTweetFromRetweet(outRetweets.get(i).getOldText());
			hashedTweets.add(originalTweet);
		}

		HashMap<String, ParsedThinUser> followees = userActivity.getFollowees();
		for (Map.Entry<String, ParsedThinUser> entry : followees.entrySet()) {

			if (users.containsKey(entry.getKey())) {
				List<ParsedTweet> inTweets = entry.getValue().getOutgoingTweets();
				for (ParsedTweet intweet : inTweets) {
					if (hashedTweets.contains(intweet.getOldText()))
						tweetsMatched.add(new TweetEvent(intweet.getTweet(), true,intweet.getOldText(),entry.getKey()));
					else
						tweetsNotMatched.add(new TweetEvent(intweet.getTweet(), false,intweet.getOldText(),entry.getKey()));
				}

				List<ParsedRetweet> inReTweets = entry.getValue().getOutgoingReTweets();
				for (ParsedRetweet inretweet : inReTweets) {
					String originalTweet = TwitterUtilities.getOriginalTweetFromRetweet(inretweet.getOldText());
					if (hashedTweets.contains(originalTweet))
						tweetsMatched.add(new TweetEvent(inretweet.getTweet(), true,inretweet.getOldText(),entry.getKey()));
					else
						tweetsNotMatched.add(new TweetEvent(inretweet.getTweet(), false,inretweet.getOldText(),entry.getKey()));
				}
			}
		}
		for (TweetEvent tweet : tweetsMatched) {
			if (tweet.getThinTweet().getTimeStamp() < refTime)
				tweetsTemp.add(tweet);
		}
		for (TweetEvent tweet : tweetsNotMatched) {
			if (tweet.getThinTweet().getTimeStamp() < refTime)
				tweetsTemp.add(tweet);
		}
		return tweetsTemp;
	}

	private List<TweetEvent> matchedRetweetsUniform(double trainPercent) {
		List<TweetEvent> tweetsMatched = new ArrayList<TweetEvent>();
		List<TweetEvent> tweetsNotMatched = new ArrayList<TweetEvent>();
		List<TweetEvent> tweetsTemp = new ArrayList<TweetEvent>();

		List<ParsedRetweet> outRetweets = userActivity.getOutgoingReTweets();
		for (int i = 0; i < outRetweets.size(); i++) {

			String originalTweet = TwitterUtilities.getOriginalTweetFromRetweet(outRetweets.get(i).getOldText());
			hashedTweets.add(originalTweet);
		}
		int dupls = 0;
		HashMap<String, ParsedThinUser> followees = userActivity.getFollowees();
		for (Map.Entry<String, ParsedThinUser> entry : followees.entrySet()) {
			// Set<String> hashedtemp = new HashSet<String>();

			List<ParsedTweet> inTweets = entry.getValue().getOutgoingTweets();
			// for (ThinTweet tevent : inTweets) {
			// hashedtemp.add(tevent.getText());
			// }

			for (ParsedTweet intweet : inTweets) {
				if (hashedTweets.contains(intweet.getOldText()))
					tweetsMatched.add(new TweetEvent(intweet.getTweet(), true,intweet.getOldText(),entry.getKey()));
				else
					tweetsNotMatched.add(new TweetEvent(intweet.getTweet(), false,intweet.getOldText(),entry.getKey()));
			}

			List<ParsedRetweet> inReTweets = entry.getValue().getOutgoingReTweets();
			// for (ReTweet tevent : inReTweets) {
			// hashedtemp.add(TwitterUtilities.getOriginalTweetFromRetweet(tevent.getText()));
			// }
			// dupls += inTweets.size() + inReTweets.size() - hashedtemp.size();
			for (ParsedRetweet inretweet : inReTweets) {
				String originalTweet = TwitterUtilities.getOriginalTweetFromRetweet(inretweet.getOldText());
				if (hashedTweets.contains(originalTweet))
					tweetsMatched.add(new TweetEvent(inretweet.getTweet(), true,inretweet.getOldText(),entry.getKey()));
				else
					tweetsNotMatched.add(new TweetEvent(inretweet.getTweet(), false,inretweet.getOldText(),entry.getKey()));
			}
		}
		for (TweetEvent tweet : tweetsMatched) {
			if (tweet.getThinTweet().getTimeStamp() < refTime)
				tweetsTemp.add(tweet);
		}
		for (TweetEvent tweet : tweetsNotMatched) {
			if (tweet.getThinTweet().getTimeStamp() < refTime)
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

package twitter.modelUtilities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import twitter.TestDataCreator;
import twitter.TrainDataCreator;
import twitter.DataStructures.ParsedRetweet;
import twitter.DataStructures.ParsedThinUser;
import twitter.DataStructures.ParsedTweet;
import twitter.DataStructures.ParsedUserActivity;
import twitter.DataStructures.ReTweet;
import twitter.DataStructures.ThinTweet;
import twitter.DataStructures.ThinUser;
import twitter.DataStructures.TweetEvent;
import twitter.DataStructures.UserActivity;
import twitter.DataStructures.UserData;
import twitter.Utilities.Constants;
import twitter.Utilities.Twitter.TwitterUtilities;

public class ModelsUtilities implements Constants {
	public static String getUserTrainProfile(UserData userData) {
		List<TweetEvent> tweets = userData.getTrainTweets();
		String userProfile = "";
		for (TweetEvent tevent : tweets) {
			userProfile += tevent.getThinTweet().getText() + " ";
		}
		return userProfile;
	}

	public static void printUserTweets(UserActivity userActivity) {
		List<ThinTweet> outTweets = userActivity.getOutgoingTweets();
		for (int i = 0; i < outTweets.size() / 8; i++) {
			// System.out.println("username: "+userActivity.getUserName());
			System.out.println(outTweets.get(i).getText());

		}
	}

	public static int getIncomingFreq(UserActivity ua) {
		return followeesTweets(ua).size();
	}
	public static int getOutgoingFreq(UserActivity ua) {
		return ua.getOutgoingReTweets().size()+ua.getOutgoingTweets().size();
	}
	public static int getIncomingFreq(ParsedUserActivity ua) {
		return followeesTweets(ua).size();
	}
	public static int getOutgoingFreq(ParsedUserActivity ua) {
		return ua.getOutgoingReTweets().size()+ua.getOutgoingTweets().size();
	}
	public static List<String> getAllCorpus(List<UserActivity> acts) {
		List<ThinTweet> allTweets = new ArrayList<ThinTweet>();
		List<String> corpus = new ArrayList<String>();
		for (UserActivity ua : acts) {
			TrainDataCreator tr = new TrainDataCreator(ua, null,0);
			allTweets.addAll(ua.getOutgoingTweets());
			allTweets.addAll(ua.getOutgoingReTweets());
			allTweets.addAll(followersTweets(ua));
			allTweets.addAll(followeesTweets(ua));
		}
		for(ThinTweet tweet:allTweets){
			corpus.add(tweet.getText().toLowerCase());
		}
		return corpus;
	}
	public static List<String> getAllCorpusWithoutTestsets(List<UserActivity> acts) {
		List<ThinTweet> allTweets = new ArrayList<ThinTweet>();
		List<String> corpus = new ArrayList<String>();
		List<TweetEvent> testTweets=new ArrayList<TweetEvent>();
		for (UserActivity ua : acts) {
			UserData userData=new UserData(ua.getUserName());
			TestDataCreator creator=new TestDataCreator(ua,userData);
			creator.computeTestTweets(0.8);
			//testTweets.addAll(userData.getTestTweets());
			allTweets.addAll(getTrainTweets(ua,creator.getRefTime()));
			//allTweets.addAll(ua.getOutgoingReTweets());
			//allTweets.addAll(followersTweets(ua));
			//allTweets.addAll(followeesTweets(ua));
			
		}
		//removeTestTweets(allTweets,testTweets);
		//removeTestTweets();
		for(ThinTweet tweet:allTweets){
			corpus.add(tweet.getText());
		}
		return corpus;
	}
	private static void removeTestTweets(List<ThinTweet> tweets,List<TweetEvent> testTweets) {
		Set<String> hashedTestTweets = new HashSet<String>();
		for (TweetEvent tevent : testTweets) {
			if (tevent.isRetweeted())
				hashedTestTweets.add(TwitterUtilities.getOriginalTweetFromRetweet(tevent.getThinTweet().getText()));
		}
		Iterator<ThinTweet> iterator = tweets.iterator();
		while (iterator.hasNext()) {
			ThinTweet tevent = iterator.next();
			if (hashedTestTweets
					.contains(TwitterUtilities.getOriginalTweetFromRetweet(tevent.getText())))
				iterator.remove();
		}
	}
	public static List<ThinTweet> getTrainTweets(UserActivity ua,long refTime) {
		List<ThinTweet> allTweets = new ArrayList<ThinTweet>();
		List<ThinTweet> tweets=ua.getOutgoingTweets();
		for(ThinTweet tweet:tweets)
		{
			if(tweet.getTimeStamp()<refTime)
				allTweets.add(tweet);
		}
		List<ReTweet> retweets=ua.getOutgoingReTweets();
		for(ThinTweet retweet:retweets)
		{
			if(retweet.getTimeStamp()<refTime)
				allTweets.add(retweet);
		}
		 List<ThinTweet> folleTweets=followeesTweets(ua);
		 for(ThinTweet tweet:folleTweets)
			{
				if(tweet.getTimeStamp()<refTime)
					allTweets.add(tweet);
			}
		 List<ThinTweet> follTweets=followersTweets(ua);
		 for(ThinTweet tweet:follTweets)
			{
				if(tweet.getTimeStamp()<refTime)
					allTweets.add(tweet);
			}
		
		return allTweets;
	}
	public static List<String> getAllCorpusP(List<ParsedUserActivity> acts) {
		List<ParsedTweet> allTweets = new ArrayList<ParsedTweet>();
		List<String> corpus = new ArrayList<String>();
		for (ParsedUserActivity ua : acts) {
			allTweets.addAll(ua.getOutgoingTweets());
			allTweets.addAll(ua.getOutgoingReTweets());
			allTweets.addAll(followersTweets(ua));
			allTweets.addAll(followeesTweets(ua));
		}
		for(ParsedTweet tweet:allTweets){
			corpus.add(tweet.getTweet().getText());
		}
		return corpus;
	}
	public static List<ThinTweet> followersTweets(UserActivity userActivity) {
		List<ThinTweet> follTweets = new ArrayList<ThinTweet>();
		HashMap<String, ThinUser> followers = userActivity.getFollowers();
		for (Map.Entry<String, ThinUser> entry : followers.entrySet()) {

			follTweets.addAll(entry.getValue().getOutgoingTweets());
			follTweets.addAll(entry.getValue().getOutgoingReTweets());
		}
		return follTweets;
	}
	public static List<ParsedTweet> followersTweets(ParsedUserActivity userActivity) {
		List<ParsedTweet> follTweets = new ArrayList<ParsedTweet>();
		HashMap<String, ParsedThinUser> followers = userActivity.getFollowers();
		for (Map.Entry<String, ParsedThinUser> entry : followers.entrySet()) {

			follTweets.addAll(entry.getValue().getOutgoingTweets());
			follTweets.addAll(entry.getValue().getOutgoingReTweets());
		}
		return follTweets;
	}

	public static List<ThinTweet> followeesTweets(UserActivity userActivity) {
		List<ThinTweet> follTweets = new ArrayList<ThinTweet>();
		HashMap<String, ThinUser> followees = userActivity.getFollowees();
		for (Map.Entry<String, ThinUser> entry : followees.entrySet()) {

			follTweets.addAll(entry.getValue().getOutgoingTweets());
			follTweets.addAll(entry.getValue().getOutgoingReTweets());
		}
		return follTweets;
	}
	public static List<ParsedTweet> followeesTweets(ParsedUserActivity userActivity) {
		List<ParsedTweet> follTweets = new ArrayList<ParsedTweet>();
		HashMap<String, ParsedThinUser> followers = userActivity.getFollowees();
		for (Map.Entry<String, ParsedThinUser> entry : followers.entrySet()) {

			follTweets.addAll(entry.getValue().getOutgoingTweets());
			follTweets.addAll(entry.getValue().getOutgoingReTweets());
		}
		return follTweets;
	}
	public static Map<String, ThinUser> getReciprocalConnections(UserActivity userActivity) {
		Map<String, ThinUser> reciprocalConnections = new HashMap<String, ThinUser>();
		Map<String, ThinUser> followees = userActivity.getFollowees();
		Map<String, ThinUser> followers = userActivity.getFollowers();
		for (String followee : followees.keySet()) {
			if (followers.containsKey(followee))
				reciprocalConnections.put(followee, followees.get(followee));
		}

		return reciprocalConnections;
	}
	public static Map<String, ParsedThinUser> getReciprocalConnections(ParsedUserActivity userActivity) {
		Map<String, ParsedThinUser> reciprocalConnections = new HashMap<String, ParsedThinUser>();
		Map<String, ParsedThinUser> followees = userActivity.getFollowees();
		Map<String, ParsedThinUser> followers = userActivity.getFollowers();
		for (String followee : followees.keySet()) {
			if (followers.containsKey(followee))
				reciprocalConnections.put(followee, followees.get(followee));
		}

		return reciprocalConnections;
	}

	public static int matchedRetweets(UserActivity userActivity) {
		int retweets = 0;
		
		String[] retUserNames = null;
		
		Set<String> hashedTweets = new HashSet<String>();
		List<ReTweet> outRetweets = userActivity.getOutgoingReTweets();
		for (int i = 0; i < outRetweets.size(); i++) {
			String originalTweet = TwitterUtilities.getOriginalTweetFromRetweet(outRetweets.get(i).getText());
			hashedTweets.add(originalTweet);
		}
		HashMap<String, ThinUser> followees = userActivity.getFollowees();
		for (Map.Entry<String, ThinUser> entry : followees.entrySet()) {

			List<ThinTweet> inTweets = entry.getValue().getOutgoingTweets();
			for (ThinTweet intweet : inTweets) {
				if (hashedTweets.contains(intweet.getText()))
					retweets++;
			}

			List<ReTweet> inReTweets = entry.getValue().getOutgoingReTweets();
			for (ThinTweet inretweet : inReTweets) {
				String originalTweet = TwitterUtilities.getOriginalTweetFromRetweet(inretweet.getText());
				if (hashedTweets.contains(originalTweet))
					retweets++;

			}

		}
		return retweets;
	}

	public static int userStatistics(UserActivity userActivity) {
		Set<String> hashedTweets = new HashSet<String>();
		List<ReTweet> outRetweets = userActivity.getOutgoingReTweets();
		List<ThinTweet> inTweetsAll = new ArrayList<ThinTweet>();
		for (int i = 0; i < outRetweets.size(); i++) {
			String originalTweet = TwitterUtilities.getOriginalTweetFromRetweet(outRetweets.get(i).getText());
			hashedTweets.add(originalTweet);
		}
		int retweets = 0;
		int followeesT = 0;
		int followersT = 0;
		HashMap<String, ThinUser> followees = userActivity.getFollowees();
		for (Map.Entry<String, ThinUser> entry : followees.entrySet()) {

			List<ThinTweet> inTweets = entry.getValue().getOutgoingTweets();
			inTweetsAll.addAll(inTweets);
			for (ThinTweet intweet : inTweets) {
				if (hashedTweets.contains(intweet.getText()))
					retweets++;
			}

			List<ReTweet> inReTweets = entry.getValue().getOutgoingReTweets();
			inTweetsAll.addAll(inReTweets);
			for (ThinTweet inretweet : inReTweets) {
				String originalTweet = TwitterUtilities.getOriginalTweetFromRetweet(inretweet.getText());
				if (hashedTweets.contains(originalTweet))
					retweets++;

			}
			followeesT += inTweets.size();
			followeesT += inReTweets.size();
		}
		HashMap<String, ThinUser> followers = userActivity.getFollowers();
		for (Map.Entry<String, ThinUser> entry : followers.entrySet()) {
			followersT += entry.getValue().getOutgoingTweets().size();
			followersT += entry.getValue().getOutgoingReTweets().size();
		}
		List<ThinTweet> outTweets = new ArrayList<ThinTweet>();
		outTweets.addAll(userActivity.getOutgoingTweets());
		outTweets.addAll(userActivity.getOutgoingReTweets());
		Collections.sort(outTweets);

		Collections.sort(inTweetsAll);
		System.out.println("username: " + userActivity.getUserName());
		System.out.println("follet :" + followeesT);
		System.out.println("follt :" + followersT);
		System.out.println("retweets :" + outRetweets.size());
		System.out.println("tws :" + userActivity.getOutgoingTweets().size());
		System.out.println("outall :" + (outRetweets.size() + userActivity.getOutgoingTweets().size()));

		System.out.println("matched: " + retweets);
		System.out.println("tfd\t "
				+ DEFAULT_DATE_FORMAT.format(new Date(outTweets.get(0).getTimeStamp())));
		System.out.println("tld\t "
				+ DEFAULT_DATE_FORMAT.format(new Date(outTweets.get(outTweets.size()-1).getTimeStamp())));
		System.out.println("ifd\t "
				+ DEFAULT_DATE_FORMAT.format(new Date(inTweetsAll.get(0).getTimeStamp())));
		System.out.println("ild\t "
				+ DEFAULT_DATE_FORMAT.format(new Date(inTweetsAll.get(inTweetsAll.size() - 1).getTimeStamp())));
		System.out.println("folle2: " + followees.size());
		System.out.println("foll2: " + followers.size());
		System.out.println(" ");
		//

		return retweets;
	}
	public static int userStatisticsP(ParsedUserActivity userActivity) {
		Set<String> hashedTweets = new HashSet<String>();
		List<ParsedRetweet> outRetweets = userActivity.getOutgoingReTweets();
		List<ParsedTweet> inTweetsAll = new ArrayList<ParsedTweet>();
		for (int i = 0; i < outRetweets.size(); i++) {
			String originalTweet = TwitterUtilities.getOriginalTweetFromRetweet(outRetweets.get(i).getOldText());
			hashedTweets.add(originalTweet);
		}
		int retweets = 0;
		int followeesT = 0;
		int followersT = 0;
		HashMap<String, ParsedThinUser> followees = userActivity.getFollowees();
		for (Map.Entry<String, ParsedThinUser> entry : followees.entrySet()) {

			List<ParsedTweet> inTweets = entry.getValue().getOutgoingTweets();
			inTweetsAll.addAll(inTweets);
			for (ParsedTweet intweet : inTweets) {
				if (hashedTweets.contains(intweet.getOldText()))
					retweets++;
			}

			List<ParsedRetweet> inReTweets = entry.getValue().getOutgoingReTweets();
			inTweetsAll.addAll(inReTweets);
			for (ParsedTweet inretweet : inReTweets) {
				String originalTweet = TwitterUtilities.getOriginalTweetFromRetweet(inretweet.getOldText());
				if (hashedTweets.contains(originalTweet))
					retweets++;

			}
			followeesT += inTweets.size();
			followeesT += inReTweets.size();
		}
		HashMap<String, ParsedThinUser> followers = userActivity.getFollowers();
		for (Map.Entry<String, ParsedThinUser> entry : followers.entrySet()) {
			followersT += entry.getValue().getOutgoingTweets().size();
			followersT += entry.getValue().getOutgoingReTweets().size();
		}
		List<ParsedTweet> outTweets = new ArrayList<ParsedTweet>();
		outTweets.addAll(userActivity.getOutgoingTweets());
		outTweets.addAll(userActivity.getOutgoingReTweets());
		Collections.sort(outTweets);

		Collections.sort(inTweetsAll);
		System.out.println("username: " + userActivity.getUserName());
		System.out.println("followees' tweets :" + followeesT);
		System.out.println("followers' tweets :" + followersT);
		System.out.println("retweets :" + outRetweets.size());
		System.out.println("tweets :" + userActivity.getOutgoingTweets().size());
		System.out.println("outall :" + (outRetweets.size() + userActivity.getOutgoingTweets().size()));

		System.out.println("matched: " + retweets);
		System.out.println("tweets: first date: "
				+ DEFAULT_DATE_FORMAT.format(new Date(outTweets.get(0).getTweet().getTimeStamp())) + " last date: "
				+ DEFAULT_DATE_FORMAT.format(new Date(outTweets.get(outTweets.size() - 1).getTweet().getTimeStamp())));
		System.out.println("in tweets: first date: "
				+ DEFAULT_DATE_FORMAT.format(new Date(inTweetsAll.get(0).getTweet().getTimeStamp())) + " last date: "
				+ DEFAULT_DATE_FORMAT.format(new Date(inTweetsAll.get(inTweetsAll.size() - 1).getTweet().getTimeStamp())));
		System.out.println("followees: " + followees.size());
		System.out.println("followers: " + followers.size());
		System.out.println(" ");
		//

		return retweets;
	}
	public static void printUserDataStatistics(UserData userData) {
		System.out.println("test dataset size is: " + userData.getTestTweets().size());
		System.out.println("train dataset size is: " + userData.getTrainTweets().size());
		int positive = 0;
		for (TweetEvent tevent : userData.getTestTweets()) {
			if (tevent.isRetweeted())
				positive++;

		}
		System.out.println("test positives: " + positive);
		System.out.println("test negatives: " + (userData.getTestTweets().size() - positive));
	}

	public static void printTrainSetStatistics(UserData userData, ModelInfoSource type) {
		System.out.println(
				"train_" + type.name() + "_" + userData.getUserName() + ": " + userData.getTrainTweets().size());
	}

	public static void printTestSetStatistics(UserData userData) {
		//System.out.println("test" + "_" + userData.getUserName() + ": " + userData.getTestTweets().size());
		int positive = 0;
		for (TweetEvent tevent : userData.getTestTweets()) {
			if (tevent.isRetweeted())
				positive++;

		}
		System.out.println("test pos_" + userData.getUserName() + ": " + positive+":test neg_" + userData.getUserName() + ": " + (userData.getTestTweets().size() - positive));
	//	System.out.println("test neg_" + userData.getUserName() + ": " + (userData.getTestTweets().size() - positive));
	}
	
	public static void checkUserData(UserData userData) {
		System.out.println(userData.getUserName() + ": train " + userData.getTrainTweets().size()+" test "+ userData.getTestTweets().size());
		List<TweetEvent> tweets=userData.getTrainTweets();
		List<TweetEvent> testtweets=userData.getTestTweets();
		Collections.sort(tweets);
		Collections.sort(testtweets);
		System.out.println(DEFAULT_DATE_FORMAT.format(new Date(tweets.get(tweets.size()-1).getThinTweet().getTimeStamp())));
		
		for (TweetEvent tevent : testtweets) {
			
			System.out.println(DEFAULT_DATE_FORMAT.format(new Date(tevent.getThinTweet().getTimeStamp())));

		}
		int positive = 0;
		for (TweetEvent tevent : userData.getTestTweets()) {
			if (tevent.isRetweeted())
				positive++;

		}
		System.out.println("test pos_" + userData.getUserName() + ": " + positive);
		System.out.println("test neg_" + userData.getUserName() + ": " + (userData.getTestTweets().size() - positive));
	}
}

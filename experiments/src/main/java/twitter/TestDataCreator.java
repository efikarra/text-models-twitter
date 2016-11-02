package twitter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import twitter.DataStructures.Twitter.ReTweet;
import twitter.DataStructures.Twitter.ThinTweet;
import twitter.DataStructures.Twitter.ThinUser;
import twitter.DataStructures.Twitter.TweetEvent;
import twitter.DataStructures.Twitter.UserActivity;
import twitter.DataStructures.Twitter.UserData;
import twitter.Utilities.Twitter.TwitterUtilities;

public class TestDataCreator {
	private UserActivity userActivity;
	private final UserData userData;
	Set<String> hashedTweets = new HashSet<String>();
	private long refTime;

	public TestDataCreator(UserActivity userActivity, UserData userData) {
		this.userActivity = userActivity;
		this.userData = userData;
	}

	public void computeTestTweets(double trainPercent) {
		List<TweetEvent> tempTweets = matchedRetweetsNew(trainPercent);
		//int trainSize = (int) Math.ceil((double) trainPercent * (double) tempTweets.size());
		//userData.setTestTweets(tempTweets.subList(trainSize, tempTweets.size()));
		userData.setTestTweets(tempTweets);
	}
	public void findTestTweets(List<TweetEvent> testTweets) {
		List<TweetEvent> tweets=findFolloweesTweets(testTweets);
		Collections.sort(tweets);
		refTime=tweets.get(0).getThinTweet().getTimeStamp();
		userData.setTestTweets(tweets);
	}
	public void changeTextWithOldText(List<TweetEvent> testTweets) {
		List<TweetEvent> tweets=new ArrayList<TweetEvent>();
		for(TweetEvent tevent:testTweets){
			tweets.add(new TweetEvent(tevent.getThinTweet(),tevent.isRetweeted(),tevent.getOldText(),tevent.getUser()));
		}
		Collections.sort(tweets);
		refTime=tweets.get(0).getThinTweet().getTimeStamp();
		userData.setTestTweets(tweets);
	}
	private List<TweetEvent> matchedRetweetsNew(double trainPercent) {
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
		List<TweetEvent> tweetsAll = new ArrayList<TweetEvent>();
		tweetsAll.addAll(tweetsMatched);
		tweetsAll.addAll(tweetsNotMatched);
		Collections.sort(tweetsAll);

		int posSize = (int) Math.ceil((double) trainPercent * (double) tweetsMatched.size());
		List<TweetEvent> tweetsMSubset = tweetsMatched.subList(posSize, tweetsMatched.size());
		List<TweetEvent> tweetsNMSubset = new ArrayList<TweetEvent>();
		refTime = tweetsMSubset.get(0).getThinTweet().getTimeStamp();

		for (TweetEvent tweet : tweetsNotMatched) {
			if (tweet.getThinTweet().getTimeStamp() > refTime)
				tweetsNMSubset.add(tweet);
		}
		Collections.shuffle(tweetsNMSubset);
		int offset=0;
		if (tweetsNMSubset.size() <= tweetsMSubset.size() * 4) {
			tweetsTemp.addAll(tweetsMSubset);
			tweetsTemp.addAll(tweetsNMSubset);
		} else {
			for (TweetEvent tweet : tweetsMSubset) {
				tweetsTemp.add(tweet);
				for (int i = offset; i < offset+4; i++)
					tweetsTemp.add(tweetsNMSubset.get(i));
				offset+=4;
			}
		}

		//System.out.println("all positives: " + tweetsMatched.size());
		//System.out.println("all negatives: " + tweetsNotMatched.size());
		return tweetsTemp;
		
//		List<TweetEvent> tweetsMatchedTrain = tweetsMatched.subList(0, posSize);
//		List<TweetEvent> tweetsNotMatchedTrain = tweetsNotMatched.subList(0, negSize);
//		List<TweetEvent> tweetsMatchedTest = tweetsMatched.subList(posSize, tweetsMatched.size());
//		List<TweetEvent> tweetsNotMatchedTest = tweetsNotMatched.subList(negSize, tweetsNotMatched.size());
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

//		int window=10;
//		int i = 0;
//		int k = 0;
//		int groupsize = 0;
////		Collections.shuffle(tweetsNotMatched);
////		tweetsTemp.addAll(tweetsMatched);
////		tweetsTemp.addAll(tweetsNotMatched.subList(0, negSize));
//		List<TweetEvent> allTweets=new ArrayList<TweetEvent>();
//		allTweets.addAll(tweetsNotMatched);
//		allTweets.addAll(tweetsMatched);
//		TreeMap<Long,TweetEvent> mapNotMatched=new TreeMap<Long,TweetEvent>();
//		
//		for(TweetEvent tweet:allTweets){
//			mapNotMatched.put(tweet.getThinTweet().getTimeStamp(), tweet);
//		}
//		while (i<tweetsMatched.size()) {
//			
//			Long matchedTime=tweetsMatched.get(i).getThinTweet().getTimeStamp();
//			int position=new Random().nextInt(10);
//			int prev=0;
//			Entry<Long,TweetEvent> entry=null;
//			Long currTime=matchedTime;
//			while(prev<position){
//				entry=mapNotMatched.lowerEntry(currTime);
//				if(entry.getValue().isRetweeted())
//					System.out.println("conflict");
//				currTime=entry.getKey();
//				prev++;
//				tweetsTemp.add(entry.getValue());
//			}
//			tweetsTemp.add(tweetsMatched.get(i));
//			int next=0;
//			while(next<window-position-1){
//				entry=mapNotMatched.higherEntry(currTime);
//				currTime=entry.getKey();
//				next++;
//				tweetsTemp.add(entry.getValue());
//			}
//			i++;
//		}
//		//tweetsTemp.addAll(tweetsMatched.subList(j, tweetsMatched.size()));
//		//System.out.println("all tweets: " + allTweets);
//		System.out.println("all positives: " + tweetsMatched.size());
//		System.out.println("all negatives: " + tweetsNotMatched.size());
////		int negSize = (int) Math.ceil((double) trainPercent * (double) tweetsNotMatched.size());
////		int posSize = (int) Math.ceil((double) trainPercent * (double) tweetsMatched.size());
////		tweetsTemp.addAll(tweetsNotMatched.subList(0, negSize));
////		tweetsTemp.addAll(tweetsMatched.subList(0, posSize));
////
////		tweetsTemp.addAll(tweetsMatched.subList(posSize, tweetsMatched.size()));
////		tweetsTemp.addAll(tweetsNotMatched.subList(negSize, tweetsNotMatched.size()));
//		// System.out.println("all positives: " + tweetsMatched.size());
//		// System.out.println("all negatives: " + tweetsNotMatched.size());
//		return tweetsTemp;
	}
	private List<TweetEvent> findFolloweesTweets(List<TweetEvent> tweets) {
		Map<String,TweetEvent> hashedTweets=new HashMap<String,TweetEvent>();
		for(TweetEvent tevent:tweets)
			hashedTweets.put(tevent.getOldText(),tevent);
		Map<String,TweetEvent> tweetsTemp = new HashMap<String,TweetEvent>();
		HashMap<String, ThinUser> followees = userActivity.getFollowees();
		for (Map.Entry<String, ThinUser> entry : followees.entrySet()) {

			List<ThinTweet> inTweets = entry.getValue().getOutgoingTweets();
			for (ThinTweet intweet : inTweets) {
				TweetEvent event=hashedTweets.get(intweet.getText());
				if (event!=null){
					tweetsTemp.put(event.getOldText(),event);
				}
		
			}

			List<ReTweet> inReTweets = entry.getValue().getOutgoingReTweets();
			for (ThinTweet inretweet : inReTweets) {
				TweetEvent event=hashedTweets.get(inretweet.getText());
				if (event!=null){
					tweetsTemp.put(event.getOldText(),event);
				}
			}
		}
		return new ArrayList<TweetEvent>(tweetsTemp.values());
		
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

	public long getRefTime() {
		return refTime;
	}

	public void setRefTime(long refTime) {
		this.refTime = refTime;
	}
}

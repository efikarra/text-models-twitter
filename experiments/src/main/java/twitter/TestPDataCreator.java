package twitter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
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

public class TestPDataCreator {
	private ParsedUserActivity userActivity;
	private final UserData userData;
	Set<String> hashedTweets = new HashSet<String>();
	private long refTime;
	private List<TweetEvent> matchedTweets;
	private List<TweetEvent> notMatchedTweets;

	public TestPDataCreator(ParsedUserActivity userActivity, UserData userData) {
		this.userActivity = userActivity;
		this.userData = userData;
	}

	public void getTestTweets(double trainPercent) {
		List<TweetEvent> tempTweets = matchedRetweetsNew(trainPercent);
		//int trainSize = (int) Math.ceil((double) trainPercent * (double) tempTweets.size());
		userData.setTestTweets(tempTweets);
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
			List<ParsedTweet> inTweets = entry.getValue().getOutgoingTweets();
			for (ParsedTweet intweet : inTweets) {
				if (hashedTweets.contains(intweet.getOldText()))
					tweetsMatched.add(new TweetEvent(intweet.getTweet(), true, intweet.getOldText()));
				else
					tweetsNotMatched.add(new TweetEvent(intweet.getTweet(), false, intweet.getOldText()));
			}

			List<ParsedRetweet> inReTweets = entry.getValue().getOutgoingReTweets();
			// for (ReTweet tevent : inReTweets) {
			// hashedtemp.add(TwitterUtilities.getOriginalTweetFromRetweet(tevent.getText()));
			// }
			// dupls += inTweets.size() + inReTweets.size() - hashedtemp.size();
			for (ParsedRetweet inretweet : inReTweets) {
				String originalTweet = TwitterUtilities.getOriginalTweetFromRetweet(inretweet.getOldText());
				if (hashedTweets.contains(originalTweet))
					tweetsMatched.add(new TweetEvent(inretweet.getTweet(), true, inretweet.getOldText()));
				else
					tweetsNotMatched.add(new TweetEvent(inretweet.getTweet(), false, inretweet.getOldText()));
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

	private List<TweetEvent> matchedRetweetsNew(double trainPercent) {
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
			List<ParsedTweet> inTweets = entry.getValue().getOutgoingTweets();

			for (ParsedTweet intweet : inTweets) {
				if (hashedTweets.contains(intweet.getOldText()))
					tweetsMatched.add(new TweetEvent(intweet.getTweet(), true, intweet.getOldText(),entry.getKey()));
				else
					tweetsNotMatched.add(new TweetEvent(intweet.getTweet(), false, intweet.getOldText(),entry.getKey()));
			}

			List<ParsedRetweet> inReTweets = entry.getValue().getOutgoingReTweets();
			for (ParsedRetweet inretweet : inReTweets) {
				String originalTweet = TwitterUtilities.getOriginalTweetFromRetweet(inretweet.getOldText());
				if (hashedTweets.contains(originalTweet))
					tweetsMatched.add(new TweetEvent(inretweet.getTweet(), true, inretweet.getOldText(),entry.getKey()));
				else
					tweetsNotMatched.add(new TweetEvent(inretweet.getTweet(), false, inretweet.getOldText(),entry.getKey()));
			}
		}
		Collections.sort(tweetsMatched);
		//Collections.sort(tweetsNotMatched);
		//Collections.sort(tweetsAll);
	//	int posSize = (int) Math.ceil((double) trainPercent * (double) tweetsAll.size());
		//tweetsTemp.addAll(tweetsAll.subList(posSize, tweetsAll.size()-1));
		//refTime=tweetsTemp.get(0).getThinTweet().getTimeStamp();
		int posSize = (int) Math.ceil((double) trainPercent * (double) tweetsMatched.size());
		List<TweetEvent> tweetsMSubset = tweetsMatched.subList(posSize, tweetsMatched.size() );
		List<TweetEvent> tweetsNMSubset = new ArrayList<TweetEvent>();
		refTime = tweetsMSubset.get(0).getThinTweet().getTimeStamp();

		for (TweetEvent tweet : tweetsNotMatched) {
			if (tweet.getThinTweet().getTimeStamp() >= refTime)
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

		// List<TweetEvent> allTweets=new ArrayList<TweetEvent>();
		// allTweets.addAll(tweetsNotMatched);
		// allTweets.addAll(tweetsMatched);
		// TreeMap<Long,TweetEvent> mapNotMatched=new
		// TreeMap<Long,TweetEvent>();
		//
		// for(TweetEvent tweet:allTweets){
		// mapNotMatched.put(tweet.getThinTweet().getTimeStamp(), tweet);
		// }
		// while (i<tweetsMatched.size()) {
		//
		// Long matchedTime=tweetsMatched.get(i).getThinTweet().getTimeStamp();
		// int position=new Random().nextInt(10);
		// int prev=0;
		// Entry<Long,TweetEvent> entry=null;
		// Long currTime=matchedTime;
		// while(prev<position){
		// entry=mapNotMatched.lowerEntry(currTime);
		// if(entry.getValue().isRetweeted())
		// System.out.println("conflict");
		// currTime=entry.getKey();
		// prev++;
		// tweetsTemp.add(entry.getValue());
		// }
		// tweetsTemp.add(tweetsMatched.get(i));
		// int next=0;
		// while(next<window-position-1){
		// entry=mapNotMatched.higherEntry(currTime);
		// currTime=entry.getKey();
		// next++;
		// tweetsTemp.add(entry.getValue());
		// }
		// i++;
		// }
		// tweetsTemp.addAll(tweetsMatched.subList(j, tweetsMatched.size()));
		// System.out.println("all tweets: " + allTweets);
		//System.out.println("all positives: " + tweetsMatched.size());
		//System.out.println("all negatives: " + tweetsNotMatched.size());
		// int negSize = (int) Math.ceil((double) trainPercent * (double)
		// tweetsNotMatched.size());
		// int posSize = (int) Math.ceil((double) trainPercent * (double)
		// tweetsMatched.size());
		// tweetsTemp.addAll(tweetsNotMatched.subList(0, negSize));
		// tweetsTemp.addAll(tweetsMatched.subList(0, posSize));
		//
		// tweetsTemp.addAll(tweetsMatched.subList(posSize,
		// tweetsMatched.size()));
		// tweetsTemp.addAll(tweetsNotMatched.subList(negSize,
		// tweetsNotMatched.size()));
		// System.out.println("all positives: " + tweetsMatched.size());
		// System.out.println("all negatives: " + tweetsNotMatched.size());
		return tweetsTemp;
	}

	public long getRefTime() {
		return refTime;
	}

	public void setRefTime(long refTime) {
		this.refTime = refTime;
	}
}

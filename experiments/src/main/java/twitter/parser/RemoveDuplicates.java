package twitter.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import twitter.DataStructures.ReTweet;
import twitter.DataStructures.ThinTweet;
import twitter.DataStructures.ThinUser;
import twitter.DataStructures.UserActivity;
import twitter.Utilities.Constants;
import twitter.Utilities.SerializationUtilities;
import twitter.Utilities.Twitter.GetUserActivities;

public class RemoveDuplicates {
	public static void main(String[] args) {
		// GetUserActivities getActivities = new
		// GetUserActivities("/home/efi/master-thesis/resultUsers/usersRatioOnly/");
		String usersPath = "/media/efi/APOTHETHS/master/thesis/dataset/usersFinal/";
		GetUserActivities getActivities = new GetUserActivities(usersPath);

		 List<UserActivity> bottom =
		 getActivities.getUserActivities("bottom");
		List<UserActivity> top = getActivities.getUserActivities("top");
		List<UserActivity> avg = getActivities.getUserActivities("avg");
		 remove(bottom,Constants.BOTTOM_FOLDER) ;
		remove(top, Constants.TOP_FOLDER);
		remove(avg, Constants.AVG_FOLDER);
	}

	private static void remove(List<UserActivity> acts, String folder) {
		for (UserActivity ua : acts) {
			UserActivity fua = new UserActivity(ua.getUserName(),
					ua.getFollowees(), ua.getFollowers());

			ArrayList<ThinTweet> filteredTweets = new ArrayList<ThinTweet>();
			ArrayList<ThinTweet> tweets = ua.getOutgoingTweets();
			Map<String, ThinTweet> setTweets = new HashMap<String, ThinTweet>();
			
			for (ThinTweet tweet : tweets) {
				setTweets.put(tweet.getText(), tweet);
			}
			for (ThinTweet tweet : setTweets.values()) {
				filteredTweets.add(new ThinTweet(tweet.getTimeStamp(), tweet
						.getText()));
			}
			if(tweets.size()!= filteredTweets.size())
				System.out.println("dupl");
			fua.getOutgoingTweets().addAll(filteredTweets);

			ArrayList<ReTweet> filteredReTweets = new ArrayList<ReTweet>();
			ArrayList<ReTweet> retweets = ua.getOutgoingReTweets();
			Map<String, ReTweet> setReTweets = new HashMap<String, ReTweet>();
			for (ReTweet tweet : retweets) {
				setReTweets.put(tweet.getText(), tweet);
			}
			for (ReTweet retweet : setReTweets.values()) {
				filteredReTweets.add(new ReTweet(retweet.getTimeStamp(),
						retweet.getText(), retweet.getRetweetedUsers()));
			}
			if(retweets.size()!= filteredReTweets.size())
				System.out.println("dupl");
			fua.getOutgoingReTweets().addAll(filteredReTweets);

			HashMap<String, ThinUser> followers = fua.getFollowers();
			for (Map.Entry<String, ThinUser> entry : followers.entrySet()) {
				filteredTweets = new ArrayList<ThinTweet>();
				tweets = entry.getValue().getOutgoingTweets();
				setTweets = new HashMap<String, ThinTweet>();

				for (ThinTweet tweet : tweets) {
					setTweets.put(tweet.getText(), tweet);
				}
				for (ThinTweet tweet : setTweets.values()) {
					filteredTweets.add(new ThinTweet(tweet.getTimeStamp(),
							tweet.getText()));
				}
				if(tweets.size()!= filteredTweets.size())
					System.out.println("dupl");
				entry.getValue().setOutgoingTweets(filteredTweets);

				filteredReTweets = new ArrayList<ReTweet>();
				retweets = entry.getValue().getOutgoingReTweets();
				setReTweets = new HashMap<String, ReTweet>();
				for (ReTweet tweet : retweets) {
					setReTweets.put(tweet.getText(), tweet);
				}
				for (ReTweet retweet : setReTweets.values()) {
					filteredReTweets.add(new ReTweet(retweet.getTimeStamp(),
							retweet.getText(), retweet.getRetweetedUsers()));
				}
				entry.getValue().setOutgoingReTweets(filteredReTweets);
			}
			if(retweets.size()!= filteredReTweets.size())
				System.out.println("dupl");
			HashMap<String, ThinUser> followees = fua.getFollowees();
			for (Map.Entry<String, ThinUser> entry : followees.entrySet()) {
				filteredTweets = new ArrayList<ThinTweet>();
				tweets = entry.getValue().getOutgoingTweets();
				setTweets = new HashMap<String, ThinTweet>();
				for (ThinTweet tweet : tweets) {
					setTweets.put(tweet.getText(), tweet);
				}
				for (ThinTweet tweet : setTweets.values()) {
					filteredTweets.add(new ThinTweet(tweet.getTimeStamp(),
							tweet.getText()));
				}
				if(tweets.size()!= filteredTweets.size())
					System.out.println("dupl");
				entry.getValue().setOutgoingTweets(filteredTweets);

				filteredReTweets = new ArrayList<ReTweet>();
				retweets = entry.getValue().getOutgoingReTweets();
				setReTweets = new HashMap<String, ReTweet>();
				for (ReTweet tweet : retweets) {
					setReTweets.put(tweet.getText(), tweet);
				}
				for (ReTweet retweet : setReTweets.values()) {
					filteredReTweets.add(new ReTweet(retweet.getTimeStamp(),
							retweet.getText(), retweet.getRetweetedUsers()));
				}
				if(retweets.size()!= filteredReTweets.size())
					System.out.println("dupl");
				entry.getValue().setOutgoingReTweets(filteredReTweets);
			}
			// for(UserActivity act:acts)
			SerializationUtilities.storeSerializedObject(fua,
					"/home/efi/master-thesis/resultUsers/testUsers/" + folder
							+ fua.getUserName());
			// SerializationUtilities.storeSerializedObject(act,
			// "/home/efi/master-thesis/resultUsers/FINALUSERSdupls/"+folder+act.getUserName());
		}

	}
}

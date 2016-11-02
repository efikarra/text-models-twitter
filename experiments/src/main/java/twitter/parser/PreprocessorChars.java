package twitter.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.twitter.Extractor;

import twitter.DataStructures.ParsedRetweet;
import twitter.DataStructures.ParsedThinUser;
import twitter.DataStructures.ParsedTweet;
import twitter.DataStructures.ParsedUserActivity;
import twitter.DataStructures.ReTweet;
import twitter.DataStructures.ThinTweet;
import twitter.DataStructures.ThinUser;
import twitter.DataStructures.UserActivity;
import twitter.Utilities.Constants;
import twitter.Utilities.SerializationUtilities;
import twitter.Utilities.Twitter.GetUserActivities;
import twitter.modelUtilities.ModelsUtilities;

public class PreprocessorChars {
	private String resFolder;
	public PreprocessorChars(List<UserActivity> bottom, List<UserActivity> top, List<UserActivity> avg,
			 String resFolder) {
		this.resFolder = resFolder;
		normalizeURLS(bottom, Constants.BOTTOM_FOLDER);
		normalizeURLS(top, Constants.TOP_FOLDER);
		normalizeURLS(avg, Constants.AVG_FOLDER);

	}

	private void normalizeURLS(List<UserActivity> acts, String folder) {
		//String urlToken="token_url";
		String urlToken=" ";
		List<ParsedUserActivity> filteredActivities = new ArrayList<ParsedUserActivity>();
		Extractor ext=new Extractor();
		for (UserActivity ua : acts) {
			HashMap<String, ThinUser> followees = ua.getFollowees();
			HashMap<String, ThinUser> followers = ua.getFollowers();
			HashMap<String, ParsedThinUser> pfollowees = convertToParsedUsers(ua.getFollowees());
			HashMap<String, ParsedThinUser> pfollowers = convertToParsedUsers(ua.getFollowers());
			
			ParsedUserActivity fua = new ParsedUserActivity(ua.getUserName(), pfollowees, pfollowers);
			ArrayList<ParsedTweet> filteredTweets = new ArrayList<ParsedTweet>();
			ArrayList<ThinTweet> tweets = ua.getOutgoingTweets();
			System.out.println("it was " + tweets.size());
			for (ThinTweet tweet : tweets) {
				String normTweet = tweet.getText();
				List<String> urls=ext.extractURLs(tweet.getText());
				for(String url:urls)
					normTweet = normTweet.replace(url, urlToken);
				if(normTweet.isEmpty())
					continue;
				filteredTweets.add(new ParsedTweet(
						new ThinTweet(tweet.getTimeStamp(), normTweet), tweet.getText()));

			}
			System.out.println("it is " + filteredTweets.size());
			fua.setOutgoingTweets(filteredTweets);

			ArrayList<ParsedRetweet> filteredReTweets = new ArrayList<ParsedRetweet>();
			ArrayList<ReTweet> retweets = ua.getOutgoingReTweets();
			for (ReTweet retweet : retweets) {
				String normTweet = retweet.getText();
				List<String> urls=ext.extractURLs(retweet.getText());
				for(String url:urls)
					normTweet = normTweet.replace(url, urlToken);
				if(normTweet.isEmpty())
					continue;
				filteredReTweets.add(
						new ParsedRetweet(new ThinTweet(retweet.getTimeStamp(), normTweet),
								retweet.getText(), retweet.getRetweetedUsers()));

			}
			fua.setOutgoingReTweets(filteredReTweets);

			for (Map.Entry<String, ThinUser> entry : followers.entrySet()) {
				filteredTweets = new ArrayList<ParsedTweet>();
				tweets = entry.getValue().getOutgoingTweets();
				for (ThinTweet tweet : tweets) {
					String normTweet = tweet.getText();
					List<String> urls=ext.extractURLs(tweet.getText());
					for(String url:urls)
						normTweet = normTweet.replace(url, urlToken);
					if(normTweet.isEmpty())
						continue;
					filteredTweets.add(
							new ParsedTweet(new ThinTweet(tweet.getTimeStamp(), normTweet),
									tweet.getText()));

				}
				pfollowers.get(entry.getKey()).setOutgoingTweets(filteredTweets);

				filteredReTweets = new ArrayList<ParsedRetweet>();
				retweets = entry.getValue().getOutgoingReTweets();
				for (ReTweet retweet : retweets) {
					String normTweet = retweet.getText();
					List<String> urls=ext.extractURLs(retweet.getText());
					for(String url:urls)
						normTweet = normTweet.replace(url, urlToken);
					if(normTweet.isEmpty())
						continue;
					filteredReTweets.add(new ParsedRetweet(
							new ThinTweet(retweet.getTimeStamp(), normTweet),
							retweet.getText(), retweet.getRetweetedUsers()));

				}
				pfollowers.get(entry.getKey()).setOutgoingReTweets(filteredReTweets);
			}

			for (Map.Entry<String, ThinUser> entry : followees.entrySet()) {
				filteredTweets = new ArrayList<ParsedTweet>();
				tweets = entry.getValue().getOutgoingTweets();
				for (ThinTweet tweet : tweets) {
					String normTweet = tweet.getText();
					List<String> urls=ext.extractURLs(tweet.getText());
					for(String url:urls)
						normTweet = normTweet.replace(url, urlToken);
					if(normTweet.isEmpty())
						continue;
					filteredTweets.add(
							new ParsedTweet(new ThinTweet(tweet.getTimeStamp(), normTweet),
									tweet.getText()));

				}

				pfollowees.get(entry.getKey()).setOutgoingTweets(filteredTweets);

				filteredReTweets = new ArrayList<ParsedRetweet>();
				retweets = entry.getValue().getOutgoingReTweets();
				for (ReTweet retweet : retweets) {
					String normTweet = retweet.getText();
					List<String> urls=ext.extractURLs(retweet.getText());
					for(String url:urls)
						normTweet = normTweet.replace(url, urlToken);
					if(normTweet.isEmpty())
						continue;
					filteredReTweets.add(new ParsedRetweet(
							new ThinTweet(retweet.getTimeStamp(),normTweet),
							retweet.getText(), retweet.getRetweetedUsers()));

				}
				pfollowees.get(entry.getKey()).setOutgoingReTweets(filteredReTweets);
			}
			filteredActivities.add(fua);
		}
		SerializationUtilities.storeSerializedObject(filteredActivities,
				"/home/efi/master-thesis/resultUsers/" +resFolder+"/"+ folder + "filteredActivities");
		System.out.println("filtered " + folder);
		
	}

	public static void main(String[] args) throws IOException {

		int matchedThreshold = 20;
		List<UserActivity> acts = new ArrayList<UserActivity>();
		GetUserActivities getActivities = new GetUserActivities(
				"/home/efi/master-thesis/resultUsers/serializedUsersdupls/");
		List<UserActivity> bottom = getActivities.getUserActivities("bottom");
		List<UserActivity> top = getActivities.getUserActivities("top");
		List<UserActivity> avg = getActivities.getUserActivities("avg");
		removeUsers(bottom, matchedThreshold);
		removeUsers(top, matchedThreshold);
		removeUsers(avg, matchedThreshold);
		acts.addAll(bottom);
		acts.addAll(top);
		acts.addAll(avg);

		PreprocessorChars pre = new PreprocessorChars(bottom, top, avg, "filteredUsersChars");
	}

	private static void removeUsers(List<UserActivity> activities, int matchedThreshold) {
		Iterator<UserActivity> iterator = activities.iterator();
		while (iterator.hasNext()) {
			if (ModelsUtilities.matchedRetweets(iterator.next()) < matchedThreshold) {
				iterator.remove();
			}

		}

	}
	private HashMap<String, ParsedThinUser> convertToParsedUsers(HashMap<String, ThinUser> foll) {
		HashMap<String, ParsedThinUser> pfolls = new HashMap<String, ParsedThinUser>();
		for (Map.Entry<String, ThinUser> entry : foll.entrySet()) {
			ThinUser u = entry.getValue();
			ParsedThinUser puser = new ParsedThinUser(u.getUserName());
			pfolls.put(u.getUserName(), puser);

		}
		return pfolls;

	}
}

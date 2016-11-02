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

public class Preprocessor2 {
	private Set<String> stopWords;
	private String resFolder;

	public Preprocessor2(List<UserActivity> bottom, List<UserActivity> top, List<UserActivity> avg, String resFolder)
			throws IOException {
		List<UserActivity> acts = new ArrayList<UserActivity>();
		acts.addAll(bottom);
		acts.addAll(top);
		acts.addAll(avg);

		//StopwordsRemoval st = new StopwordsRemoval(ModelsUtilities.getAllCorpusWithoutTestsets(acts));
		//st.findFilteredWords(30, 1, "/home/efi/master-thesis/stopWordsFinal30only.txt");
		//st.findFilteredWords(100, 2, "/home/efi/master-thesis/stopWordsFinal100only.txt");
		//stopWords = st.getStopWords();
		stopWords = new HashSet<String>();
		BufferedReader reader = new BufferedReader(
				new FileReader(new File("/home/efi/master-thesis/stopWordsFinal100only.txt")));
		String line = reader.readLine();
		while (line != null) {

			stopWords.add(line.trim());
			line = reader.readLine();
		}
		reader.close();
		this.resFolder = resFolder;
		removeStopwords(bottom, Constants.BOTTOM_FOLDER);
		removeStopwords(top, Constants.TOP_FOLDER);
		removeStopwords(avg, Constants.AVG_FOLDER);

	}

	public void removeStopwords(List<UserActivity> acts, String folder) {
		System.out.println("filter " + folder);
		Tokenizer tok = new Tokenizer();
		List<ParsedUserActivity> filteredActivities = new ArrayList<ParsedUserActivity>();
		for (UserActivity ua : acts) {
			HashMap<String, ThinUser> followees = ua.getFollowees();
			HashMap<String, ThinUser> followers = ua.getFollowers();
			HashMap<String, ParsedThinUser> pfollowees = convertToParsedUsers(ua.getFollowees());
			HashMap<String, ParsedThinUser> pfollowers = convertToParsedUsers(ua.getFollowers());

			ParsedUserActivity fua = new ParsedUserActivity(ua.getUserName(), pfollowees, pfollowers);
			ArrayList<ParsedTweet> filteredTweets = new ArrayList<ParsedTweet>();
			ArrayList<ThinTweet> tweets = ua.getOutgoingTweets();
			// System.out.println("it was " + tweets.size());
			for (ThinTweet tweet : tweets) {
				String[] tokens = tok.tokenize(tweet.getText().toLowerCase());
				List<String> filteredTokens = new ArrayList<String>();
				for (String token : tokens) {
					if (stopWords.contains(token))
						continue;
					filteredTokens.add(token);
				}
				if (filteredTokens.isEmpty())
					continue;
				filteredTweets.add(new ParsedTweet(
						new ThinTweet(tweet.getTimeStamp(), StringUtils.join(filteredTokens, " ")), tweet.getText()));

			}
//			Map<String, ParsedTweet> setTweets = new HashMap<String, ParsedTweet>();
//			for (ParsedTweet tweet : filteredTweets) {
//				setTweets.put(tweet.getTweet().getText(), tweet);
//			}
			if (tweets.size() != filteredTweets.size())
				System.out.println("it was " + tweets.size() + " it is " + filteredTweets.size());
			// System.out.println("it is " + filteredTweets.size());
			fua.setOutgoingTweets(filteredTweets);

			ArrayList<ParsedRetweet> filteredReTweets = new ArrayList<ParsedRetweet>();
			ArrayList<ReTweet> retweets = ua.getOutgoingReTweets();
			for (ReTweet retweet : retweets) {
				String[] tokens = tok.tokenize(retweet.getText().toLowerCase());
				List<String> filteredTokens = new ArrayList<String>();
				for (String token : tokens) {
					if (stopWords.contains(token))
						continue;
					filteredTokens.add(token);
				}
				if (filteredTokens.isEmpty())
					continue;
				filteredReTweets.add(
						new ParsedRetweet(new ThinTweet(retweet.getTimeStamp(), StringUtils.join(filteredTokens, " ")),
								retweet.getText(), retweet.getRetweetedUsers()));

			}
			fua.setOutgoingReTweets(filteredReTweets);

			for (Map.Entry<String, ThinUser> entry : followers.entrySet()) {
				filteredTweets = new ArrayList<ParsedTweet>();
				tweets = entry.getValue().getOutgoingTweets();
				for (ThinTweet tweet : tweets) {
					String[] tokens = tok.tokenize(tweet.getText().toLowerCase());
					List<String> filteredTokens = new ArrayList<String>();
					for (String token : tokens) {
						if (stopWords.contains(token))
							continue;
						filteredTokens.add(token);
					}
					if (filteredTokens.isEmpty())
						continue;
					filteredTweets.add(
							new ParsedTweet(new ThinTweet(tweet.getTimeStamp(), StringUtils.join(filteredTokens, " ")),
									tweet.getText()));

				}
				pfollowers.get(entry.getKey()).setOutgoingTweets(filteredTweets);

				filteredReTweets = new ArrayList<ParsedRetweet>();
				retweets = entry.getValue().getOutgoingReTweets();
				for (ReTweet retweet : retweets) {
					String[] tokens = tok.tokenize(retweet.getText().toLowerCase());
					List<String> filteredTokens = new ArrayList<String>();
					for (String token : tokens) {
						if (stopWords.contains(token))
							continue;
						filteredTokens.add(token);
					}
					if (filteredTokens.isEmpty())
						continue;
					filteredReTweets.add(new ParsedRetweet(
							new ThinTweet(retweet.getTimeStamp(), StringUtils.join(filteredTokens, " ")),
							retweet.getText(), retweet.getRetweetedUsers()));

				}
				pfollowers.get(entry.getKey()).setOutgoingReTweets(filteredReTweets);
			}

			for (Map.Entry<String, ThinUser> entry : followees.entrySet()) {
				filteredTweets = new ArrayList<ParsedTweet>();
				tweets = entry.getValue().getOutgoingTweets();
				for (ThinTweet tweet : tweets) {
					String[] tokens = tok.tokenize(tweet.getText().toLowerCase());
					List<String> filteredTokens = new ArrayList<String>();
					for (String token : tokens) {
						if (stopWords.contains(token))
							continue;
						filteredTokens.add(token);
					}
					if (filteredTokens.isEmpty())
						continue;
					filteredTweets.add(
							new ParsedTweet(new ThinTweet(tweet.getTimeStamp(), StringUtils.join(filteredTokens, " ")),
									tweet.getText()));

				}

				pfollowees.get(entry.getKey()).setOutgoingTweets(filteredTweets);

				filteredReTweets = new ArrayList<ParsedRetweet>();
				retweets = entry.getValue().getOutgoingReTweets();
				for (ReTweet retweet : retweets) {
					String[] tokens = tok.tokenize(retweet.getText().toLowerCase());
					List<String> filteredTokens = new ArrayList<String>();
					for (String token : tokens) {
						if (stopWords.contains(token))
							continue;
						filteredTokens.add(token);
					}
					if (filteredTokens.isEmpty())
						continue;
					filteredReTweets.add(new ParsedRetweet(
							new ThinTweet(retweet.getTimeStamp(), StringUtils.join(filteredTokens, " ")),
							retweet.getText(), retweet.getRetweetedUsers()));

				}
				pfollowees.get(entry.getKey()).setOutgoingReTweets(filteredReTweets);
			}
			filteredActivities.add(fua);
		}
		// SerializationUtilities.storeSerializedObject(filteredActivities,
		// "/home/efi/master-thesis/resultUsers/" +resFolder+"/"+ folder +
		// "filteredActivities");
		// SerializationUtilities.storeSerializedObject(filteredActivities,
		// "D:\\master\\thesis\\dataset\\" + resFolder + "\\" + folder +
		// "filteredActivities");
		SerializationUtilities.storeSerializedObject(filteredActivities,
				"/home/efi/master-thesis/resultUsers/" + resFolder + "/" + folder + "filteredActivities");
		System.out.println("filtered " + folder);
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

	public static void main(String[] args) throws IOException {
		// GetUserActivities getActivities = new
		// GetUserActivities("D:\\master\\thesis\\dataset\\usersFinal\\");
		GetUserActivities getActivities = new GetUserActivities(
				"/media/efi/APOTHETHS/master/thesis/dataset/usersFinal/");
		List<UserActivity> bottom = getActivities.getUserActivities("bottom");
		List<UserActivity> top = getActivities.getUserActivities("top");
		List<UserActivity> avg = getActivities.getUserActivities("avg");

		Preprocessor2 pre = new Preprocessor2(bottom, top, avg, "filteredUsersFinal100only");
	}

	private static void removeUsers(List<UserActivity> activities, int matchedThreshold) {
		Iterator<UserActivity> iterator = activities.iterator();
		while (iterator.hasNext()) {
			if (ModelsUtilities.matchedRetweets(iterator.next()) < matchedThreshold) {
				iterator.remove();
			}

		}

	}
}

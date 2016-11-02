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
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import twitter.DataStructures.Twitter.ReTweet;
import twitter.DataStructures.Twitter.ThinTweet;
import twitter.DataStructures.Twitter.ThinUser;
import twitter.DataStructures.Twitter.UserActivity;
import twitter.Utilities.Constants;
import twitter.Utilities.SerializationUtilities;
import twitter.Utilities.Twitter.GetUserActivities;
import twitter.modelUtilities.ModelsUtilities;

public class Preprocessor {
	private Set<String> stopWords;
	
	
	public Preprocessor(List<UserActivity> bottom ,List<UserActivity> top,List<UserActivity> avg,Set<String> stopWords) {
		this.stopWords=stopWords;
		removeStopwords(bottom,Constants.BOTTOM_FOLDER);
		removeStopwords(top,Constants.TOP_FOLDER);
		removeStopwords(avg,Constants.AVG_FOLDER);
		
	}

	public void removeStopwords(List<UserActivity> acts,String folder) {
		System.out.println("filter "+folder);
		Tokenizer tok = new Tokenizer();
		List<UserActivity> filteredActivities = new ArrayList<UserActivity>();
		for (UserActivity ua : acts) {
			UserActivity fua = new UserActivity(ua.getUserName(), ua.getFollowees(), ua.getFollowers());

			ArrayList<ThinTweet> filteredTweets = new ArrayList<ThinTweet>();
			ArrayList<ThinTweet> tweets = ua.getOutgoingTweets();
			System.out.println("it was "+tweets.size());
			for (ThinTweet tweet : tweets) {
				String[] tokens = tok.tokenize(tweet.getText());
				List<String> filteredTokens = new ArrayList<String>();
				for (String token : tokens) {
					if (stopWords.contains(token))
						continue;
					filteredTokens.add(token);
				}
				if(filteredTokens.isEmpty())
					continue;
				filteredTweets.add(new ThinTweet(tweet.getTimeStamp(), StringUtils.join(filteredTokens, " ")));

			}
			System.out.println("it is "+filteredTweets.size());
			fua.getOutgoingTweets().addAll(filteredTweets);

			ArrayList<ReTweet> filteredReTweets = new ArrayList<ReTweet>();
			ArrayList<ReTweet> retweets = ua.getOutgoingReTweets();
			for (ReTweet retweet : retweets) {
				String[] tokens = tok.tokenize(retweet.getText());
				List<String> filteredTokens = new ArrayList<String>();
				for (String token : tokens) {
					if (stopWords.contains(token))
						continue;
					filteredTokens.add(token);
				}
				if(filteredTokens.isEmpty())
					continue;
				filteredReTweets.add(new ReTweet(retweet.getTimeStamp(), StringUtils.join(filteredTokens, " "),
						retweet.getRetweetedUsers()));

			}
			fua.getOutgoingReTweets().addAll(filteredReTweets);

			HashMap<String, ThinUser> followers = fua.getFollowers();
			for (Map.Entry<String, ThinUser> entry : followers.entrySet()) {
				filteredTweets = new ArrayList<ThinTweet>();
				tweets = entry.getValue().getOutgoingTweets();
				for (ThinTweet tweet : tweets) {
					String[] tokens = tok.tokenize(tweet.getText());
					List<String> filteredTokens = new ArrayList<String>();
					for (String token : tokens) {
						if (stopWords.contains(token))
							continue;
						filteredTokens.add(token);
					}
					if(filteredTokens.isEmpty())
						continue;
					filteredTweets.add(new ThinTweet(tweet.getTimeStamp(), StringUtils.join(filteredTokens, " ")));

				}

				entry.getValue().setOutgoingTweets(filteredTweets);

				filteredReTweets = new ArrayList<ReTweet>();
				retweets = entry.getValue().getOutgoingReTweets();
				for (ReTweet retweet : retweets) {
					String[] tokens = tok.tokenize(retweet.getText());
					List<String> filteredTokens = new ArrayList<String>();
					for (String token : tokens) {
						if (stopWords.contains(token))
							continue;
						filteredTokens.add(token);
					}
					if(filteredTokens.isEmpty())
						continue;
					filteredReTweets.add(new ReTweet(retweet.getTimeStamp(), StringUtils.join(filteredTokens, " "),
							retweet.getRetweetedUsers()));

				}
				entry.getValue().setOutgoingReTweets(filteredReTweets);
			}

			HashMap<String, ThinUser> followees = fua.getFollowees();
			for (Map.Entry<String, ThinUser> entry : followees.entrySet()) {
				filteredTweets = new ArrayList<ThinTweet>();
				tweets = entry.getValue().getOutgoingTweets();
				for (ThinTweet tweet : tweets) {
					String[] tokens = tok.tokenize(tweet.getText());
					List<String> filteredTokens = new ArrayList<String>();
					for (String token : tokens) {
						if (stopWords.contains(token))
							continue;
						filteredTokens.add(token);
					}
					if(filteredTokens.isEmpty())
						continue;
					filteredTweets.add(new ThinTweet(tweet.getTimeStamp(), StringUtils.join(filteredTokens, " ")));

				}

				entry.getValue().setOutgoingTweets(filteredTweets);

				filteredReTweets = new ArrayList<ReTweet>();
				retweets = entry.getValue().getOutgoingReTweets();
				for (ReTweet retweet : retweets) {
					String[] tokens = tok.tokenize(retweet.getText());
					List<String> filteredTokens = new ArrayList<String>();
					for (String token : tokens) {
						if (stopWords.contains(token))
							continue;
						filteredTokens.add(token);
					}
					if(filteredTokens.isEmpty())
						continue;
					filteredReTweets.add(new ReTweet(retweet.getTimeStamp(), StringUtils.join(filteredTokens, " "),
							retweet.getRetweetedUsers()));

				}
				entry.getValue().setOutgoingReTweets(filteredReTweets);
			}
			filteredActivities.add(fua);
		}
		SerializationUtilities.storeSerializedObject(filteredActivities, "/home/efi/master-thesis/resultUsers/"+folder+"filteredActivities");
		System.out.println("filtered "+folder);
	}
	public static void main(String[] args) throws IOException {

		int matchedThreshold=20;
		List<UserActivity> acts = new ArrayList<UserActivity>();
		GetUserActivities getActivities = new GetUserActivities("/home/efi/master-thesis/resultUsers/serializedUsers/");
		List<UserActivity> bottom = getActivities.getUserActivities("bottom");
		List<UserActivity> top = getActivities.getUserActivities("top");
		List<UserActivity> avg = getActivities.getUserActivities("avg");
		removeUsers(bottom,matchedThreshold);	
		removeUsers(top,matchedThreshold);	
		removeUsers(avg,matchedThreshold);	
		acts.addAll(bottom);
		acts.addAll(top);
		acts.addAll(avg);
		//StopwordsRemoval st=new StopwordsRemoval(ModelsUtilities.getAllCorpus(acts));
		//st.findCorpusStopWords(0.02, "/home/efi/stopWords.txt");
		Set<String> stops=new HashSet<String>();
		BufferedReader reader=new BufferedReader(new FileReader(new File("/home/efi/stopWords.txt")));
		String line=reader.readLine();
		while(line!=null){
			
			stops.add(line.trim());
			line=reader.readLine();
		}
		reader.close();;
		Preprocessor pre=new Preprocessor(bottom,top,avg,stops);
	}
	private static void removeUsers(List<UserActivity> activities,int matchedThreshold) {
		Iterator<UserActivity> iterator=activities.iterator();
		while(iterator.hasNext()){
			if (ModelsUtilities.matchedRetweets(iterator.next()) < matchedThreshold) {
				iterator.remove();
			}
			
		}
		
	}
}

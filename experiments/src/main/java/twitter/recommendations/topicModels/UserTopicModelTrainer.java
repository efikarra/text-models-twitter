package twitter.recommendations.topicModels;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import twitter.AbstractModelTrainer;
import twitter.DataStructures.Twitter.RankedTopicTweet;
import twitter.DataStructures.Twitter.TopicTweet;
import twitter.DataStructures.Twitter.TweetEvent;
import twitter.DataStructures.Twitter.UserData;
import twitter.Utilities.Similarities;
import twitter.modelUtilities.AbstractStrategy;
import twitter.modelUtilities.MergeStrategy;
import twitter.modelUtilities.TopicModelTrainStrategy;
import twitter.recommendations.evaluation.RankingMetrics;

public abstract class UserTopicModelTrainer extends AbstractModelTrainer{
	
	protected Map<Integer, TopicTweet> hashedTweets;
	private HashMap<Integer, Integer> rankedTweets;
	protected double[] userModel;
	public UserTopicModelTrainer(UserData userData) {
		super(userData);
	}
	public abstract void trainUserModel(MergeStrategy strategyName,TopicModelTrainStrategy trainStrategy) throws Exception;
	@Override
	public void evaluateUserModel() {
		metrics=new RankingMetrics();
		for (Map.Entry<Integer, Integer> rank : rankedTweets.entrySet()) {
			TopicTweet tweet=hashedTweets.get(rank.getKey());
			if(tweet.getTweetEvent().isRetweeted())
				metrics.getRankingPositions().add(rank.getValue());
		}
		metrics.printPerfomanceEvaluation();
		
	}
	public void rankAllTweets() {
		final List<RankedTopicTweet> tweetsRanking = new ArrayList<RankedTopicTweet>();
		for (Entry<Integer, TopicTweet> entry : hashedTweets.entrySet()) {
			double sim = Similarities.computeCosineSimilarity(entry.getValue().getVector(), userModel);
			RankedTopicTweet rtweet=new RankedTopicTweet(entry.getValue());
			rtweet.setSimilarity(sim);
			tweetsRanking.add(rtweet);
			
		}
		Collections.sort(tweetsRanking);

		int counter = 1;
		rankedTweets = new HashMap<Integer, Integer>(2 * tweetsRanking.size());
		for (RankedTopicTweet tweet : tweetsRanking) {
			rankedTweets.put(tweet.getTopicTweet().getTweetEvent().getEventId(), counter);
			counter++;
		}
	}
}

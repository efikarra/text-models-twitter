package twitter.recommendations.content;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import Models.AbstractModel;
import Utilities.SimilarityName;
import twitter.AbstractModelTrainer;
import twitter.DataStructures.Twitter.CombinedTweet;
import twitter.DataStructures.Twitter.RankedTweet;
import twitter.DataStructures.Twitter.TweetEvent;
import twitter.DataStructures.Twitter.UserData;
import twitter.recommendations.content.models.ContentAbstractModel;
import twitter.recommendations.evaluation.RankingMetrics;

public class UserModelTrainer extends AbstractModelTrainer{

	private Map<Integer, CombinedTweet> hashedTweets;
	private HashMap<Integer, Integer> rankedTweets;
	private ContentAbstractModel userModel;
	public UserModelTrainer(UserData userData,ContentAbstractModel userModel) {
		super(userData);
		this.userModel=userModel;
	}

	public void trainUserModel() {

		List<TweetEvent> trainTweets = userData.getTrainTweets();
		//System.out.println("train tweets:" + trainTweets.size());
		int i = 0;
		for (TweetEvent ctweet : trainTweets) {
			//System.out.println("train  " + i);
			userModel.updateModel(ctweet.getThinTweet().getText().toLowerCase());
			i++;
		}
	}
	@Override
	public void modelTestTweets() {
		List<TweetEvent> testTweets = userData.getTestTweets();
		//System.out.println("test tweets:" + testTweets.size());
		hashedTweets = new HashMap<Integer, CombinedTweet>();
		int eventId = 0;
		for (TweetEvent tweet : testTweets) {
			hashedTweets.put(eventId, new CombinedTweet(userModel.getModelType(),userModel.getModelInfoSource(), eventId + "", tweet,eventId));
			eventId++;
		}
	}

	public void rankAllTweets(SimilarityName similarityType) {
		ModelComparator modelComparator = new ModelComparator(similarityType);
		final ArrayList<RankedTweet> tweetsRanking = new ArrayList<RankedTweet>();
		for (Entry<Integer, CombinedTweet> entry : hashedTweets.entrySet()) {
			entry.getValue().getModel().finalizeModel();
			double sim = modelComparator.compareModels(userModel, entry.getValue().getModel());
			RankedTweet rTweet = new RankedTweet(entry.getValue());
			rTweet.setSimilarity(sim);
			tweetsRanking.add(rTweet);
		}
		Collections.sort(tweetsRanking);

		int counter = 1;
		rankedTweets = new HashMap<Integer, Integer>(2 * tweetsRanking.size());
		for (RankedTweet tweet : tweetsRanking) {
			rankedTweets.put(tweet.getCombinedTweet().getEventId(), counter);
			counter++;
		}
	}
	@Override
	public void evaluateUserModel(){
		metrics=new RankingMetrics();
		for (Map.Entry<Integer, Integer> rank : rankedTweets.entrySet()) {
			CombinedTweet tweet=hashedTweets.get(rank.getKey());
			if(tweet.getTweetEvent().isRetweeted())
				metrics.getRankingPositions().add(rank.getValue());
		}
		metrics.printPerfomanceEvaluation();

	}

	public HashMap<Integer, Integer> getRankedTweets() {
		return rankedTweets;
	}

	public ContentAbstractModel getUserModel() {
		return userModel;
	}

	public void setUserModel(ContentAbstractModel userModel) {
		this.userModel = userModel;
	}
}

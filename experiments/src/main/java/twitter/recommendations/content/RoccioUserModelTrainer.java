package twitter.recommendations.content;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import Models.VectorModel;
import twitter.AbstractModelTrainer;
import twitter.DataStructures.Twitter.CombinedTweet;
import twitter.DataStructures.Twitter.RankedTweet;
import twitter.DataStructures.Twitter.TweetEvent;
import twitter.DataStructures.Twitter.UserData;
import twitter.modelUtilities.RoccioModelType;
import twitter.modelUtilities.RoccioSimilarityType;
import twitter.recommendations.content.models.ContentVectorModel;
import twitter.recommendations.content.models.RoccioVectorModel;
import twitter.recommendations.evaluation.RankingMetrics;

public class RoccioUserModelTrainer extends AbstractModelTrainer{
	private Map<Integer, CombinedTweet> hashedTweets;
	private HashMap<Integer, Integer> rankedTweets;
	private ContentVectorModel posUserModel;
	private ContentVectorModel negUserModel;
	private RoccioVectorModel roccioModel;
	private RoccioModelType type;
	private RoccioParams params;
	public RoccioUserModelTrainer(UserData userData,ContentVectorModel posUserModel,ContentVectorModel negUserModel,RoccioModelType type,RoccioParams params) {
		super(userData);
		this.posUserModel=posUserModel;
		this.negUserModel=negUserModel;
		this.type=type;
		this.params=params;
	}

	public void trainUserModel() {

		List<TweetEvent> trainTweets = userData.getTrainTweets();
		//System.out.println("train tweets:" + trainTweets.size());
		int i = 0;
		for (TweetEvent ctweet : trainTweets) {
			//System.out.println("train  " + i);
			if(ctweet.isRetweeted())
				posUserModel.updateModel(ctweet.getThinTweet().getText().toLowerCase());
			else
				negUserModel.updateModel(ctweet.getThinTweet().getText().toLowerCase());
			i++;
		}
		roccioModel=RoccioModelType.getRoccioModel(type, posUserModel, negUserModel);
		roccioModel.createModel(params);
	}
	@Override
	public void modelTestTweets() {
		List<TweetEvent> testTweets = userData.getTestTweets();
		//System.out.println("test tweets:" + testTweets.size());
		hashedTweets = new HashMap<Integer, CombinedTweet>();
		int eventId = 0;
		for (TweetEvent tweet : testTweets) {
			hashedTweets.put(eventId, new CombinedTweet(posUserModel.getModelType(),posUserModel.getModelInfoSource(), eventId + "", tweet,eventId));
			eventId++;
		}
	}

	public void rankAllTweets(RoccioSimilarityType simtype) {
		final ArrayList<RankedTweet> tweetsRanking = new ArrayList<RankedTweet>();
		RoccioSimilarity rsim=RoccioSimilarityType.getRoccioSimilarity(simtype);
		for (Entry<Integer, CombinedTweet> entry : hashedTweets.entrySet()) {
			entry.getValue().getModel().finalizeModel();
			double sim = rsim.getValue(roccioModel, (VectorModel) entry.getValue().getModel().getModel());
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

	public ContentVectorModel getPosUserModel() {
		return posUserModel;
	}

	public void setPosUserModel(ContentVectorModel posUserModel) {
		this.posUserModel = posUserModel;
	}

	public ContentVectorModel getNegUserModel() {
		return negUserModel;
	}

	public void setNegUserModel(ContentVectorModel negUserModel) {
		this.negUserModel = negUserModel;
	}
}

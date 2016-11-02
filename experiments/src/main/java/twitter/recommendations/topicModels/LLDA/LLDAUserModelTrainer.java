package twitter.recommendations.topicModels.LLDA;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import twitter.DataStructures.LabeledDocument;
import twitter.DataStructures.Twitter.TopicTweet;
import twitter.DataStructures.Twitter.TweetEvent;
import twitter.DataStructures.Twitter.UserData;
import twitter.modelUtilities.AbstractStrategy;
import twitter.modelUtilities.MergeStrategy;
import twitter.modelUtilities.RocchioStrategy;
import twitter.modelUtilities.TopicModelTrainStrategy;
import twitter.recommendations.content.RoccioParams;
import twitter.recommendations.topicModels.UserTopicModelTrainer;

public class LLDAUserModelTrainer extends UserTopicModelTrainer {
	private LLDATopicModel topicModel;
	private LLDATweetLabels labels;

	public LLDAUserModelTrainer(UserData userData, LLDATopicModel userModel,LLDATweetLabels labels) {
		super(userData);
		this.topicModel = userModel;
		this.labels = labels;
	}

	public void trainUserModel(MergeStrategy strategyName,TopicModelTrainStrategy trainStrategy) {
		List<double[]> list=new ArrayList<double[]>();
		Map<Integer,double[]> map=topicModel.getTopicProbabilities();
		for(TweetEvent tevent:userData.getTrainTweets()){
		if(TopicModelTrainStrategy.getName(trainStrategy).equals("HASH")||TopicModelTrainStrategy.getName(trainStrategy).equals("UP"))
			list.add(topicModel.modelDocument(labelTestTweet(tevent)));
		else
			list.add(map.get(tevent.getEventId()));
		}
		AbstractStrategy strategy=MergeStrategy.getStrategy(strategyName);
		userModel=strategy.mergeVectors(list);
	}
	public void trainRocchioUserModel(RoccioParams rparams,TopicModelTrainStrategy trainStrategy) {
		List<double[]> posVectors=new ArrayList<double[]>();
		List<double[]> negVectors=new ArrayList<double[]>();
		Map<Integer,double[]> map=topicModel.getTopicProbabilities();
		for(TweetEvent tevent:userData.getTrainTweets()){
			if(tevent.isRetweeted()){
				if(TopicModelTrainStrategy.getName(trainStrategy).equals("HASH")||TopicModelTrainStrategy.getName(trainStrategy).equals("UP"))
					posVectors.add(topicModel.modelDocument(labelTestTweet(tevent)));
				else
					posVectors.add(map.get(tevent.getEventId()));
			}	
			else{
				if(TopicModelTrainStrategy.getName(trainStrategy).equals("HASH")||TopicModelTrainStrategy.getName(trainStrategy).equals("UP"))
					negVectors.add(topicModel.modelDocument(labelTestTweet(tevent)));
				else
					negVectors.add(map.get(tevent.getEventId()));
			}
		}
		RocchioStrategy rstrategy=new RocchioStrategy(rparams);
		userModel=rstrategy.mergeVectors(posVectors,negVectors);
		
	}
	public void trainUserModelOnProfile(Integer userProfileId) {
		userModel=topicModel.getTopicProbabilities().get(userProfileId);	
	}
	private LabeledDocument labelTestTweet(TweetEvent tevent) {
		return labels.labelNewTweet(tevent.getThinTweet().getText(),tevent.getEventId());
	}

	@Override
	public void modelTestTweets() {
		hashedTweets = new HashMap<Integer, TopicTweet>();
		for (TweetEvent tevent : userData.getTestTweets())
			hashedTweets.put(tevent.getEventId(),
					new TopicTweet(tevent, topicModel.modelDocument(labelTestTweet(tevent)),tevent.getEventId()));
	}
}
package twitter.recommendations.topicModels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import twitter.DataStructures.TopicTweet;
import twitter.DataStructures.TweetEvent;
import twitter.DataStructures.UserData;
import twitter.modelUtilities.AbstractStrategy;
import twitter.modelUtilities.MergeStrategy;
import twitter.modelUtilities.RocchioStrategy;
import twitter.modelUtilities.TopicModelTrainStrategy;
import twitter.recommendations.content.RoccioParams;

public class UnsUserTopicModelTrainer extends UserTopicModelTrainer{
	private UnsupTopicModel topicModel;
	public UnsUserTopicModelTrainer(UserData userData,UnsupTopicModel topicModel) {
		super(userData);
		this.topicModel=topicModel;
		
	}

	public void trainUserModel(MergeStrategy strategyName,TopicModelTrainStrategy trainStrategy) {
		List<double[]> list=new ArrayList<double[]>();
		Map<Integer,double[]> map=topicModel.getTopicProbabilities();
		for(TweetEvent tevent:userData.getTrainTweets()){
			if(TopicModelTrainStrategy.getName(trainStrategy).equals("HASH")||TopicModelTrainStrategy.getName(trainStrategy).equals("UP"))
				list.add(topicModel.modelDocument(tevent.getThinTweet().getText()));
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
					posVectors.add(topicModel.modelDocument(tevent.getThinTweet().getText()));
				else
					posVectors.add(map.get(tevent.getEventId()));
			}	
			else{
				if(TopicModelTrainStrategy.getName(trainStrategy).equals("HASH")||TopicModelTrainStrategy.getName(trainStrategy).equals("UP"))
					negVectors.add(topicModel.modelDocument(tevent.getThinTweet().getText()));
				else
					negVectors.add(map.get(tevent.getEventId()));
			}
		}
		RocchioStrategy rstrategy=new RocchioStrategy(rparams);
		userModel=rstrategy.mergeVectors(posVectors,negVectors);
		
	}
	public void trainUserModelOnProfiles(Integer userProfileId) {
		userModel=topicModel.getTopicProbabilities().get(userProfileId);	
	}
	@Override
	public void modelTestTweets() {
		hashedTweets = new HashMap<Integer, TopicTweet>();
		for (TweetEvent tevent : userData.getTestTweets())
			hashedTweets.put(tevent.getEventId(),
					new TopicTweet(tevent, topicModel.modelDocument(tevent.getThinTweet().getText()),tevent.getEventId()));
	}
}

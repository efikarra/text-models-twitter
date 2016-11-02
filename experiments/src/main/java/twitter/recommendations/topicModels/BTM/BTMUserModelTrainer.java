package twitter.recommendations.topicModels.BTM;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ooon.lzj.model.Infer;
import com.ooon.lzj.model.Model;

import twitter.DataStructures.BTMTweet;
import twitter.DataStructures.TopicTweet;
import twitter.DataStructures.TweetEvent;
import twitter.Utilities.SerializationUtilities;
import twitter.modelUtilities.AbstractStrategy;
import twitter.modelUtilities.MergeStrategy;
import twitter.modelUtilities.RocchioStrategy;
import twitter.modelUtilities.TopicModelTrainStrategy;
import twitter.recommendations.content.RoccioParams;
import twitter.recommendations.topicModels.UserTopicModelTrainer;

public class BTMUserModelTrainer extends UserTopicModelTrainer{
	private List<BTMTweet> trainData;
	private String tmodel_dir;
	private Model bitermModel ;
	private Infer inf;
	private String type;
	private List<BTMTweet> testData;
	private List<Integer[]> trainVectors;
	public BTMUserModelTrainer(List<BTMTweet> trainData,List<BTMTweet> testData,String tmodel_dir,Model bitermModel,String type) {
		super(null);
		this.trainData=trainData;
		this.testData=testData;
		this.tmodel_dir=tmodel_dir;
		this.bitermModel=bitermModel;
		this.type=type;
		trainVectors=new ArrayList<Integer[]>();
		for(BTMTweet tweet:trainData)
			trainVectors.add(tweet.getIds());
	}
	@Override
	public void trainUserModel(MergeStrategy strategyName,TopicModelTrainStrategy trainStrategy) throws Exception {
		inf = new Infer(bitermModel.K,bitermModel.V ,type,bitermModel.getWindow());
		List< double[]> vectors=inf.infer(trainVectors, bitermModel);
		AbstractStrategy strategy = MergeStrategy.getStrategy(strategyName);
		userModel=strategy.mergeVectors(vectors);

	}
	public void trainRocchioUserModel(RoccioParams rparams,TopicModelTrainStrategy trainStrategy) throws IOException {
		List<double[]> posVectors=new ArrayList<double[]>();
		List<double[]> negVectors=new ArrayList<double[]>();
		inf = new Infer(bitermModel.K,bitermModel.V ,type,bitermModel.getWindow());
		List< double[]> vectors=inf.infer(trainVectors, bitermModel);
		int i=0;
		for(BTMTweet tweet:trainData){
			if(tweet.getTweetEvent().isRetweeted())
				posVectors.add(vectors.get(i));
			else
				negVectors.add(vectors.get(i));
			i++;
		}
		RocchioStrategy rstrategy=new RocchioStrategy(rparams);
		userModel=rstrategy.mergeVectors(posVectors,negVectors);
		
	}
	public void trainUserModelOnProfiles(Integer[] userProfile) throws Exception {
		inf = new Infer(bitermModel.K,bitermModel.V ,type,bitermModel.getWindow());
		ArrayList<Integer[]> ids=new ArrayList<Integer[]>();
		ids.add(userProfile);
		userModel=inf.infer(ids, tmodel_dir).get(0);
	}
	@Override
	public void modelTestTweets() throws Exception {
		List<Integer[]> idsAll=new ArrayList<Integer[]>();
		for(BTMTweet tweet:testData)
			idsAll.add(tweet.getIds());
		List< double[]> testVectors=inf.infer(idsAll, bitermModel);
		hashedTweets=new HashMap<Integer, TopicTweet> ();
		for(int i=0;i<testData.size();i++){
			hashedTweets.put(testData.get(i).getTweetEvent().getEventId(), new TopicTweet(testData.get(i).getTweetEvent(),testVectors.get(i),testData.get(i).getTweetEvent().getEventId()));
		}
	}
	
}

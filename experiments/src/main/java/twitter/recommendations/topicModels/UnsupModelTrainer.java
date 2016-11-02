package twitter.recommendations.topicModels;

import java.util.List;

import twitter.DataStructures.UserData;

public abstract class UnsupModelTrainer extends AbstractModelTrainer{
	protected UnsupTopicModel topicModel;
	public UnsupModelTrainer(List<UserData> data,UnsupTopicModel topicModel) {
		super(data);
		this.topicModel=topicModel;
	}
	public UnsupTopicModel getTopicModel() {
		return topicModel;
	}
	public void setTopicModel(UnsupTopicModel topicModel) {
		this.topicModel = topicModel;
	}
	
	
}

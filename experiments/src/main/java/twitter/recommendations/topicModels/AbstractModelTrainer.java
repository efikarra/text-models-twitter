package twitter.recommendations.topicModels;

import java.util.List;

import twitter.DataStructures.Twitter.UserData;
import twitter.recommendations.topicModels.LDA.LDATopicModel;

public abstract class AbstractModelTrainer {
	
	protected List<UserData> data;
	public AbstractModelTrainer(List<UserData> data) {
		this.data=data;;
	}
	public abstract void train() throws Exception;
}

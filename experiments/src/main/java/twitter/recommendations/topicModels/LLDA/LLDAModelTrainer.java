package twitter.recommendations.topicModels.LLDA;

import java.util.List;

import twitter.DataStructures.LabeledDocument;
import twitter.DataStructures.Twitter.UserData;

public abstract class LLDAModelTrainer {
	protected List<UserData> data;
	protected LLDATopicModel topicModel;
	protected LLDATweetLabels labels;
	public LLDAModelTrainer(List<UserData> data,LLDATopicModel topicModel) {
		this.data=data;
		this.topicModel=topicModel;
	}
	
	public abstract void train() throws Exception;
	
	protected List<LabeledDocument> labelTrainData() {
		return labels.labelTweets();
	}
	public LLDATopicModel getTopicModel() {
		return topicModel;
	}
	public void setTopicModel(LLDATopicModel topicModel) {
		this.topicModel = topicModel;
	}
	public LLDATweetLabels getLabels() {
		return labels;
	}
	public void setLabels(LLDATweetLabels labels) {
		this.labels = labels;
	}
}

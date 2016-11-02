package twitter.recommendations.topicModels;

import java.util.List;

import twitter.DataStructures.TrainingDocument;

public abstract class UnsupTopicModel extends AbstractTopicModel{
	public UnsupTopicModel(TopicModelParams params,String modelInstName) {
		super(params,modelInstName);
	}
	public abstract void trainModel(List<TrainingDocument> documents);
	public abstract double[] modelDocument(String document);
}

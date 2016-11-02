package twitter.recommendations.topicModels;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractTopicModel {
	protected Map<Integer, double[]> topicProbabilities;
	protected TopicModelParams params;
	protected String modelInstName;
	public AbstractTopicModel(TopicModelParams params,String modelInstName) {
		topicProbabilities=new HashMap<Integer,double[]>();
		this.params=params;
		this.modelInstName=modelInstName;
	}

	public Map<Integer, double[]> getTopicProbabilities() {
		return topicProbabilities;
	}

	public void setTopicProbabilities(Map<Integer, double[]> topicProbabilities) {
		this.topicProbabilities = topicProbabilities;
	}

	public TopicModelParams getParams() {
		return params;
	}

	public void setParams(TopicModelParams params) {
		this.params = params;
	}

	public String getModelInstName() {
		return modelInstName;
	}

	public void setModelInstName(String modelInstName) {
		this.modelInstName = modelInstName;
	}
	
}

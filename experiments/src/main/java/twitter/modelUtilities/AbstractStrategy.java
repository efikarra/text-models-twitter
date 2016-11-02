package twitter.modelUtilities;

import java.util.List;

import twitter.recommendations.topicModels.AbstractTopicModel;

public abstract class AbstractStrategy {

	public abstract double[] mergeVectors(List<double[]>  vectors);
}

package twitter.modelUtilities;

import twitter.recommendations.topicModels.AbstractTopicModel;
import twitter.recommendations.topicModels.TopicModelParams;
import twitter.recommendations.topicModels.HDP.HDPParameters;
import twitter.recommendations.topicModels.HDP.HDPTopicModel;
import twitter.recommendations.topicModels.HLDA.HLDAParameters;
import twitter.recommendations.topicModels.HLDA.HLDATopicModel;
import twitter.recommendations.topicModels.LDA.LDAParameters;
import twitter.recommendations.topicModels.LDA.LDATopicModel;
import twitter.recommendations.topicModels.PLSA.PLSAParameters;
import twitter.recommendations.topicModels.PLSA.PLSATopicModel;

public enum TopicModelName {
	LDA, HLDA, PLSA, HDP;

	public static AbstractTopicModel getTopicModel(TopicModelName topicModelName, TopicModelParams params,
			String modelName) {
		switch (topicModelName) {
		case LDA:
			return new LDATopicModel((LDAParameters) params, modelName);
		case HLDA:
			return new HLDATopicModel((HLDAParameters) params, modelName);
		case PLSA:
			return new PLSATopicModel((PLSAParameters) params, modelName);
		case HDP:
			return new HDPTopicModel((HDPParameters) params, modelName);
		default:
			return null;
		}

	}

	public static String getName(TopicModelName topicModelName) {
		switch (topicModelName) {
		case LDA:
			return "LDA";
		case HLDA:
			return "HLDA";
		case PLSA:
			return "PLSA";
		case HDP:
			return "HDP";
		default:
			return null;
		}
	}
}
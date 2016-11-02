package twitter.modelUtilities;

import java.util.List;

import twitter.DataStructures.Twitter.UserData;
import twitter.recommendations.topicModels.TrainsetCreator;
import twitter.recommendations.topicModels.UnsupModelTrainer;
import twitter.recommendations.topicModels.UnsupTopicModel;
import twitter.recommendations.topicModels.BTM.BTMModelTrainer;
import twitter.recommendations.topicModels.BTM.BTMModelTrainerHASH;
import twitter.recommendations.topicModels.BTM.BTMModelTrainerMSG;
import twitter.recommendations.topicModels.BTM.BTMModelTrainerUP;
import twitter.recommendations.topicModels.BTM.BTMParameters;
import twitter.recommendations.topicModels.HDP.HDPTopicModel;
import twitter.recommendations.topicModels.HLDA.HLDATopicModel;
import twitter.recommendations.topicModels.LDA.LDATopicModel;
import twitter.recommendations.topicModels.LLDA.LLDAModelTrainer;
import twitter.recommendations.topicModels.LLDA.LLDAModelTrainerHASH;
import twitter.recommendations.topicModels.LLDA.LLDAModelTrainerMSG;
import twitter.recommendations.topicModels.LLDA.LLDAModelTrainerUP;
import twitter.recommendations.topicModels.LLDA.LLDAParameters;
import twitter.recommendations.topicModels.LLDA.LLDATopicModel;
import twitter.recommendations.topicModels.LLDA.LabeledSetCreator;
import twitter.recommendations.topicModels.LLDA.LabeledTrainsetCreatorHASH;
import twitter.recommendations.topicModels.LLDA.LabeledTrainsetCreatorMSG;
import twitter.recommendations.topicModels.LLDA.LabeledTrainsetCreatorUP;
import twitter.recommendations.topicModels.PLSA.PLSATopicModel;
import twitter.recommendations.topicModels.poolig.TrainsetCreatorHASH;
import twitter.recommendations.topicModels.poolig.TrainsetCreatorMSG;
import twitter.recommendations.topicModels.poolig.TrainsetCreatorUP;
import twitter.recommendations.topicModels.poolig.UnsupModelTrainerHASH;
import twitter.recommendations.topicModels.poolig.UnsupModelTrainerMSG;
import twitter.recommendations.topicModels.poolig.UnsupModelTrainerUP;

public enum TopicModelTrainStrategy {
	MSG_STRATEGY,UP_STRATEGY,HASH_STRATEGY,TEMPORAL_STRATEGY;
	public static UnsupModelTrainer getUnsupModelTrainer(TopicModelTrainStrategy strategy,List<UserData> userData,UnsupTopicModel model) {
		switch (strategy) {
		case MSG_STRATEGY:
			return new UnsupModelTrainerMSG(userData,model);
		case UP_STRATEGY:
			return new UnsupModelTrainerUP(userData,model);
		case HASH_STRATEGY:
			return new UnsupModelTrainerHASH(userData,model);
		default:
			return null;
		}
}
	public static TrainsetCreator getTrainsetcreator(TopicModelTrainStrategy strategy,UserData userData) {
		switch (strategy) {
		case MSG_STRATEGY:
			return new TrainsetCreatorMSG(userData);
		case UP_STRATEGY:
			return new TrainsetCreatorUP(userData);
		case HASH_STRATEGY:
			return new TrainsetCreatorHASH(userData);
		default:
			return null;
		}
}
	public static LabeledSetCreator getLabeledTrainsetcreator(TopicModelTrainStrategy strategy,UserData userData,LLDAParameters params) {
		switch (strategy) {
		case MSG_STRATEGY:
			return new LabeledTrainsetCreatorMSG(userData,params);
		case UP_STRATEGY:
			return new LabeledTrainsetCreatorUP(userData,params);
		case HASH_STRATEGY:
			return new LabeledTrainsetCreatorHASH(userData,params);
		default:
			return null;
		}
}
public static LLDAModelTrainer getLLDAModelTrainer(TopicModelTrainStrategy strategy,List<UserData> userData,LLDATopicModel model) {
		switch (strategy) {
		case MSG_STRATEGY:
			return new LLDAModelTrainerMSG(userData,model);
		case UP_STRATEGY:
			return new LLDAModelTrainerUP(userData,model);
		case HASH_STRATEGY:
			return new LLDAModelTrainerHASH(userData,model);
		default:
			return null;
		}
	}

public static BTMModelTrainer getBTMModelTrainer(String model_dir,TopicModelTrainStrategy strategy,List<UserData> userData,BTMParameters params) {
	switch (strategy) {
	case MSG_STRATEGY:
		return new BTMModelTrainerMSG(model_dir,userData,params);
	case UP_STRATEGY:
		return new BTMModelTrainerUP(model_dir,userData,params);
	case HASH_STRATEGY:
		return new BTMModelTrainerHASH(model_dir,userData,params);
	default:
		return null;
	}
	
}
public static String getName(TopicModelTrainStrategy strategy){
	switch (strategy) {
	case MSG_STRATEGY:
		return "MSG";
	case UP_STRATEGY:
		return "UP";
	case HASH_STRATEGY:
		return "HASH";
	case TEMPORAL_STRATEGY:
		return "TEMP";
	default:
		return null;
	}
}

}

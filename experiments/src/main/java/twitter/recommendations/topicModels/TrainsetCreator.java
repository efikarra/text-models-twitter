package twitter.recommendations.topicModels;

import java.util.List;

import twitter.DataStructures.TrainingDocument;
import twitter.DataStructures.UserData;

public abstract class TrainsetCreator {
	protected UserData data;
	public TrainsetCreator(UserData data) {
		this.data=data;
	}
	public abstract List<TrainingDocument> createTrainset();
}

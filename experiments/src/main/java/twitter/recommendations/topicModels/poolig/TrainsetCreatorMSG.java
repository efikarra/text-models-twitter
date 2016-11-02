package twitter.recommendations.topicModels.poolig;

import java.util.ArrayList;
import java.util.List;

import twitter.DataStructures.TrainingDocument;
import twitter.DataStructures.TweetEvent;
import twitter.DataStructures.UserData;
import twitter.recommendations.topicModels.TrainsetCreator;

public class TrainsetCreatorMSG extends TrainsetCreator{
public TrainsetCreatorMSG(UserData userData) {
	super(userData);
}

@Override
public List<TrainingDocument> createTrainset() {
	List<TrainingDocument> documents = new ArrayList<TrainingDocument>();
	List<TweetEvent> tweets=data.getTrainTweets();
	for (int i = 0; i < tweets.size(); i++) {
		TweetEvent tevent = tweets.get(i);
			documents.add(new TrainingDocument(tevent.getThinTweet().getText(),tevent.getEventId()));
	}
	return documents;
}
}

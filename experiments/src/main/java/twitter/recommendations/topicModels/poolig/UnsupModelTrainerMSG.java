package twitter.recommendations.topicModels.poolig;

import java.util.ArrayList;
import java.util.List;

import twitter.DataStructures.TrainingDocument;
import twitter.DataStructures.TweetEvent;
import twitter.DataStructures.UserData;
import twitter.recommendations.topicModels.UnsupModelTrainer;
import twitter.recommendations.topicModels.UnsupTopicModel;

public class UnsupModelTrainerMSG extends UnsupModelTrainer{
	
	public UnsupModelTrainerMSG(List<UserData> data,UnsupTopicModel topicModel) {
		super(data,topicModel);
	}
	@Override
	public void train() {
		List<TrainingDocument> documents = new ArrayList<TrainingDocument>();
		for (int i = 0; i < data.size(); i++) {
			UserData udata = data.get(i);
			List<TweetEvent> tweets = udata.getTrainTweets();
			for (TweetEvent tevent : tweets) {
				documents.add(new TrainingDocument(tevent.getThinTweet().getText(),tevent.getEventId()));
			}
		}
		topicModel.trainModel(documents);
	}
}

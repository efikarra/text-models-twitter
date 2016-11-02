package twitter.recommendations.topicModels.LLDA;

import java.util.ArrayList;
import java.util.List;

import twitter.DataStructures.TrainingDocument;
import twitter.DataStructures.TweetEvent;
import twitter.DataStructures.UserData;
import twitter.recommendations.topicModels.LDA.LDAParameters;

public class LLDAModelTrainerMSG extends LLDAModelTrainer{
	public LLDAModelTrainerMSG(List<UserData> data,LLDATopicModel topicModel) {
		super(data,topicModel);
	}

	@Override
	public void train() throws Exception {
		List<TrainingDocument> allTweets=new ArrayList<TrainingDocument>();
		for (int i = 0; i < data.size(); i++) {
			UserData udata=data.get(i);
			List<TweetEvent> tweets=udata.getTrainTweets();
			if(tweets.isEmpty())
				continue;
			for (TweetEvent tevent : tweets) {
				allTweets.add(new TrainingDocument(tevent.getThinTweet().getText(),tevent.getEventId()));
			}
		}
		labels = new LLDATweetLabels(allTweets,((LLDAParameters)topicModel.getParams()).getLatentTopics(),((LLDAParameters)topicModel.getParams()).getHashThres());
		topicModel.trainLabeledModel(labelTrainData());
		
	}
}

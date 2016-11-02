package twitter.recommendations.topicModels.LLDA;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import twitter.DataStructures.LabeledDocument;
import twitter.DataStructures.TrainingDocument;
import twitter.DataStructures.Twitter.TweetEvent;
import twitter.DataStructures.Twitter.UserData;

public class LabeledTrainsetCreatorMSG extends LabeledSetCreator {
	LLDAParameters params;
	public LabeledTrainsetCreatorMSG(UserData data,LLDAParameters params) {
		super(data);
		this.params=params;
		
	}

	@Override
	public List<LabeledDocument> createTrainset() {
		List<TrainingDocument> allTweets = new ArrayList<TrainingDocument>();
		List<TweetEvent> tweets = data.getTrainTweets();
		for (TweetEvent tevent : tweets) {
			allTweets.add(new TrainingDocument(tevent.getThinTweet().getText(), tevent.getEventId()));
		}
		labels = new LLDATweetLabels(allTweets, ((LLDAParameters) params).getLatentTopics(),
				((LLDAParameters) params).getHashThres());
		return labels.labelTweets();
	}

}

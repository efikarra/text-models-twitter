package twitter.recommendations.topicModels.poolig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import twitter.DataStructures.TrainingDocument;
import twitter.DataStructures.Twitter.TweetEvent;
import twitter.DataStructures.Twitter.UserData;
import twitter.recommendations.topicModels.TrainsetCreator;

public class TrainsetCreatorUP extends TrainsetCreator {
	protected Map<String, String> profilesMap;

	public TrainsetCreatorUP(UserData userData) {
		super(userData);
		profilesMap = new HashMap<String, String>();

	}

	@Override
	public List<TrainingDocument> createTrainset() {
		List<TrainingDocument> documents = new ArrayList<TrainingDocument>();
		List<TweetEvent> tweets = data.getTrainTweets();
		for (TweetEvent tevent : tweets) {
			String username = tevent.getUser();
			String profile = profilesMap.get(username);
			if (profile == null)
				profile = tevent.getThinTweet().getText();
			else
				profile += " " + tevent.getThinTweet().getText();
			profilesMap.put(username, profile);
		}
		int id = 0;
		for (Map.Entry<String, String> entry : profilesMap.entrySet()) {
			documents.add(new TrainingDocument(entry.getValue(), id));
			id++;
		}
		return documents;
	}

}

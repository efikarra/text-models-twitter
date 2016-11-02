package twitter.recommendations.topicModels.poolig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import twitter.DataStructures.TrainingDocument;
import twitter.DataStructures.Twitter.TweetEvent;
import twitter.DataStructures.Twitter.UserData;
import twitter.recommendations.topicModels.UnsupModelTrainer;
import twitter.recommendations.topicModels.UnsupTopicModel;

public class UnsupModelTrainerUP extends UnsupModelTrainer {
	protected Map<String, String> profilesMap;

	public UnsupModelTrainerUP(List<UserData> data, UnsupTopicModel topicModel) {
		super(data, topicModel);
		profilesMap = new HashMap<String, String>();
	}

	@Override
	public void train() {
		List<TrainingDocument> documents = new ArrayList<TrainingDocument>();
		for (int i = 0; i < data.size(); i++) {
			UserData udata = data.get(i);
			List<TweetEvent> tweets = udata.getTrainTweets();
			if (tweets.isEmpty())
				continue;
			for (TweetEvent tevent : tweets) {
				String username = tevent.getUser();
				String profile = profilesMap.get(username);
				if (profile == null)
					profile = tevent.getThinTweet().getText();
				else
					profile += " " + tevent.getThinTweet().getText();
				profilesMap.put(username, profile);
			}
		}
		int id = 0;
		for (Map.Entry<String, String> entry : profilesMap.entrySet()) {
			documents.add(new TrainingDocument(entry.getValue(), id));
			id++;
		}
		System.out.println("UP:"+documents.size());
		topicModel.trainModel(documents);
	}

	public Map<String, String> getProfilesMap() {
		return profilesMap;
	}

	public void setProfilesMap(Map<String, String> profilesMap) {
		this.profilesMap = profilesMap;
	}
}

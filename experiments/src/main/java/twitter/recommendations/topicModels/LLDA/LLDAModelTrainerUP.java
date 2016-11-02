package twitter.recommendations.topicModels.LLDA;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import twitter.DataStructures.ThinTweet;
import twitter.DataStructures.TrainingDocument;
import twitter.DataStructures.TweetEvent;
import twitter.DataStructures.UserData;
import twitter.recommendations.topicModels.LDA.LDAParameters;

public class LLDAModelTrainerUP extends LLDAModelTrainer{
	protected Map<String, String> profilesMap;
	public LLDAModelTrainerUP(List<UserData> data,LLDATopicModel topicModel) {
		super(data,topicModel);
		profilesMap=new HashMap<String,String>();
	}
	@Override
	public void train() throws Exception {
		List<TrainingDocument> allTweets=new ArrayList<TrainingDocument>();
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
			allTweets.add(new TrainingDocument(entry.getValue(), id));
			id++;
		}
		labels = new LLDATweetLabels(allTweets,((LLDAParameters)topicModel.getParams()).getLatentTopics(),((LLDAParameters)topicModel.getParams()).getHashThres());
		topicModel.trainLabeledModel(labelTrainData());
		
	}
	public Map<String, String> getProfilesMap() {
		return profilesMap;
	}
	public void setProfilesMap(Map<String, String> profilesMap) {
		this.profilesMap = profilesMap;
	}

}

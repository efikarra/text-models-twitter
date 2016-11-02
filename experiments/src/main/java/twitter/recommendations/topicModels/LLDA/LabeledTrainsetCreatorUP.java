package twitter.recommendations.topicModels.LLDA;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import twitter.DataStructures.LabeledDocument;
import twitter.DataStructures.TrainingDocument;
import twitter.DataStructures.Twitter.TweetEvent;
import twitter.DataStructures.Twitter.UserData;

public class LabeledTrainsetCreatorUP extends LabeledSetCreator{
	protected LLDAParameters params;
	protected Map<String, String> profilesMap;

	public LabeledTrainsetCreatorUP(UserData data,LLDAParameters params) {
		super(data);
		this.params=params;
		profilesMap=new HashMap<String,String>();
	}

@Override
public List<LabeledDocument> createTrainset() {
	List<TrainingDocument> allTweets=new ArrayList<TrainingDocument>();

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
		allTweets.add(new TrainingDocument(entry.getValue(), id));
		id++;
	}
	labels = new LLDATweetLabels(allTweets,((LLDAParameters)params).getLatentTopics(),((LLDAParameters)params).getHashThres());
	return labels.labelTweets();
}
}

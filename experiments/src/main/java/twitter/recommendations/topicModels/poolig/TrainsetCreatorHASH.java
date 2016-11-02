package twitter.recommendations.topicModels.poolig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.twitter.Extractor;

import twitter.DataStructures.TrainingDocument;
import twitter.DataStructures.TweetEvent;
import twitter.DataStructures.UserData;
import twitter.recommendations.topicModels.TrainsetCreator;

public class TrainsetCreatorHASH extends TrainsetCreator {
	protected Map<String, String> hashtagDocsMap;
	protected List<String> notTaggedDocs;

	public TrainsetCreatorHASH(UserData userData) {
		super(userData);
		hashtagDocsMap = new HashMap<String, String>();
		notTaggedDocs = new ArrayList<String>();
	}

	@Override
	public List<TrainingDocument> createTrainset() {
		List<TrainingDocument> documents = new ArrayList<TrainingDocument>();

		Extractor extractor = new Extractor();
		List<TweetEvent> tweets = data.getTrainTweets();
		for (TweetEvent tevent : tweets) {
			String tweetText = tevent.getThinTweet().getText();
			Set<String> extractedHashtags = new HashSet<String>(extractor.extractHashtags(tweetText));
			if (extractedHashtags.isEmpty())
				notTaggedDocs.add(tweetText);
			for (String hashtag : extractedHashtags) {
				String profile = hashtagDocsMap.get(hashtag);
				if (profile == null)
					profile = tweetText;
				else
					profile += " " + tweetText;
				hashtagDocsMap.put(hashtag, profile);
			}
		}
		int id = 0;
		for (Map.Entry<String, String> entry : hashtagDocsMap.entrySet()) {
			documents.add(new TrainingDocument(entry.getValue(), id));
			id++;
		}
		for (String doc : notTaggedDocs) {
			documents.add(new TrainingDocument(doc, id));
			id++;
		}
		return documents;
	}

}

package twitter.recommendations.topicModels.LLDA;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.twitter.Extractor;

import twitter.DataStructures.LabeledDocument;
import twitter.DataStructures.TrainingDocument;
import twitter.DataStructures.Twitter.TweetEvent;
import twitter.DataStructures.Twitter.UserData;

public class LabeledTrainsetCreatorHASH extends LabeledSetCreator{
	protected LLDAParameters params;
	protected Map<String, String> hashtagDocsMap;
	protected List<String> notTaggedDocs;
	public LabeledTrainsetCreatorHASH(UserData data,LLDAParameters params) {
		super(data);
		this.params=params;
		hashtagDocsMap = new HashMap<String, String>();
		notTaggedDocs=new ArrayList<String>();
	}
	@Override
	public List<LabeledDocument> createTrainset() {
		List<TrainingDocument> allTweets=new ArrayList<TrainingDocument>();

		Extractor extractor = new Extractor();
			List<TweetEvent> tweets = data.getTrainTweets();
			for(TweetEvent tevent:tweets){
			String tweetText = tevent.getThinTweet().getText();
			Set<String> extractedHashtags = new HashSet<String>(extractor.extractHashtags(tweetText));
			if(extractedHashtags.isEmpty())
				notTaggedDocs.add(tweetText);
			for (String hashtag : extractedHashtags) {
				String profile=hashtagDocsMap.get(hashtag);
				if(profile==null)
					profile=tweetText;
				else
					profile+=" "+tweetText;
				hashtagDocsMap.put(hashtag, profile);
			}
		}
		int id = 0;
		for (Map.Entry<String, String> entry : hashtagDocsMap.entrySet()) {
			allTweets.add(new TrainingDocument(entry.getValue(),id ));
			id++;
		}
		for(String doc:notTaggedDocs){
			allTweets.add(new TrainingDocument(doc, id));
			id++;
		}
		labels = new LLDATweetLabels(allTweets,((LLDAParameters)params).getLatentTopics(),((LLDAParameters)params).getHashThres());
		return labels.labelTweets();
	}

}

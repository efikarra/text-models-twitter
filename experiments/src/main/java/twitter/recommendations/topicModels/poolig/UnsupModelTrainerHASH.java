package twitter.recommendations.topicModels.poolig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.twitter.Extractor;

import twitter.DataStructures.TrainingDocument;
import twitter.DataStructures.Twitter.TweetEvent;
import twitter.DataStructures.Twitter.UserData;
import twitter.recommendations.topicModels.UnsupModelTrainer;
import twitter.recommendations.topicModels.UnsupTopicModel;

public class UnsupModelTrainerHASH extends UnsupModelTrainer {
	protected Map<String, String> hashtagDocsMap;
	protected List<String> notTaggedDocs;
	public UnsupModelTrainerHASH(List<UserData> data, UnsupTopicModel topicModel) {
		super(data, topicModel);
		hashtagDocsMap = new HashMap<String, String>();
		notTaggedDocs=new ArrayList<String>();
	}

	@Override
	public void train() throws Exception {
		List<TrainingDocument> documents = new ArrayList<TrainingDocument>();

		Extractor extractor = new Extractor();
		for (int i = 0; i < data.size(); i++) {
			UserData udata = data.get(i);
			List<TweetEvent> tweets = udata.getTrainTweets();
			if (tweets.isEmpty())
				continue;
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
		}
		int id = 0;
		for (Map.Entry<String, String> entry : hashtagDocsMap.entrySet()) {
			documents.add(new TrainingDocument(entry.getValue(), id));
			id++;
		}
		for(String doc:notTaggedDocs){
			documents.add(new TrainingDocument(doc, id));
			id++;
		}
		System.out.println("HASH:"+documents.size());
		topicModel.trainModel(documents);
	}

	public Map<String, String> getHashtagDocsMap() {
		return hashtagDocsMap;
	}

	public void setHashtagDocsMap(Map<String, String> hashtagDocsMap) {
		this.hashtagDocsMap = hashtagDocsMap;
	}
}

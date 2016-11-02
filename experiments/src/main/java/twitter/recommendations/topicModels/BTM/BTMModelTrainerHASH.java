package twitter.recommendations.topicModels.BTM;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.ooon.lzj.model.Model;
import com.ooon.lzj.pretreatment.TestData2id;
import com.ooon.lzj.pretreatment.word2id;
import com.twitter.Extractor;

import twitter.DataStructures.Twitter.BTMTweet;
import twitter.DataStructures.Twitter.TweetEvent;
import twitter.DataStructures.Twitter.UserData;

public class BTMModelTrainerHASH extends BTMModelTrainer {
	protected Map<String, String> hashtagDocsMap;
	protected List<String> notTaggedDocs;

	public BTMModelTrainerHASH(String tmodel_dir, List<UserData> data, BTMParameters param) {
		super(tmodel_dir, data, param);
		hashtagDocsMap = new HashMap<String, String>();
		notTaggedDocs = new ArrayList<String>();
	}

	@Override
	public void train() throws Exception {
		List<String> trainDocs = new ArrayList<String>();
		Extractor extractor = new Extractor();
		for (int i = 0; i < data.size(); i++) {
			UserData udata = data.get(i);
			List<TweetEvent> tweets = udata.getTrainTweets();
			if (tweets.isEmpty())
				continue;
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
		}
		for (Map.Entry<String, String> entry : hashtagDocsMap.entrySet()) {
			trainDocs.add(entry.getValue());
		}
		for (String doc : notTaggedDocs) {
			trainDocs.add(doc);
		}

		word2id wi = new word2id();
		wi.computeDocsToIds(trainDocs);
		List<Integer[]> mappedIds = wi.getDocs();

		TestData2id testData2id = new TestData2id(wi.getWord2id());
		for (UserData userData : data){
			List<BTMTweet> list = new ArrayList<BTMTweet>();
			trainData.put(userData.getUserName(), list);
			for (TweetEvent tevent : userData.getTrainTweets()) {
				Integer[] ids = testData2id.getDocIds(tevent.getThinTweet().getText());
				if (ids.length != 0)
					list.add(new BTMTweet(tevent, ids));
			}
		}
		for (UserData userData : data) {
			List<BTMTweet> list = new ArrayList<BTMTweet>();
			testData.put(userData.getUserName(), list);
			for (TweetEvent tevent : userData.getTestTweets()) {
				Integer[] ids = testData2id.getDocIds(tevent.getThinTweet().getText());
				if (ids.length != 0)
					list.add(new BTMTweet(tevent, ids));
			}
		}
		biterm_model = new Model(param.getK(), wi.getWord2id().size(), param.getAlpha(), param.getBeta(),
				param.getnIter(), param.getSave_step(), param.getWindow());
		biterm_model.run(mappedIds, tmodel_dir);

	}
}

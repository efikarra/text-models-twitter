package twitter.recommendations.topicModels.BTM;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ooon.lzj.model.Model;
import com.ooon.lzj.pretreatment.TestData2id;
import com.ooon.lzj.pretreatment.word2id;

import twitter.DataStructures.BTMTweet;
import twitter.DataStructures.TrainingDocument;
import twitter.DataStructures.TweetEvent;
import twitter.DataStructures.UserData;
import twitter.modelUtilities.ModelsUtilities;

public class BTMModelTrainerUP extends BTMModelTrainer {
	protected Map<String, String> profilesMap;

	public BTMModelTrainerUP(String tmodel_dir, List<UserData> data, BTMParameters params) {
		super(tmodel_dir, data, params);
		profilesMap = new HashMap<String, String>();
	}

	@Override
	public void train() throws Exception {
		List<String> trainDocs = new ArrayList<String>();
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
		for (Map.Entry<String, String> entry : profilesMap.entrySet()) {
			trainDocs.add(entry.getValue());
		}
		word2id wi = new word2id();
		wi.computeDocsToIds(trainDocs);
		List<Integer[]> mappedIds = wi.getDocs();

		TestData2id testData2id = new TestData2id(wi.getWord2id());
		for (UserData userData : data) {
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
		biterm_model = new Model(param.getK(), wi.getWord2id().size(), param.getAlpha(), param.getBeta(), param.getnIter(), param.getSave_step(),param.getWindow());
		biterm_model.run(mappedIds, tmodel_dir);
	}

	
}

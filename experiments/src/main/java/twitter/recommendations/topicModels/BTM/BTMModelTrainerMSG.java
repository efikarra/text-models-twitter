package twitter.recommendations.topicModels.BTM;

import java.util.ArrayList;
import java.util.List;

import com.ooon.lzj.model.Model;
import com.ooon.lzj.pretreatment.TestData2id;
import com.ooon.lzj.pretreatment.word2id;

import twitter.DataStructures.Twitter.BTMTweet;
import twitter.DataStructures.Twitter.TweetEvent;
import twitter.DataStructures.Twitter.UserData;

public class BTMModelTrainerMSG extends BTMModelTrainer {
	public BTMModelTrainerMSG(String tmodel_dir, List<UserData> data, BTMParameters param) {
		super(tmodel_dir, data, param);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void train() throws Exception {
		List<String> trainDocs = new ArrayList<String>();
		List<TweetEvent> trainTweetsAll = new ArrayList<TweetEvent>();
		for (UserData userData : data) {
			if (userData.getTrainTweets().isEmpty())
				continue;
			for (TweetEvent tevent : userData.getTrainTweets()) {
				trainDocs.add(tevent.getThinTweet().getText());
				trainTweetsAll.add(tevent);
			}
		}
		word2id wi = new word2id();
		wi.computeDocsToIds(trainDocs);

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
				if (ids.length != 0) {
					list.add(new BTMTweet(tevent, ids));
				}
			}

		}
		biterm_model = new Model(param.getK(), wi.getWord2id().size(), param.getAlpha(), param.getBeta(),
				param.getnIter(), param.getSave_step(), param.getWindow());
		biterm_model.run(wi.getDocs(), tmodel_dir);

	}

}

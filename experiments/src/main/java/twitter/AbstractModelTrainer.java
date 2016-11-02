package twitter;

import twitter.DataStructures.Twitter.UserData;
import twitter.recommendations.evaluation.RankingMetrics;
import twitter.recommendations.topicModels.AbstractTopicModel;

public abstract class AbstractModelTrainer {
	public AbstractModelTrainer(UserData userData) {
		this.userData=userData;
	}
	protected RankingMetrics metrics;
	protected UserData userData;
	public abstract void modelTestTweets() throws Exception;
	public abstract void evaluateUserModel();
	public RankingMetrics getMetrics() {
		return metrics;
	}
	public void setMetrics(RankingMetrics metrics) {
		this.metrics = metrics;
	}
	public UserData getUserData() {
		return userData;
	}
	public void setUserData(UserData userData) {
		this.userData = userData;
	}
}

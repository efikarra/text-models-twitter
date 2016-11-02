package twitter.recommendations.baseline;

import java.util.Collections;
import java.util.List;

import twitter.AbstractModelTrainer;
import twitter.DataStructures.Twitter.TweetEvent;
import twitter.DataStructures.Twitter.UserData;
import twitter.recommendations.evaluation.RankingMetrics;

public class RandomBaselineTrainer extends AbstractModelTrainer {
	public RandomBaselineTrainer(UserData userData) {
		super(userData);
	}

	private void rankTweets() {
		// System.out.println(userData.getUserName());
		double MAP_sum = 0.0;
		for (int i = 0; i < 1; i++) {
			List<TweetEvent> testData = userData.getTestTweets();
			Collections.shuffle(testData);
			metrics = new RankingMetrics();
			int tweetsCounter = 1;
			for (int j = 0; j < testData.size(); j++) {
				TweetEvent tweet = testData.get(j);
				if (tweet.isRetweeted()) {
					metrics.getRankingPositions().add(tweetsCounter);
				}
				tweetsCounter++;
			}
			metrics.computeAP();
			MAP_sum += metrics.getAvgPrecision();
		}
		metrics.setAvgPrecision(MAP_sum / 1);
		// System.out.println(MAP_sum/1);
	}

	@Override
	public void evaluateUserModel() {
		metrics.printPerfomanceEvaluation();
	}

	@Override
	public void modelTestTweets() {
		rankTweets();

	}
}

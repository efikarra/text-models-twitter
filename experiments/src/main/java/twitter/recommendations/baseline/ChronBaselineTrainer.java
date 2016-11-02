package twitter.recommendations.baseline;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import twitter.AbstractModelTrainer;
import twitter.DataStructures.Twitter.TweetEvent;
import twitter.DataStructures.Twitter.UserData;
import twitter.Utilities.Constants;
import twitter.recommendations.evaluation.RankingMetrics;

public class ChronBaselineTrainer extends AbstractModelTrainer{
	public ChronBaselineTrainer(UserData userData) {
		super(userData);
	}
	 
	private void rankTweets() {
		 metrics = new RankingMetrics();
		 List<TweetEvent> timeline=userData.getTestTweets();
		 Collections.sort(timeline);
		 Collections.reverse(timeline);
		 int tweetsCounter=1;
		 for (int i=0;i<timeline.size();i++) {
			 TweetEvent tweet=timeline.get(i);
			 //if(tweet.isRetweeted())
			//	 System.out.println(Constants.DEFAULT_DATE_FORMAT.format(new Date(tweet.getThinTweet().getTimeStamp()))+" "+tweet.isRetweeted());
			 if(tweet.isRetweeted())
				 metrics.getRankingPositions().add(tweetsCounter);
			 tweetsCounter++;
		 }
	}
	@Override
	public void evaluateUserModel(){
		metrics.printPerfomanceEvaluation();
	}
	


	@Override
	public void modelTestTweets() {
		rankTweets() ;
		
	}
}

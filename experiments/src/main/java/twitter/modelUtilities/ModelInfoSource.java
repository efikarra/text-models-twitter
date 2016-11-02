package twitter.modelUtilities;

import twitter.TrainDataCreator;
import twitter.TrainPDataCreator;
import twitter.DataStructures.Twitter.ParsedUserActivity;
import twitter.DataStructures.Twitter.TweetEvent;
import twitter.DataStructures.Twitter.UserActivity;
import twitter.DataStructures.Twitter.UserData;

public enum ModelInfoSource {
	URT,UT,FR,FE,RE,UT_URT,U_FR,URT_FR,U_FE,URT_FE,FE_FR,U_RE,URT_RE;
	
	public static String name(ModelInfoSource modelType) {
	switch (modelType) {
	case URT:
		return "URT";
	case UT:
		return "UT";
	case FR:
		return "FR";
	case FE:
		return "FE";
	case RE:
		return "RE";
	case UT_URT:
		return "UT_URT";
	case U_FR:
		return "U_FR";
	case URT_FR:
		return "URT_FR";
	case U_FE:
		return "U_FE";
	case URT_FE:
		return "URT_FE";
	case FE_FR:
		return "FE_FR";
	case U_RE:
		return "U_RE";
	case URT_RE:
		return "URT_RE";
	default:
		return null;
	}
	}
	public static UserData computeUserData(ModelInfoSource modelType,UserActivity userActivity,UserData userData,double trainPercent,long refTime) {
		TrainDataCreator creator=new TrainDataCreator(userActivity,userData,refTime);
		switch (modelType) {
		case URT:
			creator.postedReTweets(trainPercent);
			break;
		case UT:
			creator.postedTweets(trainPercent);
			break;
		case FR:
			creator.followersTweetsRetweets(trainPercent);
			break;
		case FE:
			creator.followeesTweetsRetweets(trainPercent);
			break;
		case RE:
			creator.RCTweetsRetweets(trainPercent);
			break;
		case UT_URT:
			creator.postedTweetsRetweets(trainPercent);
			break;
		case U_FR:
			creator.postedTweetsFollowersTweetsRT(trainPercent);
			break;
		case URT_FR:
			creator.postedReTweetsFollowersTweetsRT(trainPercent);
			break;
		case U_FE:
			creator.postedTweetsFolloweesTweetsRT(trainPercent);
			break;
		case URT_FE:
			creator.postedReTweetsFolloweesTweetsRT(trainPercent);
			break;
		case FE_FR:
			creator.followeesFollowersTweetsRT(trainPercent);
			break;
		case U_RE:
			creator.postedTweetsRC(trainPercent);
			break;
		case URT_RE:
			creator.postedReTweetsRC(trainPercent);
			break;
		default:
			creator.postedTweets(trainPercent);
			break;
		}
		return creator.getUserData();
	}
	public static UserData computeUserDataP(ModelInfoSource modelType,ParsedUserActivity userActivity,UserData userData,double trainPercent,long refDate) {
		TrainPDataCreator creator=new TrainPDataCreator(userActivity,userData,refDate);
		switch (modelType) {
		case URT:
			creator.postedReTweets(trainPercent);
			break;
		case UT:
			creator.postedTweets(trainPercent);
			break;
		case FR:
			creator.followersTweetsRetweets(trainPercent);
			break;
		case FE:
			creator.followeesTweetsRetweets(trainPercent);
			break;
		case RE:
			creator.RCTweetsRetweets(trainPercent);
			break;
		case UT_URT:
			creator.postedTweetsRetweets(trainPercent);
			break;
		case U_FR:
			creator.postedTweetsFollowersTweetsRT(trainPercent);
			break;
		case URT_FR:
			creator.postedReTweetsFollowersTweetsRT(trainPercent);
			break;
		case U_FE:
			creator.postedTweetsFolloweesTweetsRT(trainPercent);
			break;
		case URT_FE:
			creator.postedReTweetsFolloweesTweetsRT(trainPercent);
			break;
		case FE_FR:
			creator.followeesFollowersTweetsRT(trainPercent);
			break;
		case U_RE:
			creator.postedTweetsRC(trainPercent);
			break;
		case URT_RE:
			creator.postedReTweetsRC(trainPercent);
			break;
		default:
			creator.postedTweets(trainPercent);
			break;
		}
		assignIds(creator.getUserData());
		return creator.getUserData();
	}
	private static void assignIds(UserData userdata) {
		int eventId=0;
		for(TweetEvent tevent: userdata.getTrainTweets()){
			tevent.setEventId(eventId);
			eventId++;
		}
		for(TweetEvent tevent: userdata.getTestTweets()){
			tevent.setEventId(eventId);
			eventId++;
		}
		
	}
}

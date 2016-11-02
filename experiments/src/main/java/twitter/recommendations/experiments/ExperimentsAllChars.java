package twitter.recommendations.experiments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import twitter.TestDataCreator;
import twitter.DataStructures.Twitter.ParsedUserActivity;
import twitter.DataStructures.Twitter.UserActivity;
import twitter.DataStructures.Twitter.UserData;
import twitter.Utilities.Twitter.GetUserActivities;
import twitter.modelUtilities.MergeStrategy;
import twitter.modelUtilities.ModelInfoSource;
import twitter.modelUtilities.ModelsUtilities;
import twitter.modelUtilities.RoccioModelType;
import twitter.modelUtilities.TopicModelTrainStrategy;
import twitter.recommendations.content.RoccioParams;
import twitter.recommendations.topicModels.BTM.BTMParameters;
import twitter.recommendations.topicModels.LDA.LDAParameters;

public class ExperimentsAllChars {
	public static void main(String[] args) throws Exception {
		int matchedThreshold = 20;
		// String usersPath =
		// "/home/efi/master-thesis/resultUsers/serializedUsersdupls/";
		String usersPath = "/home/efikarra/Twitter/experiments/processing/serializedUsersdupls/";
		// String usersPath =
		// "/media/efi/APOTHETHS/master/thesis/dataset/serializedUsers/";
		// String usersPath="D:\\master\\thesis\\dataset\\serializedUsers\\";
		// String
		// usersPath="/home/efikarra/Twitter/experiments/processing/serializedUsers/";
		Map<String, String> usersMap = new HashMap<String, String>();
		GetUserActivities getActivities = new GetUserActivities(usersPath);
		List<UserActivity> pacts = new ArrayList<UserActivity>();
		List<UserActivity> bottom = getActivities.getUserActivities("bottom");
		List<UserActivity> top = getActivities.getUserActivities("top");
		List<UserActivity> avg = getActivities.getUserActivities("avg");
		Map<String, String> usersStrictMap = new HashMap<String, String>();
		for (UserActivity activity : top) {
			int inc = ModelsUtilities.getIncomingFreq(activity);
			int out = ModelsUtilities.getOutgoingFreq(activity);
			double ratio = (double) ((double) out / (double) inc);
			if (ratio >= 2) {
				usersStrictMap.put("top_new", activity.getUserName());
			}
		}
		for (UserActivity userActivity : top)
			ModelsUtilities.userStatistics(userActivity);
		for (UserActivity activity : bottom) {
			usersMap.put(activity.getUserName(), "bottom");
		}
		for (UserActivity activity : top) {
			usersMap.put(activity.getUserName(), "top");
		}
		for (UserActivity activity : avg) {
			usersMap.put(activity.getUserName(), "avg");
		}

		pacts.addAll(bottom);
		pacts.addAll(top);
		pacts.addAll(avg);
		System.out.println("size " + pacts.size());
		List<ModelInfoSource> types = new ArrayList<ModelInfoSource>();
		types.add(ModelInfoSource.URT);
		types.add(ModelInfoSource.UT);
		types.add(ModelInfoSource.FR);
		types.add(ModelInfoSource.FE);
		types.add(ModelInfoSource.RE);
		types.add(ModelInfoSource.UT_URT);
		types.add(ModelInfoSource.U_FR);
		types.add(ModelInfoSource.URT_FR);
		types.add(ModelInfoSource.U_FE);
		types.add(ModelInfoSource.URT_FE);
		types.add(ModelInfoSource.FE_FR);
		types.add(ModelInfoSource.U_RE);
		types.add(ModelInfoSource.URT_RE);
		List<MergeStrategy> mergeStrategies = new ArrayList<MergeStrategy>();
		mergeStrategies.add(MergeStrategy.CENTROID_STRATEGY);
		Map<String, UserData> userDataMap = new HashMap<String, UserData>();
		Map<String, Long> refDateMap = new HashMap<String, Long>();
		List<RoccioParams> params = new ArrayList<RoccioParams>();
		params.add(new RoccioParams(0.8, -1));
		List<RoccioModelType> rtypes = new ArrayList<RoccioModelType>();
		rtypes.add(RoccioModelType.TF);
		rtypes.add(RoccioModelType.TF_IDF);
		// create test data for all users
		for (int i = 0; i < pacts.size(); i++) {
			UserData userData = new UserData(pacts.get(i).getUserName());
			TestDataCreator creator = new TestDataCreator(pacts.get(i), userData);
			creator.computeTestTweets(0.8);
			userDataMap.put(pacts.get(i).getUserName(), userData);
			ModelsUtilities.printTestSetStatistics(userData);
			refDateMap.put(pacts.get(i).getUserName(), creator.getRefTime());
		}
		ExperimentsBaseline.runRandom(userDataMap, usersMap, usersStrictMap);
		ExperimentsBaseline.runChronological(userDataMap, usersMap, usersStrictMap);
		for (ModelInfoSource type : types) {

			for (int i = 0; i < pacts.size(); i++)
				ModelInfoSource.computeUserData(type, pacts.get(i), userDataMap.get(pacts.get(i).getUserName()), 0.8,
						refDateMap.get(pacts.get(i).getUserName()));
			// print dataset statistics
			for (UserData uData : userDataMap.values()) {
				ModelsUtilities.printTrainSetStatistics(uData, type);
			}
		//	ExperimentsContentM.runCharBagsRoccio(type, userDataMap, usersMap, params, rtypes, usersStrictMap);
			ExperimentsContentM.runCharGraphs(type, userDataMap, usersMap, usersStrictMap);
			ExperimentsContentM.runCharBags(type, userDataMap, usersMap, usersStrictMap);
		}

	}

	private static void removeUsers(List<UserActivity> activities, int matchedThreshold) {
		Iterator<UserActivity> iterator = activities.iterator();
		while (iterator.hasNext()) {
			if (ModelsUtilities.matchedRetweets(iterator.next()) < matchedThreshold) {
				iterator.remove();
			}

		}

	}
}

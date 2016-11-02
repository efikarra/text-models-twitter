package twitter.recommendations.experiments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import twitter.TestDataCreator;
import twitter.TestPDataCreator;
import twitter.DataStructures.Twitter.ParsedUserActivity;
import twitter.DataStructures.Twitter.UserActivity;
import twitter.DataStructures.Twitter.UserData;
import twitter.Utilities.Twitter.GetUserActivities;
import twitter.modelUtilities.MergeStrategy;
import twitter.modelUtilities.ModelInfoSource;
import twitter.modelUtilities.ModelsUtilities;
import twitter.modelUtilities.RoccioModelType;
import twitter.recommendations.content.RoccioParams;

public class ExperimentsRoccioAll {
	public static void main(String[] args) throws Exception {
		tokensRoccio();
		// charsRoccio();
	}

	public static void tokensRoccio() throws Exception {

		//String usersPath = "/home/efi/master-thesis/resultUsers/filteredUsers100/";
		String usersPath ="/media/efi/APOTHETHS/master/thesis/dataset/filteredUsersFinal100only/";

		Map<String, String> usersMap = new HashMap<String, String>();
		GetUserActivities getActivities = new GetUserActivities(usersPath);
		List<ParsedUserActivity> pacts = new ArrayList<ParsedUserActivity>();
		List<ParsedUserActivity> bottom = getActivities.getFilteredUserActivities("bottom");
		List<ParsedUserActivity> top = getActivities.getFilteredUserActivities("top");
		List<ParsedUserActivity> avg = getActivities.getFilteredUserActivities("avg");

		for (ParsedUserActivity activity : bottom) {
			usersMap.put(activity.getUserName(), "bottom");
		}
		for (ParsedUserActivity activity : top) {
			usersMap.put(activity.getUserName(), "top");
		}
		for (ParsedUserActivity activity : avg) {
			usersMap.put(activity.getUserName(), "avg");
		}
		pacts.addAll(bottom);
		pacts.addAll(top);
		pacts.addAll(avg);

		List<ModelInfoSource> types = new ArrayList<ModelInfoSource>();

		types.add(ModelInfoSource.FE);
		types.add(ModelInfoSource.RE);
		// types.add(ModelType.U_FE);
		// types.add(ModelType.URT_FE);
		// types.add(ModelType.FE_FR);
		// types.add(ModelType.U_RE);
		// types.add(ModelType.URT_RE);

		List<RoccioParams> params = new ArrayList<RoccioParams>();
		params.add(new RoccioParams(0.8, -1));
		List<RoccioModelType> rtypes = new ArrayList<RoccioModelType>();
		 rtypes.add(RoccioModelType.TF);
		rtypes.add(RoccioModelType.TF_IDF);

		Map<String, UserData> userDataMap = new HashMap<String, UserData>();
		// create test data for all users
		Map<String, Long> refDateMap = new HashMap<String, Long>();

		// create test data for all users
		for (int i = 0; i < pacts.size(); i++) {
			UserData userData = new UserData(pacts.get(i).getUserName());
			TestPDataCreator creator = new TestPDataCreator(pacts.get(i), userData);
			creator.getTestTweets(0.8);
			userDataMap.put(pacts.get(i).getUserName(), userData);
			ModelsUtilities.printTestSetStatistics(userData);
			refDateMap.put(pacts.get(i).getUserName(), creator.getRefTime());
		}
		//ExperimentsBaseline.runRandom(userDataMap, usersMap);
		for (ModelInfoSource type : types) {

			for (int i = 0; i < pacts.size(); i++)
				ModelInfoSource.computeUserDataP(type, pacts.get(i), userDataMap.get(pacts.get(i).getUserName()), 0.8,
						refDateMap.get(pacts.get(i).getUserName()));
			// print dataset statistics
			for (UserData uData : userDataMap.values()) {
				ModelsUtilities.printTrainSetStatistics(uData, type);
			}

			// ExperimentsContentM.runTokenGraphs(type.name(), userDataMap,
			// usersMap);
			//ExperimentsContentM.runTokenBagsRoccio(type, userDataMap, usersMap, params, rtypes);
		}

	}

	public static void charsRoccio() throws Exception {
		int matchedThreshold = 20;
		String usersPath = "/home/efikarra/Twitter/experiments/processing/serializedUsersdupls/";

		Map<String, String> usersMap = new HashMap<String, String>();
		GetUserActivities getActivities = new GetUserActivities(usersPath);
		List<UserActivity> pacts = new ArrayList<UserActivity>();
		List<UserActivity> bottom = getActivities.getUserActivities("bottom");
		List<UserActivity> top = getActivities.getUserActivities("top");
		List<UserActivity> avg = getActivities.getUserActivities("avg");
		
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
		
		List<RoccioParams> params = new ArrayList<RoccioParams>();
		params.add(new RoccioParams(0.8, -1));
		List<RoccioModelType> rtypes = new ArrayList<RoccioModelType>();
		rtypes.add(RoccioModelType.TF);
	
		List<MergeStrategy> mergeStrategies = new ArrayList<MergeStrategy>();
		mergeStrategies.add(MergeStrategy.CENTROID_STRATEGY);
		Map<String, UserData> userDataMap = new HashMap<String, UserData>();

		// create test data for all users
		Map<String, Long> refDateMap = new HashMap<String, Long>();

		//create test data for all users
		for (int i = 0; i < pacts.size(); i++){
			UserData userData=new UserData(pacts.get(i).getUserName());
			TestDataCreator creator=new TestDataCreator(pacts.get(i),userData);
			creator.computeTestTweets(0.8);
			userDataMap.put(pacts.get(i).getUserName(), userData);
			ModelsUtilities.printTestSetStatistics(userData);
			refDateMap.put(pacts.get(i).getUserName(), creator.getRefTime());
		}
		//ExperimentsBaseline.runRandom( userDataMap, usersMap);
		for (ModelInfoSource type : types) {
			
			for (int i = 0; i < pacts.size(); i++)
				ModelInfoSource.computeUserData(type, pacts.get(i),userDataMap.get(pacts.get(i).getUserName()), 0.8,refDateMap.get(pacts.get(i).getUserName()));
			//print dataset statistics
			for(UserData uData:userDataMap.values()){
				ModelsUtilities.printTrainSetStatistics(uData,type);
			}
			//ExperimentsContentM.runCharBagsRoccio (type, userDataMap, usersMap,params, rtypes);
		}

	}
}

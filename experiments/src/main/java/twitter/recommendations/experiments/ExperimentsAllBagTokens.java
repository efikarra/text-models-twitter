package twitter.recommendations.experiments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import twitter.TestPDataCreator;
import twitter.DataStructures.Twitter.ParsedUserActivity;
import twitter.DataStructures.Twitter.UserData;
import twitter.Utilities.Twitter.GetUserActivities;
import twitter.modelUtilities.ModelInfoSource;
import twitter.modelUtilities.ModelsUtilities;

public class ExperimentsAllBagTokens {
	public static void main(String[] args) throws Exception {
		
		//String usersPath = "/home/efi/master-thesis/resultUsers/";
		//String usersPath = "/home/efikarra/Twitter/experiments/processing/filteredUsers/";
		//String usersPath = "/home/efi/master-thesis/resultUsers/filteredUsersFinal100only/";
		String usersPath ="/media/efi/APOTHETHS/master/thesis/dataset/filteredUsersFinal100only/";
		//String usersPath = "/home/efi/master-thesis/resultUsers/filteredUsersChars/";
		//String usersPath ="D:\\master\\thesis\\dataset\\filteredUsersFinal\\";
		// String usersPath =
		// "/media/efi/APOTHETHS/master/thesis/dataset/serializedUsers/";
		// String usersPath="D:\\master\\thesis\\dataset\\serializedUsers\\";
		// String
		// usersPath="/home/efikarra/Twitter/experiments/processing/serializedUsers/";
		Map<String, String> usersMap = new HashMap<String, String>();
		GetUserActivities getActivities = new GetUserActivities(usersPath);
		List<ParsedUserActivity> pacts = new ArrayList<ParsedUserActivity>();
		List<ParsedUserActivity> bottom = getActivities.getFilteredUserActivities("bottom");
		List<ParsedUserActivity> top = getActivities.getFilteredUserActivities("top");
		List<ParsedUserActivity> avg = getActivities.getFilteredUserActivities("avg");
		
		
//		for(ParsedUserActivity userActivity:top)
//			ModelsUtilities.userStatisticsP(userActivity);
//		for(ParsedUserActivity userActivity:avg)
//			ModelsUtilities.userStatisticsP(userActivity);
//		for(ParsedUserActivity userActivity:bottom)
//			ModelsUtilities.userStatisticsP(userActivity);
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
//		List<String> corpus=ModelsUtilities.getAllCorpusP(pacts);
//		System.out.println("corpuss "+corpus.size());
//		Set<String> corpus2=new HashSet<String>(corpus);

//		System.out.println(corpus2.size());
		
		List<ModelInfoSource> types = new ArrayList<ModelInfoSource>();
		//types.add(ModelInfoSource.URT);
//		types.add(ModelInfoSource.UT);
//		types.add(ModelInfoSource.FR);
		types.add(ModelInfoSource.FE);
		types.add(ModelInfoSource.RE);
//		types.add(ModelInfoSource.UT_URT);
//		types.add(ModelInfoSource.U_FR);
//		types.add(ModelInfoSource.URT_FR);
//		types.add(ModelInfoSource.U_FE);
//		types.add(ModelInfoSource.URT_FE);
//		types.add(ModelInfoSource.FE_FR);
//		types.add(ModelInfoSource.U_RE);
//		types.add(ModelInfoSource.URT_RE);
		Map<String, UserData> userDataMap = new HashMap<String, UserData>();
		Map<String, Long> refDateMap = new HashMap<String, Long>();
		//create test data for all users
		for (int i = 0; i < pacts.size(); i++){
			System.out.println(pacts.get(i).getUserName());
			UserData userData=new UserData(pacts.get(i).getUserName());
			TestPDataCreator creator=new TestPDataCreator(pacts.get(i),userData);
			creator.getTestTweets(0.8);
			userDataMap.put(pacts.get(i).getUserName(), userData);
			ModelsUtilities.printTestSetStatistics(userData);
			refDateMap.put(pacts.get(i).getUserName(), creator.getRefTime());
		}
//		double MAP_sum=0.0;
//		for(int i=0;i<100;i++){
//			double sum=ExperimentsBaseline.runRandom( userDataMap, usersMap);
//			MAP_sum+=sum;}
//		System.out.println(MAP_sum/100);
		//ExperimentsBaseline.runChronological( userDataMap, usersMap);
		for (ModelInfoSource type : types) {;
			
			for (int i = 0; i < pacts.size(); i++)
				ModelInfoSource.computeUserDataP(type, pacts.get(i),userDataMap.get(pacts.get(i).getUserName()), 0.8,refDateMap.get(pacts.get(i).getUserName()));
			//print dataset statistics
			for(UserData uData:userDataMap.values()){
				ModelsUtilities.printTrainSetStatistics(uData,type);
				//ModelsUtilities.checkUserData(uData) ;
			}
			
			//ExperimentsContentM.runCharGraphs(type, userDataMap, usersMap);
			//ExperimentsContentM.runTokenGraphs(type, userDataMap, usersMap);
		//	ExperimentsContentM.runTokenBags(type, userDataMap, usersMap);
		}
//		

	}

	
}

package twitter.recommendations.experiments;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import twitter.ParametersGenerator;
import twitter.DataStructures.ParsedUserActivity;
import twitter.DataStructures.UserActivity;
import twitter.DataStructures.UserData;
import twitter.Utilities.Twitter.GetUserActivities;
import twitter.Utilities.Twitter.StoreTestData;
import twitter.modelUtilities.MergeStrategy;
import twitter.modelUtilities.ModelInfoSource;
import twitter.modelUtilities.ModelsUtilities;
import twitter.modelUtilities.TopicModelName;
import twitter.modelUtilities.TopicModelTrainStrategy;
import twitter.recommendations.content.RoccioParams;
import twitter.recommendations.topicModels.TopicModelParams;
import twitter.recommendations.topicModels.BTM.BTMParameters;
import twitter.recommendations.topicModels.HDP.HDPParameters;
import twitter.recommendations.topicModels.LLDA.LLDAParameters;

public class ExperimentsAllTopics {

	public static void main(String[] args) throws Exception {
		//String initUsersPath = "/media/efi/APOTHETHS/master/thesis/dataset/usersFinal/";
	//String initUsersPath = "/home/efikarra/Twitter/usersFinal/";
	//	String initUsersPath = "/home/efi/master-thesis/experimentUsers/usersFinal/";
	String initUsersPath = "D://master//thesis//dataset//usersFinal//";

		GetUserActivities getActivities1 = new GetUserActivities(initUsersPath);
		List<UserActivity> pacts1 = new ArrayList<UserActivity>();
		List<UserActivity> bottom1 = getActivities1.getUserActivities("bottom");
		List<UserActivity> top1 = getActivities1.getUserActivities("top");
		List<UserActivity> avg1 = getActivities1.getUserActivities("avg");
		pacts1.addAll(bottom1);
		pacts1.addAll(top1);
		pacts1.addAll(avg1);
		Map<String, String> usersStrictMap = new HashMap<String, String>();
		
		for (UserActivity activity : pacts1) {
			int inc = ModelsUtilities.getIncomingFreq(activity);
			int out = ModelsUtilities.getOutgoingFreq(activity);
			double ratio = (double) ((double) out / (double) inc);
			if (ratio >= 2) {
				usersStrictMap.put( activity.getUserName(),"top_new");
			}
			if (ratio <=0.5) {
				usersStrictMap.put( activity.getUserName(),"bottom_new");
			}
			if (ratio >= 0.8&&ratio<=1.2) {
				usersStrictMap.put( activity.getUserName(),"avg_new");
			}
			
		}
		//String usersPath = "/media/efi/APOTHETHS/master/thesis/dataset/filteredUsersFinal100only/";
		//String usersPath = "/home/efikarra/Twitter/filteredUsersFinal100only/";
		//String usersPath = "/home/efi/master-thesis/experimentUsers/filteredUsersFinal100only/";
		String usersPath = "D://master//thesis//dataset//filteredUsersFinal100only/";

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
		types.add(ModelInfoSource.URT);
	types.add(ModelInfoSource.UT);
//		
		// types.add(ModelInfoSource.FR);
//		
		// types.add(ModelInfoSource.FE);
		
		// types.add(ModelInfoSource.RE);
//		
//		types.add(ModelInfoSource.UT_URT);
//		 types.add(ModelInfoSource.URT_FR);
//		 types.add(ModelInfoSource.URT_FE);
	 //types.add(ModelInfoSource.URT_RE);
//		 types.add(ModelInfoSource.FE_FR);
//		 types.add(ModelInfoSource.U_FR);
//		 types.add(ModelInfoSource.U_FE);
		// types.add(ModelInfoSource.U_RE);

		List<TopicModelParams> paramsLDAMSG = new ArrayList<TopicModelParams>();
		ParametersGenerator.ldaParams(paramsLDAMSG);
		//List<TopicModelParams> paramsLDAUP = new ArrayList<TopicModelParams>();
		//List<TopicModelParams> paramsLDAHASH = new ArrayList<TopicModelParams>();
		Map<String, List<TopicModelParams>> strategyParamsLDA = new HashMap<String, List<TopicModelParams>>();
		strategyParamsLDA.put(TopicModelTrainStrategy.getName(TopicModelTrainStrategy.MSG_STRATEGY), paramsLDAMSG);
		strategyParamsLDA.put(TopicModelTrainStrategy.getName(TopicModelTrainStrategy.UP_STRATEGY), paramsLDAMSG);	
		strategyParamsLDA.put(TopicModelTrainStrategy.getName(TopicModelTrainStrategy.HASH_STRATEGY), paramsLDAMSG);

		List<TopicModelParams> paramsLLDAMSG = new ArrayList<TopicModelParams>();
		paramsLLDAMSG.add(new LLDAParameters(20, 0.01, 1000,30));
		paramsLLDAMSG.add(new LLDAParameters(50, 0.01,1000,30));
		paramsLLDAMSG.add(new LLDAParameters(100, 0.01, 1000,30));
		paramsLLDAMSG.add(new LLDAParameters(150, 0.01, 1000,30));
		paramsLLDAMSG.add(new LLDAParameters(20, 0.01, 2000,30));
		paramsLLDAMSG.add(new LLDAParameters(50, 0.01,  2000,30));
		paramsLLDAMSG.add(new LLDAParameters(100, 0.01,2000,30));
		paramsLLDAMSG.add(new LLDAParameters(150, 0.01, 2000,30));
	//	List<TopicModelParams> paramsLLDAUP = new ArrayList<TopicModelParams>();
		//List<TopicModelParams> paramsLLDAHASH = new ArrayList<TopicModelParams>();
				
		Map<String, List<TopicModelParams>> strategyParamsLLDA = new HashMap<String, List<TopicModelParams>>();
		strategyParamsLLDA.put(TopicModelTrainStrategy.getName(TopicModelTrainStrategy.MSG_STRATEGY), paramsLLDAMSG);
		strategyParamsLLDA.put(TopicModelTrainStrategy.getName(TopicModelTrainStrategy.UP_STRATEGY), paramsLLDAMSG);
		strategyParamsLLDA.put(TopicModelTrainStrategy.getName(TopicModelTrainStrategy.HASH_STRATEGY), paramsLLDAMSG);
		
		List<BTMParameters> paramsBTMMSG = new ArrayList<BTMParameters>();
		paramsBTMMSG.add(new BTMParameters(50,1000, 0.01, 25000));
		paramsBTMMSG.add(new BTMParameters(100, 1000, 0.01, 25000));
		paramsBTMMSG.add(new BTMParameters(150,1000, 0.01, 25000));
		paramsBTMMSG.add(new BTMParameters(200, 1000, 0.01, 25000));
//		paramsBTMMSG.add(new BTMParameters(50, 2000, 0.01, 25000));
//		paramsBTMMSG.add(new BTMParameters(100, 2000, 0.01, 25000));
//		paramsBTMMSG.add(new BTMParameters(150, 2000, 0.01, 25000));
//		paramsBTMMSG.add(new BTMParameters(200, 2000, 0.01, 25000));
		List<BTMParameters> paramsBTMUP = new ArrayList<BTMParameters>();
		paramsBTMUP.add(new BTMParameters(50, 1000, 0.01, 30));
		paramsBTMUP.add(new BTMParameters(100, 1000, 0.01, 30));
		paramsBTMUP.add(new BTMParameters(150, 1000, 0.01, 30));
		paramsBTMUP.add(new BTMParameters(200, 1000, 0.01, 30));
//		paramsBTMUP.add(new BTMParameters(50, 1000, 0.01, 50));
//		paramsBTMUP.add(new BTMParameters(100, 1000, 0.01, 50));
//		paramsBTMUP.add(new BTMParameters(150, 1000, 0.01, 50));
//		paramsBTMUP.add(new BTMParameters(200, 1000, 0.01, 50));
//		paramsBTMUP.add(new BTMParameters(100, 2000, 0.01, 30));
//		paramsBTMUP.add(new BTMParameters(150, 2000, 0.01, 30));
//		paramsBTMUP.add(new BTMParameters(200, 2000, 0.01, 30));
		//List<BTMParameters> paramsBTMHASH = new ArrayList<BTMParameters>();
		
		Map<String, List<BTMParameters>> strategyParamsbtm = new HashMap<String, List<BTMParameters>>();
		//strategyParamsbtm.put(TopicModelTrainStrategy.getName(TopicModelTrainStrategy.MSG_STRATEGY), paramsBTMMSG);
		//strategyParamsbtm.put(TopicModelTrainStrategy.getName(TopicModelTrainStrategy.UP_STRATEGY), paramsBTMUP);
		//strategyParamsbtm.put(TopicModelTrainStrategy.getName(TopicModelTrainStrategy.HASH_STRATEGY), paramsBTMUP);
		
		List<TopicModelParams> paramsHLDAMSG = new ArrayList<TopicModelParams>();
		ParametersGenerator.hldaParams(paramsHLDAMSG);
		//List<TopicModelParams> paramsHLDAUP = new ArrayList<TopicModelParams>();
		//paramsHLDAUP.add(new HLDAParameters(10.0, 1.0, 0.1, 5, 3));
		Map<String, List<TopicModelParams>> strategyHLDAParams = new HashMap<String, List<TopicModelParams>>();
		//strategyHLDAParams.put(TopicModelTrainStrategy.getName(TopicModelTrainStrategy.MSG_STRATEGY), paramsHLDAMSG);
		//strategyHLDAParams.put(TopicModelTrainStrategy.getName(TopicModelTrainStrategy.UP_STRATEGY), paramsHLDAMSG);
		strategyHLDAParams.put(TopicModelTrainStrategy.getName(TopicModelTrainStrategy.HASH_STRATEGY), paramsHLDAMSG);
		
		List<TopicModelParams> paramsHDPMSG = new ArrayList<TopicModelParams>();
		ParametersGenerator.hdpParams(paramsHDPMSG);
		List<TopicModelParams> paramsHDPUP = new ArrayList<TopicModelParams>();
		paramsHDPUP.add(new HDPParameters(1.0, 0.1, 1.0, 150,1000));
		paramsHDPUP.add(new HDPParameters(1.0, 0.5, 1.0, 150,1000));
		paramsHDPUP.add(new HDPParameters(1.0, 0.1, 1.0, 200,1000));
		paramsHDPUP.add(new HDPParameters(1.0, 0.5, 1.0, 200,1000));
//		List<TopicModelParams> paramsHDPHASH = new ArrayList<TopicModelParams>();
//		paramsHDPHASH.add(new HDPParameters(1.0, 0.1, 1.0, 10,3));
		
		Map<String, List<TopicModelParams>> strategyParamsHDP = new HashMap<String, List<TopicModelParams>>();
		//strategyParamsHDP.put(TopicModelTrainStrategy.getName(TopicModelTrainStrategy.MSG_STRATEGY), paramsHDPMSG);
		strategyParamsHDP.put(TopicModelTrainStrategy.getName(TopicModelTrainStrategy.UP_STRATEGY), paramsHDPUP);
		strategyParamsHDP.put(TopicModelTrainStrategy.getName(TopicModelTrainStrategy.HASH_STRATEGY), paramsHDPMSG);
		
		List<TopicModelParams> paramsPLSAMSG = new ArrayList<TopicModelParams>();
		ParametersGenerator.plsaParams(paramsPLSAMSG);
//		List<TopicModelParams> paramsPLSAUP = new ArrayList<TopicModelParams>();
//		paramsPLSAUP.add(new PLSAParameters(10, 1e-10, new Random(), 250));
//		List<TopicModelParams> paramsPLSAHASH = new ArrayList<TopicModelParams>();
//		paramsPLSAHASH.add(new PLSAParameters(10, 1e-10, new Random(), 250));
//		
		Map<String, List<TopicModelParams>> strategyParamsPLSA = new HashMap<String, List<TopicModelParams>>();
		strategyParamsPLSA.put(TopicModelTrainStrategy.getName(TopicModelTrainStrategy.MSG_STRATEGY), paramsPLSAMSG);
		strategyParamsPLSA.put(TopicModelTrainStrategy.getName(TopicModelTrainStrategy.UP_STRATEGY), paramsPLSAMSG);
		strategyParamsPLSA.put(TopicModelTrainStrategy.getName(TopicModelTrainStrategy.HASH_STRATEGY), paramsPLSAMSG);

		List<TopicModelTrainStrategy> topicStrategies = new ArrayList<TopicModelTrainStrategy>();
		topicStrategies.add(TopicModelTrainStrategy.MSG_STRATEGY);
		topicStrategies.add(TopicModelTrainStrategy.UP_STRATEGY);
		topicStrategies.add(TopicModelTrainStrategy.HASH_STRATEGY);

		Map<TopicModelName, Map<String, List<TopicModelParams>>> allParamsMap = new HashMap<TopicModelName, Map<String, List<TopicModelParams>>>();
		allParamsMap.put(TopicModelName.LDA, strategyParamsLDA);
		//allParamsMap.put(TopicModelName.PLSA, strategyParamsPLSA);
		//allParamsMap.put(TopicModelName.HLDA, strategyHLDAParams);
		//allParamsMap.put(TopicModelName.HDP, strategyParamsHDP);

		//String BTMModelDir = "/home/efi/master-thesis/models/biterm/";
		//String BTMModelDir = "/home/efikarra/Twitter/models-backup/biterm/";

		List<MergeStrategy> mergeStrategies = new ArrayList<MergeStrategy>();
		mergeStrategies.add(MergeStrategy.CENTROID_STRATEGY);
		Map<String, UserData> userDataMap = new HashMap<String, UserData>();
		Map<String, Long> refDateMap = new HashMap<String, Long>();
		RoccioParams rparams=new RoccioParams(0.8, -0.2);
//		for (int i = 0; i < pacts.size(); i++) {
//		UserData userData = new UserData(pacts.get(i).getUserName());
//		TestPDataCreator creator = new TestPDataCreator(pacts.get(i), userData);
//		creator.getTestTweets(0.8);
//		userDataMap.put(pacts.get(i).getUserName(), userData);
//		//ModelsUtilities.printTestSetStatistics(userData);
//		refDateMap.put(pacts.get(i).getUserName(), creator.getRefTime());
//	}
		//userDataMap=StoreTestData.loadTestUserData("/home/efikarra/Twitter/all_userdata");
		userDataMap=StoreTestData.loadTestUserData("D://master//thesis//dataset//all_userdata");
		for(UserData uData : userDataMap.values()){
			Collections.sort(uData.getTestTweets());
			refDateMap.put(uData.getUserName(),uData.getTestTweets().get(0).getThinTweet().getTimeStamp());
		}
//		
		//System.out.println("test data stored successfully");
		Set<String> negposTypes=new HashSet<String>();
		negposTypes.add(ModelInfoSource.name(ModelInfoSource.FE));
		negposTypes.add(ModelInfoSource.name(ModelInfoSource.RE));
		negposTypes.add(ModelInfoSource.name(ModelInfoSource.FE_FR));
		negposTypes.add(ModelInfoSource.name(ModelInfoSource.URT_FE));
		negposTypes.add(ModelInfoSource.name(ModelInfoSource.URT_RE));
		negposTypes.add(ModelInfoSource.name(ModelInfoSource.U_FE));
		negposTypes.add(ModelInfoSource.name(ModelInfoSource.U_RE));

		//ExperimentsBaseline.runRandom(userDataMap, usersMap, usersStrictMap);
		//ExperimentsBaseline.runChronological(userDataMap, usersMap, usersStrictMap);
		for (ModelInfoSource type : types) {

			for (int i = 0; i < pacts.size(); i++)
				ModelInfoSource.computeUserDataP(type, pacts.get(i), userDataMap.get(pacts.get(i).getUserName()), 0.8,
						refDateMap.get(pacts.get(i).getUserName()));
			// print dataset statistics
			List<UserData> skippednames=new ArrayList<UserData>();
			for (UserData uData : userDataMap.values()) {
				//ModelsUtilities.printTrainSetStatistics(uData, type);
				if(uData.getTrainTweets().size()<=5){
					skippednames.add(uData);
				}
			}
			for(UserData skudata:skippednames){
				userDataMap.remove(skudata.getUserName());
			}
		//ExperimentsLLDA.run(ModelInfoSource.name(type), userDataMap,
			//// strategyParamsLLDA, mergeStrategies, topicStrategies,usersMap,usersStrictMap,negposTypes,rparams);
//			ExperimentsUnsupTopic.run(ModelInfoSource.name(type), userDataMap, allParamsMap, mergeStrategies,
				//	topicStrategies, usersMap,usersStrictMap,negposTypes,rparams);
			 //ExperimentsBTM.run(ModelInfoSource.name(type), userDataMap,strategyParamsbtm, mergeStrategies, topicStrategies,
			 //BTMModelDir,usersMap,usersStrictMap,negposTypes,rparams);
	//		ExperimentsContentM.runCharGraphs(type, userDataMap, usersMap, usersStrictMap);
			//List<RoccioParams> roccioParams = new ArrayList<RoccioParams>();
			//roccioParams.add(new RoccioParams(0.8, -0.2));
			//if(negposTypes.contains(ModelInfoSource.name(type))){
			//	ExperimentsContentM.runCharBagsRoccio(type, userDataMap, usersMap, roccioParams, usersStrictMap);			}
			ExperimentsContentM.runCharBags(type, userDataMap, usersMap, usersStrictMap);
//			ExperimentsContentM.runTokenBags(type, userDataMap, usersMap, usersStrictMap);

			for(UserData skudata:skippednames){
				userDataMap.put(skudata.getUserName(),skudata);
			}
		}

	}
}

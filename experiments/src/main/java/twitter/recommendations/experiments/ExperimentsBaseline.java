package twitter.recommendations.experiments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import twitter.AbstractModelTrainer;
import twitter.DataStructures.Twitter.UserData;
import twitter.recommendations.baseline.ChronBaselineTrainer;
import twitter.recommendations.baseline.RandomBaselineTrainer;
import twitter.recommendations.evaluation.EvaluatorUtils;
import twitter.recommendations.evaluation.RankingMetrics;

public class ExperimentsBaseline {
	public static double runRandom(Map<String, UserData> userDataMap,
			Map<String, String> userCatMap, Map<String, String> userStrictMap)
			throws Exception {
		String model_name = "base_ran";
		double ranall = 0.0;
		double minall=25000;
		double maxall=0.0;
		Map<String, Double> userTypesMAP1 = new HashMap<String, Double>();
		Map<String, Double> userStrictTypesMAP1 = new HashMap<String, Double>();
		Map<String, Double> userTypesMAP1Max = new HashMap<String, Double>();
		Map<String, Double> userStrictTypesMAP1Max = new HashMap<String, Double>();
		Map<String, Double> userTypesMAP1Min = new HashMap<String, Double>();
		Map<String, Double> userStrictTypesMAP1Min = new HashMap<String, Double>();
		for (int i = 0; i < 1000; i++) {
			List<RankingMetrics> metrics = new ArrayList<RankingMetrics>();
			Map<String, List<RankingMetrics>> metricsMap = new HashMap<String, List<RankingMetrics>>();
			Map<String, List<RankingMetrics>> metricsStrictMap = new HashMap<String, List<RankingMetrics>>();
			
			
			for (Map.Entry<String, UserData> uentry : userDataMap.entrySet()) {

				UserData act = uentry.getValue();
				// AbstractModelTrainer baseline=new ChronBaselineTrainer(act);
				AbstractModelTrainer baseline = new RandomBaselineTrainer(act);
				baseline.modelTestTweets();
				// baseline.evaluateUserModel();
				populateUserCatMap(userCatMap.get(act.getUserName()),
						baseline.getMetrics(), metricsMap);
				metrics.add(baseline.getMetrics());
				String ct = userStrictMap.get(act.getUserName());
				if (ct != null) {
					populateUserCatMap(userStrictMap.get(act.getUserName()),
							baseline.getMetrics(), metricsStrictMap);

				}
			}
			Map<String, Double> userTypesMAP = new HashMap<String, Double>();
			Map<String, Double> userStrictTypesMAP = new HashMap<String, Double>();
			userTypesMAP = EvaluatorUtils.evaluateByUserCategory(metricsMap,
					model_name);
			userStrictTypesMAP = EvaluatorUtils.evaluateByUserCategory(
					metricsStrictMap, model_name);
			double mapall=EvaluatorUtils.evaluateAll(metrics, model_name);
			if(mapall>maxall)
				maxall=mapall;
			if(mapall<minall)
				minall=mapall;
			ranall += mapall;

			for (Map.Entry<String, Double> entry : userTypesMAP.entrySet()) {
				Double map = userTypesMAP1.get(entry.getKey());
				if (map == null)
					map = new Double(0);
				userTypesMAP1.put(entry.getKey(),
						map += userTypesMAP.get(entry.getKey()));
				
				Double minmap = userTypesMAP1Min.get(entry.getKey());
				if (minmap == null)
					minmap = 25000.0;
				if(entry.getValue()<minmap)
					userTypesMAP1Min.put(entry.getKey(),entry.getValue());
				
				Double maxmap = userTypesMAP1Max.get(entry.getKey());
				if (maxmap == null)
					maxmap = 0.0;
				if(entry.getValue()>maxmap)
					userTypesMAP1Max.put(entry.getKey(),entry.getValue());
			}
			for (Map.Entry<String, Double> entry : userStrictTypesMAP.entrySet()) {
				Double map = userStrictTypesMAP1.get(entry.getKey());
				if (map == null)
					map = new Double(0);
				userStrictTypesMAP1.put(entry.getKey(),
						map += userStrictTypesMAP.get(entry.getKey()));
				
				Double minmap = userStrictTypesMAP1Min.get(entry.getKey());
				if (minmap == null)
					minmap = 25000.0;
				if(entry.getValue()<minmap)
					userStrictTypesMAP1Min.put(entry.getKey(),entry.getValue());
				
				Double maxmap = userStrictTypesMAP1Max.get(entry.getKey());
				if (maxmap == null)
					maxmap = 0.0;
				if(entry.getValue()>maxmap)
					userStrictTypesMAP1Max.put(entry.getKey(),entry.getValue());
			}
		}
		System.out.println("all: " + ranall/(double)(1000));
		System.out.println("all_min: " + minall);
		System.out.println("all_max: " + maxall);

		for (Map.Entry<String, Double> entry : userStrictTypesMAP1.entrySet()) {
			System.out.println(entry.getKey() + ": " + entry.getValue()/(double)(1000));
			
		}
		for (Map.Entry<String, Double> entry : userStrictTypesMAP1Min.entrySet()) {
			System.out.println("min_"+entry.getKey() + ": " + entry.getValue());
			
		}
		for (Map.Entry<String, Double> entry : userStrictTypesMAP1Max.entrySet()) {
			System.out.println("max_"+entry.getKey() + ": " + entry.getValue());
			
		}
		for (Map.Entry<String, Double> entry : userTypesMAP1.entrySet()) {
			System.out.println(entry.getKey() + ": " + entry.getValue()/(double)(1000));
			
		}
		for (Map.Entry<String, Double> entry : userTypesMAP1Min.entrySet()) {
			System.out.println("min_"+entry.getKey() + ": " + entry.getValue());
			
		}
		for (Map.Entry<String, Double> entry : userTypesMAP1Max.entrySet()) {
			System.out.println("max_"+entry.getKey() + ": " + entry.getValue());
			
		}
		return 0.0;

	}

	public static double runChronological(Map<String, UserData> userDataMap,
			Map<String, String> userCatMap, Map<String, String> userStrictMap)
			throws Exception {
		List<RankingMetrics> metrics = new ArrayList<RankingMetrics>();
		Map<String, List<RankingMetrics>> metricsMap = new HashMap<String, List<RankingMetrics>>();
		Map<String, List<RankingMetrics>> metricsStrictMap = new HashMap<String, List<RankingMetrics>>();

		String model_name = "base_chron";
		for (Map.Entry<String, UserData> uentry : userDataMap.entrySet()) {

			UserData act = uentry.getValue();
			// AbstractModelTrainer baseline=new ChronBaselineTrainer(act);
			AbstractModelTrainer baseline = new ChronBaselineTrainer(act);
			baseline.modelTestTweets();
			baseline.evaluateUserModel();
			populateUserCatMap(userCatMap.get(act.getUserName()),
					baseline.getMetrics(), metricsMap);
			String ct = userStrictMap.get(act.getUserName());
			if (ct != null)
				populateUserCatMap(userStrictMap.get(act.getUserName()),
						baseline.getMetrics(), metricsStrictMap);
			metrics.add(baseline.getMetrics());
		}
		EvaluatorUtils.evaluateByUserCategory(metricsMap, model_name);
		EvaluatorUtils.evaluateByUserCategory(metricsStrictMap, model_name);
		return EvaluatorUtils.evaluateAll(metrics, model_name);

	}

	private static void populateUserCatMap(String userCat,
			RankingMetrics metrics, Map<String, List<RankingMetrics>> metricsMap) {
		List<RankingMetrics> metricsCat = metricsMap.get(userCat);
		if (metricsCat == null) {
			metricsCat = new ArrayList<RankingMetrics>();
		}
		metricsCat.add(metrics);
		metricsMap.put(userCat, metricsCat);
	}

	public static void main(String[] args) throws Exception {
		// //String usersPath="D:\\master\\thesis\\dataset\\serializedUsers\\";
		// String usersPath =
		// "/home/efi/master-thesis/resultUsers/serializedUsers/";
		// //String usersPath =
		// "/media/efi/APOTHETHS/master/thesis/dataset/serializedUsers/";
		// GetUserActivities getActivities = new GetUserActivities(usersPath);
		// List<UserActivity> acts=new ArrayList<UserActivity>();
		// acts.addAll(getActivities.getUserActivities("bottom"));
		// acts.addAll(getActivities.getUserActivities("top"));
		// acts.addAll(getActivities.getUserActivities("avg"));
		//
		// int skipped=0;
		// List<RankingMetrics> metrics = new ArrayList<RankingMetrics>();
		// for (int i = 0; i < acts.size(); i++) {
		// TrainDataCreator dataCreator = new TrainDataCreator(acts.get(i));
		//
		// System.out.println("user : " + acts.get(i).getUserName());
		// if(ModelsUtilities.userStatistics(acts.get(i))<20){
		// System.out.println("user : " + acts.get(i).getUserName()+" skipped");
		// skipped++;
		// continue;
		// }
		// dataCreator.postedTweetsRetweets(0.8);
		// AbstractModelTrainer baseline=new
		// BaselineTrainer(dataCreator.getUserData());
		// baseline.modelTestTweets();
		// baseline.evaluateUserModel();
		// metrics.add(baseline.getMetrics());
		// }
		// UserModelEvaluator evaluator=new UserModelEvaluator(metrics);
		// evaluator.evaluateUserModel();
		// System.out.println(skipped);
	}
}

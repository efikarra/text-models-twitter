package twitter.recommendations.experiments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import twitter.DataStructures.Twitter.UserData;
import twitter.modelUtilities.MergeStrategy;
import twitter.modelUtilities.TopicModelTrainStrategy;
import twitter.recommendations.content.RoccioParams;
import twitter.recommendations.evaluation.EvaluatorUtils;
import twitter.recommendations.evaluation.RankingMetrics;
import twitter.recommendations.topicModels.BTM.BTMModelTrainer;
import twitter.recommendations.topicModels.BTM.BTMParameters;
import twitter.recommendations.topicModels.BTM.BTMUserModelTrainer;

public class ExperimentsBTM {

	public static void run(String typeName, Map<String, UserData> userDataMap,
			Map<String, List<BTMParameters>> stratParams, List<MergeStrategy> strategies,
			List<TopicModelTrainStrategy> topicStrategies, String model_dir, Map<String, String> userCatMap,
			Map<String, String> userStrictMap, Set<String> negposTypes,RoccioParams rparams) throws Exception {
		Map<String, Long> timesMap = new HashMap<String, Long>();
		Map<String, Long> testtimesMap = new HashMap<String, Long>();

		for (TopicModelTrainStrategy topicStrategy : topicStrategies) {
			for (BTMParameters param : stratParams.get(TopicModelTrainStrategy.getName(topicStrategy))) {
				String model_name = "btm_" + typeName + "_" + TopicModelTrainStrategy.getName(topicStrategy) + "_"
						+ param.getParamsName();
				BTMModelTrainer BTMtrainer = TopicModelTrainStrategy.getBTMModelTrainer(model_dir + "_" + model_name,
						topicStrategy, new ArrayList<UserData>(userDataMap.values()), param);
				long currtime=System.currentTimeMillis();
				BTMtrainer.train();
				timesMap.put(model_name, System.currentTimeMillis()-currtime);
				for (MergeStrategy strategy : strategies) {
					Map<String, List<RankingMetrics>> metricsMap = new HashMap<String, List<RankingMetrics>>();
					Map<String, List<RankingMetrics>> metricsStrictMap = new HashMap<String, List<RankingMetrics>>();
					List<RankingMetrics> metrics = new ArrayList<RankingMetrics>();
					model_name = "btm_" + typeName + "_" + TopicModelTrainStrategy.getName(topicStrategy) + "_"
							+ param.getParamsName() + "_" + MergeStrategy.getName(strategy);

					for (Map.Entry<String, UserData> uentry : userDataMap.entrySet()) {
						
						UserData act = uentry.getValue();
						BTMUserModelTrainer trainer = new BTMUserModelTrainer(BTMtrainer.getTrainData().get(act.getUserName()),
								BTMtrainer.getTestData().get(act.getUserName()), BTMtrainer.getTmodel_dir(),
								BTMtrainer.getBiterm_model(), "sum_b");
						trainer.trainUserModel(strategy,topicStrategy);
						long testtimeStart=System.currentTimeMillis();
						trainer.modelTestTweets();
						trainer.rankAllTweets();
						trainer.evaluateUserModel();
						Long timetestall=testtimesMap.get(model_name);
						if(timetestall==null)
							testtimesMap.put(model_name, System.currentTimeMillis()-testtimeStart);
						else
							testtimesMap.put(model_name,timetestall+= System.currentTimeMillis()-testtimeStart);
						
						if(!(Double.valueOf(trainer.getMetrics().getAvgPrecision()).isNaN())){
						metrics.add(trainer.getMetrics());
						populateUserCatMap(userCatMap.get(act.getUserName()), trainer.getMetrics(), metricsMap);
						String ct = userStrictMap.get(act.getUserName());
						if (ct != null)
							populateUserCatMap(userStrictMap.get(act.getUserName()), trainer.getMetrics(),
									metricsStrictMap);
						}
					}
					EvaluatorUtils.evaluateAll(metrics, model_name);
					EvaluatorUtils.evaluateByUserCategory(metricsMap, model_name);
					EvaluatorUtils.evaluateByUserCategory(metricsStrictMap, model_name);
					
					if (negposTypes.contains(typeName)) {
						model_name = "btm_" + typeName + "_" + TopicModelTrainStrategy.getName(topicStrategy) + "_"
								+ param.getParamsName() + "_" + MergeStrategy.getName(MergeStrategy.ROCCHIO_STRATEGY);
						metricsMap = new HashMap<String, List<RankingMetrics>>();
						metricsStrictMap = new HashMap<String, List<RankingMetrics>>();
						metrics = new ArrayList<RankingMetrics>();
						for (Map.Entry<String, UserData> uentry : userDataMap.entrySet()) {
							UserData act = uentry.getValue();

							BTMUserModelTrainer trainer = new BTMUserModelTrainer(BTMtrainer.getTrainData().get(act.getUserName()),
									BTMtrainer.getTestData().get(act.getUserName()), BTMtrainer.getTmodel_dir(),
									BTMtrainer.getBiterm_model(), "sum_b");
							trainer.trainRocchioUserModel(rparams,topicStrategy);
							long testtimeStart=System.currentTimeMillis();
							trainer.modelTestTweets();
							trainer.rankAllTweets();
							trainer.evaluateUserModel();
							Long timetestall=testtimesMap.get(model_name);
							if(timetestall==null)
								testtimesMap.put(model_name, System.currentTimeMillis()-testtimeStart);
							else
								testtimesMap.put(model_name,timetestall+= System.currentTimeMillis()-testtimeStart);
							
							if(!(Double.valueOf(trainer.getMetrics().getAvgPrecision()).isNaN())){
							metrics.add(trainer.getMetrics());
							populateUserCatMap(userCatMap.get(act.getUserName()), trainer.getMetrics(), metricsMap);
							String ct = userStrictMap.get(act.getUserName());
							if (ct != null)
								populateUserCatMap(userStrictMap.get(act.getUserName()), trainer.getMetrics(),
										metricsStrictMap);
							}
						}
						EvaluatorUtils.evaluateAll(metrics, model_name);
						EvaluatorUtils.evaluateByUserCategory(metricsMap, model_name);
						EvaluatorUtils.evaluateByUserCategory(metricsStrictMap, model_name);

					}
				}

			}
		}
		for (Map.Entry<String, Long> entry2: timesMap.entrySet()) {
			System.out.println(typeName+"_"+entry2.getKey()+"_" +entry2.getValue());
		}
		for (Map.Entry<String, Long> entry: testtimesMap.entrySet()) {
			System.out.println("test_"+typeName+"_"+entry.getKey()+"_" +entry.getValue());
		}
	}

	private static void populateUserCatMap(String userCat, RankingMetrics metrics,
			Map<String, List<RankingMetrics>> metricsMap) {
		List<RankingMetrics> metricsCat = metricsMap.get(userCat);
		if (metricsCat == null) {
			metricsCat = new ArrayList<RankingMetrics>();
		}
		metricsCat.add(metrics);
		metricsMap.put(userCat, metricsCat);
	}

	public static void main(String[] args) throws Exception {
		// String usersPath =
		// "/media/efi/APOTHETHS/master/thesis/dataset/serializedUsers/";
		// //String usersPath="D:\\master\\thesis\\dataset\\serializedUsers\\";
		// // String
		// //
		// usersPath="/home/efikarra/Twitter/experiments/processing/serializedUsers/";
		// GetUserActivities getActivities = new GetUserActivities(usersPath);
		// List<UserActivity> acts=new ArrayList<UserActivity>();
		// acts.addAll(getActivities.getUserActivities("bottom"));
		// acts.addAll(getActivities.getUserActivities("top"));
		// acts.addAll(getActivities.getUserActivities("avg"));
		//
		//
		// int skipped=0;
		// List<RankingMetrics> metrics = new ArrayList<RankingMetrics>();
		// for (int i = 0; i <acts.size(); i++) {
		// UserDataCreator dataCreator = new UserDataCreator(acts.get(i));
		//
		// if(ModelsUtilities.userStatistics(acts.get(i))<20){
		// skipped++;
		// continue;
		// }
		//
		// dataCreator.postedTweetsRetweets(0.8);
		// int topics=100;
		// double a=(double)50/(double)topics;
		// double b=0.01;
		// //AbstractTopicModel topicModel = new LDATopicModel(topics,a, b);
		// int K = 50;
		// int n_iter = 1000;
		// int save_step = 1001;
		// double alpha = 50/K ;
		// double beta = 0.01;
		//
		// //get vocabulary size from map file
		// BufferedReader br = new BufferedReader(new FileReader(new
		// File("/media/efi/APOTHETHS/master/thesis/biterm/maps/tweets-retweets-map-"+acts.get(i).getUserName()+".dic")));
		// int V=Integer.parseInt(br.readLine());
		//
		// Model biterm_model = new Model(K, V, alpha, beta, n_iter, save_step);
		// BTMUserModelTrainer trainer = new BTMUserModelTrainer(
		// "/media/efi/APOTHETHS/master/thesis/biterm/train/tweets-retweets-"+acts.get(i).getUserName()+".dat","/media/efi/APOTHETHS/master/thesis/biterm/test/test-tweets-"+acts.get(i).getUserName(),"/media/efi/APOTHETHS/master/thesis/biterm/models/"+acts.get(i).getUserName(),biterm_model,"sum_b");
		// trainer.trainUserModel(MergeStrategy.CENTROID_STRATEGY);
		// trainer.modelTestTweets();
		// trainer.rankAllTweets();
		// trainer.evaluateUserModel();
		// metrics.add(trainer.getMetrics());
		// }
		// UserModelEvaluator evaluator=new UserModelEvaluator(metrics);
		// evaluator.evaluateUserModel();
		// System.out.println("skipped: "+skipped);
	}
}
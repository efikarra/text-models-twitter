package twitter.recommendations.experiments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import twitter.DataStructures.UserData;
import twitter.modelUtilities.MergeStrategy;
import twitter.modelUtilities.TopicModelTrainStrategy;
import twitter.recommendations.content.RoccioParams;
import twitter.recommendations.evaluation.EvaluatorUtils;
import twitter.recommendations.evaluation.RankingMetrics;
import twitter.recommendations.topicModels.TopicModelParams;
import twitter.recommendations.topicModels.UserTopicModelTrainer;
import twitter.recommendations.topicModels.LLDA.LLDAModelTrainer;
import twitter.recommendations.topicModels.LLDA.LLDAParameters;
import twitter.recommendations.topicModels.LLDA.LLDATopicModel;
import twitter.recommendations.topicModels.LLDA.LLDAUserModelTrainer;

public class ExperimentsLLDA {

	public static void run(String typeName, Map<String, UserData> userDataMap,  Map<String,List<TopicModelParams>> stratParams,
			List<MergeStrategy> strategies, List<TopicModelTrainStrategy> topicStrategies,
			Map<String, String> userCatMap,Map<String, String> userStrictMap,Set<String> negposTypes,RoccioParams rparams) throws Exception {
		Map<String, Long> timesMap = new HashMap<String, Long>();
		Map<String, Long> testtimesMap = new HashMap<String, Long>();

		for (TopicModelTrainStrategy topicStrategy : topicStrategies) {

			for (TopicModelParams param : stratParams.get(TopicModelTrainStrategy.getName(topicStrategy))) {
				String  model_name="llda_"+typeName+"_"+TopicModelTrainStrategy.getName(topicStrategy)+"_"+param.getParamsName();

				LLDATopicModel topicModel = new LLDATopicModel((LLDAParameters)param,model_name);
				LLDAModelTrainer LLDAtrainer = TopicModelTrainStrategy.getLLDAModelTrainer(topicStrategy,
						new ArrayList<UserData>(userDataMap.values()), topicModel);
				long currtime=System.currentTimeMillis();
				LLDAtrainer.train();
				timesMap.put(model_name, System.currentTimeMillis()-currtime);
				
					for (MergeStrategy strategy : strategies) {
						Map<String, List<RankingMetrics>> metricsMap = new HashMap<String, List<RankingMetrics>>();
						Map<String, List<RankingMetrics>> metricsStrictMap = new HashMap<String, List<RankingMetrics>>();
						List<RankingMetrics> metrics = new ArrayList<RankingMetrics>();
						for (Map.Entry<String, UserData> uentry:userDataMap.entrySet()) {
							UserData act = uentry.getValue();
							if(act.getTrainTweets().isEmpty())
								continue;
							UserTopicModelTrainer trainer = new LLDAUserModelTrainer(
									act, LLDAtrainer.getTopicModel(),
									LLDAtrainer.getLabels());
							((UserTopicModelTrainer) trainer).trainUserModel(strategy,topicStrategy);
							
							long testtimeStart=System.currentTimeMillis();
							trainer.modelTestTweets();
						
							((UserTopicModelTrainer) trainer).rankAllTweets();
							trainer.evaluateUserModel();
							Long timetestall=testtimesMap.get(model_name);
							
							if(timetestall==null)
								testtimesMap.put(model_name, System.currentTimeMillis()-testtimeStart);
							else
								testtimesMap.put(model_name,timetestall+= System.currentTimeMillis()-testtimeStart);
							
							metrics.add(trainer.getMetrics());
							populateUserCatMap(userCatMap.get(act.getUserName()), trainer.getMetrics(), metricsMap);
							String ct=userStrictMap.get(act.getUserName());
							if(ct!=null)
								populateUserCatMap(userStrictMap.get(act.getUserName()), trainer.getMetrics(), metricsStrictMap);
						}
						EvaluatorUtils.evaluateAll(metrics,model_name);
						EvaluatorUtils.evaluateByUserCategory(metricsMap, model_name);
						EvaluatorUtils.evaluateByUserCategory(metricsStrictMap, model_name);
						if(negposTypes.contains(typeName)){
							model_name = "llda_" + typeName + "_" + TopicModelTrainStrategy.getName(topicStrategy) + "_"
									+ param.getParamsName()+ "_" +MergeStrategy.getName(MergeStrategy.ROCCHIO_STRATEGY) ;
							metricsMap = new HashMap<String, List<RankingMetrics>>();
							metricsStrictMap = new HashMap<String, List<RankingMetrics>>();
							metrics = new ArrayList<RankingMetrics>();
							for (Map.Entry<String, UserData> uentry : userDataMap.entrySet()) {
								UserData act = uentry.getValue();
								if(act.getTrainTweets().isEmpty())
									continue;
								UserTopicModelTrainer trainer = new LLDAUserModelTrainer(
										act, LLDAtrainer.getTopicModel(),
										LLDAtrainer.getLabels());
								((LLDAUserModelTrainer)trainer).trainRocchioUserModel(rparams,topicStrategy);
								long testtimeStart=System.currentTimeMillis();
								trainer.modelTestTweets();
								((UserTopicModelTrainer) trainer).rankAllTweets();
								trainer.evaluateUserModel();
								Long timetestall=testtimesMap.get(model_name);
								if(timetestall==null)
									testtimesMap.put(model_name, System.currentTimeMillis()-testtimeStart);
								else
									testtimesMap.put(model_name,timetestall+= System.currentTimeMillis()-testtimeStart);
								
								
								metrics.add(trainer.getMetrics());
								populateUserCatMap(userCatMap.get(act.getUserName()), trainer.getMetrics(), metricsMap);
								String ct=userStrictMap.get(act.getUserName());
								if(ct!=null)
									populateUserCatMap(userStrictMap.get(act.getUserName()), trainer.getMetrics(), metricsStrictMap);
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
}

package twitter.recommendations.experiments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import Models.AbstractModel;
import Utilities.RepresentationModel;
import Utilities.SimilarityName;
import twitter.DataStructures.Twitter.UserData;
import twitter.modelUtilities.ContentModelType;
import twitter.modelUtilities.ModelInfoSource;
import twitter.modelUtilities.RoccioModelType;
import twitter.modelUtilities.RoccioSimilarityType;
import twitter.recommendations.content.RoccioParams;
import twitter.recommendations.content.RoccioUserModelTrainer;
import twitter.recommendations.content.UserModelTrainer;
import twitter.recommendations.content.models.ContentAbstractModel;
import twitter.recommendations.content.models.ContentVectorModel;
import twitter.recommendations.evaluation.RankingMetrics;
import twitter.recommendations.evaluation.UserModelEvaluator;

public class ExperimentsContentM {
	
	public static void runCharBags(ModelInfoSource utype, Map<String, UserData> userDataMap,
			Map<String, String> userCatMap, Map<String, String> userStrictMap) {
		List<SimilarityName> sims = new ArrayList<SimilarityName>();
		sims.add(SimilarityName.JACCARD_SIMILARITY);
		sims.add(SimilarityName.COSINE_BOOLEAN_SIMILARITY);
		sims.add(SimilarityName.GENERALIZED_JACCARD_SIMILARITY);
		sims.add(SimilarityName.COSINE_TF_SIMILARITY);
		sims.add(SimilarityName.GENERALIZED_JACCARD_SIMILARITY_CENTROID);
		sims.add(SimilarityName.COSINE_TF_SIMILARITY_CENTROID);
		List<RepresentationModel> models = new ArrayList<RepresentationModel>();
		models.add(RepresentationModel.CHARACTER_BIGRAMS);
		models.add(RepresentationModel.CHARACTER_TRIGRAMS);
		models.add(RepresentationModel.CHARACTER_FOURGRAMS);
		runBags(utype, sims, models, userDataMap, userCatMap, userStrictMap);
	}

	public static void runTokenBags(ModelInfoSource utype, Map<String, UserData> userDataMap,
			Map<String, String> userCatMap, Map<String, String> userStrictMap) {
		List<SimilarityName> sims = new ArrayList<SimilarityName>();
		List<RepresentationModel> models = new ArrayList<RepresentationModel>();

		sims = new ArrayList<SimilarityName>();
		sims.add(SimilarityName.JACCARD_SIMILARITY);
		sims.add(SimilarityName.COSINE_BOOLEAN_SIMILARITY);
		sims.add(SimilarityName.GENERALIZED_JACCARD_SIMILARITY);
		sims.add(SimilarityName.COSINE_TF_SIMILARITY);
		sims.add(SimilarityName.COSINE_TFIDF_SIMILARITY);
		sims.add(SimilarityName.COSINE_TFIDF_SIMILARITY_CENTROID);
		sims.add(SimilarityName.GENERALIZED_JACCARD_SIMILARITY_CENTROID);
		sims.add(SimilarityName.COSINE_TF_SIMILARITY_CENTROID);

		models = new ArrayList<RepresentationModel>();
		models.add(RepresentationModel.IDF_TOKEN_UNIGRAMS);
		models.add(RepresentationModel.IDF_TOKEN_BIGRAMS);
		models.add(RepresentationModel.IDF_TOKEN_TRIGRAMS);
		runBags(utype, sims, models, userDataMap, userCatMap, userStrictMap);
	}

	public static void runTokenBagsRoccio(ModelInfoSource utype, Map<String, UserData> userDataMap,
			Map<String, String> userCatMap, List<RoccioParams> params, Map<String, String> userStrictMap) {
		List<RepresentationModel> models = new ArrayList<RepresentationModel>();
		Map<RoccioModelType, List<RoccioSimilarityType>> rtypes = new HashMap<RoccioModelType, List<RoccioSimilarityType>>();
		List<RoccioSimilarityType> tfList = new ArrayList<RoccioSimilarityType>();
		List<RoccioSimilarityType> tfidfList = new ArrayList<RoccioSimilarityType>();
		tfList.add(RoccioSimilarityType.ROCCIO_COSINE_TF);
		tfidfList.add(RoccioSimilarityType.ROCCIO_COSINE_TFIDF);
		rtypes.put(RoccioModelType.TF, tfList);
		rtypes.put(RoccioModelType.TF_IDF, tfidfList);
		models = new ArrayList<RepresentationModel>();
		models.add(RepresentationModel.IDF_TOKEN_UNIGRAMS);
		models.add(RepresentationModel.IDF_TOKEN_BIGRAMS);
		models.add(RepresentationModel.IDF_TOKEN_TRIGRAMS);
		runBagsRoccio(utype, models, userDataMap, userCatMap, rtypes, params, userStrictMap);
	}

	public static void runCharBagsRoccio(ModelInfoSource utype, Map<String, UserData> userDataMap,
			Map<String, String> userCatMap, List<RoccioParams> params, 
			Map<String, String> userStrictMap) {
		List<RepresentationModel> models = new ArrayList<RepresentationModel>();
		Map<RoccioModelType, List<RoccioSimilarityType>> rtypes = new HashMap<RoccioModelType, List<RoccioSimilarityType>>();
		List<RoccioSimilarityType> tfList = new ArrayList<RoccioSimilarityType>();
		tfList.add(RoccioSimilarityType.ROCCIO_COSINE_TF);
		rtypes.put(RoccioModelType.TF, tfList);
		models = new ArrayList<RepresentationModel>();
		models.add(RepresentationModel.CHARACTER_BIGRAMS);
		models.add(RepresentationModel.CHARACTER_TRIGRAMS);
		models.add(RepresentationModel.CHARACTER_FOURGRAMS);
		runBagsRoccio(utype, models, userDataMap, userCatMap, rtypes, params, userStrictMap);
	}

	private static void runBags(ModelInfoSource utype, List<SimilarityName> sims, List<RepresentationModel> models,
			Map<String, UserData> userDataMap, Map<String, String> userCatMap, Map<String, String> userStrictMap) {
		Map<String, List<RankingMetrics>> metricsMap = new HashMap<String, List<RankingMetrics>>();
		Map<String, Map<String, List<RankingMetrics>>> metricsMapByCategory = new HashMap<String, Map<String, List<RankingMetrics>>>();
		Map<String, Map<String, List<RankingMetrics>>> metricsStrictMap = new HashMap<String, Map<String, List<RankingMetrics>>>();
		Map<String, Long> timesMap = new HashMap<String, Long>();
		Map<String, Long> testtimesMap = new HashMap<String, Long>();

		for (RepresentationModel model : models) {
			// System.out.println("model : " + model.name());
			Map<String, UserModelTrainer> trainersMap = new HashMap<String, UserModelTrainer>();
			long currtime=System.currentTimeMillis();
			long time=0;
			long testtimeStart=0L;
			for (Map.Entry<String, UserData> uentry : userDataMap.entrySet()) {
				if (uentry.getValue().getTrainTweets().isEmpty())
					continue;
				AbstractModel vectorModel = RepresentationModel.getModel(model, "test");
				ContentAbstractModel userModel = ContentModelType.getContentModel(vectorModel, utype, false);
				UserModelTrainer trainer = new UserModelTrainer(uentry.getValue(), userModel);
				
				trainer.trainUserModel();
				time+=System.currentTimeMillis()-currtime;
				testtimeStart=System.currentTimeMillis();

				trainer.modelTestTweets();
				Long timetestall=testtimesMap.get(model.name());
				
				if(timetestall==null)
					testtimesMap.put(model.name(), System.currentTimeMillis()-testtimeStart);
				else
					testtimesMap.put(model.name(),timetestall+= System.currentTimeMillis()-testtimeStart);
				trainersMap.put(uentry.getKey(), trainer);

			}
			for (Map.Entry<String, UserModelTrainer> trainerEntry : trainersMap.entrySet()) {
				UserModelTrainer trainer = trainerEntry.getValue();
				trainer.getUserModel().finalizeModel();
				String username = trainerEntry.getKey();
				for (SimilarityName sim : sims) {
					String msimname = model.name() + "_" + sim.name();
					long testRankStart=System.currentTimeMillis();
					trainer.rankAllTweets(sim);
					trainer.evaluateUserModel();
					Long ttime=testtimesMap.get(model.name());
					testtimesMap.put(msimname, ttime+=System.currentTimeMillis()-testRankStart);
					
					String userCategory = userCatMap.get(username);
					
					populateMetricsMapByCategory(userCategory, trainer.getMetrics(), msimname, metricsMapByCategory);
					populateMetricsMapAll(msimname, trainer.getMetrics(), metricsMap);
					String ct = userStrictMap.get(username);
					if (ct != null)
						populateMetricsMapByCategory(userStrictMap.get(username), trainer.getMetrics(), msimname,
								metricsStrictMap);
					Long timeall=timesMap.get(msimname);
					if(timeall==null)
						timesMap.put(msimname, System.currentTimeMillis()-currtime);
					else
						timesMap.put(msimname, timeall+=(System.currentTimeMillis()-currtime));
					
				}
			}
			timesMap.put(model.name(),time);
		}
		for (Map.Entry<String, Long> entry: timesMap.entrySet()) {
			System.out.println(ModelInfoSource.name(utype)+"_"+entry.getKey()+"_" +entry.getValue());
		}
		for (Map.Entry<String, Long> entry: testtimesMap.entrySet()) {
			System.out.println("test_"+ModelInfoSource.name(utype)+"_"+entry.getKey()+"_" +entry.getValue());
		}
		evaluateAll(utype.name(), models, sims, metricsMap);
		evaluateByUserCategory(utype.name(), models, sims, metricsMapByCategory);
		evaluateByUserCategory(utype.name(), models, sims, metricsStrictMap);

	}

	private static void runBagsRoccio(ModelInfoSource utype, List<RepresentationModel> models,
			Map<String, UserData> userDataMap, Map<String, String> userCatMap,
			Map<RoccioModelType, List<RoccioSimilarityType>> types, List<RoccioParams> params,
			Map<String, String> userStrictMap) {
		Map<String, List<RankingMetrics>> metricsMap = new HashMap<String, List<RankingMetrics>>();
		Map<String, Map<String, List<RankingMetrics>>> metricsStrictMap = new HashMap<String, Map<String, List<RankingMetrics>>>();
		Map<String, Map<String, List<RankingMetrics>>> metricsMapByCategory = new HashMap<String, Map<String, List<RankingMetrics>>>();
		Map<String, Long> timesMap = new HashMap<String, Long>();
		Map<String, Long> testtimesMap = new HashMap<String, Long>();

		for (RepresentationModel model : models) {
			long currtime=System.currentTimeMillis();
			long time=0;
			for (Map.Entry<RoccioModelType, List<RoccioSimilarityType>> entry : types.entrySet()) {
				RoccioModelType type = entry.getKey();
				List<RoccioSimilarityType> sims = entry.getValue();
				Map<String, RoccioUserModelTrainer> trainersMap = new HashMap<String, RoccioUserModelTrainer>();

				for (RoccioParams param : params) {
					// System.out.println("model : " + model.name());
					List<RankingMetrics> metrics = new ArrayList<RankingMetrics>();
					String model_name = utype.name() + "_" + RepresentationModel.getShortName(model) + "_"
							+ RoccioModelType.getName(type) + "_" + param.getB() + "_" + param.getC();
					long testtimeStart=0L;
					for (Map.Entry<String, UserData> uentry : userDataMap.entrySet()) {
						UserData udata = userDataMap.get(uentry.getValue().getUserName());
						if (udata.getTrainTweets().isEmpty())
							continue;
						AbstractModel posVectorModel = RepresentationModel.getModel(model, "test");
						AbstractModel negVectorModel = RepresentationModel.getModel(model, "test");
						ContentAbstractModel posUserModel = ContentModelType.getContentModel(posVectorModel, utype,
								false);
						ContentAbstractModel negUserModel = ContentModelType.getContentModel(negVectorModel, utype,
								false);
						RoccioUserModelTrainer trainer = new RoccioUserModelTrainer(udata,
								(ContentVectorModel) posUserModel, (ContentVectorModel) negUserModel, type, param);
						// System.out.println(uentry.getKey());
						trainer.trainUserModel();
			
						time+=System.currentTimeMillis()-currtime;
						testtimeStart=System.currentTimeMillis();

						trainer.modelTestTweets();
						Long timetestall=testtimesMap.get(model.name());
						
						if(timetestall==null)
							testtimesMap.put(model.name(), System.currentTimeMillis()-testtimeStart);
						else
							testtimesMap.put(model.name(),timetestall+= System.currentTimeMillis()-testtimeStart);
						trainersMap.put(uentry.getKey(), trainer);
					}
					for (Map.Entry<String, RoccioUserModelTrainer> trainerEntry : trainersMap.entrySet()) {
						RoccioUserModelTrainer trainer = trainerEntry.getValue();
						trainer.getPosUserModel().finalizeModel();
						trainer.getNegUserModel().finalizeModel();
						String username = trainerEntry.getKey();
						for (RoccioSimilarityType sim : sims) {
							
							long testRankStart=System.currentTimeMillis();
							trainer.rankAllTweets(sim);
							trainer.evaluateUserModel();
							Long ttime=testtimesMap.get(model.name());
							String msimname = model.name() + "_" + RoccioModelType.getName(type) + "_" + sim.name();
							testtimesMap.put(msimname, ttime+=System.currentTimeMillis()-testRankStart);
							String userCategory = userCatMap.get(username);
							
							populateMetricsMapByCategory(userCategory, trainer.getMetrics(), msimname,
									metricsMapByCategory);
							populateMetricsMapAll(msimname, trainer.getMetrics(), metricsMap);
							String ct = userStrictMap.get(username);
							if (ct != null)
								populateMetricsMapByCategory(userStrictMap.get(username), trainer.getMetrics(),
										msimname, metricsStrictMap);
							Long timeall=timesMap.get(msimname);
							if(timeall==null)
								timesMap.put(msimname, System.currentTimeMillis()-currtime);
							else
								timesMap.put(msimname, timeall+=(System.currentTimeMillis()-currtime));
							
							
		
						}
					}
				}
			}
			timesMap.put(model.name(),time);
		}
		for (Map.Entry<String, Long> entry: timesMap.entrySet()) {
			System.out.println(ModelInfoSource.name(utype)+"_"+entry.getKey()+"_" +entry.getValue());
		}
		for (Map.Entry<String, Long> entry: testtimesMap.entrySet()) {
			System.out.println("test_"+ModelInfoSource.name(utype)+"_"+entry.getKey()+"_" +entry.getValue());
		}
		evaluateAllR(utype.name(), models, types, metricsMap);
		evaluateByUserCategoryR(utype.name(), models, types, metricsMapByCategory);
		evaluateByUserCategoryR(utype.name(), models, types, metricsStrictMap);

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

	public static void runCharGraphs(ModelInfoSource utype, Map<String, UserData> userDataMap,
			Map<String, String> userCatMap, Map<String, String> userStrictMap) {
		List<SimilarityName> sims = new ArrayList<SimilarityName>();
		sims.add(SimilarityName.GRAPHS_CONTAINMENT_SIMILARITY);
		sims.add(SimilarityName.GRAPHS_VALUE_SIMILARITY);
		sims.add(SimilarityName.GRAPHS_NORMALIZED_VALUE_SIMILARITY);
		List<RepresentationModel> models = new ArrayList<RepresentationModel>();

		models.add(RepresentationModel.CHARACTER_BIGRAM_GRAPHS);
		models.add(RepresentationModel.CHARACTER_TRIGRAM_GRAPHS);
		models.add(RepresentationModel.CHARACTER_FOURGRAM_GRAPHS);
		runGraphs(models, sims, utype, userDataMap, userCatMap, userStrictMap);
	}

	public static void runTokenGraphs(ModelInfoSource utype, Map<String, UserData> userDataMap,
			Map<String, String> userCatMap, Map<String, String> userStrictMap) {
		List<SimilarityName> sims = new ArrayList<SimilarityName>();
		sims.add(SimilarityName.GRAPHS_CONTAINMENT_SIMILARITY);
		sims.add(SimilarityName.GRAPHS_VALUE_SIMILARITY);
		sims.add(SimilarityName.GRAPHS_NORMALIZED_VALUE_SIMILARITY);
		List<RepresentationModel> models = new ArrayList<RepresentationModel>();

		models.add(RepresentationModel.TOKEN_UNIGRAM_GRAPHS);
		models.add(RepresentationModel.TOKEN_BIGRAM_GRAPHS);
		models.add(RepresentationModel.TOKEN_TRIGRAM_GRAPHS);

		runGraphs(models, sims, utype, userDataMap, userCatMap, userStrictMap);
	}

	private static void runGraphs(List<RepresentationModel> models, List<SimilarityName> sims, ModelInfoSource utype,
			Map<String, UserData> userDataMap, Map<String, String> userCatMap, Map<String, String> userStrictMap) {

		Map<String, List<RankingMetrics>> metricsMap = new HashMap<String, List<RankingMetrics>>();
		Map<String, Map<String, List<RankingMetrics>>> metricsMapByCategory = new HashMap<String, Map<String, List<RankingMetrics>>>();
		Map<String, Map<String, List<RankingMetrics>>> metricsStrictMap = new HashMap<String, Map<String, List<RankingMetrics>>>();
		Map<String, Long> timesMap = new HashMap<String, Long>();
		Map<String, Long> testtimesMap = new HashMap<String, Long>();

		for (RepresentationModel model : models) {
			// System.out.println("model : " + model.name());
			long currtime=System.currentTimeMillis();
			long time=0;
			for (Map.Entry<String, UserData> uentry : userDataMap.entrySet()) {
				AbstractModel graphModel = RepresentationModel.getModel(model, "test");
				ContentAbstractModel userModel = ContentModelType.getContentModel(graphModel, utype, false);
				UserModelTrainer trainer = new UserModelTrainer(uentry.getValue(), userModel);
				
				trainer.trainUserModel();
				time+=System.currentTimeMillis()-currtime;
				
				long testtimeStart=System.currentTimeMillis();
				trainer.modelTestTweets();
				Long timetestall=testtimesMap.get(model.name());
				if(timetestall==null)
					testtimesMap.put(model.name(), System.currentTimeMillis()-testtimeStart);
				else
					testtimesMap.put(model.name(),timetestall+= System.currentTimeMillis()-testtimeStart);
				for (SimilarityName sim : sims) {
					String msimname = model.name() + "_" + sim.name();
					long testRankStart=System.currentTimeMillis();
					trainer.rankAllTweets(sim);
					trainer.evaluateUserModel();
					Long ttime=testtimesMap.get(model.name());
					testtimesMap.put(msimname, ttime+=System.currentTimeMillis()-testRankStart);
					
					String userCategory = userCatMap.get(uentry.getValue().getUserName());
					populateMetricsMapByCategory(userCategory, trainer.getMetrics(), msimname, metricsMapByCategory);
					populateMetricsMapAll(msimname, trainer.getMetrics(), metricsMap);
					String ct = userStrictMap.get(uentry.getValue().getUserName());
					if (ct != null)
						populateMetricsMapByCategory(ct, trainer.getMetrics(),msimname,
								metricsStrictMap);
					Long timeall=timesMap.get(msimname);
					if(timeall==null)
						timesMap.put(msimname, System.currentTimeMillis()-currtime);
					else
						timesMap.put(msimname, timeall+=(System.currentTimeMillis()-currtime));
					
				}
			}
			timesMap.put(model.name(),time);
		}
		evaluateAll(utype.name(), models, sims, metricsMap);
		evaluateByUserCategory(utype.name(), models, sims, metricsMapByCategory);
		evaluateByUserCategory(utype.name(), models, sims, metricsStrictMap);
		for (Map.Entry<String, Long> entry: timesMap.entrySet()) {
			System.out.println(ModelInfoSource.name(utype)+"_"+entry.getKey()+"_" +entry.getValue());
		}
		for (Map.Entry<String, Long> entry: testtimesMap.entrySet()) {
			System.out.println("test_"+ModelInfoSource.name(utype)+"_"+entry.getKey()+"_" +entry.getValue());
		}

	}

	private static void populateMetricsMapAll(String msimname, RankingMetrics metrics,
			Map<String, List<RankingMetrics>> metricsMap) {
		List<RankingMetrics> metricsList = metricsMap.get(msimname);
		if (metricsList == null) {
			metricsList = new ArrayList<RankingMetrics>();
			metricsList.add(metrics);
			metricsMap.put(msimname, metricsList);
		} else {
			metricsList.add(metrics);
		}

	}

	private static void populateMetricsMapByCategory(String userCategory, RankingMetrics metrics, String msimname,
			Map<String, Map<String, List<RankingMetrics>>> metricsMapByCategory) {
		Map<String, List<RankingMetrics>> map = metricsMapByCategory.get(msimname);
		if (map == null) {
			map = new HashMap<String, List<RankingMetrics>>();
			List<RankingMetrics> list = new ArrayList<RankingMetrics>();
			list.add(metrics);
			map.put(userCategory, list);
			metricsMapByCategory.put(msimname, map);
		} else {
			List<RankingMetrics> catMetrics = map.get(userCategory);
			if (catMetrics == null) {
				catMetrics = new ArrayList<RankingMetrics>();
				catMetrics.add(metrics);
				map.put(userCategory, catMetrics);
			} else
				catMetrics.add(metrics);
		}

	}

	private static void evaluateByUserCategory(String name, List<RepresentationModel> models, List<SimilarityName> sims,
			Map<String, Map<String, List<RankingMetrics>>> metricsMapByCategory) {
		for (RepresentationModel model : models) {
			// System.out.println("model : " + model.name());

			for (SimilarityName sim : sims) {

				String msimname = model.name() + "_" + sim.name();
				Map<String, List<RankingMetrics>> map = metricsMapByCategory.get(msimname);
				for (Map.Entry<String, List<RankingMetrics>> entry : map.entrySet()) {
					UserModelEvaluator evaluator = new UserModelEvaluator(entry.getValue());
					double MAP = evaluator.evaluateUserModel();
					System.out.println(
							"MAP_" + name + "_" + entry.getKey() + "_" + RepresentationModel.getShortName(model) + "_"
									+ SimilarityName.getShortName(sim) + "_" + ": " + MAP);
				}
			}
		}

	}

	private static void evaluateAll(String name, List<RepresentationModel> models, List<SimilarityName> sims,
			Map<String, List<RankingMetrics>> metricsMap) {
		for (RepresentationModel model : models) {
			for (SimilarityName sim : sims) {
				String msimname = model.name() + "_" + sim.name();
				UserModelEvaluator evaluator = new UserModelEvaluator(metricsMap.get(msimname));
				double MAP = evaluator.evaluateUserModel();
				System.out.println("MAP_" + name + "_all_" + RepresentationModel.getShortName(model) + "_"
						+ SimilarityName.getShortName(sim) + ": " + MAP);
			}
		}
	}

	private static void evaluateByUserCategoryR(String name, List<RepresentationModel> models,
			Map<RoccioModelType, List<RoccioSimilarityType>> types,
			Map<String, Map<String, List<RankingMetrics>>> metricsMapByCategory) {
		for (RepresentationModel model : models) {
			// System.out.println("model : " + model.name());

			for (Map.Entry<RoccioModelType, List<RoccioSimilarityType>> entry1 : types.entrySet()) {
				RoccioModelType type = entry1.getKey();
				List<RoccioSimilarityType> sims = entry1.getValue();
				for (RoccioSimilarityType sim : sims) {
					String msimname = model.name() + "_" + RoccioModelType.getName(type) + "_" + sim.name();
					Map<String, List<RankingMetrics>> map = metricsMapByCategory.get(msimname);
					for (Map.Entry<String, List<RankingMetrics>> entry : map.entrySet()) {
						UserModelEvaluator evaluator = new UserModelEvaluator(entry.getValue());
						double MAP = evaluator.evaluateUserModel();
						System.out.println(
								"MAP_" + name + "_" + entry.getKey() + "_" + RepresentationModel.getShortName(model)
										+ "_" + RoccioSimilarityType.getName(sim) + "_" + ": " + MAP);
					}
				}
			}
		}

	}

	private static void evaluateAllR(String name, List<RepresentationModel> models,
			Map<RoccioModelType, List<RoccioSimilarityType>> types, Map<String, List<RankingMetrics>> metricsMap) {
		for (RepresentationModel model : models) {
			for (Map.Entry<RoccioModelType, List<RoccioSimilarityType>> entry1 : types.entrySet()) {
				RoccioModelType type = entry1.getKey();
				List<RoccioSimilarityType> sims = entry1.getValue();
				for (RoccioSimilarityType sim : sims) {
					String msimname = model.name() + "_" + RoccioModelType.getName(type) + "_" + sim.name();
					UserModelEvaluator evaluator = new UserModelEvaluator(metricsMap.get(msimname));
					double MAP = evaluator.evaluateUserModel();
					System.out.println("MAP_" + name + "_all_" + RepresentationModel.getShortName(model) + "_"
							+ RoccioSimilarityType.getName(sim) + ": " + MAP);
				}
			}
		}
	}
	// public static void main(String[] args) {
	// String usersPath =
	// "/home/efi/master-thesis/resultUsers/serializedUsers/";
	// GetUserActivities getActivities = new GetUserActivities(usersPath);
	// List<UserActivity> acts = new ArrayList<UserActivity>();
	// acts.addAll(getActivities.getUserActivities("bottom"));
	// acts.addAll(getActivities.getUserActivities("top"));
	// acts.addAll(getActivities.getUserActivities("avg"));
	// List<SimilarityName> sims = new ArrayList<SimilarityName>();
	//
	// sims.add(SimilarityName.JACCARD_SIMILARITY);
	// sims.add(SimilarityName.COSINE_BOOLEAN_SIMILARITY);
	// sims.add(SimilarityName.GENERALIZED_JACCARD_SIMILARITY);
	// sims.add(SimilarityName.COSINE_TF_SIMILARITY);
	// sims.add(SimilarityName.GENERALIZED_JACCARD_SIMILARITY_CENTROID);
	// sims.add(SimilarityName.COSINE_TF_SIMILARITY_CENTROID);
	// // sims.add(SimilarityName.COSINE_TFIDF_SIMILARITY);
	// List<RepresentationModel> models = new ArrayList<RepresentationModel>();
	// models.add(RepresentationModel.CHARACTER_BIGRAMS);
	// models.add(RepresentationModel.CHARACTER_TRIGRAMS);
	// models.add(RepresentationModel.CHARACTER_FOURGRAMS);
	// models.add(RepresentationModel.TOKEN_UNIGRAMS);
	// models.add(RepresentationModel.TOKEN_BIGRAMS);
	// models.add(RepresentationModel.TOKEN_TRIGRAMS);
	//
	// Map<String, List<RankingMetrics>> metricsMap = new HashMap<String,
	// List<RankingMetrics>>();
	// for (RepresentationModel model : models) {
	// System.out.println("model : " + model.name());
	//
	// //
	// if(model.name().equals("TOKEN_BIGRAM_GRAPHS")&&sim.name().equals("GRAPHS_VALUE_SIMILARITY"))
	// // continue;
	// int skipped = 0;
	//
	// for (int i = 0; i < acts.size(); i++) {
	// UserDataCreator dataCreator = new UserDataCreator(acts.get(i));
	//
	// // System.out.println("user : " + acts.get(i).getUserName());
	// if (TwitterUtilities.userStatistics(acts.get(i)) < 20) {
	// // System.out.println("user : " +
	// // acts.get(i).getUserName()+" skipped");
	// skipped++;
	// continue;
	// }
	// // TwitterUtilities.printUserTweets(acts.get(i));
	// // System.out.println("followers : "+
	// // acts.get(i).getFollowees().size());
	// // System.out.println("followees : "+
	// // acts.get(i).getFollowers().size());
	// // dataCreator.followeesTweetsUniform(0.8);
	// // dataCreator.followersTweetsRetweets(0.8);
	// dataCreator.postedTweetsRetweets(0.8);
	// AbstractModel vectorModel = RepresentationModel.getModel(model, "test" +
	// i);
	// UserModelTrainer trainer = new
	// UserModelTrainer(dataCreator.getUserData(), vectorModel);
	// trainer.trainUserModel();
	// trainer.modelTestTweets();
	// for (SimilarityName sim : sims) {
	//
	// trainer.rankAllTweets(sim);
	// trainer.evaluateUserModel();
	//
	// List<RankingMetrics> metricsList = metricsMap.get(model.name() + "_" +
	// sim.name());
	// if (metricsList == null) {
	// metricsList = new ArrayList<RankingMetrics>();
	// metricsList.add(trainer.getMetrics());
	// metricsMap.put(model.name() + "_" + sim.name(), metricsList);
	// } else {
	// metricsList.add(trainer.getMetrics());
	// }
	// }
	// }
	//
	// System.out.println("skipped: " + skipped);
	// }
	// for (RepresentationModel model : models) {
	// System.out.println("model : " + model.name());
	// for (SimilarityName sim : sims) {
	// System.out.println("sim " + sim.name());
	// UserModelEvaluator evaluator = new
	// UserModelEvaluator(metricsMap.get(model.name() + "_" + sim.name()));
	// evaluator.evaluateUserModel();
	// }
	// }
	// }
}

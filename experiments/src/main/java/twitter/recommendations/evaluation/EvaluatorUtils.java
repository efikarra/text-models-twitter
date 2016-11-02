package twitter.recommendations.evaluation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EvaluatorUtils {
	public static double evaluateAll(List<RankingMetrics> metrics,String name) {
		UserModelEvaluator evaluator = new UserModelEvaluator(metrics);
		double MAP =evaluator.evaluateUserModel();
		System.out.println("MAP_" + name + ": " + MAP);
		return MAP;
	}
	public static Map<String, Double> evaluateByUserCategory(Map<String, List<RankingMetrics>> metricsByCategory,String name) {
		String prefix="";
		Map<String, Double> userTypesMAP=new HashMap<String, Double>();
		for (Map.Entry<String, List<RankingMetrics>> entry : metricsByCategory.entrySet()) {
			prefix = name+"_"+entry.getKey();
			UserModelEvaluator evaluator = new UserModelEvaluator(entry.getValue());
			double MAP = evaluator.evaluateUserModel();
			System.out.println("MAP_" + prefix + ": " + MAP);
			userTypesMAP.put(entry.getKey(), MAP);
		}
		return userTypesMAP;
	}
}

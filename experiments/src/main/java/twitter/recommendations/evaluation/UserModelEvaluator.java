package twitter.recommendations.evaluation;

import java.util.List;

public class UserModelEvaluator {
	List<RankingMetrics> usersMetrics;
	double meanAvgPrecision;
	double meanReciprocalRank;

	public UserModelEvaluator(List<RankingMetrics> usersMetrics) {
		this.usersMetrics = usersMetrics;
		meanAvgPrecision = 0.0;
		meanReciprocalRank = 0.0;
	}

	private void computeMAP() {
		double sumAvgPrecision = 0.0;
		for (RankingMetrics rankingMetric : usersMetrics) {
			sumAvgPrecision += rankingMetric.getAvgPrecision();
		}
		meanAvgPrecision = sumAvgPrecision / ((double) usersMetrics.size());
	}

	private void computeMRR() {
		double sumRRank = 0.0;
		for (RankingMetrics rankingMetric : usersMetrics) {
			sumRRank += rankingMetric.getReciprocalRank();
		}
		meanReciprocalRank = sumRRank / ((double) usersMetrics.size());
	}
	public double evaluateUserModel() {
		computeMAP();
		return meanAvgPrecision;
		//computeMRR();
		//System.out.println("Mean average precsision: "+meanAvgPrecision);
		//System.out.println("Mean reciprocal rank: "+meanReciprocalRank);
	}
	public List<RankingMetrics> getUsersMetrics() {
		return usersMetrics;
	}

	public void setUsersMetrics(List<RankingMetrics> usersMetrics) {
		this.usersMetrics = usersMetrics;
	}

	public double getMeanAvgPrecision() {
		return meanAvgPrecision;
	}

	public void setMeanAvgPrecision(double meanAvgPrecision) {
		this.meanAvgPrecision = meanAvgPrecision;
	}

	public double getMeanReciprocalRank() {
		return meanReciprocalRank;
	}

	public void setMeanReciprocalRank(double meanReciprocalRank) {
		this.meanReciprocalRank = meanReciprocalRank;
	}
}

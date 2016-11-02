package twitter.recommendations.evaluation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RankingMetrics {
	private final List<Integer> rankingPositions;
	private double avgPrecision;
	private double reciprocalRank;

	public RankingMetrics() {
		rankingPositions = new ArrayList<Integer>();
		avgPrecision=0.0;
		reciprocalRank=0.0;
	}
	public List<Integer> getRankingPositions() {
        return rankingPositions;
    }
	public void printPerfomanceEvaluation() {
		computeAP();
		//computeRR();
		//System.out.println("Average precsision: "+avgPrecision);
		//System.out.println("Reciprocal rank: "+reciprocalRank);
    }

	public void computeAP() {
		Collections.sort(rankingPositions);
		int count=0;
		double sum=0.0;
		for(Integer rankingPosition : rankingPositions) {
			count++;
			sum += ((double)count)/((double)rankingPosition);
			//System.out.println(count+" in "+rankingPosition);
        }
		avgPrecision=sum/(double)rankingPositions.size();
	}

	public void computeRR() {
		Collections.sort(rankingPositions);
		reciprocalRank=(double)((double)1/(double)rankingPositions.get(0));
	}
	public double getAvgPrecision() {
		return avgPrecision;
	}
	public void setAvgPrecision(double avgPrecision) {
		this.avgPrecision = avgPrecision;
	}
	public double getReciprocalRank() {
		return reciprocalRank;
	}
	public void setReciprocalRank(double reciprocalRank) {
		this.reciprocalRank = reciprocalRank;
	}
}

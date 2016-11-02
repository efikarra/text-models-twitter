package twitter.modelUtilities;

import java.util.List;

import twitter.recommendations.content.RoccioParams;

public class RocchioStrategy {
	private RoccioParams params;
	public RocchioStrategy(RoccioParams params) {
		this.params=params;
	}

	public double[] mergeVectors(List<double[]> posVectors,List<double[]> negVectors) {
		int length = 0;
		if(posVectors.isEmpty()){
			System.out.println("empty");
			length = negVectors.get(0).length;
		}
		else
			length = posVectors.get(0).length;
		double[] mergedVector = new double[length];

		for (int i = 0; i < posVectors.size(); i++) {
			for (int j = 0; j < posVectors.get(i).length; j++)
				mergedVector[j] += (params.getB()*posVectors.get(i)[j])/(double)posVectors.size();
		}
		for (int i = 0; i < negVectors.size(); i++) {
			for (int j = 0; j < negVectors.get(i).length; j++)
				mergedVector[j] += (params.getC()*negVectors.get(i)[j])/(double)negVectors.size();
		}
		return mergedVector;

	}
}

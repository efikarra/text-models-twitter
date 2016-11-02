package twitter.modelUtilities;

import java.util.List;
import java.util.Map;

public class CentroidStrategy extends AbstractStrategy {

	@Override
	public double[] mergeVectors(List<double[]> vectors) {
		int length = vectors.get(0).length;
		double[] mergedVector = new double[length];

		for (int i = 0; i < vectors.size(); i++) {
			for (int j = 0; j < vectors.get(i).length; j++)
				mergedVector[j] += vectors.get(i)[j];
		}
		for (int i = 0; i < length; i++) {
			mergedVector[i] = mergedVector[i] / (double)vectors.size();

		}
		return mergedVector;

	}
}

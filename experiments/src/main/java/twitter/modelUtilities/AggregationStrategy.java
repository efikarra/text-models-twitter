package twitter.modelUtilities;

import java.util.List;

public class AggregationStrategy extends AbstractStrategy{

	@Override
	public double[] mergeVectors(List<double[]>  vectors){
		double[] mergedVector = new double[vectors.get(0).length];

		for(int i=0;i<vectors.size();i++){
			for(int j=0;j<vectors.get(i).length;j++)
				mergedVector[j]+=vectors.get(i)[j];
		}
		return mergedVector;
	}

}

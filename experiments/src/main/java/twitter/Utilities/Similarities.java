package twitter.Utilities;

public class Similarities {

	public static double computeCosineSimilarity(double[] vectorA,double[] vectorB) {
		 double dotProduct = 0.0;
		    double normA = 0.0;
		    double normB = 0.0;
		    for (int i = 0; i < vectorA.length; i++) {
		        dotProduct += vectorA[i] * vectorB[i];
		        normA += Math.pow(vectorA[i], 2);
		        normB += Math.pow(vectorB[i], 2);
		    }   
		    double denominator=(Math.sqrt(normA) * Math.sqrt(normB));
		    if(denominator==0)
		    	return -1.0;
		    return dotProduct / denominator;
	}
}

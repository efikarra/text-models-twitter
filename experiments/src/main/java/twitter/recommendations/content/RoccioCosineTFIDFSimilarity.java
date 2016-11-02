package twitter.recommendations.content;

import java.util.Map;
import java.util.Map.Entry;

import Models.VectorModel;
import twitter.recommendations.content.models.RoccioVectorModel;

public class RoccioCosineTFIDFSimilarity extends RoccioSimilarity {
	public double getValue(RoccioVectorModel vector1, VectorModel vector2) {
		double sim = 0.0;
		Map<String, Double> itemVector1 = vector1.getFrequencies();
		Map<String, Double> itemVector2 = vector2.getItemsFrequency();
		if (itemVector2.size() < itemVector1.size()) {
			Map<String, Double> tempItemVector = itemVector1;
			itemVector1 = itemVector2;
			itemVector2 = tempItemVector;
		}

		double numerator = 0.0;
		for (Entry<String, Double> entry : itemVector1.entrySet()) {
			Double frequency2 = itemVector2.get(entry.getKey());
			Double docFreq=vector2.getDocumentFrequency().get(entry.getKey());
			if(docFreq==null)
				docFreq=0.0;
			if (frequency2 != null) {
				double inverseDocFreq1 = Math
						.log(vector2.getNoOfDocuments() / (1 + docFreq));
				numerator += (entry.getValue()  * inverseDocFreq1* frequency2) / vector2.getNoOfTotalTerms();
			}
		}

		double denominator = getVectorMagnitude(vector1) * getVectorMagnitude(vector2);
		if (denominator == 0)
			return -1.0;
		sim = numerator / denominator;
		return sim;
	}

	private double getVectorMagnitude(RoccioVectorModel vecModel) {
		double magnitude = 0.0;
		for (Entry<String, Double> entry : vecModel.getFrequencies().entrySet()) {
			double weight = entry.getValue();
			magnitude += Math.pow(weight, 2.0);
		}

		return Math.sqrt(magnitude);
	}

	private double getVectorMagnitude(VectorModel vecModel) {
		double magnitude = 0.0;
		for (Entry<String, Double> entry : vecModel.getItemsFrequency().entrySet()) {
			Double docFreq=vecModel.getDocumentFrequency().get(entry.getKey());
			if(docFreq==null)
				docFreq=0.0;
			double inverseDocFreq1 = Math
					.log(vecModel.getNoOfDocuments() / (1 + docFreq));
			double weight = (entry.getValue() * inverseDocFreq1) / vecModel.getNoOfTotalTerms();
			magnitude += Math.pow(weight, 2.0);
		}

		return Math.sqrt(magnitude);
	}
}

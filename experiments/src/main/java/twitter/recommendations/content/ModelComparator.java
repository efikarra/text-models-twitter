package twitter.recommendations.content;

import Similarities.AbstractSimilarity;
import Utilities.SimilarityName;
import twitter.recommendations.content.models.ContentAbstractModel;

public class ModelComparator {
	private AbstractSimilarity similarity;
	public ModelComparator(SimilarityName similarityType) {
		this.similarity=SimilarityName .getMetric(similarityType);
	}
	public double compareModels(ContentAbstractModel model1, ContentAbstractModel model2) {
		return similarity.getValue(model1.getModel(), model2.getModel());

	}
}

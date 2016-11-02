package twitter.recommendations.content;

import Models.VectorModel;
import twitter.recommendations.content.models.RoccioVectorModel;

public abstract class RoccioSimilarity {

	public abstract double getValue(RoccioVectorModel vector1,VectorModel vector2);
}

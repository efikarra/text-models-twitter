package twitter.modelUtilities;

import twitter.recommendations.content.RoccioCosineTFIDFSimilarity;
import twitter.recommendations.content.RoccioCosineTFSimilarity;
import twitter.recommendations.content.RoccioSimilarity;

public enum RoccioSimilarityType {
	ROCCIO_COSINE_TF, ROCCIO_COSINE_TFIDF;
	public static RoccioSimilarity getRoccioSimilarity(RoccioSimilarityType type) {
		switch (type) {
		case ROCCIO_COSINE_TF:
			return new RoccioCosineTFSimilarity();
		case ROCCIO_COSINE_TFIDF:
			return new RoccioCosineTFIDFSimilarity();
		default:
			return null;
		}
		
	}
	
	public static String getName(RoccioSimilarityType type){
		switch (type) {
		case ROCCIO_COSINE_TF:
			return "RTFS";
		case ROCCIO_COSINE_TFIDF:
			return "RTFIDFS";
		default:
			return null;
		}
	}
}

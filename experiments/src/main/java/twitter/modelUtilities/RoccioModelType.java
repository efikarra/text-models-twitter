package twitter.modelUtilities;

import Models.VectorModel;
import twitter.recommendations.content.models.ContentVectorModel;
import twitter.recommendations.content.models.RoccioTFIDFVectorModel;
import twitter.recommendations.content.models.RoccioTFVectorModel;
import twitter.recommendations.content.models.RoccioVectorModel;

public enum RoccioModelType {
TF,TF_IDF;
	
	public static RoccioVectorModel getRoccioModel(RoccioModelType type,ContentVectorModel posModel,ContentVectorModel negModel) {
		switch (type) {
		case TF:
			return new RoccioTFVectorModel(posModel, negModel);
		case TF_IDF:
			return new RoccioTFIDFVectorModel(posModel, negModel);
		default:
			return null;
		}
		
	}
	public static String getName(RoccioModelType type){
		switch (type) {
		case TF:
			return "TF";
		case TF_IDF:
			return "TFIDF";
		default:
			return null;
		}
	}
}

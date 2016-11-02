package twitter.recommendations.content.models;

import java.util.Map;
import java.util.Map.Entry;

import Models.VectorModel;
import twitter.recommendations.content.RoccioParams;

public class RoccioTFIDFVectorModel extends RoccioVectorModel {
	public RoccioTFIDFVectorModel(ContentVectorModel posModel, ContentVectorModel negModel) {
		super(posModel, negModel);
	}

	@Override
	public void createModel(RoccioParams params) {
		VectorModel posVModel=(VectorModel)posModel.getModel();
		VectorModel negVVector=(VectorModel)negModel.getModel();
		Map<String, Double> posVector = posVModel.getItemsFrequency();
		Map<String, Double> negVector = negVVector.getItemsFrequency();
		double posMagn=getVectorMagnitude(posVModel);
		for (Entry<String, Double> entry : posVector.entrySet()) {
			Double freq = frequencies.get(entry.getKey());
			Double docFreq=posVModel.getDocumentFrequency().get(entry.getKey());
			if(docFreq==null)
				docFreq=0.0;
			double inverseDocFreq1 = Math
					.log(posVModel.getNoOfCorpusDocuments() / (1 + docFreq));
			double tfidf = (entry.getValue() / posVModel.getNoOfTotalTerms()) * inverseDocFreq1;
			if (freq != null) {
				frequencies.put(entry.getKey(),
						freq + (params.getB()/posVModel.getNoOfDocuments()) * ((inverseDocFreq1 * tfidf)/posMagn));
			} else
				frequencies.put(entry.getKey(),
						(params.getB()/posVModel.getNoOfDocuments()) * ((inverseDocFreq1 * tfidf)/posMagn) );

		}
		double negMagn=getVectorMagnitude(posVModel);
		for (Entry<String, Double> entry : negVector.entrySet()) {
			Double freq = frequencies.get(entry.getKey());
			Double docFreq=negVVector.getDocumentFrequency().get(entry.getKey());
			if(docFreq==null)
				docFreq=0.0;
			double inverseDocFreq1 = Math
					.log(negVVector.getNoOfCorpusDocuments() / (1 + docFreq));
			double tfidf = (entry.getValue() / negVVector.getNoOfTotalTerms()) * inverseDocFreq1;
			if (freq != null) {

				frequencies.put(entry.getKey(),
						freq + (params.getC()/negVVector.getNoOfDocuments()) * ((inverseDocFreq1 * tfidf)/negMagn) );
			} else
				frequencies.put(entry.getKey(),
						(params.getC()/negVVector.getNoOfDocuments()) * ((inverseDocFreq1 * tfidf)/negMagn));
		}
	}
	 private double getVectorMagnitude(VectorModel vecModel) {
	        double magnitude = 0.0;
	        for (Entry<String, Double> entry : vecModel.getItemsFrequency().entrySet()) {
	            Double docFreq=vecModel.getDocumentFrequency().get(entry.getKey());
	            if(docFreq==null)
	                docFreq=0.0;
	            double inverseDocFreq = Math.log(vecModel.getNoOfCorpusDocuments()/(1+docFreq));
	            double weight = (entry.getValue()/vecModel.getNoOfTotalTerms())*inverseDocFreq;
	            magnitude += Math.pow(weight, 2.0);
	        }

	        return Math.sqrt(magnitude);
	    }
}

package twitter.recommendations.content.models;

import java.util.Map;
import java.util.Map.Entry;

import Models.VectorModel;
import twitter.recommendations.content.RoccioParams;

public class RoccioTFVectorModel extends RoccioVectorModel {

	public RoccioTFVectorModel(ContentVectorModel posModel, ContentVectorModel negModel) {
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
			if (freq != null) {
				frequencies.put(entry.getKey(),
						freq + (params.getB()/posVModel.getNoOfDocuments()) * (entry.getValue() / posVModel.getNoOfTotalTerms()/posMagn));
			} else
				frequencies.put(entry.getKey(), (params.getB()/posVModel.getNoOfDocuments()) * (entry.getValue() / posVModel.getNoOfTotalTerms()/posMagn));

		}
		double negMagn=getVectorMagnitude(posVModel);

		for (Entry<String, Double> entry : negVector.entrySet()) {
			Double freq = frequencies.get(entry.getKey());
			if (freq != null) {
				frequencies.put(entry.getKey(),
						freq + (params.getC()/negVVector.getNoOfDocuments()) * (entry.getValue() / negVVector.getNoOfTotalTerms()/negMagn));
			} else
				frequencies.put(entry.getKey(), (params.getC()/negVVector.getNoOfDocuments()) * (entry.getValue() / negVVector.getNoOfTotalTerms()/negMagn));
		}
	}
	 private double getVectorMagnitude(VectorModel vecModel) {
	        double magnitude = 0.0;
	        for (Entry<String, Double> entry : vecModel.getItemsFrequency().entrySet()) {
	            double weight = entry.getValue()/vecModel.getNoOfTotalTerms()/vecModel.getNoOfDocuments();
	            magnitude += Math.pow(weight, 2.0);
	        }

	        return Math.sqrt(magnitude);
	    }
}

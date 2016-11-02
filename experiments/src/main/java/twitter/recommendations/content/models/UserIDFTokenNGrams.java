package twitter.recommendations.content.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import Models.IDFTokenNGrams;
import twitter.modelUtilities.ModelInfoSource;
import twitter.recommendations.content.IDFSingleton;

public class UserIDFTokenNGrams extends ContentVectorModel {
	public UserIDFTokenNGrams(IDFTokenNGrams model, ModelInfoSource utype) {
		super(model, utype);
	}

	@Override
	public void updateModel(String text) {
		IDFSingleton idfsingl = IDFSingleton.getInstance();
		model.updateModel(text);
		if (!idfsingl.getFinalizedFreqs()
				.contains(modelInfoSource.name() + "_" + model.getModelType().getShortName(model.getModelType()))) {
			Map<String, Double> docFreqMap = idfsingl.getDocumentFrequency()
					.get(modelInfoSource.name() + "_" + model.getModelType().getShortName(model.getModelType()));
			if (docFreqMap == null) {
				docFreqMap = new HashMap<String, Double>();
				idfsingl.getDocumentFrequency().put(
						modelInfoSource.name() + "_" + model.getModelType().getShortName(model.getModelType()),
						docFreqMap);
			}
			ArrayList<String> items = ((IDFTokenNGrams) model).getLastTokenizedFeatures();
			final HashSet<String> distinctItems = new HashSet<String>(2 * items.size());
			distinctItems.addAll(items);
			for (String item : distinctItems) {
				Double frequency = docFreqMap.get(item);
				if (frequency == null) {
					frequency = new Double(0);
				}
				frequency++;
				docFreqMap.put(item, frequency);
			}
		}
	}

	public void setDocumentFrequencies() {
		IDFSingleton idfsingl = IDFSingleton.getInstance();
		Map<String, Double> docFreqMap = idfsingl.getDocumentFrequency().get(modelInfoSource.name()+ "_" + model.getModelType().getShortName(model.getModelType()));
		((IDFTokenNGrams) model).setDocumentFrequency((HashMap<String, Double>) docFreqMap);
		idfsingl.getFinalizedFreqs()
		.add(modelInfoSource.name() + "_" + model.getModelType().getShortName(model.getModelType()));
	}

	@Override
	public void finalizeModel() {
		setDocumentFrequencies();
		

	}

}

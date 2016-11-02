package twitter.recommendations.content.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Models.IDFTokenNGrams;
import twitter.modelUtilities.ModelInfoSource;
import twitter.recommendations.content.IDFSingleton;

public class TweetIDFTokenNGrams extends ContentVectorModel{
	public TweetIDFTokenNGrams(IDFTokenNGrams model,ModelInfoSource utype) {
		super(model,utype);
	}
	@Override
	public void updateModel(String text) {
		model.updateModel(text);
	}
	
	public void setDocumentFrequencies() {
		IDFSingleton idfsingl=IDFSingleton.getInstance();
		Map<String,Double> docFreqMap=idfsingl.getDocumentFrequency().get(modelInfoSource.name()+"_"+model.getModelType().getShortName(model.getModelType()));
		((IDFTokenNGrams)model).setDocumentFrequency((HashMap<String, Double>) docFreqMap);
	}
	@Override
	public void finalizeModel() {
		setDocumentFrequencies();
		
	}
}

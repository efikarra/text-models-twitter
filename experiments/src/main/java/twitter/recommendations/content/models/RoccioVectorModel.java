package twitter.recommendations.content.models;

import java.util.HashMap;
import java.util.Map;

import Models.VectorModel;
import twitter.recommendations.content.RoccioParams;

public abstract class RoccioVectorModel {
	protected ContentVectorModel posModel;
	protected ContentVectorModel negModel;
	protected Map<String,Double> frequencies;
	public RoccioVectorModel(ContentVectorModel posModel,ContentVectorModel negModel){
		this.posModel=posModel;
		this.negModel=negModel;
		frequencies=new HashMap<String,Double>();
	}
	public abstract void createModel(RoccioParams params);
	public Map<String, Double> getFrequencies() {
		return frequencies;
	}
	public void setFrequencies(Map<String, Double> frequencies) {
		this.frequencies = frequencies;
	}
}

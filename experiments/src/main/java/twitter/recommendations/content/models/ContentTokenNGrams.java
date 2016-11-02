package twitter.recommendations.content.models;

import Models.TokenNGrams;
import twitter.modelUtilities.ModelInfoSource;

public class ContentTokenNGrams extends ContentVectorModel{
	public ContentTokenNGrams(TokenNGrams model,ModelInfoSource utype) {
		super(model,utype);
	}
	@Override
	public void updateModel(String text) {
		model.updateModel(text);
	}
	@Override
	public void finalizeModel() {
		
	}
}

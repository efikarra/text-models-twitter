package twitter.recommendations.content.models;

import Models.CharacterNGrams;
import twitter.modelUtilities.ModelInfoSource;

public class ContentCharacterNGrams extends ContentVectorModel{
	public ContentCharacterNGrams(CharacterNGrams model,ModelInfoSource utype) {
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

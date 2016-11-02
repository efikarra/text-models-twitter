package twitter.recommendations.content.models;

import Models.VectorModel;
import twitter.modelUtilities.ModelInfoSource;

public abstract class ContentVectorModel extends ContentAbstractModel{
	public ContentVectorModel(VectorModel model,ModelInfoSource utype) {
		super(model,utype);
	}
	public abstract void finalizeModel();
	@Override
	public void updateModel(String text) {
		model.updateModel(text);
	}
}

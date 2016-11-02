package twitter.recommendations.content.models;

import Models.AbstractModel;
import Utilities.RepresentationModel;
import twitter.modelUtilities.ModelInfoSource;

public abstract class ContentAbstractModel {
	protected AbstractModel model;
	public abstract void updateModel(String text);
	public abstract void finalizeModel();
	protected ModelInfoSource modelInfoSource;
	public ContentAbstractModel(AbstractModel model,ModelInfoSource modelInfoSource) {
		this.model=model;
		this.modelInfoSource=modelInfoSource;
	}
	public AbstractModel getModel() {
		return model;
	}
	public void setModel(AbstractModel model) {
		this.model = model;
	}
	public RepresentationModel getModelType() {
        return model.getModelType();
    }
	public ModelInfoSource getModelInfoSource() {
		return modelInfoSource;
	}
	public void setModelInfoSource(ModelInfoSource type) {
		this.modelInfoSource = type;
	}
}

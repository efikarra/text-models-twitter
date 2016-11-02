package twitter.recommendations.content.models;

import Models.GraphModel;
import twitter.modelUtilities.ModelInfoSource;

public class ContentGraphModel extends ContentAbstractModel{
	public ContentGraphModel(GraphModel model,ModelInfoSource userModelType) {
		super(model,userModelType);
	}

	@Override
	public void updateModel(String text) {
		model.updateModel(text);
		
	}

	@Override
	public void finalizeModel() {
		// TODO Auto-generated method stub
		
	}
}

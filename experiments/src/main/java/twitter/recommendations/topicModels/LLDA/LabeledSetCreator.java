package twitter.recommendations.topicModels.LLDA;

import java.util.List;

import twitter.DataStructures.LabeledDocument;
import twitter.DataStructures.UserData;

public abstract class LabeledSetCreator {
	protected UserData data;
	protected LLDATweetLabels labels;
	public LabeledSetCreator(UserData data) {
		this.data=data;
	}
	public abstract List<LabeledDocument> createTrainset();
	public UserData getData() {
		return data;
	}
	public void setData(UserData data) {
		this.data = data;
	}
	public LLDATweetLabels getLabels() {
		return labels;
	}
	public void setLabels(LLDATweetLabels labels) {
		this.labels = labels;
	}
}

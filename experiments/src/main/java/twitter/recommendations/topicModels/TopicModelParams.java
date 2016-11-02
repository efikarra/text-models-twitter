package twitter.recommendations.topicModels;

public abstract class TopicModelParams {
	protected int nIter;
	public abstract String getParamsName();
	public TopicModelParams(int nIter) {
		this.nIter = nIter;
	}

	public int getnIter() {
		return nIter;
	}

	public void setnIter(int nIter) {
		this.nIter = nIter;
	}
}

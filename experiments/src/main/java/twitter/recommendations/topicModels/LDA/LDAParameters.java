package twitter.recommendations.topicModels.LDA;

import twitter.recommendations.topicModels.TopicModelParams;

public class LDAParameters extends TopicModelParams{
	int topics;
	double a;
	double b;

	public LDAParameters(int topics, double a, double b, int nIter) {
		super(nIter);
		this.topics = topics;
		this.a = a;
		this.b = b;
	}

	public LDAParameters() {
		this(50, 0.01, 0.01, 500);
	}

	public LDAParameters(int topics, double b,int nIter) {
		this(topics, (double) 50 / (double) topics, b, nIter);
	}
	public int getLatentTopics() {
		return topics;
	}

	public void setLatentTopics(int topics) {
		this.topics = topics;
	}

	public double getA() {
		return a;
	}

	public void setA(double a) {
		this.a = a;
	}

	public double getB() {
		return b;
	}

	public void setB(double b) {
		this.b = b;
	}

	@Override
	public String getParamsName() {
		return this.getA()+"_"+this.getB()+"_"+this.getLatentTopics()+"_"+this.getnIter();
	}

}

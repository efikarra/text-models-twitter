package twitter.recommendations.topicModels.LLDA;

import twitter.recommendations.topicModels.TopicModelParams;

public class LLDAParameters extends TopicModelParams{
	int latentTopics;
	int hashThres;
	double a;
	double b;

	public LLDAParameters(int topics, double a, double b, int nIter,int hashThres) {
		super(nIter);
		this.latentTopics = topics;
		this.a = a;
		this.b = b;
		this.hashThres=hashThres;
	}

	public LLDAParameters() {
		this(50, 0.01, 0.01, 500,5);
	}

	public LLDAParameters(int topics, double b,int nIter) {
		this(topics, (double) 50 / (double) topics, b, nIter,30);
	}
	public LLDAParameters(int topics, double b,int nIter,int hashThres) {
		this(topics, (double) 50 / (double) topics, b, nIter,hashThres);
	}
	public int getLatentTopics() {
		return latentTopics;
	}

	public void setLatentTopics(int topics) {
		this.latentTopics = topics;
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
		return this.getA()+"_"+this.getB()+"_"+this.getLatentTopics()+"_"+nIter;
	}

	public int getHashThres() {
		return hashThres;
	}

	public void setHashThres(int hashThres) {
		this.hashThres = hashThres;
	}

}


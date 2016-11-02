package twitter.recommendations.topicModels.HDP;

import twitter.recommendations.topicModels.TopicModelParams;

public class HDPParameters extends TopicModelParams{
	private double alpha;
	private double beta;
	private double gamma;
	private int initialK;
	public HDPParameters(double alpha,double beta,double gamma,int initialK,int nIter) {
		super(nIter);
		this.alpha=alpha;
		this.beta=beta;
		this.gamma=gamma;
		this.initialK=initialK;
	}
	public double getAlpha() {
		return alpha;
	}
	public void setAlpha(double alpha) {
		this.alpha = alpha;
	}
	public double getBeta() {
		return beta;
	}
	public void setBeta(double beta) {
		this.beta = beta;
	}
	public double getGamma() {
		return gamma;
	}
	public void setGamma(double gamma) {
		this.gamma = gamma;
	}
	public int getInitialK() {
		return initialK;
	}
	public void setInitialK(int initialK) {
		this.initialK = initialK;
	}
	@Override
	public String getParamsName() {
		return this.getAlpha() + "_" + this.getBeta() + "_" + this.getGamma()+ "_" + this.getInitialK()+"_"+nIter;
	}
}

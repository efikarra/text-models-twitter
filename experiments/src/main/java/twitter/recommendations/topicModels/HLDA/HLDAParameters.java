package twitter.recommendations.topicModels.HLDA;

import twitter.recommendations.topicModels.TopicModelParams;

public class HLDAParameters extends TopicModelParams{
	private double alpha;
	private double gamma;
	private double eta;
	private int numLevels;

	public HLDAParameters(double alpha,double gamma,double eta,int numLevels,int nIter) {
		super(nIter);
		this.alpha=alpha;
		this.gamma=gamma;
		this.eta=eta;
		this.numLevels=numLevels;
		
	}
	public double getAlpha() {
		return alpha;
	}

	public void setAlpha(double alpha) {
		this.alpha = alpha;
	}

	public double getGamma() {
		return gamma;
	}

	public void setGamma(double gamma) {
		this.gamma = gamma;
	}

	public double getEta() {
		return eta;
	}

	public void setEta(double eta) {
		this.eta = eta;
	}
	public int getNumLevels() {
		return numLevels;
	}
	public void setNumLevels(int numLevels) {
		this.numLevels = numLevels;
	}
	@Override
	public String getParamsName() {
		return this.getAlpha() + "_" + this.getEta() + "_" + this.getGamma()+ "_" + this.getNumLevels()+"_"+nIter;
	}
}

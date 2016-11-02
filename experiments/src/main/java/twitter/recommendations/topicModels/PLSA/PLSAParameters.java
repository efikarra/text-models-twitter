package twitter.recommendations.topicModels.PLSA;

import java.util.Random;

import twitter.recommendations.topicModels.TopicModelParams;

public class PLSAParameters extends TopicModelParams{

	private int requestedRank;
	private double minimumChange;
	private Random random;
	public PLSAParameters(int requestedRank,double minimumChange,Random random,int nIter) {
		super(nIter);
		this.requestedRank=requestedRank;
		this.minimumChange=minimumChange;
		this.random=random;
	}
	public int getRequestedRank() {
		return requestedRank;
	}
	public void setRequestedRank(int requestedRank) {
		this.requestedRank = requestedRank;
	}
	public double getMinimumChange() {
		return minimumChange;
	}
	public void setMinimumChange(double minimumChange) {
		this.minimumChange = minimumChange;
	}
	public Random getRandom() {
		return random;
	}
	public void setRandom(Random random) {
		this.random = random;
	}
	@Override
	public String getParamsName() {
		return this.getMinimumChange()+"_"+this.getRequestedRank()+"_"+nIter;
	}
	
}

package twitter.recommendations.topicModels.BTM;

import twitter.recommendations.topicModels.TopicModelParams;

public class BTMParameters extends TopicModelParams{
	private int K;	
	private int window;
	private int save_step ;
	private double alpha ;	
	private double beta;
	public BTMParameters(int K,int n_iter,int save_step, double alpha,double beta,int window) {
		super(n_iter);
		this.K=K;
		this.save_step=save_step;
		this.alpha=alpha;
		this.beta=beta;
		this.window=window;
	}
	public BTMParameters(int K,int n_iter,double beta,int window) {
		this(K,n_iter,n_iter+1,(double) 50 / (double) K,beta,window);
	}
	public int getK() {
		return K;
	}
	public void setK(int k) {
		K = k;
	}
	public int getSave_step() {
		return save_step;
	}
	public void setSave_step(int save_step) {
		this.save_step = save_step;
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
	public int getWindow() {
		return window;
	}
	public void setWindow(int window) {
		this.window = window;
	}
	@Override
	public String getParamsName() {
		return this.getK()+"_"+this.getBeta()+"_"+this.getAlpha()+"_"+this.getWindow()+"_"+nIter;
	}
}

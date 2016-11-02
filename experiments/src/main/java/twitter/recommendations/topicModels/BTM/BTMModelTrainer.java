package twitter.recommendations.topicModels.BTM;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ooon.lzj.model.Model;

import twitter.DataStructures.Twitter.BTMTweet;
import twitter.DataStructures.Twitter.UserData;
import twitter.recommendations.topicModels.AbstractModelTrainer;

public abstract class BTMModelTrainer extends AbstractModelTrainer{
	protected Model biterm_model;
	protected String tmodel_dir;
	protected Map<String,List<BTMTweet>> trainData;
	protected Map<String,List<BTMTweet>> testData;
	protected BTMParameters param;
	public BTMModelTrainer(String tmodel_dir,List<UserData> data,BTMParameters param) {
		super(data);
		this.param=param;
		this.tmodel_dir=tmodel_dir;
		this.data=data;
		this.trainData=new HashMap<String,List<BTMTweet>>();
		this.testData=new HashMap<String,List<BTMTweet>>();
	}
	public abstract void train() throws Exception;
	public List<UserData> getData() {
		return data;
	}
	public void setData(List<UserData> data) {
		this.data = data;
	}
	public Model getBiterm_model() {
		return biterm_model;
	}
	public void setBiterm_model(Model biterm_model) {
		this.biterm_model = biterm_model;
	}
	public String getTmodel_dir() {
		return tmodel_dir;
	}
	public void setTmodel_dir(String tmodel_dir) {
		this.tmodel_dir = tmodel_dir;
	}
	public Map<String,List<BTMTweet>> getTrainData() {
		return trainData;
	}
	public void setTrainData(Map<String,List<BTMTweet>> trainData) {
		this.trainData = trainData;
	}
	public Map<String,List<BTMTweet>> getTestData() {
		return testData;
	}
	public void setTestData(Map<String,List<BTMTweet>> testData) {
		this.testData = testData;
	}
	
}

package twitter.recommendations.topicModels.LLDA;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import cc.mallet.pipe.Pipe;
import cc.mallet.pipe.SerialPipes;
import cc.mallet.pipe.TargetStringToFeatures;
import cc.mallet.pipe.TokenSequence2FeatureSequence;
import cc.mallet.topics.LabeledLDA;
import cc.mallet.topics.ParallelTopicModel;
import cc.mallet.topics.TopicInferencer;
import cc.mallet.types.Instance;
import cc.mallet.types.InstanceList;
import cc.mallet.types.TokenSequence;
import twitter.DataStructures.LabeledDocument;
import twitter.recommendations.topicModels.AbstractTopicModel;

public class LLDATopicModel extends AbstractTopicModel{
	private LabeledLDA labeledlda;
	private InstanceList instances;
	private ParallelTopicModel topicModel =null;
	private ArrayList<Pipe> pipeList;
	public LLDATopicModel(LLDAParameters params,String modelName) {
		super(params,modelName);
		this.params=params;
		labeledlda = new LabeledLDA(params.getA(), params.getB());
		pipeList = new ArrayList<Pipe>();
		pipeList.add(new TargetStringToFeatures());
		pipeList.add(new TokenSequence2FeatureSequence());
		instances = new InstanceList(new SerialPipes(pipeList));
	}

	public void trainLabeledModel(List<LabeledDocument> data) throws IOException {

		for(int i=0;i<data.size();i++){
				
				TokenSequence tseq=new TokenSequence();
				tseq.addAll(data.get(i).getText().split("\\s+"));
				instances.addThruPipe(new Instance(tseq,StringUtils.join(data.get(i).getLabels(), " "),"train tweet "+i,null));
	}
		labeledlda.addInstances(instances);
		labeledlda.setNumIterations(params.getnIter());
		labeledlda.estimate();
		topicModel = new ParallelTopicModel(labeledlda.getTopicAlphabet(), ((LLDAParameters)params).getA() * labeledlda.getTopicAlphabet().size(), ((LLDAParameters)params).getB());
		topicModel.data = labeledlda.getData();
		topicModel.alphabet = labeledlda.getAlphabet();
		topicModel.numTypes = labeledlda.getAlphabet().size();
		topicModel.betaSum = ((LLDAParameters)params).getB()*labeledlda.getAlphabet().size();
		topicModel.buildInitialTypeTopicCounts();
		for (int i = 0; i < data.size(); i++) {
				topicProbabilities.put(data.get(i).getId(), topicModel.getTopicProbabilities(i));
			}
	}


	public double[] modelDocument(LabeledDocument document) {
		TokenSequence tseq=new TokenSequence();
		tseq.addAll(document.getText().split("\\s+"));
		InstanceList testing = new InstanceList(instances.getPipe());
		testing.addThruPipe(new Instance(tseq,StringUtils.join(document.getLabels(), " "),"test tweet",null));
		
		
		TopicInferencer inferencer = topicModel.getInferencer();
		return inferencer.getSampledDistribution(testing.get(0), params.getnIter()/2, 1, 5);
	}

}

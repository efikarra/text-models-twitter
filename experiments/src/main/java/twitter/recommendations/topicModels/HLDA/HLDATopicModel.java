package twitter.recommendations.topicModels.HLDA;

import java.util.ArrayList;
import java.util.List;

import cc.mallet.pipe.Pipe;
import cc.mallet.pipe.SerialPipes;
import cc.mallet.pipe.TokenSequence2FeatureSequence;
import cc.mallet.topics.HierarchicalLDA;
import cc.mallet.topics.HierarchicalLDAInferencer;
import cc.mallet.types.Instance;
import cc.mallet.types.InstanceList;
import cc.mallet.types.TokenSequence;
import cc.mallet.util.Randoms;
import twitter.DataStructures.TrainingDocument;
import twitter.recommendations.topicModels.UnsupTopicModel;

public class HLDATopicModel extends UnsupTopicModel {
	private HierarchicalLDA hlda;
	private InstanceList instances;
	private HierarchicalLDAInferencer inferencer;
	
	public HLDATopicModel(HLDAParameters params,String modelName) {
		super(params,modelName);
		hlda = new HierarchicalLDA();
		hlda.setAlpha(params.getAlpha());
		hlda.setGamma(params.getGamma());
		hlda.setEta(params.getEta());

		ArrayList<Pipe> pipeList = new ArrayList<Pipe>();
		pipeList.add(new TokenSequence2FeatureSequence());
		instances = new InstanceList(new SerialPipes(pipeList));
	}
	@Override
	public void trainModel(List<TrainingDocument> documents) {
		for(TrainingDocument doc:documents){
			
			TokenSequence tseq=new TokenSequence();
			tseq.addAll(doc.getText().split("\\s+"));
			instances.addThruPipe(new Instance(tseq,null,null,null));
		}
		hlda.initialize(instances, instances, ((HLDAParameters)params).getNumLevels(), new Randoms());
		try {
			hlda.estimate(params.getnIter());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		inferencer=new HierarchicalLDAInferencer(hlda);
		for (int i = 0; i < documents.size(); i++) {
			topicProbabilities.put(documents.get(i).getId(), inferencer.getTopicDistribution(i));
		}
	}
@Override
	public double[] modelDocument(String document) {

		TokenSequence tseq=new TokenSequence();
		tseq.addAll(document.split("\\s+"));
		
		InstanceList testing = new InstanceList(instances.getPipe());
		testing.addThruPipe(new Instance(tseq, null, "new instance", null));
		return inferencer.getSampledDistribution2(testing.get(0), params.getnIter()/2, 1, 5);
	}

}

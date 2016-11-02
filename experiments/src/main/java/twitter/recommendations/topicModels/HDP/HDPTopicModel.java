package twitter.recommendations.topicModels.HDP;

import java.util.ArrayList;
import java.util.List;

import cc.mallet.pipe.Pipe;
import cc.mallet.pipe.SerialPipes;
import cc.mallet.pipe.TokenSequence2FeatureSequence;
import cc.mallet.topics.HDP;
import cc.mallet.topics.HDPInferencer;
import cc.mallet.types.Instance;
import cc.mallet.types.InstanceList;
import cc.mallet.types.TokenSequence;
import twitter.DataStructures.TrainingDocument;
import twitter.recommendations.topicModels.UnsupTopicModel;

public class HDPTopicModel extends UnsupTopicModel {

	private HDP hdp;
	private InstanceList instances;
	private HDPInferencer inferencer;

	public HDPTopicModel(HDPParameters params,String modelName) {
		super(params,modelName);
		hdp = new HDP(params.getAlpha(), params.getBeta(), params.getGamma(), params.getInitialK());
		ArrayList<Pipe> pipeList = new ArrayList<Pipe>();
		pipeList.add(new TokenSequence2FeatureSequence());
		instances = new InstanceList(new SerialPipes(pipeList));
	}

	@Override
	public void trainModel(List<TrainingDocument> documents) {

		// instances.addThruPipe(new
		// StringArrayIterator((String[])userProfile.getDocuments().toArray()));
		// // data, label, name fields
		for (TrainingDocument doc : documents) {

			TokenSequence tseq = new TokenSequence();
			tseq.addAll(doc.getText().split("\\s+"));
			instances.addThruPipe(new Instance(tseq, null, null, null));
		}
		// SerializationUtilities.storeSerializedObject(instances.getPipe(),
		// "/home/efikarra/Twitter/experiments/processing/lda-100-1000-ins");
		// SerializationUtilities.storeSerializedObject(instances.getPipe(),
		// "/home/efikarra/Twitter/experiments/models-backup/lda/"+modelName+"-ins");

		hdp.initialize(instances);
		hdp.estimate(params.getnIter());
		inferencer=hdp.getInferencer();
		for (int i = 0; i < documents.size(); i++) {
			// double[] d=lda.getTopicProbabilities(i);
			topicProbabilities.put(documents.get(i).getId(), hdp.topicDistribution(i));
		}
		// lda.write(new
		// File("/home/efikarra/Twitter/experiments/models-backup/lda/"+modelName+".bin"));
		// lda.write(new
		// File("/home/efikarra/Twitter/experiments/processing/lda-100-1000.bin"));

	}

	@Override
	public double[] modelDocument(String document) {
		TokenSequence tseq = new TokenSequence();
		tseq.addAll(document.split("\\s+"));

		InstanceList testing = new InstanceList(instances.getPipe());
		testing.addThruPipe(new Instance(tseq, null, "new instance", null));
		inferencer.setInstance(testing);
		inferencer.estimate(params.getnIter()/2);
		return inferencer.topicDistribution(0);
	}
}

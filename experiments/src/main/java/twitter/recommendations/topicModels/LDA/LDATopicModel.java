package twitter.recommendations.topicModels.LDA;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cc.mallet.pipe.Pipe;
import cc.mallet.pipe.SerialPipes;
import cc.mallet.pipe.TokenSequence2FeatureSequence;
import cc.mallet.topics.ParallelTopicModel;
import cc.mallet.topics.TopicInferencer;
import cc.mallet.types.Instance;
import cc.mallet.types.InstanceList;
import cc.mallet.types.TokenSequence;
import twitter.DataStructures.TrainingDocument;
import twitter.recommendations.topicModels.UnsupTopicModel;

public class LDATopicModel extends UnsupTopicModel {

	private ParallelTopicModel lda;
	private InstanceList instances;

	public LDATopicModel(LDAParameters params, String modelName) {
		super(params, modelName);
		this.params = params;
		lda = new ParallelTopicModel(params.getLatentTopics(), params.getA(), params.getB());
		ArrayList<Pipe> pipeList = new ArrayList<Pipe>();
		// pipeList.add(new CharSequence2TokenSequence());
		pipeList.add(new TokenSequence2FeatureSequence());
		instances = new InstanceList(new SerialPipes(pipeList));
	}

	@Override
	public void trainModel(List<TrainingDocument> documents) {

		for (TrainingDocument doc : documents) {

			TokenSequence tseq = new TokenSequence();
			tseq.addAll(doc.getText().split("\\s+"));
			instances.addThruPipe(new Instance(tseq, null, null, null));
		}
		lda.addInstances(instances);
		lda.setNumThreads(6);
		lda.setNumIterations(params.getnIter());
		try {
			lda.estimate();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (int i = 0; i < documents.size(); i++) {
			// double[] d=lda.getTopicProbabilities(i);
			topicProbabilities.put(documents.get(i).getId(), lda.getTopicProbabilities(i));
		}
	}

	@Override
	public double[] modelDocument(String document) {
		TokenSequence tseq = new TokenSequence();
		tseq.addAll(document.split("\\s+"));

		InstanceList testing = new InstanceList(instances.getPipe());
		testing.addThruPipe(new Instance(tseq, null, "new instance", null));
		TopicInferencer inferencer = lda.getInferencer();
		return inferencer.getSampledDistribution(testing.get(0), params.getnIter() / 2, 1, 5);
	}

}

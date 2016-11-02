package twitter.recommendations.topicModels.PLSA;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gov.sandia.cognition.algorithm.event.IterationStartReporter;
import gov.sandia.cognition.math.matrix.SparseVectorFactory;
import gov.sandia.cognition.math.matrix.Vector;
import gov.sandia.cognition.math.matrix.VectorFactory;
import gov.sandia.cognition.math.matrix.mtj.SparseMatrixFactoryMTJ;
import gov.sandia.cognition.text.term.DefaultTermIndex;
import gov.sandia.cognition.text.term.Term;
import gov.sandia.cognition.text.term.TermOccurrence;
import gov.sandia.cognition.text.token.DefaultToken;
import gov.sandia.cognition.text.token.Token;
import gov.sandia.cognition.text.topic.ProbabilisticLatentSemanticAnalysis;
import gov.sandia.cognition.text.topic.ProbabilisticLatentSemanticAnalysis.Result;
import twitter.DataStructures.TrainingDocument;
import twitter.recommendations.topicModels.UnsupTopicModel;

public class PLSATopicModel extends UnsupTopicModel {
	private ProbabilisticLatentSemanticAnalysis plsa;
	private Result result;
	private final DefaultTermIndex termIndex;
	private VectorFactory vectorFactory;
	private Map<Integer,List<Token>> maptokens;
	public PLSATopicModel(PLSAParameters params,String modelName) {
		super(params,modelName);
		this.params=params;
		vectorFactory=SparseVectorFactory.getDefault();
		termIndex = new DefaultTermIndex();
		plsa=new ProbabilisticLatentSemanticAnalysis(params.getRequestedRank(),params.getMinimumChange(),params.getRandom());
		plsa.setVectorFactory(vectorFactory);
		plsa.setMatrixFactory(new SparseMatrixFactoryMTJ());
		plsa.setMaxIterations(params.getnIter());
	}
	@Override
	public void trainModel(List<TrainingDocument> documents) {
		 maptokens=new HashMap<Integer,List<Token>>();
		
		for(TrainingDocument document:documents){
			List<Token> tokensList=new ArrayList<Token>();
			String[] tokens=document.getText().split("\\s+");
			for(int i=0;i<tokens.length;i++){
				tokensList.add(new DefaultToken(tokens[i],0));
			}
			termIndex.addAll(tokensList);
			maptokens.put(document.getId(), tokensList);
		}
		List<Vector> vectors=new ArrayList<Vector>();
		for(TrainingDocument document:documents){
			vectors.add(convertToVector(document));
		}
	    System.out.println(modelInstName+" starts");
	    plsa.addIterativeAlgorithmListener(new IterationStartReporter());
		plsa.learn(vectors);
		
		result=plsa.getResult();
	}
	@Override
	public double[] modelDocument(String document) {
		List<Token> tokensList=new ArrayList<Token>();
		String[] tokens=document.split("\\s+");
		for(int i=0;i<tokens.length;i++){
			tokensList.add(new DefaultToken(tokens[i],0));
		}
		termIndex.addAll(tokensList);
		
		Vector resultVec=result.evaluate(convertToVector(tokensList));
		double[] resultD=new double[resultVec.getDimensionality()];
		for(int i=0;i<resultVec.getDimensionality();i++){
			resultD[i]=resultVec.get(i);
		}
		return resultD;

	}
	private Vector convertToVector(TrainingDocument document) {
		List<Token> tokensList=maptokens.get(document.getId());
		Iterable<? extends TermOccurrence> terms =tokensList;
		final Vector vector = vectorFactory.createVector(termIndex.getTermCount());
		
		//final BagOfWordsTransform bagOfWords = new BagOfWordsTransform(termIndex);
	    //final Vector vector = bagOfWords.convertToVector(terms);
		for (TermOccurrence termocc:terms)
        {
			final Term term = termocc.asTerm();
			int index=termIndex.getIndex(term);
			if(index>=0)
				vector.setElement(index, 1.0);
        }
		return vector;
	}
	private Vector convertToVector(List<Token> tokensList) {
		Iterable<? extends TermOccurrence> terms =tokensList;
		final Vector vector = vectorFactory.createVector(termIndex.getTermCount());

		for (TermOccurrence termocc:terms)
        {
			final Term term = termocc.asTerm();
			int index=termIndex.getIndex(term);
			if(index>=0)
				vector.setElement(index, 1.0);
        }
		return vector;
	}
}

package twitter;

import java.util.List;
import java.util.Random;

import twitter.recommendations.topicModels.TopicModelParams;
import twitter.recommendations.topicModels.HDP.HDPParameters;
import twitter.recommendations.topicModels.HLDA.HLDAParameters;
import twitter.recommendations.topicModels.LDA.LDAParameters;
import twitter.recommendations.topicModels.PLSA.PLSAParameters;

public class ParametersGenerator {

	public static void hldaParams(List<TopicModelParams> paramsHLDA) {
		paramsHLDA.add(new HLDAParameters(10.0, 0.5, 0.1, 3, 1000));
//		paramsHLDA.add(new HLDAParameters(10.0, 0.5, 0.5, 3, 1000));
//		paramsHLDA.add(new HLDAParameters(10.0, 1.0, 0.1, 3, 1000));
//		paramsHLDA.add(new HLDAParameters(10.0, 1.0, 0.5, 3, 1000));
		paramsHLDA.add(new HLDAParameters(20.0, 0.5, 0.5, 3, 1000));
//		paramsHLDA.add(new HLDAParameters(20.0, 0.5, 0.1, 3, 1000));
//		paramsHLDA.add(new HLDAParameters(20.0, 1.0, 0.1, 3, 1000));
//		paramsHLDA.add(new HLDAParameters(20.0, 1.0, 0.5, 3, 1000));
		
//		paramsHLDA.add(new HLDAParameters(10.0, 0.5, 0.1, 5, 1000));
//		paramsHLDA.add(new HLDAParameters(10.0, 0.5, 0.5, 5, 1000));
//		paramsHLDA.add(new HLDAParameters(10.0, 1.0, 0.1, 5, 1000));
//		paramsHLDA.add(new HLDAParameters(10.0, 1.0, 0.5, 5, 1000));
//		paramsHLDA.add(new HLDAParameters(20.0, 0.5, 0.1, 5, 1000));
//		paramsHLDA.add(new HLDAParameters(20.0, 0.5, 0.5, 5, 1000));
//		paramsHLDA.add(new HLDAParameters(20.0, 1.0, 0.1, 5, 1000));
//		paramsHLDA.add(new HLDAParameters(20.0, 1.0, 0.5, 5, 1000));
//		paramsHLDA.add(new HLDAParameters(10.0, 0.5, 0.1, 10, 1000));
//		paramsHLDA.add(new HLDAParameters(10.0, 0.5, 0.5, 10, 1000));
//		paramsHLDA.add(new HLDAParameters(10.0, 1.0, 0.1, 10, 1000));
//		paramsHLDA.add(new HLDAParameters(10.0, 1.0, 0.5, 10, 1000));
//		paramsHLDA.add(new HLDAParameters(20.0, 0.5, 0.1, 10, 1000));
//		paramsHLDA.add(new HLDAParameters(20.0, 0.5, 0.5, 10, 1000));
//		paramsHLDA.add(new HLDAParameters(20.0, 1.0, 0.1, 10, 1000));
//		paramsHLDA.add(new HLDAParameters(20.0, 1.0, 0.5, 10, 1000));
	}
	public static void ldaParams(List<TopicModelParams> paramsLDA ) {
	//	paramsLDA.add(new LDAParameters(50, 0.01,  1000));
		paramsLDA.add(new LDAParameters(100, 0.01,1000));
		paramsLDA.add(new LDAParameters(150, 0.01, 1000));
		//paramsLDA.add(new LDAParameters(200, 0.01, 1000));
//		paramsLDA.add(new LDAParameters(50, 0.01,  2000));
		//paramsLDA.add(new LDAParameters(100, 0.01,2000));
		//paramsLDA.add(new LDAParameters(150, 0.01, 2000));
		//paramsLDA.add(new LDAParameters(200, 0.01, 2000));
	}
	public static void plsaParams(List<TopicModelParams> paramsPLSA ) {
		paramsPLSA.add(new PLSAParameters(50, 1e-10, new Random(), 1000));
		paramsPLSA.add(new PLSAParameters(100, 1e-10, new Random(), 1000));
		paramsPLSA.add(new PLSAParameters(150, 1e-10, new Random(), 1000));
		paramsPLSA.add(new PLSAParameters(200, 1e-10, new Random(), 1000));
		paramsPLSA.add(new PLSAParameters(50, 1e-10, new Random(), 2000));
		paramsPLSA.add(new PLSAParameters(100, 1e-10, new Random(), 2000));
		paramsPLSA.add(new PLSAParameters(150, 1e-10, new Random(), 2000));
		paramsPLSA.add(new PLSAParameters(200, 1e-10, new Random(), 2000));
	}
	public static void hdpParams(List<TopicModelParams> paramsHDP) {
		//paramsHDP.add(new HDPParameters(1.0, 0.1, 1.0, 50,1000));
		//paramsHDP.add(new HDPParameters(1.0, 0.5, 1.0, 50,1000));
		//paramsHDP.add(new HDPParameters(1.0, 0.1, 1.0, 100,1000));
		paramsHDP.add(new HDPParameters(1.0, 0.5, 1.0, 100,1000));
		paramsHDP.add(new HDPParameters(1.0, 0.1, 1.0, 150,1000));
		//paramsHDP.add(new HDPParameters(1.0, 0.5, 1.0, 150,1000));
		//paramsHDP.add(new HDPParameters(1.0, 0.1, 1.0, 200,1000));
		//paramsHDP.add(new HDPParameters(1.0, 0.5, 1.0, 200,1000));
		
	}
}

package twitter.recommendations.experiments;

public class ExperimentsAllContent {
	public static void main(String[] args) throws Exception {
		System.out.println("tokens start ");
		ExperimentsAllBagTokens.main(args);
		System.out.println("chars start ");
		ExperimentsAllChars.main(args);
		
	}

}

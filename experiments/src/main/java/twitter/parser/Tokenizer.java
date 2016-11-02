package twitter.parser;

import java.util.List;

public class Tokenizer {

	private Twokenize twokenize;

	public Tokenizer() {
		this.twokenize = new Twokenize();
	}

	public String[] tokenize(String text) {
		text = text.toLowerCase();
		Twokenize tokenizer = new Twokenize();
		List<String> tokensList = tokenizer.tokenizeRawTweetText(text);
		return tokensList.toArray(new String[tokensList.size()]);
	}
}

package twitter.parser;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Pattern;

public class StopwordsRemoval {

	private Map<Integer, List<String>> tokens;
	private Map<String, Integer> wordsFreq;
	private Map<String, Integer> docsFreq;
	private TreeMap<Integer, List<String>> sortedWordsFreq;
	private TreeMap<Integer, List<String>> sortedDocsFreq;
	private Set<String> stopWords;

	public StopwordsRemoval(List<String> corpus) {
		tokens = new HashMap<Integer, List<String>>();
		stopWords = new HashSet<String>();
		Tokenizer tok = new Tokenizer();
		int twords=0;
		for (int i = 0; i < corpus.size(); i++) {
			String[] t = tok.tokenize(corpus.get(i));
			tokens.put(i, Arrays.asList(t));
			twords+=t.length;
		}
		System.out.println("total docs: " + tokens.size());
		System.out.println("total words: " + twords);
		corpus = null;
		System.gc();
		wordsFreq = new HashMap<String, Integer>();
		sortedWordsFreq = new TreeMap<Integer, List<String>>();
		docsFreq=new HashMap<String, Integer>();
		sortedDocsFreq=	new TreeMap<Integer, List<String>>();
	}

	public void findFilteredWords(int upperThres, int lowerThres, String savePath) {
		computeFrequencies();
		findCorpusStopWords(upperThres);
		//findInfreqWords(lowerThres);
		saveStopWords(savePath);
	}

	public void findCorpusStopWords(int upperThres) {
		System.out.println("frequencies computed");
		int corpusSize = wordsFreq.size();
		int stopWordsSize = upperThres;
		System.out.println("stop words: " + stopWordsSize);
		System.out.println("corpusSize: " + corpusSize);
		int counter = 0;
		while (counter < stopWordsSize) {
			Map.Entry<Integer, List<String>> entry = sortedWordsFreq.pollLastEntry();
			List<String> words = entry.getValue();
			if (counter + words.size() > stopWordsSize) {
				words = words.subList(0, stopWordsSize - counter);
			}
			counter += words.size();
			System.out.println("counter" + counter);
			// Iterator<String> wit=words.iterator();
			// while(wit.hasNext()){
			// if(wit.next().matches(Protected.pattern()))
			// wit.remove();
			// }
			stopWords.addAll(words);
		}
		System.out.println("stop words: " + stopWords.size());
		

	}

	public void findInfreqWords(int lowerThres) {
		System.out.println("infreq words");
		int freq = 0;
		while (freq < lowerThres) {

			Map.Entry<Integer, List<String>> entry = sortedDocsFreq.pollFirstEntry();
			freq = entry.getKey();
			List<String> words = entry.getValue();
			if (freq < lowerThres) {
				stopWords.addAll(words);
			}
		}
		System.out.println("stop words: " + stopWords.size());
		System.out.println("total words: " + tokens.size());
	}

	private void computeFrequencies() {
		System.out.println("compute frequencies ");
		for (List<String> doc : tokens.values()) {
			for (String token : doc) {
				Integer freq = wordsFreq.get(token);
				if (freq == null)
					freq = new Integer(0);
				freq++;
				wordsFreq.put(token, freq);

			}
			Set<String> tokenshash = new HashSet<String>();
			tokenshash.addAll(doc);
			for (String token : tokenshash) {
				Integer freq = docsFreq.get(token);
				if (freq == null)
					freq = new Integer(0);
				freq++;
				docsFreq.put(token, freq);
			}
		}
		for (Entry<String, Integer> entry : wordsFreq.entrySet()) {
			Integer freq = entry.getValue();
			List<String> words = sortedWordsFreq.get(freq);
			if (words == null) {
				words = new ArrayList<String>();
				words.add(entry.getKey());
				sortedWordsFreq.put(freq, words);
			} else
				words.add(entry.getKey());
		}
		for (Entry<String, Integer> entry : docsFreq.entrySet()) {
			Integer freq = entry.getValue();
			List<String> words = sortedDocsFreq.get(freq);
			if (words == null) {
				words = new ArrayList<String>();
				words.add(entry.getKey());
				sortedDocsFreq.put(freq, words);
			} else
				words.add(entry.getKey());
		}
	}

	private void saveStopWords(String savePath) {
		FileOutputStream outputStream;
		try {
			outputStream = new FileOutputStream(savePath);
			OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, "UTF-8");
			BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
			for (String word : stopWords) {
				bufferedWriter.write(word);
				bufferedWriter.newLine();
			}
			bufferedWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Set<String> getStopWords() {
		return stopWords;
	}

	public void setStopWords(Set<String> stopWords) {
		this.stopWords = stopWords;
	}
}

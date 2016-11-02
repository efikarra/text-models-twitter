package twitter.recommendations.topicModels.LLDA;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import com.twitter.Extractor;

import twitter.DataStructures.LabeledDocument;
import twitter.DataStructures.TrainingDocument;
import twitter.Utilities.Constants;

public class LLDATweetLabels implements Constants {
	private List<TrainingDocument> tweets;
	private Map<String, Integer> labelsFreqs;
	Map<String, Integer> hashtags = new HashMap<String, Integer>();
	private int latentTopics;
	private int hashThreshold;
	public LLDATweetLabels(List<TrainingDocument> tweets,int latentTopics,int hashThreshold) {
		this.tweets = tweets;
		labelsFreqs = new HashMap<String, Integer>();
		for(int i=0;i<10;i++)
			labelsFreqs.put(QUESTION_LABEL+"-"+i, 0);
		for(int i=0;i<10;i++)
			labelsFreqs.put(AT_USER_LABEL+"-"+i, 0);
		for(int i=0;i<10;i++)
			labelsFreqs.put(SMILE_LABEL+"-"+i, 0);
		for(int i=0;i<10;i++)
			labelsFreqs.put(FROWN_LABEL+"-"+i, 0);
		labelsFreqs.put(BIG_GRIN_LABEL, 0);
		for(int i=0;i<10;i++)
			labelsFreqs.put(TONGUE_LABEL+"-"+i, 0);
		labelsFreqs.put(HEART_LABEL, 0);
		labelsFreqs.put(SURPRISE_LABEL, 0);
		for(int i=0;i<10;i++)
			labelsFreqs.put(AWKWARD_LABEL+"-"+i, 0);
		labelsFreqs.put(CONFUSED_LABEL, 0);
		for(int i=0;i<10;i++)
			labelsFreqs.put(WINK_LABEL+"-"+i, 0);
		this.latentTopics=latentTopics;
		this.hashThreshold=hashThreshold;
		findHashtags();
	}

	private void findHashtags() {
		Extractor extractor = new Extractor();

		for (TrainingDocument tweet : tweets) {
			// @user label
			String tweetText=tweet.getText();
			Set<String> extractedHashtags = new HashSet<String>(extractor.extractHashtags(tweetText));
			for (String hashtag : extractedHashtags) {
				Integer freq = hashtags.get(hashtag);
				if (freq != null)
					hashtags.put(hashtag, freq + 1);
				else
					hashtags.put(hashtag, 1);
			}
		}
		for (Map.Entry<String, Integer> entry : hashtags.entrySet()) {
			if (entry.getValue() >= hashThreshold)
				labelsFreqs.put(entry.getKey(), entry.getValue());
		}

	}

	public List<LabeledDocument> labelTweets() {

		Extractor extractor = new Extractor();
		List<LabeledDocument> labeledTweets = new ArrayList<LabeledDocument>();
		for (TrainingDocument tweet : tweets) {
//			if(tweet.getThinTweet().getText().split("\\s+").length<=2)
//				continue;
			String tweetText=tweet.getText();
			LabeledDocument ltweet = new LabeledDocument(tweet.getText(),tweet.getId());
			if(latentTopics>0){
				for(int i=0;i<latentTopics;i++)
					ltweet.getLabels().add("Topic "+i);
			}
			String username = extractor.extractReplyScreenname(tweetText);
			if (username != null){
				for(int i=0;i<10;i++){
				ltweet.getLabels().add(AT_USER_LABEL+"-"+i);
				labelsFreqs.put(AT_USER_LABEL+"-"+i, labelsFreqs.get(AT_USER_LABEL+"-"+i)+1);
				}

			}
			Set<String> extractedHashtags = new HashSet<String>(extractor.extractHashtags(tweetText));
			for (String hashtag : extractedHashtags) {
				if (labelsFreqs.containsKey(hashtag))
					ltweet.getLabels().add(hashtag);
			}
			for (String emoticon : SMILE_EMOTICONS) {
				if (tweetText.contains(emoticon)){
					for(int i=0;i<10;i++){
						ltweet.getLabels().add(SMILE_LABEL+"-"+i);
						labelsFreqs.put(SMILE_LABEL+"-"+i, labelsFreqs.get(SMILE_LABEL+"-"+i)+1);}
				}
			}
			for (String emoticon : FROWN_EMOTICONS) {
				if (tweetText.contains(emoticon)){
					for(int i=0;i<10;i++){
						ltweet.getLabels().add(FROWN_LABEL+"-"+i);
						labelsFreqs.put(FROWN_LABEL+"-"+i, labelsFreqs.get(FROWN_LABEL+"-"+i)+1);}
				}
			}
			for (String emoticon : WINK_EMOTICONS) {
				if (tweetText.contains(emoticon)){
					for(int i=0;i<10;i++){
						ltweet.getLabels().add(WINK_LABEL+"-"+i);
						labelsFreqs.put(WINK_LABEL+"-"+i, labelsFreqs.get(WINK_LABEL+"-"+i)+1);}
				}
			}
			for (String emoticon : BIG_GRIN_EMOTICONS) {
				if (tweetText.contains(emoticon)){
					ltweet.getLabels().add(BIG_GRIN_LABEL);
					labelsFreqs.put(BIG_GRIN_LABEL, labelsFreqs.get(BIG_GRIN_LABEL)+1);
				}
			}
			for (String emoticon : TONGUE_EMOTICONS) {
				if (tweetText.contains(emoticon)){
					for(int i=0;i<10;i++){
						ltweet.getLabels().add(TONGUE_LABEL+"-"+i);
						labelsFreqs.put(TONGUE_LABEL+"-"+i, labelsFreqs.get(TONGUE_LABEL+"-"+i)+1);
					}
				}
			}
			for (String emoticon : HEART_EMOTICONS) {
				if (tweetText.contains(emoticon)){
					ltweet.getLabels().add(HEART_LABEL);
					labelsFreqs.put(HEART_LABEL, labelsFreqs.get(HEART_LABEL)+1);
				}
			}
			for (String emoticon : SURPRISE_EMOTICONS) {
				if (tweetText.contains(emoticon)){
					ltweet.getLabels().add(SURPRISE_LABEL);
					labelsFreqs.put(SURPRISE_LABEL, labelsFreqs.get(SURPRISE_LABEL)+1);
				}
			}
			for (String emoticon : AWKWARD_EMOTICONS) {
				if (tweetText.contains(emoticon)){
					for(int i=0;i<10;i++){
						ltweet.getLabels().add(AWKWARD_LABEL+"-"+i);
						labelsFreqs.put(AWKWARD_LABEL+"-"+i, labelsFreqs.get(AWKWARD_LABEL+"-"+i)+1);
					}
				}
			}
			for (String emoticon : CONFUSED_EMOTICONS) {
				if (tweetText.contains(emoticon)){
					ltweet.getLabels().add(CONFUSED_LABEL);
					labelsFreqs.put(CONFUSED_LABEL, labelsFreqs.get(CONFUSED_LABEL)+1);
				}
			}
			if (tweetText.contains(QUESTION_LABEL)){
				for(int i=0;i<10;i++){
					ltweet.getLabels().add(QUESTION_LABEL+"-"+i);
					labelsFreqs.put(QUESTION_LABEL+"-"+i, labelsFreqs.get(QUESTION_LABEL+"-"+i)+1);
				}
			}
			if(!ltweet.getLabels().isEmpty())
				labeledTweets.add(ltweet);
		}
		//System.out.println("lsize"+labelsFreqs.size());
//		TreeMap<String,Integer> tmap=new TreeMap<String,Integer>(labelsFreqs);
//		while(!tmap.isEmpty()){
//			Entry<String,Integer> entry=tmap.pollFirstEntry();
//			System.out.println(entry.getKey()+" "+entry.getValue());
//		}
		return labeledTweets;
	}

	public LabeledDocument labelNewTweet(TrainingDocument tweet) {
		LabeledDocument ltweet = new LabeledDocument(tweet.getText(),tweet.getId());
		Extractor extractor = new Extractor();
		String tweetText = tweet.getText();
		String username = extractor.extractReplyScreenname(tweetText);
		if (username != null)
			ltweet.getLabels().add(AT_USER_LABEL);
		if(latentTopics>0){
			for(int i=0;i<latentTopics;i++)
				ltweet.getLabels().add("Topic "+i);
		}
		Set<String> extractedHashtags = new HashSet<String>(extractor.extractHashtags(tweetText));
		for (String hashtag : extractedHashtags) {
			if (labelsFreqs.containsKey(hashtag))
				ltweet.getLabels().add(hashtag);
		}
		for (String emoticon : SMILE_EMOTICONS) {
			if (tweetText.contains(emoticon))
				ltweet.getLabels().add(SMILE_LABEL);
		}
		for (String emoticon : FROWN_EMOTICONS) {
			if (tweetText.contains(emoticon))
				ltweet.getLabels().add(FROWN_LABEL);
		}
		for (String emoticon : WINK_EMOTICONS) {
			if (tweetText.contains(emoticon))
				ltweet.getLabels().add(WINK_LABEL);
		}
		for (String emoticon : BIG_GRIN_EMOTICONS) {
			if (tweetText.contains(emoticon))
				ltweet.getLabels().add(BIG_GRIN_LABEL);
		}
		for (String emoticon : TONGUE_EMOTICONS) {
			if (tweetText.contains(emoticon))
				ltweet.getLabels().add(SMILE_LABEL);
		}
		for (String emoticon : HEART_EMOTICONS) {
			if (tweetText.contains(emoticon))
				ltweet.getLabels().add(HEART_LABEL);
		}
		for (String emoticon : SURPRISE_EMOTICONS) {
			if (tweetText.contains(emoticon))
				ltweet.getLabels().add(SURPRISE_LABEL);
		}
		for (String emoticon : AWKWARD_EMOTICONS) {
			if (tweetText.contains(emoticon))
				ltweet.getLabels().add(AWKWARD_LABEL);
		}
		for (String emoticon : CONFUSED_EMOTICONS) {
			if (tweetText.contains(emoticon))
				ltweet.getLabels().add(CONFUSED_LABEL);
		}
		if (tweetText.contains(QUESTION_LABEL))
			ltweet.getLabels().add(QUESTION_LABEL);

		return ltweet;
	}
	public LabeledDocument labelNewTweet(String tweet,int id) {
		LabeledDocument ltweet = new LabeledDocument(tweet,id);
		Extractor extractor = new Extractor();
		String username = extractor.extractReplyScreenname(tweet);
		if (username != null)
			ltweet.getLabels().add(AT_USER_LABEL);
		
		Set<String> extractedHashtags = new HashSet<String>(extractor.extractHashtags(tweet));
		for (String hashtag : extractedHashtags) {
			if (labelsFreqs.containsKey(hashtag))
				ltweet.getLabels().add(hashtag);
		}
		for (String emoticon : SMILE_EMOTICONS) {
			if (tweet.contains(emoticon))
				ltweet.getLabels().add(SMILE_LABEL);
		}
		for (String emoticon : FROWN_EMOTICONS) {
			if (tweet.contains(emoticon))
				ltweet.getLabels().add(FROWN_LABEL);
		}
		for (String emoticon : WINK_EMOTICONS) {
			if (tweet.contains(emoticon))
				ltweet.getLabels().add(WINK_LABEL);
		}
		for (String emoticon : BIG_GRIN_EMOTICONS) {
			if (tweet.contains(emoticon))
				ltweet.getLabels().add(BIG_GRIN_LABEL);
		}
		for (String emoticon : TONGUE_EMOTICONS) {
			if (tweet.contains(emoticon))
				ltweet.getLabels().add(SMILE_LABEL);
		}
		for (String emoticon : HEART_EMOTICONS) {
			if (tweet.contains(emoticon))
				ltweet.getLabels().add(HEART_LABEL);
		}
		for (String emoticon : SURPRISE_EMOTICONS) {
			if (tweet.contains(emoticon))
				ltweet.getLabels().add(SURPRISE_LABEL);
		}
		for (String emoticon : AWKWARD_EMOTICONS) {
			if (tweet.contains(emoticon))
				ltweet.getLabels().add(AWKWARD_LABEL);
		}
		for (String emoticon : CONFUSED_EMOTICONS) {
			if (tweet.contains(emoticon))
				ltweet.getLabels().add(CONFUSED_LABEL);
		}
		if (tweet.contains(QUESTION_LABEL))
			ltweet.getLabels().add(QUESTION_LABEL);

		return ltweet;
	}
}

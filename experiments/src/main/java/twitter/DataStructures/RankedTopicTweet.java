package twitter.DataStructures;

public class RankedTopicTweet implements Comparable{
	 private double similarity;
	 private TopicTweet topicTweet;

	    public RankedTopicTweet(TopicTweet tw) {
	        this.similarity = 0.0;
	        topicTweet = tw;
	    }

	    public int compareTo(Object o) {
	    	RankedTopicTweet otherTweet = (RankedTopicTweet) o;

	        return new Double(otherTweet.similarity).compareTo(this.similarity);
	    }

		public double getSimilarity() {
			return similarity;
		}

		public void setSimilarity(double similarity) {
			this.similarity = similarity;
		}

		public TopicTweet getTopicTweet() {
			return topicTweet;
		}

		public void setTopicTweet(TopicTweet topicTweet) {
			this.topicTweet = topicTweet;
		}

	   
}

package twitter.DataStructures.Twitter;

public class RankedTweet implements Comparable {

    private double similarity;
    private final CombinedTweet combinedTweet;

    public RankedTweet(CombinedTweet tw) {
        similarity = 0.0;
        combinedTweet = tw;
    }

    public int compareTo(Object o) {
        RankedTweet otherTweet = (RankedTweet) o;
        int comp=new Double(otherTweet.similarity).compareTo(this.similarity);
        if(comp==0)
        	comp=new Long(this.combinedTweet.getTweetEvent().getThinTweet().getTimeStamp()).compareTo(otherTweet.getCombinedTweet().getTweetEvent().getThinTweet().getTimeStamp());
        return comp;
    }

    public double getSimilarity() {
        return similarity;
    }

    public CombinedTweet getCombinedTweet() {
        return combinedTweet;
    }

    public void setSimilarity(double similarity) {
        this.similarity = similarity;
    }
}
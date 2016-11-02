package twitter.DataStructures;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import twitter.Utilities.Constants;

public class ReTweet extends ThinTweet implements Serializable{
	static final long serialVersionUID = 111444L;
	    List<String> retweetedUsers=new ArrayList<String>();
	    public ReTweet(long timeStamp, String text,List<String> retweetedUsers) {
	    	super(timeStamp,text);
	        this.retweetedUsers=retweetedUsers;
	    }
	    public int compareTo(Object o) {
	        ThinTweet otherTTweet = (ThinTweet) o;
	        return new Long(timeStamp).compareTo(otherTTweet.getTimeStamp());
	    }

	    @Override
		public String toString() {
			String tweet=Constants.TIMESTAMP_PREFIX+this.timeStamp;
			tweet+="\n";
			tweet+=Constants.TWEET_PREFIX+this.text;
			return tweet;
		}
	    public static void main (String[] args) throws Exception {
	        ThinTweet tt1 = new ThinTweet(99, null);
	        ThinTweet tt2 = new ThinTweet(100, null);
	        ArrayList<ThinTweet> tweets = new ArrayList<ThinTweet>();
	        tweets.add(tt1);
	        tweets.add(tt2);
	        Collections.sort(tweets);
	        for (ThinTweet tweet : tweets) {
	            System.out.println(tweet.getTimeStamp());
	        }
	    }

		public List<String> getRetweetedUsers() {
			return retweetedUsers;
		}

		public void setRetweetedUsers(List<String> retweetedUsers) {
			this.retweetedUsers = retweetedUsers;
		}
}

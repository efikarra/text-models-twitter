package twitter.DataStructures.Twitter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 *
 * @author gap2
 */

public class UserActivity implements Serializable {

    private final String userName;
    private final HashMap<String,ThinUser> followees;
    private final HashMap<String,ThinUser> followers;
    private final ArrayList<ThinTweet> outgoingTweets;
    private final ArrayList<ReTweet> outgoingReTweets;

    public UserActivity(String uName, HashMap<String,ThinUser> followees,HashMap<String,ThinUser> followers) {
        this.followees = followees;
        this.followers = followers;
        outgoingTweets = new ArrayList<ThinTweet>();
        outgoingReTweets=new ArrayList<ReTweet>();
        userName = uName;
    }

	public String getUserName() {
		return userName;
	}

	public HashMap<String, ThinUser> getFollowees() {
		return followees;
	}

	public HashMap<String, ThinUser> getFollowers() {
		return followers;
	}

	public ArrayList<ThinTweet> getOutgoingTweets() {
		return outgoingTweets;
	}

	public ArrayList<ReTweet> getOutgoingReTweets() {
		return outgoingReTweets;
	}

}
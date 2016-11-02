package twitter.Utilities.Twitter;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import twitter.DataStructures.Twitter.TweetEvent;
import twitter.Utilities.Constants;
import twitter.Utilities.StringUtilities;

/**
 *
 * @author gap2
 */

public class TwitterUtilities implements Constants {

	public static String getOriginalTweetFromRetweet(String tweet) {
		Pattern p = Pattern.compile("(?i)\\s\\((retweet|from|via)((\\s@\\w+)+)\\)");
		Matcher m = p.matcher(tweet);
		//System.out.println(tweet);
		tweet=tweet.replaceAll(p.pattern(), "");
//		Pattern p2 = Pattern.compile("\\w*\\W*RT((?:\\b\\W*@\\w+)+):?");
//		Matcher m2 = p2.matcher(tweet);
//		tweet=tweet.replaceAll(p2.pattern(), "");
		
		try {
			
			tweet=tweet.substring(
					tweet.indexOf(" ", tweet.lastIndexOf(RETWEET_PREFIX)
							+ RETWEET_PREFIX.length())).trim();
			//System.out.println(tweet.trim());
			//return tweet.trim();
		} catch (Exception exception) {
			try {
				return tweet.substring(tweet.indexOf(RETWEET_PREFIX)
						+ RETWEET_PREFIX.length(),
						tweet.lastIndexOf(RETWEET_PREFIX));
			} catch (Exception innerException) {
				try {
					String originalTweet = tweet.substring(
							tweet.indexOf(":",
									tweet.lastIndexOf(RETWEET_PREFIX)
											+ RETWEET_PREFIX.length()) + 1)
							.trim();
					if (originalTweet.length() == 0) {
						throw new Exception();
					}

					return originalTweet;
				} catch (Exception thirdLayerException) {
					try {
						return tweet
								.substring(0, tweet.indexOf(RETWEET_PREFIX))
								.trim();
					} catch (Exception fourthLayerException) {
						System.err.println(tweet);
					}
				}
			}
		}
		// Pattern p =
		// Pattern.compile("(RT|retweet|from|via)((?:\\b\\W*@\\w+)+)");
		// Matcher m = p.matcher(tweet);
		// m.replaceAll("");
		return tweet;
	}

	public static long getTimestampFromTweet(String line) throws ParseException {
		String dateString = line.substring(1).trim();
		Date date = DEFAULT_DATE_FORMAT.parse(dateString);

		return date.getTime();
	}

	public static String getTextFromTweet(String line) {
		String textString = line.substring(1).trim();

		return textString;
	}

	public static String getUserFromTweet(String line) {
		// System.out.println(line);
		// String userString = line.substring(1).trim();
		// int index = userString.indexOf(TWITTER_PREFIX);
		// String user = userString;
		// if (index != -1) {
		// user = userString.substring(index + TWITTER_PREFIX.length());
		// }
		// System.out.println(user.trim());
		String[] user_url = line.split("/");
		String user = user_url[user_url.length - 1].trim();
		return user.trim();
	}

	private static boolean isValidHashTag(String hashTag) {
		int noOfCharacters = hashTag.length();

		if (noOfCharacters < 2) {
			return false;
		}

		char[] characters = hashTag.toCharArray();
		for (int i = 0; i < noOfCharacters; i++) {
			if (Character.isLetter(characters[i])) {
				return true;
			}
		}

		return false;
	}

	private static boolean isValidMention(String mention) {
		int noOfCharacters = mention.length();

		if (noOfCharacters < 2) {
			return false;
		}

		char[] characters = mention.toCharArray();
		for (int i = 0; i < noOfCharacters; i++) {
			if (Character.isLetter(characters[i])) {
				return true;
			}
		}

		return false;
	}

	private static int getHashTagFromSubtweet(int startingPoint,
			ArrayList<String> hashTags, String tweet) {
		if (tweet.length() <= startingPoint + 1) {
			return -1;
		}

		int beginOfHashTag = tweet.indexOf(HASH_TAG, startingPoint);
		if (beginOfHashTag < 0) {
			return -1;
		}

		int endOfHashTag = tweet.indexOf(" ", beginOfHashTag);
		String hashTag = null;
		if (endOfHashTag == -1) {
			hashTag = tweet.substring(beginOfHashTag);
			endOfHashTag = tweet.length() - 1;
		} else {
			hashTag = tweet.substring(beginOfHashTag, endOfHashTag);
		}

		if (hashTag.indexOf(HASH_TAG, 1) != -1) {
			String[] tokens = hashTag.split(HASH_TAG);
			for (String token : tokens) {
				if (0 < token.trim().length() && isValidHashTag(token)) {
					String topicLabel = StringUtilities.advancedTrim(HASH_TAG
							+ token);
					// hashTags.add(topicLabel.toLowerCase().trim());
					hashTags.add(topicLabel.trim());
				}
			}
		} else if (isValidHashTag(hashTag)) {
			String topicLabel = StringUtilities.advancedTrim(hashTag);
			// hashTags.add(topicLabel.toLowerCase().trim());
			hashTags.add(topicLabel.trim());
		}

		return endOfHashTag;
	}

	public static HashSet<String> getDistinctHashTagsFromTweet(String tweet) {
		ArrayList<String> hashTags = getHashTagsFromTweet(tweet);
		if (hashTags == null) {
			return null;
		}

		HashSet<String> distinctHashTags = new HashSet<String>(
				2 * hashTags.size());
		distinctHashTags.addAll(hashTags);

		return distinctHashTags;
	}

	public static ArrayList<String> getHashTagsFromTweet(String tweet) {
		if (!tweet.contains(HASH_TAG)) {
			return null;
		}

		ArrayList<String> hashTags = new ArrayList<String>();
		int startingPoint = 0;
		do {
			startingPoint = getHashTagFromSubtweet(startingPoint, hashTags,
					tweet);
		} while (0 < startingPoint);

		return hashTags;
	}

	private static int getMentionsFromSubtweet(int startingPoint,
			ArrayList<String> usernames, String tweet) {
		if (tweet.length() <= startingPoint + 1) {
			return -1;
		}

		int beginOfMention = tweet.indexOf(PAPAKI, startingPoint);
		if (beginOfMention < 0) {
			return -1;
		}

		int endOfMention = tweet.indexOf(" ", beginOfMention);
		String mention = null;
		if (endOfMention == -1) {
			mention = tweet.substring(beginOfMention);
			endOfMention = tweet.length() - 1;
		} else {
			mention = tweet.substring(beginOfMention, endOfMention);
		}

		if (mention.indexOf(PAPAKI, 1) != -1) {
			String[] tokens = mention.split(PAPAKI);
			for (String token : tokens) {
				if (0 < token.trim().length() && isValidMention(token)) {
					String username = StringUtilities.advancedTrim(PAPAKI
							+ token);
					usernames.add(username.trim());
				}
			}
		} else if (isValidMention(mention)) {
			String username = StringUtilities.advancedTrim(mention);
			usernames.add(username.trim());
		}

		return endOfMention;
	}

	public static ArrayList<String> getMentionsFromTweet(String tweet) {
		if (!tweet.contains(PAPAKI)) {
			return null;
		}

		ArrayList<String> mentions = new ArrayList<String>();
		int startingPoint = 0;
		do {
			startingPoint = getMentionsFromSubtweet(startingPoint, mentions,
					tweet);
		} while (0 < startingPoint);

		return mentions;
	}

	public static int getNoOfUserRetweetsFromTweet(String tweet, String username) {
		if (!tweet.contains(RETWEET_PREFIX)) {
			return 0;
		}

		int noOfRetweets = 0;

		if (!tweet.startsWith(RETWEET_PREFIX))
			tweet = tweet.substring(tweet.indexOf(RETWEET_PREFIX)
					+ RETWEET_PREFIX.length());

		String[] subtweets = tweet.split(RETWEET_PREFIX);
		for (String subtweet : subtweets)
			if (subtweet.startsWith(username))
				noOfRetweets++;

		return noOfRetweets;
	}

	public static int getNoOfUserMentionsFromTweet(String tweet, String username) {
		if (!tweet.contains(PAPAKI)) {
			return 0;
		}

		int noOfMentions = 0;

		if (!tweet.startsWith(PAPAKI))
			tweet = tweet.substring(tweet.indexOf(PAPAKI) + PAPAKI.length());

		String[] subtweets = tweet.split(PAPAKI);
		for (String subtweet : subtweets)
			if (subtweet.startsWith(username))
				noOfMentions++;

		return noOfMentions;
	}

	private static int getRetweetsFromSubtweet(int startingPoint,
			ArrayList<String> usernames, String tweet) {
		if (tweet.length() <= startingPoint + 1) {
			return -1;
		}

		int beginOfMention = tweet.indexOf(RETWEET_PREFIX, startingPoint);
		if (beginOfMention < 0) {
			return -1;
		}
		beginOfMention += RETWEET_PREFIX.length();

		int endOfMention = tweet.indexOf(" ", beginOfMention);
		String mention = null;
		if (endOfMention == -1) {
			mention = tweet.substring(beginOfMention);
			endOfMention = tweet.length() - 1;
		} else {
			mention = tweet.substring(beginOfMention, endOfMention);
		}
		mention = StringUtilities.advancedTrim(mention);
		if (isValidMention(mention)) {
			usernames.add(mention);
		}

		return endOfMention;
	}

	public static List<String> detectRetweet(String line) {
		Pattern p = Pattern.compile("(RT|retweet|from|via)((?:\\b\\W*@\\w+)+)");
		Matcher m = p.matcher(line);
		String[] retUserNames = null;
		if (m.find()) {
			String s = m.group();
			s.replaceAll(":", " ");
			retUserNames = s.split("(RT @|via @|retweet @|from @|@)");
		}
		List<String> retweetedUsers = null;
		if (retUserNames != null) {
			retweetedUsers = new ArrayList<String>();
			retweetedUsers.addAll(Arrays.asList(retUserNames));
		}
		return retweetedUsers;
	}

	public static boolean detectRetweet2(String line) {
		Pattern p = Pattern.compile("(RT|retweet|from|via)((?:\\b\\W*@\\w+)+)");
		Matcher m = p.matcher(line);
		if (m.find()) {
			return true;
		}
		return false;
	}

	public static List<String> getRetweetsFromTweet(String tweet) {
		if (!tweet.contains(RETWEET_PREFIX)) {
			return null;
		}
		String[] users = tweet.split("(RT @|via @|retweet @|from @|@)");
		for (int i = 0; i < users.length; i++)
			users[i] = users[i].toLowerCase();
		List<String> retweetedUsers = new ArrayList<String>();
		retweetedUsers.addAll(Arrays.asList(users));
		// ArrayList<String> retweetedUsers = new ArrayList<String>();
		// int startingPoint = 0;
		// do {
		// startingPoint = getRetweetsFromSubtweet(startingPoint,
		// retweetedUsers, tweet);
		// } while (0 < startingPoint);

		return retweetedUsers;
	}

	public static long getTimestampFromTweetEvent(TweetEvent tweet) {
		String tweetDescription = tweet.toString();
		return Long.parseLong(tweetDescription.substring(
				tweetDescription.lastIndexOf(" at ") + " at ".length(),
				tweetDescription.length()));
	}

	public static ArrayList<String> getURLsFromTweet(String tweet) {
		if (!tweet.contains(URL_PREFIX_1) && !tweet.contains(URL_PREFIX_2)) {
			return null;
		}

		final ArrayList<String> urls = new ArrayList<String>();
		final String[] tokens = tweet.split(" ");
		for (int i = 0; i < tokens.length; i++) {
			if (tokens[i].contains(URL_PREFIX_1)) {
				int index = tokens[i].lastIndexOf(URL_PREFIX_1);
				urls.add(tokens[i].substring(index));
			} else if (tokens[i].contains(URL_PREFIX_2)) {
				int index = tokens[i].lastIndexOf(URL_PREFIX_2);
				urls.add(tokens[i].substring(index));
			}
		}

		return urls;
	}
	
}
package twitter.Utilities;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;

/**
 *
 * @author gap2
 */

public interface Constants {
	double FREQUENCY_THRESHOLD = 0.010;
	double TWITTER_FREQUENCY_THRESHOLD = 0.001;

	long DAY_MILISECONDS = 86400000;

	int NUMBER_OF_USERS = 15;
	int CONTAINMENT_SIMILARITY = 1;
	int NORMALIZED_VALUE_SIMILARITY = 2;
	int VALUE_SIMILARITY = 3;

	int MAXIMUM_NO_OF_INLUENCERS = 100;

	int NO_OF_FOLDS = 10;

	int N_GRAM_SIZE = 3;
	int TWEETS_THRESHOLD = 1000;

	int NO_OF_INTERVALS = 12 * 203;

	NumberFormat doubleFormat = new DecimalFormat("#0.00");
	NumberFormat highPrecisionDoubleFormat = new DecimalFormat("#0.00000");

	String CLASS_ATTRIBUTE = "class";
	String EQUAL_ATTRIBUTE = "equal";

	String HASH_TAG = "#";
	String FOLLOWEE_PREFIX = "f:";
	String FOLLOWER_PREFIX = "u:";
	String LUCENE_FIELD_NAME = "tweet";
	String PAPAKI = "@";
	String RETWEET_PREFIX = "RT @";
	String RETWEET_PREFIX_LOWER_CASE = "rt @";
	String TIMESTAMP_PREFIX = "T\t";
	String TWEET_PREFIX = "W\t";
	String TWITTER_PREFIX = "http://twitter.com/";
	String USER_PREFIX = "U\t";
	String URL_PREFIX_1 = "http://";
	String URL_PREFIX_2 = "www.";

	String[] NEGATIVE_EMOTICONS = { ":(", ":-(", "):", ") :", ": (", ")-:" };
	String[] NEGATIVE_EMOTICONS_PATTERNS = { ":\\(", ":-\\(", "\\):", "\\) :",
			": \\(", "\\)-:" };
	String[] POSITIVE_EMOTICONS = { ":)", ":-)", ": )", ":D", "=)", "( :",
			"(:", "(-:" };
	String[] POSITIVE_EMOTICONS_PATTERNS = { ":\\)", ":-\\)", ": \\)", ":D",
			"=\\)", "\\( :", "\\(:", "\\(-:" };

	 String[] SMILE_EMOTICONS={":)", ":-)", ": )", "=)", "( :", "(:", "(-:"};

	 String[] FROWN_EMOTICONS={":(", ":-(", "):", ") :", ": (", ")-:",">:[",":-c",":c",":-<",":<",":[",":-[",":{",":っC"};

	String[] WINK_EMOTICONS={";‑)" ,";)", "*-)" ,"*)", ";‑]", ";]", ";D", ";^)", ":‑,"};

	String[] BIG_GRIN_EMOTICONS={":-D" ,"8-D", "8D",":D", "x-D", "xD", "X-D", "XD", "=-D" ,"=D", "=‑3" ,"=3" ,"B^D"};

	String[] TONGUE_EMOTICONS={">:P", ":‑P", ":P" ,"X‑P", "x‑p" ,"xp" ,"XP", ":‑p" ,":p" ,"=p" ,":‑Þ" ,":Þ" ,":þ" ,":‑þ", ":‑b" ,":b" ,"d:"};

	 String[] HEART_EMOTICONS={"<3", "</3",":-*", ":*", ":^*"};

	  String[] SURPRISE_EMOTICONS={">:O", ":-O ", ":O" ,":-o" ,":o" ,"8‑0", "O_O", "o-o", "o_o" ,"O-O"};

	 String[] AWKWARD_EMOTICONS={ ">:\\", ">:/", ":â€‘/", ":â€‘.", ":/",
				":\\", "=/", "=\\" };

	 String[] CONFUSED_EMOTICONS={"%‑)" ,"%)","O_o", "o_O",">:\\", ">:/", ":‑/", ":‑." ,":/" ,":\\" ,"=/", "=\\", ":L" ,"=L" ,":S" ,">.<"};
	String[] QUESTION_SYMBOLS = { "?" };
	// labels

	String SMILE_LABEL = ":)";
	String FROWN_LABEL = ":(";
	String WINK_LABEL = ";)";
	String BIG_GRIN_LABEL = ":D";
	String TONGUE_LABEL = ":P";
	String HEART_LABEL = "<3";
	String SURPRISE_LABEL = ":O";
	String AWKWARD_LABEL = ":/";
	String CONFUSED_LABEL = ":S";
	String AT_USER_LABEL = "@user";
	String QUESTION_LABEL = "?";

	String TOP_LEVEL = "top";
	String BOTTOM_LEVEL = "bottom";
	String AVG_LEVEL = "avg";
	String TOP_FOLDER = "topUsers/";
	String BOTTOM_FOLDER = "bottomUsers/";
	String AVG_FOLDER = "avgUsers/";
	SimpleDateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
}

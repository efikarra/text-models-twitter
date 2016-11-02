package twitter.parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.twitter.Extractor;

import twitter.DataStructures.ParsedRetweet;
import twitter.DataStructures.ParsedThinUser;
import twitter.DataStructures.ParsedTweet;
import twitter.DataStructures.ParsedUserActivity;

public class LanguageDetectPreProcessing {
	public int ndetect=0;
	static String normalEyes = "(?iu)[:=]"; // 8 and x are eyes but cause
											// problems
	static String wink = "[;]";
	static String noseArea = "(?:|-|[^a-zA-Z0-9 ])"; // doesn't get :'-(
	static String happyMouths = "[D\\)\\]\\}]+";
	static String sadMouths = "[\\(\\[\\{]+";
	static String tongue = "[pPd3]+";
	static String otherMouths = "(?:[oO]+|[/\\\\]+|[vV]+|[Ss]+|[|]+)"; // remove
																		// forward
																		// slash
																		// if
																		// http://'s
																		// aren't
																		// cleaned

	// mouth repetition examples:
	// @aliciakeys Put it in a love song :-))
	// @hellocalyclops =))=))=)) Oh well

	static String bfLeft = "(β™¥|0|o|Β°|v|\\$|t|x|;|\\u0CA0|@|Κ�|β€Ά|γƒ»|β—•|\\^|Β¬|\\*)";
	static String bfCenter = "(?:[\\.]|[_-]+)";
	static String bfRight = "\\2";
	static String s3 = "(?:--['\"])";
	static String s4 = "(?:<|&lt;|>|&gt;)[\\._-]+(?:<|&lt;|>|&gt;)";
	static String s5 = "(?:[.][_]+[.])";
	static String basicface = "(?:(?i)" + bfLeft + bfCenter + bfRight + ")|" + s3 + "|" + s4 + "|" + s5;

	static String eeLeft = "[οΌΌ\\\\Ζ�Τ„\\(οΌ�<>;γƒ½\\-=~\\*]+";
	static String eeRight = "[\\-=\\);'\\u0022<>ΚƒοΌ‰/οΌ�γƒ�οΎ‰δΈΏβ•―Οƒγ�£Βµ~\\*]+";
	static String eeSymbol = "[^A-Za-z0-9\\s\\(\\)\\*:=-]";
	static String eastEmote = eeLeft + "(?:" + basicface + "|" + eeSymbol + ")+" + eeRight;

	public static String OR(String... parts) {
		String prefix = "(?:";
		StringBuilder sb = new StringBuilder();
		for (String s : parts) {
			sb.append(prefix);
			prefix = "|";
			sb.append(s);
		}
		sb.append(")");
		return sb.toString();
	}

	public static String emoticon = OR(
			// Standard version :) :( :] :D :P
			"(?:>|&gt;)?" + OR(normalEyes, wink) + OR(noseArea, "[Oo]")
					+ OR(tongue + "(?=\\W|$|RT|rt|Rt)", otherMouths + "(?=\\W|$|RT|rt|Rt)", sadMouths, happyMouths),

			// reversed version (: D: use positive lookbehind to remove
			// "(word):"
			// because eyes on the right side is more ambiguous with the
			// standard usage of : ;
			"(?<=(?: |^))" + OR(sadMouths, happyMouths, otherMouths) + noseArea + OR(normalEyes, wink) + "(?:<|&lt;)?",

			// inspired by
			// http://en.wikipedia.org/wiki/User:Scapler/emoticons#East_Asian_style
			eastEmote.replaceFirst("2", "1"), basicface
	// iOS 'emoji' characters (some smileys, some symbols) [\ue001-\uebbb]
	// TODO should try a big precompiled lexicon from Wikipedia, Dan Ramage told
	// me (BTO) he does this
	);
	private MyLanguageDetector detector;

	public LanguageDetectPreProcessing() {
		detector = new MyLanguageDetector();
	}

	public String cleanTweetText(String text) {
		Extractor ext = new Extractor();

		List<String> urls = ext.extractURLs(text);
		for (String url : urls)
			text = text.replace(url, "");

		List<String> hashtags = ext.extractHashtags(text);
		for (String hash : hashtags)
			text = text.replace(hash, "");

		List<String> snames = ext.extractMentionedScreennames(text);
		for (String name : snames)
			text = text.replace("@" + name, "");
		text = text.replaceAll(emoticon, "");
		return text;
	}

	public Map<String, List<Integer>> detectUserLanguage(ParsedUserActivity act) throws IOException {
		String text = "";
		Map<String, List<Integer>> langCounts = new HashMap<String, List<Integer>>();

		// System.out.println(pacts.get(j).getUserName());
		List<ParsedTweet> tweets = act.getOutgoingTweets();
		for (int i = 0; i < tweets.size(); i++) {
			text += " " + tweets.get(i).getTweet().getText();
		}
		List<ParsedRetweet> retweets = act.getOutgoingReTweets();
		for (int i = 0; i < retweets.size(); i++) {
			text += " " + retweets.get(i).getTweet().getText();
		}
		String ctext = cleanTweetText(text);
		List<String> dlangs = detector.detectLanguage(ctext);
		if (!dlangs.isEmpty()) {
			String lang = dlangs.get(0);
			List<Integer> list = langCounts.get(lang);
			if (list == null)
				list = new ArrayList<Integer>();
			list.add(tweets.size() + retweets.size());
			langCounts.put(lang, list);

		}
		ndetect+=detector.ndetect;
		return langCounts;
	}

	public Map<String, List<Integer>> detectUserFolleLanguage(ParsedUserActivity act) throws IOException {
		Map<String, List<Integer>> langCounts = new HashMap<String, List<Integer>>();
		Map<String, ParsedThinUser> followees = act.getFollowees();

		for (Map.Entry<String, ParsedThinUser> folle : followees.entrySet()) {
			String text="";
			ParsedThinUser user=folle.getValue();
			List<ParsedTweet> tweets = user.getOutgoingTweets();
			for (int i = 0; i < tweets.size(); i++) {
				text += " " + tweets.get(i).getTweet().getText();
			}
			List<ParsedRetweet> retweets = user.getOutgoingReTweets();
			for (int i = 0; i < retweets.size(); i++) {
				text += " " + retweets.get(i).getTweet().getText();
			}
			String ctext = cleanTweetText(text);
			List<String> dlangs = detector.detectLanguage(ctext);
			if (!dlangs.isEmpty()) {
				String lang = dlangs.get(0);
				List<Integer> list = langCounts.get(lang);
				if (list == null)
					list = new ArrayList<Integer>();
				list.add(tweets.size() + retweets.size());
				langCounts.put(lang, list);

			}
		}
		
		ndetect+=detector.ndetect;
		return langCounts;
	}

	public Map<String, List<Integer>> detectUserFollLanguage(ParsedUserActivity act) throws IOException {
		Map<String, List<Integer>> langCounts = new HashMap<String, List<Integer>>();
		Map<String, ParsedThinUser> followers = act.getFollowers();

		for (Map.Entry<String, ParsedThinUser> folle : followers.entrySet()) {
			String text="";
			ParsedThinUser user=folle.getValue();
			List<ParsedTweet> tweets = user.getOutgoingTweets();
			for (int i = 0; i < tweets.size(); i++) {
				text += " " + tweets.get(i).getTweet().getText();
			}
			List<ParsedRetweet> retweets = user.getOutgoingReTweets();
			for (int i = 0; i < retweets.size(); i++) {
				text += " " + retweets.get(i).getTweet().getText();
			}
			String ctext = cleanTweetText(text);
			List<String> dlangs = detector.detectLanguage(ctext);
			if (!dlangs.isEmpty()) {
				String lang = dlangs.get(0);
				List<Integer> list = langCounts.get(lang);
				if (list == null)
					list = new ArrayList<Integer>();
				list.add(tweets.size() + retweets.size());
				langCounts.put(lang, list);

			}
		}
		
		ndetect+=detector.ndetect;
		return langCounts;
	}
}

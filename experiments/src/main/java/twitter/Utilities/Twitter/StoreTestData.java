package twitter.Utilities.Twitter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import twitter.DataStructures.Twitter.TweetEvent;
import twitter.DataStructures.Twitter.UserData;
import twitter.Utilities.SerializationUtilities;

public class StoreTestData {
	public static void saveTestData(Map<String, UserData> userDataMap,String pathFolder) {
		SerializationUtilities.storeSerializedObject(userDataMap, pathFolder+"all_userdata");
		Map<String,List<String>> testStrings=new HashMap<String,List<String>>();
		for(Map.Entry<String, UserData> entry:userDataMap.entrySet()){
			List<String> texts=new ArrayList<String>();
			List<TweetEvent> testTweets=entry.getValue().getTestTweets();
			for(TweetEvent tevent:testTweets)
				texts.add(tevent.getOldText());
			testStrings.put(entry.getKey(), texts);
		}
		SerializationUtilities.storeSerializedObject(userDataMap, pathFolder+"only_string");
		
	}
	public static Map<String, UserData> loadTestUserData(String filePath) {
		Map<String, UserData> userDataMap=(Map<String, UserData>) SerializationUtilities.loadSerializedObject(filePath);
		return userDataMap;
		
	}
	public static Map<String, List<String>> loadTestDataStrings(String filePath) {
		Map<String, List<String>> userDataMap=(Map<String, List<String>>) SerializationUtilities.loadSerializedObject(filePath);
		return userDataMap;
		
	}
}

package twitter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

import twitter.DataStructures.Twitter.ParsedUserActivity;
import twitter.DataStructures.Twitter.UserActivity;
import twitter.Utilities.Twitter.GetUserActivities;
import twitter.modelUtilities.ModelsUtilities;

public class PrintUsersFinalStatistics {
	public static void main(String[] args) throws Exception {
		// String usersPath =
		// "/home/efi/master-thesis/resultUsers/usersRatioOnly/";
		// String usersPath="D:\\master\\thesis\\dataset\\FinalUsers\\";
		 String usersPath="D:\\master\\thesis\\dataset\\filteredUsersFinal\\";
		//String usersPath = "D:\\master\\thesis\\dataset\\bottomUsersdupls\\";
		// String usersPath =
		// "/home/efi/master-thesis/resultUsers/serializedUsers/";
//		GetUserActivities getActivities = new GetUserActivities(usersPath);
//		 List<UserActivity> top = getActivities.getUserActivities("bottom");
//		List<UserActivity> avg = getActivities.getUserActivities("avg");
//		List<UserActivity> avg = getActivities.getUserActivities("bottom");
//		List<UserActivity> acts = new ArrayList<UserActivity>();
//		acts.addAll(top);
		//acts.addAll(avg);
		
		GetUserActivities getActivities = new GetUserActivities(usersPath);
		 List<ParsedUserActivity> bottom = getActivities.getFilteredUserActivities("bottom");
		List<ParsedUserActivity> avg = getActivities.getFilteredUserActivities("avg");
		List<ParsedUserActivity> top = getActivities.getFilteredUserActivities("top");
		List<ParsedUserActivity> pacts = new ArrayList<ParsedUserActivity>();
		pacts.addAll(top);
		pacts.addAll(avg);
		pacts.addAll(bottom);
		TreeMap<String, ParsedUserActivity> allUsers = new TreeMap<String, ParsedUserActivity>();
		for (int i = 0; i < pacts.size(); i++) {
			allUsers.put(pacts.get(i).getUserName(),pacts.get(i));
		}
		for(Entry<String, ParsedUserActivity> entry:allUsers.entrySet()){
			ModelsUtilities.userStatisticsP(entry.getValue());
		}

	}
}

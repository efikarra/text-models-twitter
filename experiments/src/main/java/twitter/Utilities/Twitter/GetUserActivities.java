package twitter.Utilities.Twitter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import twitter.DataStructures.Twitter.ParsedUserActivity;
import twitter.DataStructures.Twitter.UserActivity;
import twitter.Utilities.Constants;
import twitter.Utilities.FileUtilities;

public class GetUserActivities {
	private ArrayList<String> resultUsersFileList;
	private String usersPath;

	public GetUserActivities(String usersPath) {
		this.usersPath = usersPath;
	}

	public List<UserActivity> getUserActivities(String userLevel) {
		List<UserActivity> userActivities = new ArrayList<UserActivity>();
		String folder = null;
		if (userLevel.equals(Constants.TOP_LEVEL))
			folder = Constants.TOP_FOLDER;
		else if (userLevel.equals(Constants.BOTTOM_LEVEL))
			folder = Constants.BOTTOM_FOLDER;
		else if (userLevel.equals(Constants.AVG_LEVEL))
			folder = Constants.AVG_FOLDER;
		else
			return null;

		resultUsersFileList = twitter.Utilities.FileUtilities.getDirectoryAbsolutePaths(usersPath + folder);
		Iterator i = resultUsersFileList.iterator();
		//int limit = 0;
		while (i.hasNext()) {
			String fileName = i.next().toString();
			//if (limit >= 150) {
				userActivities.add(loadUserActivity(fileName));
			//}
			//limit++;
		}

		return userActivities;
	}

	public List<ParsedUserActivity> getFilteredUserActivities(String userLevel) {
		String folder = null;
		if (userLevel.equals(Constants.TOP_LEVEL))
			folder = Constants.TOP_FOLDER;
		else if (userLevel.equals(Constants.BOTTOM_LEVEL))
			folder = Constants.BOTTOM_FOLDER;
		else if (userLevel.equals(Constants.AVG_LEVEL))
			folder = Constants.AVG_FOLDER;
		else
			return null;
		return (List<ParsedUserActivity>) Utilities.SerializationUtilities
				.loadSerializedObject(usersPath + folder + "filteredActivities");
	}

	public UserActivity loadUserActivity(String fileName) {
		return (UserActivity) Utilities.SerializationUtilities.loadSerializedObject(fileName);

	}
	
}

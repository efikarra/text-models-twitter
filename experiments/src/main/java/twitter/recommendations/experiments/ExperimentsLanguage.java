package twitter.recommendations.experiments;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import twitter.DataStructures.Twitter.ParsedUserActivity;
import twitter.Utilities.Twitter.GetUserActivities;
import twitter.parser.LanguageDetectPreProcessing;

public class ExperimentsLanguage {
	
	public static void main(String[] args) throws IOException {
		//String initUsersPath = "/home/efi/master-thesis/resultUsers/filteredUsersFinal100only/";
		//String initUsersPath = "D://master//thesis//dataset//filteredUsersFinal100only//";
		//String initUsersPath = "D://master//thesis//dataset//filteredUsersFinal100only//";
		String initUsersPath = "/home/efikarra/Twitter/filteredUsersFinal100only/";

		Map<String, String> usersMap = new HashMap<String, String>();
		GetUserActivities getActivities = new GetUserActivities(initUsersPath);
		List<ParsedUserActivity> pacts = new ArrayList<ParsedUserActivity>();
		List<ParsedUserActivity> bottom = getActivities.getFilteredUserActivities("bottom");
		List<ParsedUserActivity> top = getActivities.getFilteredUserActivities("top");
		List<ParsedUserActivity> avg = getActivities.getFilteredUserActivities("avg");

		pacts.addAll(bottom);
		pacts.addAll(top);
		pacts.addAll(avg);
		System.out.println(pacts.size());
		
		 Map<String, List<Integer>> languages=new HashMap<String, List<Integer>>();
		LanguageDetectPreProcessing detector=new LanguageDetectPreProcessing();
		for(int j=1;j<pacts.size();j++){
			System.out.println(j+" "+pacts.get(j).getUserName());
			Map<String, List<Integer>> langs1=detector.detectUserLanguage(pacts.get(j));
			for(Map.Entry<String,List<Integer>> entry:langs1.entrySet()){
				List<Integer> counts=languages.get(entry.getKey());
				if(counts==null)
					counts=new ArrayList<Integer>();
				counts.addAll(entry.getValue());
				languages.put(entry.getKey(), counts);
			}
			System.out.println(j+"followees");
			Map<String, List<Integer>> langs2=detector.detectUserFolleLanguage(pacts.get(j));
					for(Map.Entry<String,List<Integer>> entry:langs2.entrySet()){
						List<Integer> counts=languages.get(entry.getKey());
						if(counts==null)
							counts=new ArrayList<Integer>();
						counts.addAll(entry.getValue());
						languages.put(entry.getKey(), counts);
					}
					System.out.println(j+"followers");
					Map<String, List<Integer>> langs3=detector.detectUserFollLanguage(pacts.get(j));
					for(Map.Entry<String,List<Integer>> entry:langs3.entrySet()){
						List<Integer> counts=languages.get(entry.getKey());
						if(counts==null)
							counts=new ArrayList<Integer>();
						counts.addAll(entry.getValue());
						languages.put(entry.getKey(), counts);
					}
		}
		System.out.println("not detected: "+detector.ndetect);
		for(Map.Entry<String, List<Integer>> entry:languages.entrySet()){
			List<Integer> langs=entry.getValue();
			int total=0;
			for(Integer c:langs)
				total+=c;
			System.out.println(entry.getKey()+" "+total);
		}
			
			
	}
	
}

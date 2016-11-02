package twitter.recommendations.content;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class IDFSingleton {
	 private static IDFSingleton instance = new IDFSingleton();
	 private static HashMap<String,Map<String, Double>> documentFrequency= new HashMap<String, Map<String, Double>>();
	 private static HashSet<String> finalizedFreqs= new HashSet<String>();
  
	 private IDFSingleton(){
		   
	   }

	   //Get the only object available
	   public static IDFSingleton getInstance(){
	      return instance;
	   }

	   public void showMessage(){
	      System.out.println("Hello World!");
	   }

	public static HashMap<String, Map<String, Double>> getDocumentFrequency() {
		return documentFrequency;
	}

	public static HashSet<String> getFinalizedFreqs() {
		return finalizedFreqs;
	}
}

package twitter.Utilities;

/**
 *
 * @author mania
 */

public class StringUtilities {
    public static String advancedTrim(String myString){
        myString = myString.trim();

        if (myString.length()==1) {
            return myString;
        }
        
        if ((myString.endsWith(":"))||myString.endsWith("!")||myString.endsWith(".")
          ||myString.endsWith(",")||myString.endsWith("(")||myString.endsWith(")")
          ||myString.endsWith("+")||myString.endsWith("-")||myString.endsWith("[")
          ||myString.endsWith("]")||myString.endsWith("<")||myString.endsWith(">")
          ||myString.endsWith("\'")||myString.endsWith("\"")||myString.endsWith(";")
          ||myString.endsWith("?")||myString.endsWith("*")){
            myString = myString.substring(0, myString.length()-1);
        }

        if (myString.endsWith("\'s")) {
            myString = myString.substring(0, myString.length()-2);
        }

        return myString;
    }

    public static int countSubstringInstances(String text, String substring) {
        int count = 0;
        for (int fromIndex = 0; fromIndex > -1; count++)
            fromIndex = text.indexOf(substring, fromIndex + ((count > 0) ? 1 : 0));
        return count - 1;
    }
}
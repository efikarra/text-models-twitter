package twitter.parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.common.base.Optional;
import com.optimaize.langdetect.LanguageDetector;
import com.optimaize.langdetect.LanguageDetectorBuilder;
import com.optimaize.langdetect.i18n.LdLocale;
import com.optimaize.langdetect.ngram.NgramExtractors;
import com.optimaize.langdetect.profiles.LanguageProfile;
import com.optimaize.langdetect.profiles.LanguageProfileReader;
import com.optimaize.langdetect.text.CommonTextObjectFactories;
import com.optimaize.langdetect.text.TextObject;
import com.optimaize.langdetect.text.TextObjectFactory;

public class MyLanguageDetector {
	public int ndetect=0;
    
public List<String> detectLanguage(String text) throws IOException {
	List<LanguageProfile> languageProfiles = new LanguageProfileReader().readAllBuiltIn();

	//build language detector:
	LanguageDetector languageDetector = LanguageDetectorBuilder.create(NgramExtractors.standard())
	        .withProfiles(languageProfiles)
	        .build();

	//create a text object factory
	TextObjectFactory textObjectFactory = CommonTextObjectFactories.forDetectingOnLargeText();

	//query:
	TextObject textObject = textObjectFactory.forText(text);
	Optional<LdLocale> lang = languageDetector.detect(textObject);
	Iterator<LdLocale> languages=lang.asSet().iterator();
	if(!languages.hasNext()){
		//System.out.println("not detected");
		ndetect++;
		//System.out.println(text);

	}
	 List<String> langs=new  ArrayList<String>();
	while(languages.hasNext())
		langs.add(languages.next().getLanguage());
		
	return langs;
}
//public List<String> detectLanguage2(String text) throws IOException, LangDetectException {
//	 DetectorFactory.loadProfile("D:\\language-detection-master\\profiles\\"); 
//	 List<String> langs=new  ArrayList<String>();
//	 Detector detector = DetectorFactory.create(); detector.append(text); 
//	 List<Language> languages=detector.getProbabilities();
//	 for(int i=0;i<languages.size();i++)
//		 langs.add(languages.get(i).lang);
//	 return langs;
//}
}

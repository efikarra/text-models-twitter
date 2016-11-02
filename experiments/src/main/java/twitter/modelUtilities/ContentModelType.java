package twitter.modelUtilities;

import Models.AbstractModel;
import Models.CharacterNGrams;
import Models.GraphModel;
import Models.IDFTokenNGrams;
import Models.TokenNGrams;
import twitter.recommendations.content.models.ContentAbstractModel;
import twitter.recommendations.content.models.ContentCharacterNGrams;
import twitter.recommendations.content.models.ContentGraphModel;
import twitter.recommendations.content.models.ContentTokenNGrams;
import twitter.recommendations.content.models.TweetIDFTokenNGrams;
import twitter.recommendations.content.models.UserIDFTokenNGrams;

public class ContentModelType {
//	TOKEN_NGRAMS,IDF_TOKEN_NGRAMS,CHARACTER_NGRAMS,GRAPHS;
//	public static UserAbstractModel getUserModel(AbstractModel model,ModelInfoSource utype,UserModelType userModelType) {
//			switch (userModelType) {
//			case TOKEN_NGRAMS:
//				return new UserTokenNGrams((TokenNGrams)model,utype);
//			case CHARACTER_NGRAMS:
//				return new UserCharacterNGrams((CharacterNGrams)model,utype);
//			case IDF_TOKEN_NGRAMS:
//				return new UserGraphModel((GraphModel)model,utype);
//			default:
//				return null;
//			}
//	}
	public static ContentAbstractModel getContentModel(AbstractModel model,ModelInfoSource utype,boolean isTweetModel){
		switch (model.getModelType()) {
		case CHARACTER_BIGRAMS:
			return new ContentCharacterNGrams((CharacterNGrams)model,utype);
        case CHARACTER_BIGRAM_GRAPHS:
        	return new ContentGraphModel((GraphModel)model,utype);
        case CHARACTER_FOURGRAMS:
        	return new ContentCharacterNGrams((CharacterNGrams)model,utype);
        case CHARACTER_FOURGRAM_GRAPHS:
        	return new ContentGraphModel((GraphModel)model,utype);
        case CHARACTER_TRIGRAMS:
        	return new ContentCharacterNGrams((CharacterNGrams)model,utype);
        case CHARACTER_TRIGRAM_GRAPHS:
        	return new ContentGraphModel((GraphModel)model,utype);
        case RAW_TOKEN_BIGRAMS:
        	new ContentTokenNGrams((TokenNGrams)model,utype);
        case RAW_TOKEN_TRIGRAMS:
        	new ContentTokenNGrams((TokenNGrams)model,utype);
        case RAW_TOKEN_UNIGRAMS:
        	new ContentTokenNGrams((TokenNGrams)model,utype);
        case IDF_TOKEN_BIGRAMS:
        	if(!isTweetModel)
        		return new UserIDFTokenNGrams((IDFTokenNGrams)model,utype);
        	else
        		return new TweetIDFTokenNGrams((IDFTokenNGrams)model,utype);
        case IDF_TOKEN_TRIGRAMS:
        	if(!isTweetModel)
        		return new UserIDFTokenNGrams((IDFTokenNGrams)model,utype);
        	else
        		return new TweetIDFTokenNGrams((IDFTokenNGrams)model,utype);
        case IDF_TOKEN_UNIGRAMS:
        	if(!isTweetModel)
        		return new UserIDFTokenNGrams((IDFTokenNGrams)model,utype);
        	else
        		return new TweetIDFTokenNGrams((IDFTokenNGrams)model,utype);
        case TOKEN_BIGRAMS:
        	return new ContentTokenNGrams((TokenNGrams)model,utype);
        case TOKEN_BIGRAM_GRAPHS:
        	return new ContentGraphModel((GraphModel)model,utype);
        case TOKEN_TRIGRAMS:
        	return new ContentTokenNGrams((TokenNGrams)model,utype);
        case TOKEN_TRIGRAM_GRAPHS:
        	return new ContentGraphModel((GraphModel)model,utype);
        case TOKEN_UNIGRAMS:
        	return new ContentTokenNGrams((TokenNGrams)model,utype);
        case TOKEN_UNIGRAM_GRAPHS:
        	return new ContentGraphModel((GraphModel)model,utype);
        default:
            return null;   
		}
	}
}

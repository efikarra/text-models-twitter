package twitter.DataStructures.Twitter;

import Models.AbstractModel;
import Utilities.RepresentationModel;
import twitter.modelUtilities.ContentModelType;
import twitter.modelUtilities.ModelInfoSource;
import twitter.recommendations.content.models.ContentAbstractModel;


public class CombinedTweet {
    private double similarity;
    private int eventId;
    private final ContentAbstractModel model;
    private final TweetEvent tweetEvent;
    public CombinedTweet(RepresentationModel modelType,ModelInfoSource modelSource,String modelName, TweetEvent tw,int eventId) {
    	this.eventId = eventId;
    	this.tweetEvent = tw;
    	this.similarity = 0.0;
    	AbstractModel abmodel=RepresentationModel.getModel(modelType, modelName);
        this.model = ContentModelType.getContentModel(abmodel, modelSource, true);
        model.updateModel(tweetEvent.getThinTweet().getText().toLowerCase());
    }

    public int compareTo(Object o) {
        CombinedTweet otherCT = (CombinedTweet) o;

        return this.getTweetEvent().compareTo(otherCT.getTweetEvent());
    }

    public int getEventId() {
        return eventId;
    }

    public ContentAbstractModel getModel() {
        return model;
    }
    
    public double getSimilarity() {
        return similarity;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public void setSimilarity(double similarity) {
        this.similarity = similarity;
    }

	public TweetEvent getTweetEvent() {
		return tweetEvent;
	}

}

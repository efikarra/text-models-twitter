package twitter.DataStructures;

import java.util.ArrayList;
import java.util.List;

public class LabeledDocument {
private String text;
private List<String> labels;
private int id;

public LabeledDocument(String text,int id) {
	this.text=text;
	this.id=id;
	labels=new ArrayList<String>();
}
public String getText() {
	return text;
}
public void setTweet(String text) {
	this.text = text;
}
public List<String> getLabels() {
	return labels;
}
public void setLabels(List<String> labels) {
	this.labels = labels;
}
public int getId() {
	return id;
}
public void setId(int id) {
	this.id = id;
}

}

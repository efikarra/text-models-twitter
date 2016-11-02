package twitter.recommendations.content;

public class RoccioParams {

	private double b;
	private double c;
	public RoccioParams(double b,double c) {
		this.b=b;
		this.c=c;
	}
	public double getB() {
		return b;
	}
	public void setB(double b) {
		this.b = b;
	}
	public double getC() {
		return c;
	}
	public void setC(double c) {
		this.c = c;
	}
}

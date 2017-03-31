
public class VectorElement {
	private double weight; 
	private String term; 
	public VectorElement(double tweight, String word) {
		setWeight(tweight);
		setTerm(word);
	}
	public String getTerm() {
		return term;
	}
	public void setTerm(String term) {
		this.term = term;
	}
	public double getWeight() {
		return weight;
	}
	public void setWeight(double weight) {
		this.weight = weight;
	}

}


public class Ranking implements Comparable<Ranking> {
	private int index;
	private int bushiness;
	public Ranking(int i, int b) {
		// TODO Auto-generated constructor stub
		index = i;
		bushiness = b;
	}

	@Override
	public int compareTo(Ranking o) {
		// TODO Auto-generated method stub
		int compareBushiness = o.getBushiness();
		//descending order
		return compareBushiness - this.bushiness;
		
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public int getBushiness() {
		return bushiness;
	}

	public void setBushiness(int bushiness) {
		this.bushiness = bushiness;
	}

}

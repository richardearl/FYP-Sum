import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class BushyPath {
	//bushy path chooses paragraphs with the most links to other paragrpahs.
	//linkes with a size under a certain number will be dimissed.
	List<ArrayList<Double>> weightLists;
	List<Ranking> rankings;
	public BushyPath(List<ArrayList<Double>>wLists) {
		// TODO Auto-generated constructor stub
		weightLists = wLists;
		rankings = new ArrayList<Ranking>();
		getBushyList();
		Collections.sort(rankings);
		}
	
	public int getBushiness(ArrayList<Double> wList){
		int bushiness=0;
		for(Double d: wList){
			if(d>0.001){
				bushiness++;
			}
		}
		return bushiness;
	}
	
	public void getBushyList(){
		//returns a list of the bushiest parags
		int it=0;
		for(ArrayList<Double> a:weightLists){
			int b = getBushiness(a);
			Ranking rank = new Ranking(it, b);
			rankings.add(rank);
			it++;
		}
		
	}

	public List<ArrayList<Double>> getWeightLists() {
		return weightLists;
	}

	public void setWeightLists(List<ArrayList<Double>> weightLists) {
		this.weightLists = weightLists;
	}

	public List<Ranking> getRankings() {
		return rankings;
	}

	public void setRankings(List<Ranking> rankings) {
		this.rankings = rankings;
	}
}

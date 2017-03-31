import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


public class NaiveFreqAnalysis {
	private Map<String, Integer> textWordCounts;
	private List<String> words;
	private int i;
	public NaiveFreqAnalysis( String paragraph ,Map<String, Integer> twcs, int index) {
		// TODO Auto-generated constructor stub
		textWordCounts = twcs;
		words = Arrays.asList(paragraph.split(" "));;
		i = index;
		
	}
	public Ranking freqAnal(){
		int sum =0;
		for(String word: words){
			sum = sum + textWordCounts.get(word);
		}
		Ranking rank = new Ranking(i, sum);
		return rank;
	}

}

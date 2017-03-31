import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.lang.Math;
public class TextVector {
	//Text vector class creates a vector for each paragraph that can be contains information about how often terms appear
	private List<VectorElement> tVector;
	private String paragraph;
	private Map<String, Integer> textWordCounts;
	private Map<String, Integer> paraWordCounts;
	private Map<String,Integer> docCounts;
	private int textSize;
	private List<String> paraWords;
	private double normFac;
	public TextVector(String para,Map<String, Integer> twcs,Map<String, Integer> pwcs,int tsze, Map<String,Integer> dcounts) {
		// TODO Auto-generated constructor stub
		tVector = new ArrayList<VectorElement>(); 
		paragraph = para;
		paraWordCounts = pwcs;
		textWordCounts = twcs;
		textSize=tsze;
		docCounts = dcounts;
		//paraWords gives a list of every unique word in the para 
		paraWords = new ArrayList<String>(paraWordCounts.keySet());
		normFac = calcNormFac();
		fillVector();
	}
	public List<VectorElement> gettVector() {
		return tVector;
	}
	public void settVector(List<VectorElement> tVector) {
		this.tVector = tVector;
	}
	public double calcNormFac(){
		double sum=0;
		for(String word: paraWords){
			int tf = paraWordCounts.get(word);
			int bigN = textSize;
			//someting wrong with this... getting n > than line count
			int n = docCounts.get(word);
			double ndivn = (double) bigN/(double) n;
			double f = Math.log(ndivn);
			f=f+0.000001;
			double w = f*tf;
			sum = sum + (w*w);
		}
		double nfac = 1/(Math.sqrt(sum));
		return nfac;
	}
	public double calcWeight(String word){
		int tf = paraWordCounts.get(word);
		int bigN = textSize;
		int n = docCounts.get(word);
		double ndivn = bigN/n;
		double f = Math.log(ndivn);
		f=f+0.000001;
		double w = f*tf;
		double ans = w * normFac; 
		return ans;
		//,ust compute euclidian vector len
	}
	
	public void fillVector(){
		for(String word: paraWords){
			VectorElement v = new VectorElement(calcWeight(word),word);
			tVector.add(v);
		}
	}

}

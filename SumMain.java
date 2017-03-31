import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class SumMain {

	public static void main(String[] args) throws IOException{

		//File input and processing into a list
		Input inp = new Input();
		inp.getUserInputs();
		inp.getTextFromFile();
		//inp.printTextList();
		List<String> textList = inp.getTextList();
		//Text analysis
		int textSize = textList.size();
		int mode_of_op = inp.getMode();
		TextAnalysis ta = new TextAnalysis(textList);
		ta.textCounter();
		//List of word counts over the whole input text
		Map<String, Integer> twcs = ta.getWordcounts();
		//create a vector of all words with 0 count next to them. This is so all counts use th 
		//List of how many docs/paras a word appears in (AS FAR AS I CAN TELL THESE ARE IDENTICAL?!?CHECK THISISISIS)
		Map<String, Integer> dcs = ta.getDocWordcounts();
		System.out.println(twcs.get("Note"));
		System.out.println(dcs.get("Note"));
		List<List<VectorElement>> vList = new ArrayList<List<VectorElement>>();
		List<Ranking> naiveRankList = new ArrayList<Ranking>();
		int x =0;
		for(String paragraph : textList){
			List<String> para = new ArrayList<String>();
			para.add(paragraph);
			TextAnalysis pa = new TextAnalysis(para);
			//word count for that parag
			pa.textCounter();
			Map<String, Integer> pwcs = pa.getWordcounts();
			NaiveFreqAnalysis nfa = new NaiveFreqAnalysis(paragraph, twcs, x);
			naiveRankList.add(nfa.freqAnal());
			TextVector p = new TextVector(paragraph,twcs,pwcs,textSize,dcs);
			List<VectorElement> pvec = p.gettVector();
			vList.add(pvec);
			for(VectorElement vece: pvec){
				System.out.println("| "+vece.getTerm()+ " " + vece.getWeight()+" |");
			}
			x++;
		}
		VectorAnalysis vA = new VectorAnalysis(vList);
		System.out.println(ta.getWordCount("Caesar"));
		System.out.println(ta.getDocWordCount("Caesar"));
		List<ArrayList<Double>> weightLists = vA.getWeightLists();
		
		for(ArrayList<Double> l: weightLists){
			System.out.println("-----newlist-----");
			for(Double d: l){
				System.out.println(d);
			}
			
		}
		//Bushy path takes lists of how related each sentence is to others ands scores based on how many connections they share.
		BushyPath b = new BushyPath(weightLists);
		//
		List<Ranking> rankings = null;
		//Which Ranking system to use?
		if(mode_of_op == 1){
			rankings = b.getRankings();
		}
		else if(mode_of_op == 2){
			rankings = naiveRankList;
		}
		else{
			rankings = b.getRankings();
		}
		
		//pick the first n of the ranking list
		int n=inp.getNumlines();
		int[] sentence_numbers = new int[n];
		for(int i =0; i <n ;i++){
			int paraIndex = rankings.get(i).getIndex() ;
			sentence_numbers[i] = paraIndex;
			
		}
		Arrays.sort(sentence_numbers);
		System.out.println("~~~~~~~~~~~~~~~FINISHED SUMMARY~~~~~~~~~~~~~~~~");
		System.out.println(textList.get(0));
		for(int i =0; i <n ;i++){
			System.out.println(textList.get(sentence_numbers[i]));
		}
		System.out.println("WLIST SIZE:" + weightLists.size());
		//MaxMargRefPrep mmrp = new MaxMargRefPrep(weightLists, rankings, inp.getNumlines());
		//List<ArrayList<Double>> smWeightLists = mmrp.getSmaller_weightLists();
		//System.out.println("REDUCED LIST SIZE = "+ smWeightLists.size());
		MaxMargRef test = new MaxMargRef(weightLists, inp.getNumlines(), textSize, rankings);
		//test.printPerms();
		System.out.println(test.findMax());
		System.out.println(test.getBestPermID());
		for(boolean bEl: test.getBestPerm()){
			System.out.println(bEl);
		}
		int[] sentIDs = test.getBestPermSentIDs();

		/*for(int i: sentIDs){
			System.out.println(i);
			System.out.println(textList.get(i));
		}*/
		OutputParse o = new OutputParse(sentIDs, textList);
	}

}

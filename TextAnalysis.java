import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TextAnalysis {
	Map<String,Integer> wordcounts; 
	List<String> textList;
	Map<String,Integer> doccounts; 
	public TextAnalysis(List<String> tList) {
		// TODO Auto-generated constructor stub
		textList = tList;
		wordcounts = new HashMap<String,Integer>();
		doccounts = new HashMap<String,Integer>();

	}
	
	public Map<String, Integer> getWordcounts() {
		return wordcounts;
	}
	public Map<String, Integer> getDocWordcounts() {
		return doccounts;
	}
	public Integer getWordCount(String word) {
		return wordcounts.get(word);
	}
	public Integer getDocWordCount(String word) {
		return doccounts.get(word);
	}
	public void setWordcounts(Map<String, Integer> wordcounts) {
		this.wordcounts = wordcounts;
	}

	public Map<String,Integer> textCounter(){
		for(String sentence : textList){
			List<String> words = Arrays.asList(sentence.split(" "));
			List<String> existingWords = new ArrayList<String>();
			System.out.println("--------------------NEW SENTENCE------------------");
			for(String word : words){
				Integer amt = wordcounts.get(word);
				Integer docamt = doccounts.get(word);
				
				if(existingWords.contains(word)){
				//if this sentence has already contained this word, add one to the word count
					//System.out.println("Alread has that word!");
					if(amt == null){
						wordcounts.put(word,1);
					}
					else{
						wordcounts.put(word,amt+1);
					}
				}
				else{
					//if not this is the first occurance of this word in the sentence..
					//in that case add to existing words and add to doccounts,
					//which records how many different sentences/documents the word appears in.
					
				existingWords.add(word);
				
				if(docamt == null){
					doccounts.put(word,1);
				}
				else{
					doccounts.put(word,docamt+1);
				}
				if(amt == null){
					wordcounts.put(word,1);
				}
				else{
					wordcounts.put(word,amt+1);
				}
			
				}
				
			}
			
		}
		return wordcounts;
	}

}

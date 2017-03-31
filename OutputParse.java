import java.util.List;

public class OutputParse {
    //This class does any required post-processing
    public OutputParse(int[] bestSentIds, List<String> textList){
        for(int i: bestSentIds){
            System.out.println(i);
            String line = textList.get(i);
            String newLine = line.replaceAll("you", "most of you");
            System.out.println(newLine);
        }
    }
}

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rich on 21/03/2017.
 * This class cuts down the list of potential sentences that can be looked at when doing maxmarg ref
 * This is becuase the permutations of all sentence choices can get absolutely huge
 * (choosing 6 out of 59 is about 45 million permutations!)
 * here only the top x are selected (since the summary is unlikely to include very
 * low scoring sentences anyway). This makes calculating the mmr much more
 * reasonable
 */

public class MaxMargRefPrep {
    private int finalSize;
    private int origSize;
    private int s;
    private List<ArrayList<Double>> weightLists;
    private List<ArrayList<Double>> smaller_weightLists;
    private List<Ranking> rankList;

    public List<ArrayList<Double>> getSmaller_weightLists() {
        return smaller_weightLists;
    }

    public MaxMargRefPrep(List<ArrayList<Double>> wLists, List<Ranking> ranks, int selSent) {
        weightLists = wLists;
        origSize = weightLists.size();
        s = selSent;
        rankList =ranks;
        finalSize =calcFinalWListSize();
        System.out.println("FINAL LIST SIZE SHOULD BE:" + finalSize);
        System.out.println(fac(5));
        System.out.println("wList passed size:" + origSize);
        smaller_weightLists =reduceWeightLists();

    }
    public int fac(int x){
        if (x == 0)
            return 1;
        else
            return(fac(x-1)*x);
    }
    public int calcFinalWListSize(){
        int list_size_orig =origSize;
        System.out.println("list_size_orig "+list_size_orig);
        int chosen_size = 0;
        if(list_size_orig <= 25){
            //if size <=25 my program can handle checking every permutation
            System.out.println("List size less than 25");
            return list_size_orig;
        }
        else{
            //otherwise how many it can handle depends on the number of sentences required in the summary
            //limits to 5 mil permutations
            boolean check = true;
            int n= origSize;
            long calc_perms = 6000000;
            System.out.println("List size more than 25");
            System.out.println("Selected sentences: "+s);

            while(check){
                System.out.println(n);
                try {
                    int calc1 = n -s;
                    int calc2 = fac(calc1);
                    int calc3= fac(s);
                    //int calc4 = calc3*calc4;

                    calc_perms = fac(n) / (fac(s) * (fac((n - s))));
                    System.out.println("Calc Succ " + calc_perms);
                }
                catch(Exception e){
                    System.out.println("Calc EXCP"+ e);
                    calc_perms= 6000000;
                }
                n--;
                System.out.println(calc_perms);
                if(calc_perms < 5000000){
                    System.out.println(calc_perms + " LESS THAN TARG " +5000000);
                    chosen_size=n;
                    check= false;
                }
            }
            System.out.println("LEFT LOOP");
          return 25;
        }

    }

    public  List<ArrayList<Double>> reduceWeightLists(){
        List<ArrayList<Double>> sm_weightLists = new ArrayList<ArrayList<Double>>();
        for(int i=0; i<finalSize;i++){
            int index = rankList.get(i).getIndex();
            sm_weightLists.add(weightLists.get(index));
        }
        return sm_weightLists;
    }

}

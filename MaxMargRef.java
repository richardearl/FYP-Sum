import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;

import java.util.ArrayList;
import java.util.List;


public class MaxMargRef {
	//MaxMargRef takes a list of sentences with weights denoting how linkedd they are to other sentences.
	//It ranks sentences by how relveant they are to the whole text and by how much they relate to previous
	//selections
	//works by choosing every permutation of n selections. For each permutation, flip
	//weights in all lists that correspond to selected sentences negative
	//set all over a certain threshold to 1, set all beneath to -1*(ratio)
	//sum all lists
	//choose permutation with largest sum.
	private List<ArrayList<Double>> weightLists;
	private ArrayList<boolean[]> selectedList;
	private ArrayList<boolean[]> testSelectedList;
	private int size;
	private int selected_sents;
	private int tSize;
	private int bestPermID;
	private boolean[] bestPerm;
	private int[] bestPermSentIDs;
	private List<Ranking> rankings;
	private boolean[] allowedSelect;

	public MaxMargRef(List<ArrayList<Double>> wLists, int selSen, int textSize, List<Ranking> ranks) {
		// TODO Auto-generated constructor stub
		weightLists = wLists;
		selectedList= new ArrayList<boolean[]>();
		testSelectedList= new ArrayList<boolean[]>();
		size = wLists.size();
		selected_sents = selSen;
		tSize = textSize;
		rankings = ranks;
		allowedSelect = new boolean[textSize+1];
		//convRanks2BoolList();
		collectPerms();
		convertPermsToLongerPermList();
		selectedList = testSelectedList;
		//collectPermsTest();
		System.out.flush();
		//printAllPerms();
		System.out.println(selectedList.size());
		System.out.println("Smaller perm list size:" + testSelectedList.size());
		bestPermID = findMax();
		bestPerm = selectedList.get(bestPermID);
		bestPermSentIDs = new int[selSen];
		calcBestPermSentIDs();


	}
	public int[] getBestPermSentIDs() {
		return bestPermSentIDs;
	}

	public void calcBestPermSentIDs(){
		//takes a list of true false boolean configurations and converts to a list
		//that contains only the indexes of 'true' positions
		int count =0;
		for(int i = 0; i<bestPerm.length;i++){
			if(bestPerm[i]) {
				bestPermSentIDs[count] = i;
				count++;
			}
		}

	}

	public boolean[] getBestPerm() {
		return bestPerm;
	}

	public int getBestPermID() {
		return bestPermID;
	}
	public int findMax(){
		//method that iterates over all possible permutations and finds the one that gives the best MR value
		//double[][] wLists = {{1.0, 0, 0.2, 0.3, 0.0, 0.01},{0, 1.0, 0.2, 0.3, 0.0, 0.01},{0.1, 0, 1, 0.3, 0.0, 0.01},{0.001, 0, 0.2, 1, 0.0, 0.01},{0, 0.1, 0.1, 0.3, 1, 0.01},{0.4, 0, 0.4, 0.1, 0.0, 1}};

		double max_val= -999999;
		double ratio = 0.2;
		int selectedID = 0;
		int sentID = 0;
		for(boolean[] bList:selectedList) {
			List<ArrayList<Double>> selectedSentList = new ArrayList<ArrayList<Double>>();
			double marg_val=0;
			int count= 0;
			for (int i = 0; i < bList.length; i++) {
				if (bList[i]) {

					selectedSentList.add(weightLists.get(i));
					count++;
				}
			}
			for(ArrayList<Double> sL: selectedSentList){
				for (int i = 0; i < tSize; i++) {
					if(sL.get(i)> 0.001) {
						if (bList[i] == false) {
							//marg_val++;
							marg_val = marg_val + (sL.get(i) * (ratio));
						} else {
							marg_val = marg_val - (sL.get(i) * (1-ratio));

						}
					}

				}
			}
			if(marg_val >= max_val){
				max_val = marg_val;
				selectedID =sentID;

			}
			sentID++;
			}

		return selectedID;
	}

	public void printPerms(){
		for(boolean[] bList: selectedList){
			for(boolean b: bList){
				System.out.println(b);
			}
			System.out.println("\n");
		}


	}
	public void  collectPerms(){
		boolean[] testList = {false, false, false, false, false, false};
		//boolean[] bList = new boolean[weightLists.size()];
		boolean[] bList = new boolean[25];
		selectedList.addAll(getPermutations(0, false, bList, 0, selected_sents));
		selectedList.addAll(getPermutations(0, true, bList, 0, selected_sents));

	}

	public ArrayList<boolean[]> getPermutations(int start, boolean tf, boolean[] selectList, int count, int lim){
		if(count == lim){
			ArrayList<boolean[]> perms = new ArrayList<>();
			//only add in true case so it isnt added twice
			if(tf){perms.add(selectList);}
			return perms;
		}
		else if(start == selectList.length) {
			ArrayList<boolean[]> perms = new ArrayList<>();
			return perms;
		}
		else if(tf){
			selectList[start] = true;
			ArrayList<boolean[]> perms = new ArrayList<>();
			boolean[] selectList1 = new boolean[selectList.length];
			System.arraycopy( selectList, 0, selectList1, 0, selectList.length );
			boolean[] selectList2 = new boolean[selectList.length];
			System.arraycopy( selectList, 0, selectList2, 0, selectList.length );
			perms.addAll(getPermutations(start+1, false, selectList1, count+1,lim));
			perms.addAll(getPermutations(start+1, true, selectList2, count+1,lim));
			return perms;

		}
		else {
			ArrayList<boolean[]> perms = new ArrayList<>();
			boolean[] selectList1 = new boolean[selectList.length];
			System.arraycopy( selectList, 0, selectList1, 0, selectList.length );
			boolean[] selectList2 = new boolean[selectList.length];
			System.arraycopy( selectList, 0, selectList2, 0, selectList.length );
			perms.addAll(getPermutations(start+1, false, selectList1, count,lim));
			perms.addAll(getPermutations(start+1, true, selectList2, count,lim));
			return perms;
		}

	}

	public void convertPermsToLongerPermList(){
		//this list takes all the permutations that only consider the n best senteces
		//and converts them all to boolean lists the size of the whole text.
		ArrayList<boolean[]> fullBoolLists = new ArrayList<boolean[]>();
		for(boolean[] bList: selectedList){
			boolean[] fullBool = new boolean[size];
			for(int i =0;i<bList.length;i++){
				if(bList[i]){
					Ranking r = rankings.get(i);
					int j = r.getIndex();
					fullBool[j]=true;
				}
			}
			fullBoolLists.add(fullBool);
		}
		testSelectedList = fullBoolLists;
	}
	public void  collectPermsTest(){
		boolean[] testList = new boolean[5];
		boolean[] testAllowed = new boolean[6];
		testAllowed[4]=true;
		testAllowed[1]=true;
		testAllowed[2]=true;
		testAllowed[3]=true;
		boolean[] bList = new boolean[weightLists.size()];
		//System.out.println("allowed select len" + allowedSelect.length);
		//System.out.println("BLIST len : "+ bList.length);
		//System.out.println("Selected sents: "+ selected_sents);
		//testSelectedList.addAll(getPermutations2(0, false, bList,allowedSelect, 0, selected_sents));
		//testSelectedList.addAll(getPermutations2(0, true, bList,allowedSelect, 0, selected_sents));
		if(allowedSelect[0]) {
			System.out.println("First is ok");
			//testSelectedList.addAll(getPermutations2(0, false, testList, testAllowed, 0, 3));
			//testSelectedList.addAll(getPermutations2(0, true, testList, testAllowed, 0, 3));
			testSelectedList.addAll(getPermutations2(0, false, bList,allowedSelect, 0, selected_sents));
			testSelectedList.addAll(getPermutations2(0, true, bList,allowedSelect, 0, selected_sents));
		}
		else{
			//testSelectedList.addAll(getPermutations2(0, false, testList, testAllowed, 0, 3));
			testSelectedList.addAll(getPermutations2(0, false, bList,allowedSelect, 0, selected_sents));

		}

	}
	public ArrayList<boolean[]> getPermutations2(int start, boolean tf, boolean[] selectList, boolean[] allowed_Selections, int count, int lim){
		//gets all permuations, although takes into account that only the n highest sentences will be considered
		//for MMR, so it uses a mask of allowed sentences
		System.gc();
		//System.out.println("start =" + start );
		//System.out.println("count =" + count );
		if(count == lim){
			//System.out.println("Perm added!");
			ArrayList<boolean[]> perms = new ArrayList<>();
			//only add in true case so it isnt added twice
			//System.out.println("Hit the limit");
			if(!tf){perms.add(selectList);}
			return perms;
		}
		else if(start == selectList.length) {
			ArrayList<boolean[]> perms = new ArrayList<>();
			return perms;
		}
		else if(tf){
			selectList[start] = true;
			ArrayList<boolean[]> perms = new ArrayList<>();
			boolean[] selectList1 = new boolean[selectList.length];
			System.arraycopy( selectList, 0, selectList1, 0, selectList.length );
			boolean[] selectList2 = new boolean[selectList.length];
			System.arraycopy( selectList, 0, selectList2, 0, selectList.length );
			if(allowed_Selections[start+1]){
				//System.out.println("The next thing was allowed \n");
				perms.addAll(getPermutations2(start+1, false, selectList1,allowed_Selections, count+1,lim));
				perms.addAll(getPermutations2(start+1, true, selectList2,allowed_Selections, count+1,lim));
			}
			else{
				//System.out.println("The next thing was not allowed \n");
				perms.addAll(getPermutations2(start+1, false, selectList1,allowed_Selections, count+1,lim));
			}

			return perms;

		}
		else {
			ArrayList<boolean[]> perms = new ArrayList<>();
			boolean[] selectList1 = new boolean[selectList.length];
			System.arraycopy( selectList, 0, selectList1, 0, selectList.length );
			boolean[] selectList2 = new boolean[selectList.length];
			System.arraycopy( selectList, 0, selectList2, 0, selectList.length );
			if(allowed_Selections[start+1]){
				//System.out.println("The next thing was allowed \n");
				perms.addAll(getPermutations2(start+1, false, selectList1,allowed_Selections, count,lim));
				perms.addAll(getPermutations2(start+1, true, selectList2,allowed_Selections, count,lim));
			}
			else{
				//System.out.println("The next thing was allowed \n");
				perms.addAll(getPermutations2(start+1, false, selectList1,allowed_Selections, count,lim));
			}

			return perms;
		}

	}
	public double calcMargRef(double weight){return 0.0;}

	public void convRanks2BoolList(){
		//this method takes the ranking list that is sorted by sentence score and
		//turns it into a list of booleans where only true ones can be considered in
		//the MMR. THis is to reduce the amount of permutaitons and ease load
		for(int i =0;i<25;i++){
			Ranking r = rankings.get(i);

			int j = r.getIndex();
			System.out.println("Ranking "+ i +" =" + j);
			allowedSelect[j]=true;
		}
	}
	public void printAllPerms(){
		for(boolean[] bList: testSelectedList){
			System.out.println(java.util.Arrays.toString(bList));
		}
	}
}

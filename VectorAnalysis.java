import java.util.ArrayList;
import java.util.List;


public class VectorAnalysis {
	private List<List<VectorElement>> vecList;
	private List<ArrayList<Double>>weightLists;
	public VectorAnalysis(List<List<VectorElement>> vList) {
		// TODO Auto-generated constructor stub
		vecList=vList;
		weightLists =new ArrayList<ArrayList<Double>>();
		vecComp();
		normalise();
	}

	public void vecComp(){
		
		for(List<VectorElement> v: vecList){
			ArrayList<Double> wl = new ArrayList<Double>();
			for(List<VectorElement> v1:vecList){
				double w=0;
				for(VectorElement ve: v){
					for(VectorElement ve1: v1){
						if(ve.getTerm().equals(ve1.getTerm())){
							w=ve.getWeight()*ve1.getWeight();
						}
					}
				}
				wl.add(w);
				
			}
			weightLists.add(wl);
		}
	}
	public void normalise(){
		for(int i =0; i<weightLists.size();i++){
			double normfac = 1/(weightLists.get(i).get(i));
			System.out.println("normfac = " +normfac);
			for(int j=0; j<weightLists.get(i).size();j++){
				weightLists.get(i).set(j, (weightLists.get(i).get(j)*normfac));
			}
		}
	}
	public List<List<VectorElement>> getVecList() {
		return vecList;
	}

	public void setVecList(List<List<VectorElement>> vecList) {
		this.vecList = vecList;
	}

	public List<ArrayList<Double>> getWeightLists() {
		return weightLists;
	}

	public void setWeightLists(List<ArrayList<Double>> weightLists) {
		this.weightLists = weightLists;
	}
}

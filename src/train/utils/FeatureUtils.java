/**
 * 
 */
package train.utils;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.Vector;


@SuppressWarnings({ "rawtypes", "unchecked" })
public class FeatureUtils {
	

	public static double calcutateSim(Vector vec1, Vector vec2){
		double result = 0;
		result = getVecProduct(vec1,vec2)/(getVecLength(vec1)*getVecLength(vec2));
		return result;
	}
	
	/** Vector Length */
	public static double getVecLength(Vector<Double> vec){
		double result = 0;
		for(int i=0;i<vec.size();i++){
			result += vec.get(i)*vec.get(i);
		}
		result = Math.sqrt(result);
		return result;
	}
	
	/** Vector Product*/
	public static double getVecProduct(Vector<Double> vec1, Vector<Double> vec2){
		double result = 0;
		int size1 = vec1.size();
		int size2 = vec2.size();
		if(size1 != size2){
			return 0;
		}
		for(int i=0;i<size1;i++){
			result += vec1.get(i)*vec2.get(i);
		}
		return result;
	}
	
	/** sort */
	public static Map.Entry[] getSortedHashtableByValue(Map h) {
		Set set = h.entrySet();  
	    Map.Entry[] entries = (Map.Entry[]) set.toArray(new Map.Entry[set.size()]);  
	    Arrays.sort(entries, new Comparator() {
	    	public int compare(Object arg0, Object arg1) {  
	    		Double key1 = Double.valueOf(((Map.Entry) arg0).getValue().toString());  
	    		Double key2 = Double.valueOf(((Map.Entry) arg1).getValue().toString());  
	            return key1.compareTo(key2);  
	           }  
	       });  
	  return entries;  
	}

	/** normalize Vector */
	public static Vector<Double> normalizeVec(Vector<Double> vec, double maxUrlDepth,
			double maxMarkNum, double maxLineBlockLen, double maxFigureNum) {
		vec.setElementAt(vec.get(0) / maxUrlDepth, 0);
		vec.setElementAt(vec.get(1) / maxMarkNum, 1);
		vec.setElementAt(vec.get(2) / maxLineBlockLen, 2);
		vec.setElementAt(vec.get(3) / maxFigureNum, 3);
		return vec;
	}
	
	public static void createFile(File file){
		if (file.exists()) {
			file.delete();
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}

package pl.tajchert.glassware.numpad;

import android.util.Log;

public class Tools {
	
	public static final String AWESOME_TAG = "NumPad";
	public static final int sliceSize = 5;
	
	public static String[] getNumbers(int degrees){
		String [] result = {"", "", ""};
		int middle = degrees/sliceSize;
		if(middle<0){
			middle = 0;
		}
		result[0] = getLeft(middle);
		result[1] = middle + "";
		result[2] = getRight(middle);
		Log.d(AWESOME_TAG, "degrees: " + degrees);
		Log.d(AWESOME_TAG, result[0] + ", " + result[1] + ", " + result[2]);
		return result;
		
	}
	
	private static String getLeft(int in){
		String res = "";
		int i = in - 1;
		for(; i >= 0 ; i--){
			res += i +"";
		}
		return res;
	}
	private static String getRight(int in){
		String res = "";
		int i = in + 1;
		for(; i <= 9; i++){
			res += i +"";
		}
		return res;
	}

}

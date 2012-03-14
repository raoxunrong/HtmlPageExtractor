package catfish.utils;

public class StringUtils {

	public static boolean isEmptyString(String str){
		return (str==null || str.trim().length()==0);
	}
	
	/**
	 * @param sourceStr
	 * @return
	 */
	public static String removeAllLineBreaker(String sourceStr) {
		return sourceStr.replaceAll("\\r\\n", "");
	}
	
	/**
	 * cleanup and trim the value
	 * 
	 * @param originalStr
	 */
	public static String eliminateBlankFormat(String originalStr) {
		if (originalStr == null)
			return null;
		String targetStr = originalStr.replaceAll("\\s+", " ");
		targetStr = targetStr.replaceAll("　", " ");
		targetStr = targetStr.replaceAll(" ", "");
		targetStr = targetStr.replaceAll("\u00a0", "");
		targetStr = targetStr.trim();
		return targetStr;
	}
}

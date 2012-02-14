/**
 * 
 */
package catfish.utils;
import train.TrainConstants;
import train.TrainConstants.PageType;
import train.utils.PageClassifier;
import catfish.model.FetchedDocument;

/**  
 * Filename:    PageUtils.java  
 * Description:   
 * @author:     chenran  
 * @version:    1.0  
 * Create at:   2012-2-13 下午4:07:26  
 */

public class PageUtils {

	public static PageType getPageType(FetchedDocument fetchedDocument){
		
		PageClassifier pageClassifier = new PageClassifier(TrainConstants.MODEL_LOCATION);
		return pageClassifier.doClassify(fetchedDocument);
	}
	

	
	
}

/**
 * 
 */
package catfish.utils;
import java.io.UnsupportedEncodingException;

import train.TrainConstants;
import train.TrainConstants.PageType;
import train.utils.FeatureExtractor;
import train.utils.PageClassifier;
import catfish.config.DIConfig;
import catfish.model.FetchedDocument;
import catfish.transport.http.HttpTransport;


public class PageUtils {

	public static PageType getPageType(FetchedDocument fetchedDocument) throws UnsupportedEncodingException{	
		PageClassifier pageClassifier = new PageClassifier(TrainConstants.MODEL_LOCATION);
		return pageClassifier.doClassify(fetchedDocument);
	}
	
	public static String getPageArticleText(FetchedDocument fetchedDocument) throws UnsupportedEncodingException{
		FeatureExtractor featureExtractor = new FeatureExtractor();
		String PageArticleText = "";
		if(PageUtils.getPageType(fetchedDocument).equals(PageType.Subject)){
			PageArticleText = featureExtractor.getMaxLineBlockText(fetchedDocument);
		}
		return PageArticleText;
	}

	
	public static void main(String[] args){
		HttpTransport httpTransport = DIConfig.getInjector().getInstance(HttpTransport.class);
		String text = "";
		try {
			text = PageUtils.getPageArticleText(httpTransport.fetch("http://news.163.com/12/0213/16/7Q5HH5AD00014JB5.html"));
		} catch (Exception e) {
	
			e.printStackTrace();
		}
		System.out.println(text);
	} 
}

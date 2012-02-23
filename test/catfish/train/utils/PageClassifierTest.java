package catfish.train.utils;

import java.util.Vector;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import catfish.model.FetchedDocument;

import train.TrainConstants;
import train.TrainConstants.PageType;
import train.utils.FeatureExtractor;
import train.utils.PageClassifier;

public class PageClassifierTest {

//	@Test
//	public void should_return_subject_type_when_fetchedDocument_is_subjectpage(){
//		Vector<Double> featureVec = new Vector<Double>();
//		featureVec.add(5.0);
//		featureVec.add(24.0);
//		featureVec.add(735.0);
//		featureVec.add(20.0);
//		featureVec.add(0.49547511312217196);
//
//		 PageClassifier pageClassifier = new PageClassifier(TrainConstants.MODEL_LOCATION);
//		 FetchedDocument fetchedDocument = new FetchedDocument();
//		 FeatureExtractor featureExtractor = Mockito.mock(FeatureExtractor.class);
//		  Mockito.when(featureExtractor.getFeature(fetchedDocument)).thenReturn(featureVec);
//		 Assert.assertEquals(PageType.Subject, pageClassifier.doClassify(fetchedDocument));
//	}
	
	
	@Test
	public void should_return_subject_type_when_fetchedDocument_is_subjectpage() {
		Vector<Double> featureVec = new Vector<Double>();
		featureVec.add(5.0);
		featureVec.add(24.0);
		featureVec.add(735.0);
		featureVec.add(20.0);
		featureVec.add(0.49547511312217196);

		FeatureExtractor featureExtractor = Mockito.mock(FeatureExtractor.class);
		Mockito.when(featureExtractor.getFeature(null)).thenReturn(featureVec);

		PageClassifier pageClassifier = new PageClassifier(TrainConstants.MODEL_LOCATION ,featureExtractor);

		Assert.assertEquals(PageType.Subject, pageClassifier.doClassify(null));
	}


}

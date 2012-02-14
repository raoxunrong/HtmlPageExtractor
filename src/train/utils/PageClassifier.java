/**
 * 
 */
package train.utils;
import java.io.BufferedReader;
import java.io.File;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import train.TrainConstants;
import train.TrainConstants.PageType;
import train.model.DocModel;

import catfish.model.FetchedDocument;


/**  
 * Filename:    PageClassifier.java  
 * Description:   
 * @author:     chenran  
 * @version:    1.0  
 * Create at:   2012-2-13 下午4:15:34  
 */

public class PageClassifier {
	/** 训练文档模型列表 */
	private List<DocModel> trainSetModel = new ArrayList<DocModel>();

	/** 类别列表 */
	private List<String> typeList = new ArrayList<String>();

	/** 训练集中最大url深度 */
	private double maxUrlDepth = 0;

	/** 训练集中最大句号个数 */
	private double maxMarkNum = 0;

	/** 训练集中最大行块长度 */
	private double maxLineBlockLen = 0;

	/** 训练集中url含数字的最大个数 */
	private double maxFigureNum = 0;

	/** knn算法中取最近邻居的数目 */
	private static final int k = 15;
	
	public PageClassifier(String featuremarixLocation) {
		init(featuremarixLocation);
	}

	private void init(String featuremarixLocation) {
		File featuremarixFile = new File(featuremarixLocation);
		/*如果featuremarixFile文件不存在，说明还没有训练过*/
		if(!featuremarixFile.exists()){
			PageTrainer pageTrainer = new PageTrainer(TrainConstants.TRAINSET_LOCATION, featuremarixLocation);
			pageTrainer.train();
		}
		BufferedReader reader = null;
		try {
		
			reader = new BufferedReader(new FileReader(featuremarixFile));
			
			String tempString = null;
			while ((tempString = reader.readLine()) != null) {
				String type = tempString.substring(0, tempString.indexOf(":"));
				if (type.equals("maxUrlDepth")) {
					maxUrlDepth = Double.valueOf(tempString.substring(tempString.indexOf(":") + 1));
				} else if (type.equals("maxMarkNum")) {
					maxMarkNum = Double.valueOf(tempString.substring(tempString.indexOf(":") + 1));
				} else if (type.equals("maxLineBlockLen")) {
					maxLineBlockLen = Double.valueOf(tempString.substring(tempString.indexOf(":") + 1));
				} else if (type.equals("maxFigureNum")) {
					maxFigureNum = Double.valueOf(tempString.substring(tempString.indexOf(":") + 1));
				} else {
					if (!typeList.contains(type)) {
						typeList.add(type);
					}
					String[] vec = tempString.substring(
							tempString.indexOf("[") + 1,
							tempString.lastIndexOf("]")).split(",");
					Vector<Double> featureVec = new Vector<Double>();
					for (String vecElement : vec) {
						featureVec.add(Double.valueOf(vecElement.trim()));
					}
					trainSetModel.add(new DocModel(type, featureVec));
				}
			}
			reader.close();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 返回页面类型
	 * @return  "notsubject", "notsubject"
	 * */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String doClassify(String url, String content) {
		String classification = "";
		FeatureExtractor featureExtractor = new FeatureExtractor();
		Vector<Double> featureVec = featureExtractor.getFeature(url, content);
		Vector<Double> normalizedFeatureVec = FeatureUtils.normalizeVec(featureVec, maxUrlDepth, maxMarkNum, maxLineBlockLen, maxFigureNum);

		// System.out.println(normalizedFeatureVec.toString());

		Map<Integer, Double> similarityMap = new HashMap<Integer, Double>();
		for (int i = 0; i < trainSetModel.size(); i++) {
			Vector<Double> trainVec = trainSetModel.get(i).getFeatureVector();
			double similarity = FeatureUtils.calcutateSim(normalizedFeatureVec, trainVec);
			similarityMap.put(i, similarity);
		}
		
		Map.Entry[] entries = FeatureUtils.getSortedHashtableByValue(similarityMap);
		Map<DocModel, Double> kNN = new HashMap<DocModel, Double>();
		
		for (int i = entries.length - 1; i > entries.length - 1 - k; i--) {
			kNN.put(trainSetModel.get((Integer) entries[i].getKey()),
					(Double) entries[i].getValue());
		}
		Map<String, Double> finalType = new HashMap<String, Double>();
		for (String item : typeList) {
			finalType.put(item, measure(kNN, item));

			System.out.println(measure(kNN, item));

		}

		Map.Entry<String, Double>[] sortedFinalType = FeatureUtils.getSortedHashtableByValue(finalType);
		classification = sortedFinalType[sortedFinalType.length - 1].getKey();

		if (classification.equals("subject")) {
			return "subject";
		} else {
			return "notsubject";
		}

	}

	
	/**
	 * 返回页面类型
	 * @return  "notsubject", "notsubject"
	 * */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public PageType doClassify(FetchedDocument fetchedDocument) {
		String classification = "";
		FeatureExtractor featureExtractor = new FeatureExtractor();
		Vector<Double> featureVec = featureExtractor.getFeature(fetchedDocument);
		Vector<Double> normalizedFeatureVec = FeatureUtils.normalizeVec(featureVec, maxUrlDepth, maxMarkNum, maxLineBlockLen, maxFigureNum);

		Map<Integer, Double> similarityMap = new HashMap<Integer, Double>();
		for (int i = 0; i < trainSetModel.size(); i++) {
			Vector<Double> trainVec = trainSetModel.get(i).getFeatureVector();
			double similarity = FeatureUtils.calcutateSim(normalizedFeatureVec, trainVec);
			similarityMap.put(i, similarity);
		}
		
		Map.Entry[] entries = FeatureUtils.getSortedHashtableByValue(similarityMap);
		Map<DocModel, Double> kNN = new HashMap<DocModel, Double>();
		
		for (int i = entries.length - 1; i > entries.length - 1 - k; i--) {
			kNN.put(trainSetModel.get((Integer) entries[i].getKey()),
					(Double) entries[i].getValue());
		}
		Map<String, Double> finalType = new HashMap<String, Double>();
		for (String item : typeList) {
			finalType.put(item, measure(kNN, item));

			System.out.println(measure(kNN, item));

		}

		Map.Entry<String, Double>[] sortedFinalType = FeatureUtils.getSortedHashtableByValue(finalType);
		classification = sortedFinalType[sortedFinalType.length - 1].getKey();

		if (classification.equals("subject")) {
			return PageType.Subject;
		} else {
			return PageType.Hub;
		}

	}
	
	
	private double measure(Map<DocModel, Double> kNN, String type) {
		double result = 0;
		for (Map.Entry<DocModel, Double> item : kNN.entrySet()) {
			if (!item.getKey().getType().equals(type))
				continue;
			result += item.getValue();
		}
		return result;
	}
}

/**
 * 
 */
package train.utils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import catfish.model.FetchedDocument;


public class FeatureExtractor {

		private int urlDepth = -1;
		
		private int markNum = 0;
		
		private int figureNum = 0;
		
		private double linkProportion = 0;
		
		private int maxLineBlockLength = 0;
		
		private static final int blockLines = 3;
		
		

		/**
		 * for trainset
		 * @param url
		 * @param content
		 * @return
		 */
		public Vector<Double> getFeature(String url, String content) {
			Vector<Double> vec = new Vector<Double>();
			if (url.charAt(url.length() - 1) == '/') {
				url = url.substring(0, url.length() - 1);
			}
			for (int i = 0; i < url.length()-1; i++) {
				if (url.charAt(i) == '/')
					urlDepth++;
			}
			figureNum = url.replaceAll("\\D", "").length();
			linkProportion = getLinkProportion(content);
			content = preProcess(content);
			for (int i = 0; i < content.length(); i++) {
				if (content.charAt(i) == 0x3002)
					markNum++;
			}
			maxLineBlockLength = getMaxLineBlockLength(content);
			vec.add((double)urlDepth);
			vec.add((double)markNum);
			vec.add((double)maxLineBlockLength);
			vec.add((double)figureNum);
			vec.add(linkProportion);
			
			System.out.println(vec.toString());
			
			return vec;
		}
		
		
		/**
		 * for new url
		 * @param fetchedDocument
		 * @return
		 */
		public Vector<Double> getFeature(FetchedDocument fetchedDocument) {
			String url = fetchedDocument.getDocumentURL();
			
			Vector<Double> vec = new Vector<Double>();
			if (url.charAt(url.length() - 1) == '/') {
				url = url.substring(0, url.length() - 1);
			}
			for (int i = 0; i < url.length()-1; i++) {
				if (url.charAt(i) == '/')
					urlDepth++;
			}
			figureNum = url.replaceAll("\\D", "").length();
			String content = null;
			content = getContent(fetchedDocument);
			linkProportion = getLinkProportion(content);
			content = preProcess(content);
			for (int i = 0; i < content.length(); i++) {
				if (content.charAt(i) == 0x3002)
					markNum++;
			}
			maxLineBlockLength = getMaxLineBlockLength(content);
			vec.add((double)urlDepth);
			vec.add((double)markNum);
			vec.add((double)maxLineBlockLength);
			vec.add((double)figureNum);
			vec.add(linkProportion);
			
			System.out.println(vec.toString());
			
			return vec;
		}


		private String getContent(FetchedDocument fetchedDocument) {
			String content = null;
			try {				
				content = new String(fetchedDocument.getDocumentContent(),fetchedDocument.getContentCharset());
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			return content;
		}
		
		
		private int getMaxLineBlockLength(String rawContent) {
			int length = 0;
			length = getMaxLineBlockText(rawContent).length();
			return length;

		}

		
		
		public String getMaxLineBlockText(FetchedDocument fetchedDocument) {
			String rawContent = getContent(fetchedDocument);
			String content = preProcess(rawContent);
			return getMaxLineBlockText(content);
		}
		
		
		private String getMaxLineBlockText(String content) {
			List<String> lines = Arrays.asList(content.split("\n"));
			List<Integer> indexDistribution = lineBlockDistribute(lines);

			List<String> textList = new ArrayList<String>();
			List<Integer> textBeginList = new ArrayList<Integer>();
			List<Integer> textEndList = new ArrayList<Integer>();

			for (int i = 0; i < indexDistribution.size(); i++) {
				if (indexDistribution.get(i) > 0) {
					StringBuilder tmp = new StringBuilder();
					textBeginList.add(i);
					while (i < indexDistribution.size()
							&& indexDistribution.get(i) > 0) {
						tmp.append(lines.get(i)).append("\n");
						i++;
					}
					textEndList.add(i);
					textList.add(tmp.toString());
				}
			}
			for (int i = 1; i < textList.size(); i++) {
				if (textBeginList.get(i) == textEndList.get(i - 1) + 1
						&& textEndList.get(i) > textBeginList.get(i) + blockLines
						&& textList.get(i).replaceAll("\\s+", "").length() > 40) {
					if (textEndList.get(i - 1) == textBeginList.get(i - 1) + blockLines
							&& textList.get(i - 1).replaceAll("\\s+", "").length() < 40) {
						continue;
					}
					textList.set(i - 1, textList.get(i - 1) + textList.get(i));
					textEndList.set(i - 1, textEndList.get(i));

					textList.remove(i);
					textBeginList.remove(i);
					textEndList.remove(i);
					--i;
				}
			}

			String result = "";
			for (String text : textList) {
				if (text.replaceAll("\\s+", "").length() > result.replaceAll(
						"\\s+", "").length())
					result = text;
			}
			return result.replaceAll("\\s+", "");
			
		}

		/**
		 * Pre processing.
		 * 
		 * @param htmlText
		 *            the html text
		 * 
		 * @return the string
		 */
		private String preProcess(String htmlText) {
			// DTD
			htmlText = htmlText.replaceAll("(?is)<!DOCTYPE.*?>", "");
			// html comment
			htmlText = htmlText.replaceAll("(?is)<!--.*?-->", "");
			// js
			htmlText = htmlText.replaceAll("(?is)<script.*?>.*?</script>", "");
			// css
			htmlText = htmlText.replaceAll("(?is)<style.*?>.*?</style>", "");
			//anchor text
			htmlText = htmlText.replaceAll("(?is)<a.*?>.*?</a>", "");
			// html
			htmlText = htmlText.replaceAll("(?is)<.*?>", "");

			return replaceSpecialChar(htmlText);
		}

		/**
		 * Replace special char.
		 * 
		 * @param content
		 *            the content
		 * 
		 * @return the string
		 */
		private String replaceSpecialChar(String content) {

			return content;
		}

		/**
		 * Line block distribute.
		 * 
		 * @param lines
		 *            the lines
		 * 
		 * @return the list< integer>
		 */
		private List<Integer> lineBlockDistribute(List<String> lines) {
			List<Integer> indexDistribution = new ArrayList<Integer>();

			for (int i = 0; i < lines.size(); i++) {
				indexDistribution.add(lines.get(i).replaceAll("\\s+", "").length());
			}
			for (int i = 0; i + 4 < lines.size(); i++) {
				if (indexDistribution.get(i) == 0
						&& indexDistribution.get(i + 1) == 0
						&& indexDistribution.get(i + 2) > 0
						&& indexDistribution.get(i + 2) < 40
						&& indexDistribution.get(i + 3) == 0
						&& indexDistribution.get(i + 4) == 0) {
					lines.set(i + 2, "");
					indexDistribution.set(i + 2, 0);
					i += 3;
				}
			}

			for (int i = 0; i < lines.size() - blockLines; i++) {
				int wordsNum = indexDistribution.get(i);
				for (int j = i + 1; j < i + blockLines && j < lines.size(); j++) {
					wordsNum += indexDistribution.get(j);
				}
				indexDistribution.set(i, wordsNum);
			}

			return indexDistribution;
		}
		
		private double getLinkProportion(String htmlText){
			int linkNum = 0;
			int tagNum = 0;
			// DTD
			htmlText = htmlText.replaceAll("(?is)<!DOCTYPE.*?>", "");
			// html comment
			htmlText = htmlText.replaceAll("(?is)<!--.*?-->", "");
			//head
			htmlText = htmlText.replaceAll("(?is)<head.*?>.*?</head>", "");
			// js
			htmlText = htmlText.replaceAll("(?is)<script.*?>.*?</script>", "");
			// css
			htmlText = htmlText.replaceAll("(?is)<style.*?>.*?</style>", "");
			Pattern patterna = Pattern.compile("<a.*?>.*?</a>");
			Matcher matcha = patterna.matcher(htmlText);
			while(matcha.find()){
				linkNum++;
			}
			Pattern patterntag = Pattern.compile("<.*?>.*?</.*?>");
			Matcher matchtag = patterntag.matcher(htmlText);
			while(matchtag.find()){
				tagNum++;
			}
			double proportion = (double)linkNum/tagNum;
			return proportion;
		}
		
		
	

	}

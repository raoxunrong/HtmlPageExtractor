/**
 * 
 */
package train.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import catfish.model.FetchedDocument;

/**  
 * Filename:    FeatureExtractor.java  
 * Description:   
 * @author:     chenran  
 * @version:    1.0  
 * Create at:   2012-2-13 下午4:20:47  
 */

public class FeatureExtractor {

		/** url深度 */
		private int urlDepth = -1;
		
		/** 逗号和句号个数 */
		private int markNum = 0;
		
		/** url中包含数字的个数 */
		private int figureNum = 0;
		
		/** 链接标签所占比例 */
		private double linkProportion = 0;
		
		/** 最大行块长度 */
		private int maxLineBlockLength = 0;
		
		/** 行块包含行数 */
		private static final int _block = 3;
		
		
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
				if (content.charAt(i) == '。')
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

		public Vector<Double> getFeature(FetchedDocument fetchedDocument) {
			String url = fetchedDocument.getDocumentURL();
			String content = fetchedDocument.getDocumentContent().toString();
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
				if (content.charAt(i) == '。')
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
		private int getMaxLineBlockLength(String content) {
			int length = 0;
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

			// 如果两块只差两个空行，并且两块包含文字均较多，则进行块合并，以弥补单纯抽取最大块的缺点
			for (int i = 1; i < textList.size(); i++) {
				if (textBeginList.get(i) == textEndList.get(i - 1) + 1
						&& textEndList.get(i) > textBeginList.get(i) + _block
						&& textList.get(i).replaceAll("\\s+", "").length() > 40) {
					if (textEndList.get(i - 1) == textBeginList.get(i - 1) + _block
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
				// System.out.println("text:" + text + "\n" +
				// text.replaceAll("\\s+", "").length());
				if (text.replaceAll("\\s+", "").length() > result.replaceAll(
						"\\s+", "").length())
					result = text;
			}
			length = result.replaceAll("\\s+", "").length();

			return length;

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
			String text = content.replaceAll("&quot;", "\"");
			text = text.replaceAll("&ldquo;", "“");
			text = text.replaceAll("&rdquo;", "”");
			text = text.replaceAll("&middot;", "·");
			text = text.replaceAll("&#8231;", "·");
			text = text.replaceAll("&#8212;", "——");
			text = text.replaceAll("&#28635;", "濛");
			text = text.replaceAll("&hellip;", "…");
			text = text.replaceAll("&#23301;", "嬅");
			text = text.replaceAll("&#27043;", "榣");
			text = text.replaceAll("&#8226;", "·");
			text = text.replaceAll("&#40;", "(");
			text = text.replaceAll("&#41;", ")");
			text = text.replaceAll("&#183;", "·");
			text = text.replaceAll("&amp;", "&");
			text = text.replaceAll("&bull;", "·");
			text = text.replaceAll("&lt;", "<");
			text = text.replaceAll("&#60;", "<");
			text = text.replaceAll("&gt;", ">");
			text = text.replaceAll("&#62;", ">");
			text = text.replaceAll("&nbsp;", " ");
			text = text.replaceAll("&#160;", " ");
			text = text.replaceAll("&tilde;", "~");
			text = text.replaceAll("&mdash;", "—");
			text = text.replaceAll("&copy;", "@");
			text = text.replaceAll("&#169;", "@");
			text = text.replaceAll("♂", "");
			text = text.replaceAll("\r\n|\r", "\n");

			return text;
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
			// 删除上下存在两个空行的文字行
			for (int i = 0; i + 4 < lines.size(); i++) {
				if (indexDistribution.get(i) == 0
						&& indexDistribution.get(i + 1) == 0
						&& indexDistribution.get(i + 2) > 0
						&& indexDistribution.get(i + 2) < 40
						&& indexDistribution.get(i + 3) == 0
						&& indexDistribution.get(i + 4) == 0) {
					// System.out.println("line:" + lines.get(i+2));
					lines.set(i + 2, "");
					indexDistribution.set(i + 2, 0);
					i += 3;
				}
			}

			for (int i = 0; i < lines.size() - _block; i++) {
				int wordsNum = indexDistribution.get(i);
				for (int j = i + 1; j < i + _block && j < lines.size(); j++) {
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

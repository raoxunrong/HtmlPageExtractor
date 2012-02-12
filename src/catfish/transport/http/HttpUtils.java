package catfish.transport.http;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.http.util.EntityUtils;
import org.cyberneko.html.parsers.DOMParser;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import catfish.constants.HttpConstants;
import catfish.transport.http.entity.ByteInfoHttpEntityWrapper;
import catfish.utils.DOMContentUtils;

public class HttpUtils {

	private static final String CHARSET_ATTR_NAME = "charset=";
	
	public static String getCharset(ByteInfoHttpEntityWrapper entity){
		String charset = EntityUtils.getContentCharSet(entity);
		if(charset == null){
			charset = getCharsetFromByte(entity.getByteInfo());
		}
		charset = (charset==null?HttpConstants.CHARSET_TYPE_DEFAULT:charset).toUpperCase();
		return charset;
	}
	
	public static String getContentType(ByteInfoHttpEntityWrapper entity){
		String contentType = EntityUtils.getContentMimeType(entity);
		if(contentType == null){
			//TODO need implementation that get the content type from its content
		}
		return (contentType==null?HttpConstants.CHARSET_TYPE_DEFAULT:contentType);
	}


	private static String getCharsetFromByte(byte[] data) {
		InputStream contentBytes = new ByteArrayInputStream(data);
		InputStreamReader characterStream = null;
		try {
			characterStream = new InputStreamReader(contentBytes, HttpConstants.CHARSET_TYPE_DEFAULT);
		} catch (UnsupportedEncodingException e) {
			try {
				characterStream = new InputStreamReader(contentBytes,
						HttpConstants.CHARSET_TYPE_UTF8);
			} catch (UnsupportedEncodingException e1) {
				return null;
			}
		}
		InputSource inputSource = new InputSource();
		inputSource.setCharacterStream(characterStream);
		DOMParser parser = new DOMParser();
		try {
			parser.parse(inputSource);
		} catch (Exception e) {
			return null;
		}
		Node node = parser.getDocument();
		return getCharsetFromNode(node);
	}

	private static String getCharsetFromNode(Node node) {
		if (node != null){
			org.w3c.dom.Document doc = DOMContentUtils.getDocumentNode(node);
	        NodeList nodeList = doc.getElementsByTagName("meta");
	        for(int i=0; i<nodeList.getLength(); i++){
	        	Node tempNode = nodeList.item(i); 
	        	NamedNodeMap attributes = tempNode.getAttributes();
	        	for(int j=0; j<attributes.getLength(); j++){
					if(attributes.item(j).getNodeValue() != null){
						int k = (attributes.item(j).getNodeValue()).toLowerCase().indexOf(CHARSET_ATTR_NAME);
						if (k > -1) {
							return attributes.item(j).getNodeValue().substring(
									k + CHARSET_ATTR_NAME.length()).toUpperCase();
						}
					}
					
				}
	        }
		}
		
		return null;
        
	}

}

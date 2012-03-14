package catfish.transport.http;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static org.junit.Assert.*;
import org.junit.Test;

import catfish.transport.exception.HTMLDocumentParserException;

public class HtmlUtilsTest {


	@Test
	public void should_get_preprocessed_content_when_getPreprocessedContent_is_invoked() throws HTMLDocumentParserException, IOException{
		String currentPath = this.getClass().getResource(".").getPath();
		FileInputStream fi = new FileInputStream(currentPath + File.separator + "test.fetched");
		byte[] fileInfo = new byte[1024*1024];
		fi.read(fileInfo);
		fi.close();
		String preprocessedContent = HtmlUtils.getPreprocessedContent(fileInfo, "GB2312");
		System.out.println(preprocessedContent);
		checkTagNotExisted(preprocessedContent, new String[]{"SCRIPT", "STYLE"});
		
	}

	private void checkTagNotExisted(String preprocessedContent, String[] tags) {
		for(String tag : tags){
			assertFalse(preprocessedContent.contains("<" + tag + ">"));
			assertFalse(preprocessedContent.contains("</" + tag + ">"));
		}
		
	}
}

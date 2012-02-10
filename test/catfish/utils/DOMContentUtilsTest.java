package catfish.utils;

import org.junit.Test;
import static org.mockito.Mockito.*;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import static junit.framework.Assert.*;

public class DOMContentUtilsTest {

	@Test
	public void should_return_null_when_invoke_getDocumentNode_and_node_is_null(){
		assertNull(DOMContentUtils.getDocumentNode(null));
	}
	
	@Test
	public void should_return_itself_when_invoke_getDocumentNode_and_node_is_document_node(){
		Node node = mock(Document.class);
		when(node.getNodeType()).thenReturn(Node.DOCUMENT_NODE);
		assertEquals((Document)node, DOMContentUtils.getDocumentNode(node));
	}
	
	@Test
	public void should_return_its_own_document_when_invoke_getDocumentNode_and_node_is_not_document_node(){
		Node node = mock(Node.class);
		when(node.getNodeType()).thenReturn(Node.COMMENT_NODE);
		
		Document ownDocument = mock(Document.class);
		when(node.getOwnerDocument()).thenReturn(ownDocument);
		assertEquals(ownDocument, DOMContentUtils.getDocumentNode(node));
	}
}

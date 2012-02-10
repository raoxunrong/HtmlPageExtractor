package catfish.utils;

import org.w3c.dom.Node;

public class DOMContentUtils {

	public static org.w3c.dom.Document getDocumentNode(Node node) {
		if (node == null) {
			return null;
		}

		if (Node.DOCUMENT_NODE == node.getNodeType()) {
			return (org.w3c.dom.Document) node;
		} else {
			return node.getOwnerDocument();
		}
	}

}

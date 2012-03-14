package catfish.model;

import java.util.ArrayList;
import java.util.List;

import train.TrainConstants.PageType;

/**
 * Represents Processed document with attributes that we are interested in.
 * 
 * Need to pay attention, ProcessedDocument not contains attribute called contentType, 
 * all string info here are processed and preserved in UTF-8.
 */
public class ProcessedDocument {
	
	public static final String ContentType = "UTF-8";
    
    /*
     * Unique document id.
     */
    private String documentId;
    
    /*
     * All document outlinks (links that document has to other documents).
     */
    private List<Outlink> outlinks = new ArrayList<Outlink>();
    
    /*
     * URL that was used to retrieve the document.
     */
    private String url;
    
    /*
     * Document title. 
     */
    private String title;
    
    /*
     * Processed document content. In case of HTML doc it can be HTML
     * with only relevant tags (<P>, <B>,..) preserved.
     * All unnecessary tags (<script>, <comment>, <style>, ...) removed
     */
    private String processedContent;
    
    /*
     * Text extracted from the document with all formatting removed.
     */
    private String mainBody;
    
    /*
     * Document type.
     */
    private PageType documentType;
    
    public ProcessedDocument() {
    }

    public List<Outlink> getOutlinks() {
        return outlinks;
    }

    public void setOutlinks(List<Outlink> outlinks) {
        this.outlinks = outlinks;
    }

    public void setDocumentTitle(String title) {
        this.title = title;
    }

    public String getDocumentTitle() {
        return this.title;
    }

    public void setDocumentId(String docId) {
        this.documentId = docId;
    }
    
    public String getDocumentId() {
        return documentId;
    }
    
    public PageType getDocumentType() {
        return documentType;
    }
    
    public void setDocumentType(PageType docType) {
        this.documentType = docType;
    }
    
    @Override
	public String toString() {
        return "[docId: " + documentId +
                ", type: " + documentType +
                ", url: " + url + 
                "]"; 
    }

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getProcessedContent() {
		return processedContent;
	}

	public void setProcessedContent(String processedContent) {
		this.processedContent = processedContent;
	}

	public String getMainBody() {
		return mainBody;
	}

	public void setMainBody(String mainBody) {
		this.mainBody = mainBody;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}

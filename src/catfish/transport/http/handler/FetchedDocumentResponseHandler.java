package catfish.transport.http.handler;

import java.io.IOException;

import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.GzipDecompressingEntity;
import org.apache.http.util.EntityUtils;

import catfish.model.FetchedDocument;
import catfish.transport.http.HttpUtils;
import catfish.transport.http.entity.ByteInfoHttpEntityWrapper;

public class FetchedDocumentResponseHandler implements ResponseHandler<FetchedDocument> {

	@Override
	public FetchedDocument handleResponse(HttpResponse response)
			throws ClientProtocolException, IOException {

		HttpEntity entity = response.getEntity();
		StatusLine statusLine = response.getStatusLine();
		if (isStatusError(statusLine.getStatusCode())) {
            EntityUtils.consume(entity);
            throw new HttpResponseException(statusLine.getStatusCode(),
                    statusLine.getReasonPhrase());
        }
		
		if(entity == null){
			throw new ClientProtocolException("Can not get entity info from response!!!");
		}
	
		response = decodeContent(response);
		
		return createFetchedDocument(entity);
	}

	private HttpResponse decodeContent(HttpResponse response) {
		HttpEntity entity = response.getEntity();
		if(entity.getContentEncoding() != null){
			System.out.println("contentEncodingHeaderValue is " + entity.getContentEncoding().getValue());
			
			HeaderElement[] codecs = entity.getContentEncoding().getElements();
            for (int i = 0; i < codecs.length; i++) {
            	String encoding = codecs[i].getName();
                if (encoding.equalsIgnoreCase("gzip")) {
                    response.setEntity(
                            new GzipDecompressingEntity(response.getEntity()));
                    break;
                }else if(encoding.equals("deflate")){
                	//TODO need implementation 
                	break;
                }else if(encoding.equals("compress")){
                	//TODO need implementation 
                	break;
                }
            }
		}
		
		return response;
	}

	private boolean isStatusError(int statusCode) {
		return statusCode >= 300;
	}

	private FetchedDocument createFetchedDocument(HttpEntity entity) throws IOException {
		FetchedDocument fetchedDocument = new FetchedDocument();
		byte[] byteContent = EntityUtils.toByteArray(entity);
		ByteInfoHttpEntityWrapper byteInfoHttpEntityWrapper = new ByteInfoHttpEntityWrapper(entity, byteContent);
		
		fetchedDocument.setContentCharset(HttpUtils.getCharset(byteInfoHttpEntityWrapper));
		fetchedDocument.setContentType(HttpUtils.getContentType(byteInfoHttpEntityWrapper));
		fetchedDocument.setDocumentContent(byteContent);
		
		return fetchedDocument;
	}

}

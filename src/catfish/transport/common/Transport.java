package catfish.transport.common;

import catfish.model.FetchedDocument;
import catfish.transport.exception.TransportException;


public interface Transport {
    public FetchedDocument fetch(String url) throws TransportException;
    public void init();
    public void clear();
    public boolean pauseRequired();
}

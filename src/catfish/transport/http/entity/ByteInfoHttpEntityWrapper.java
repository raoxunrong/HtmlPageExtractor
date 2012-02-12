package catfish.transport.http.entity;

import org.apache.http.HttpEntity;
import org.apache.http.entity.HttpEntityWrapper;

public class ByteInfoHttpEntityWrapper extends HttpEntityWrapper {
	
	private byte[] byteInfo;

	public ByteInfoHttpEntityWrapper(HttpEntity wrapped) {
		super(wrapped);
	}

	public ByteInfoHttpEntityWrapper(HttpEntity wrapped, byte[] byteInfo) {
		super(wrapped);
		this.byteInfo = byteInfo;
	}

	public byte[] getByteInfo() {
		return byteInfo;
	}

	public void setByteInfo(byte[] byteInfo) {
		this.byteInfo = byteInfo;
	}

}

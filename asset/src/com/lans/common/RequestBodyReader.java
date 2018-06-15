package com.lans.common;

import java.io.InputStream;
import javax.servlet.http.HttpServletRequest;

public class RequestBodyReader {

	public RequestBodyReader() {
		// TODO Auto-generated constructor stub
	}
	
	private static byte[] readBytes(InputStream is, int contentLen) {
		if (contentLen > 0) {
			int readLen = 0;
			int readLengthThisTime = 0;
			byte[] message = new byte[contentLen];
			
			try {
				while (readLen != contentLen) {
					readLengthThisTime = is.read(message, readLen, contentLen - readLen);
					if (readLengthThisTime == -1) {// Should not happen
						break;
					}
					readLen += readLengthThisTime;
				}
				
				return message;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
        return null;
}
	
	public static String read(HttpServletRequest request) throws Exception {
		request.setCharacterEncoding("UTF-8");
		
		int size = request.getContentLength();
		InputStream is = request.getInputStream();
		byte[] reqBodyBytes = readBytes(is, size);
		if (reqBodyBytes == null) {
			return "";
		}
		
		String ret = new String(reqBodyBytes);
		return ret;
	}

}

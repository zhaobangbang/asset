package com.lansitec.infrastructure.util;

public class IntBytesConverter {

	public IntBytesConverter() {
	
	}

	public static byte[] int2Bytes(int num) {  
        byte[] byteNum = new byte[4];  
        for (int ix = 0; ix < 4; ++ix) {  
            int offset = 32 - (ix + 1) * 8;  
            byteNum[ix] = (byte) ((num >> offset) & 0xff);  
        }  
        return byteNum;  
    }  
  
    public static int bytes2Int(byte[] byteNum) {  
        int num = 0;  
        for (int ix = 0; ix < 4; ++ix) {  
            num <<= 8;  
            num |= (byteNum[ix] & 0xff);  
        }  
        return num;  
    }  
    
    public static int bytes2Short(byte[] byteNum) {  
        int num = 0;  
        for (int ix = 0; ix < 2; ++ix) {  
            num <<= 8;  
            num |= (byteNum[ix] & 0xff);  
        }  
        return num;  
    }  
}

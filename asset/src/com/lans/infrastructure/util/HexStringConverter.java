package com.lans.infrastructure.util;

public class HexStringConverter {

	public HexStringConverter() {
		// TODO Auto-generated constructor stub
	}

	public static byte[] toBytes(String s) {
		String data = s;
		
		if (data.length() % 2  != 0) {
			data = "0" + data;
		}
		byte[] bData = new byte[data.length()/2];
		for (int i = 0; i < data.length()/2; i++) {
			String sub = data.substring(i*2, (i*2+2));
			bData[i] = (byte) Integer.parseInt(sub, 16);
		}
		
		return bData;
	}
	
	public static String fromBytes(byte[] input) {
		String toRet = "";
		
		for (int i = 0; i < input.length; i++)
        {
            String hex = Integer.toHexString(input[i] & 0xFF);
            if (hex.length() == 1)
            {
                hex = '0' + hex;
            }
            toRet = toRet + hex.toLowerCase();
        }
        
		return toRet;
	}
}

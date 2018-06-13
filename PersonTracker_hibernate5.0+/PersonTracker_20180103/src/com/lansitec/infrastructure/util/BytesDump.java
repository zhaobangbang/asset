package com.lansitec.infrastructure.util;

public class BytesDump {

	public BytesDump() {
		// TODO Auto-generated constructor stub
	}
	
	public static String toString(byte[] input) {
		String toRet = "";
		
		for (int i = 0; i < input.length; i++)
        {
            String hex = Integer.toHexString(input[i] & 0xFF);
            if (hex.length() == 1)
            {
                hex = '0' + hex;
            }
            toRet = toRet + hex.toUpperCase() + " ";
        }
        
		return toRet;
	}

}

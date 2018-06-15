package com.lans.common;

public class LansUtil {
	static int DEBUG = 0;
	static int INFO = 1;
	static int WARNING = 2;
	static int ERROR =3;
	
	public LansUtil(){
		
	}
	
	static private int traceLevel = INFO;
	
	static public void LogDebug(String trace){
		if(traceLevel == DEBUG){
			System.out.println(trace);
		}
	}
	static public void LogInfo(String trace){
		if(traceLevel <= INFO){
			System.out.println(trace);
		}
	}
	static public void LogWarning(String trace){
		if(traceLevel <= WARNING){
			System.out.println(trace);
		}
	}
	static public void LogError(String trace){
		if(traceLevel <= ERROR){
			System.out.println(trace);
		}
	}
}

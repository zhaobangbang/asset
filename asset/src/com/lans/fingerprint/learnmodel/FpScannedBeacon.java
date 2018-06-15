package com.lans.fingerprint.learnmodel;

import com.lans.fingerprint.FpDefs;

public class FpScannedBeacon {
	int minor;
	int count;
	
	int pwrRange1Cnt;
	int pwrRange2Cnt;
	int pwrRange3Cnt;
	int pwrRange4Cnt;
	
	public FpScannedBeacon(int minor) {
		this.minor = minor;
		count = 0;
		
		pwrRange1Cnt = 0;
		pwrRange2Cnt = 0;
		pwrRange3Cnt = 0;
		pwrRange4Cnt = 0;
	}
	
	public int getMinor() {
		return minor;
	}

	public int getCount() {
		return count;
	}

	public void addOnce(int power) {
		count++;
		
		if (power <= FpDefs.POWER_THRE_1) {
			pwrRange1Cnt++;
		} else if (power <= FpDefs.POWER_THRE_2) {
			pwrRange2Cnt++;
		} else if (power <= FpDefs.POWER_THRE_3) {
			pwrRange3Cnt++;
		} else {
			pwrRange4Cnt++;
		}
	}

	public int getPwrRange1Cnt() {
		return pwrRange1Cnt;
	}

	public int getPwrRange2Cnt() {
		return pwrRange2Cnt;
	}

	public int getPwrRange3Cnt() {
		return pwrRange3Cnt;
	}

	public int getPwrRange4Cnt() {
		return pwrRange4Cnt;
	}	
}

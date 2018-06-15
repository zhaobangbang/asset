package com.lans.controller.networkgw.tvmessages;

public class ScannedBracelet {
	public int macone;//4bytes
    public short mactwo;//2bytes
    public byte hb;//1byte
    public short steps;//2bytes
    public byte bat;//1byte
    public byte sysp;//1byte
    public byte diap;//1byte
    public short calo;//2bytes
    public byte rssi;//1byte
    public byte sos;//1byte
    
    public ScannedBracelet(int macone,short mactwo,byte hb,short steps,byte bat,byte sysp,byte diap,short calo,byte rssi,byte sos){
    	this.macone = macone;
    	this.mactwo = mactwo;
    	this.hb = hb;
    	this.steps = steps;
    	this.bat = bat;
    	this.sysp = sysp;
    	this.diap = diap;
    	this.calo = calo;
    	this.rssi = rssi;
    	this.sos = sos;
    }

	public int getMacone() {
		return macone;
	}

	public short getMactwo() {
		return mactwo;
	}

	public byte getHb() {
		return hb;
	}

	public short getSteps() {
		return steps;
	}

	public byte getBat() {
		return bat;
	}

	public byte getSysp() {
		return sysp;
	}

	public byte getDiap() {
		return diap;
	}

	public short getCalo() {
		return calo;
	}

	public byte getRssi() {
		return rssi;
	}

	public byte getSos() {
		return sos;
	}

}

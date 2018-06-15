package com.lans.beans;
//for fastJson parse

public class PositionValidPoints {
    private PositionValid3Points[] valid3Points;
    private PositionValid2Points[] valid2Points;
    
    public PositionValidPoints() {

	}
	public PositionValid2Points[] getValid2Points() {
		return valid2Points;
	}
	public void setValid2Points(PositionValid2Points[] valid2Points) {
		this.valid2Points = valid2Points;
	}
	public PositionValid3Points[] getValid3Points() {
		return valid3Points;
	}
	public void setValid3Points(PositionValid3Points[] valid3Points) {
		this.valid3Points = valid3Points;
	}    
}

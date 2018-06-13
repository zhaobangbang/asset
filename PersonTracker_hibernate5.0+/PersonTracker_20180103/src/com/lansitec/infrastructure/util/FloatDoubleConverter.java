package com.lansitec.infrastructure.util;

import java.math.BigDecimal;

public class FloatDoubleConverter {

	public FloatDoubleConverter() {
		// TODO Auto-generated constructor stub
	}
	
	public static double FloatToDouble(float f) {
		BigDecimal b = new BigDecimal(Float.toString(f));
		double d = b.doubleValue();
		return d;
	}
	
	public static float DoubleToFloat(double d) {
		BigDecimal b = new BigDecimal(Double.toString(d));
		float f = b.floatValue();
		return f;
	}

}

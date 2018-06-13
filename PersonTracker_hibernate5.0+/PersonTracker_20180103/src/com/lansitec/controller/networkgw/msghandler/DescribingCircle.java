package com.lansitec.controller.networkgw.msghandler;

import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DescribingCircle {
	private Logger logger  = LoggerFactory.getLogger(DescribingCircle.class);
	List<Point> points = new LinkedList<Point>();
    class Point{
    	public float x;
    	public float y;
    	public Point(float x,float y){
    		this.x = x;
    		this.y = y;
    	}
    }
    
	
	//the beacon's x and y belong to normal range
	public void initGetPointsCircular(float CIRCLE_CENTER_a,float CIRCLE_CENTER_b,float CIRCLE_R,int length,int width) {  
	    //length mapping y, width mapping x
        float x = getX(CIRCLE_CENTER_a,CIRCLE_R);
        //x beyond width
        if(x > width){
        	x = CIRCLE_CENTER_a;
        }
        float y = getY(CIRCLE_CENTER_b,CIRCLE_R,CIRCLE_CENTER_a,x);
        //y beyond length
        if( (y > length) && (x == CIRCLE_CENTER_a) ){
        	y = 0;
        }
        points.add(new Point(x, y));  
	}
	
	public float getX(float CIRCLE_CENTER_a,float CIRCLE_R){
		 float x = 0;
		 float a = (float) (Math.random()*((CIRCLE_CENTER_a + CIRCLE_R) + 1 - (CIRCLE_CENTER_a - CIRCLE_R))+(CIRCLE_CENTER_a - CIRCLE_R));
		 if((a > (CIRCLE_CENTER_a - CIRCLE_R)) && (a <(CIRCLE_CENTER_a + CIRCLE_R))){
	           x = a;
	       }else{
	    	   x = CIRCLE_CENTER_a;
	       }
		 if(x < 0 ){
			 x = Math.abs(x);
		 }
		 logger.info("get the x {}",x);
		 return x;
	}
	
	public float getY(float CIRCLE_CENTER_b,float CIRCLE_R,float CIRCLE_CENTER_a,float x){
		//(y-CIRCLE_CENTER_b) * (y-CIRCLE_CENTER_b) = CIRCLE_R * CIRCLE_R - (x - CIRCLE_CENTER_a) * (x - CIRCLE_CENTER_a)
		float value = (CIRCLE_R * CIRCLE_R) - (x-CIRCLE_CENTER_a)*(x-CIRCLE_CENTER_a);
		if(value < 0 ){
		     value = Math.abs(value);//¾ø¶ÔÖµ
		 }
		float y = (float) ((Math.sqrt(value) + CIRCLE_CENTER_b));
		logger.info("get the y {}",y);
		
		return y;
	}
}

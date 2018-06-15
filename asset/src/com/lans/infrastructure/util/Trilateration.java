package com.lans.infrastructure.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class POINT {  
    double x;  
    double y;
    
    public POINT(double x,double y)  
    {  
        this.x=x;  
        this.y=y;  
    }  
};

class beaconPos {
	static Logger logger = LoggerFactory.getLogger(Trilateration.class);
	int minor;
	double x;
	double y;
	double dist;

	public beaconPos(int minor, double x, double y) {
		this.minor = minor;
		this.x = x;
		this.y = y;
		this.dist = 0;
	}

	public beaconPos calDist(double a, double n, double rssi) {
		double tmp = (Math.abs(rssi) - a) / (10 * n);
		dist = Math.pow(10, tmp);
		return this;
	}

	public double getDist() {
		return dist;
	}
	
	public void setDist(double dist) {
		this.dist = dist;
	}
	public int getMinor() {
		return minor;
	}
	
	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public beaconPos setBeacon(int minor, double x, double y, double dist) {
		this.minor = minor;
		this.x = x;
		this.y = y;
		this.dist = dist;
		return this;
	}
}

public class Trilateration {
	static Logger logger = LoggerFactory.getLogger(Trilateration.class);
	beaconPos beacon1;
	beaconPos beacon2;
	beaconPos beacon3;
	double xPos;
	double yPos;

	double maxX;
	double maxY;
	double minX;
	double minY;

	double step;

	public Trilateration() {
		beacon1 = null;
		beacon2 = null;
		beacon3 = null;

		xPos = 0;
		yPos = 0;
		
		step = 0.1;
	}

	public void setBeacon1(double x, double y, double A, double N, double rssi, int minor) {
		beacon1 = new beaconPos(minor, x, y).calDist(A, N, rssi);
	}

	public void simBeacon1(double x, double y, double dist, int minor) {
		beacon1 = new beaconPos(minor, x, y).setBeacon(minor, x, y, dist);
	}

	public void setBeacon2(double x, double y, double A, double N, double rssi, int minor) {
		beacon2 = new beaconPos(minor, x, y).calDist(A, N, rssi);
	}

	public void simBeacon2(double x, double y, double dist, int minor) {
		beacon2 = new beaconPos(minor, x, y).setBeacon(minor, x, y, dist);
	}

	public void setBeacon3(double x, double y, double A, double N, double rssi, int minor) {
		beacon3 = new beaconPos(minor, x, y).calDist(A, N, rssi);
	}

	public void simBeacon3(double x, double y, double dist, int minor) {
		beacon3 = new beaconPos(minor, x, y).setBeacon(minor, x, y, dist);
	}

	public boolean calculate() {
		if ((beacon1 == null) || (beacon2 == null) || (beacon3 == null)) {
			return false;
		}

		// ( x1 - x0 )^2 + ( y1 - y0 )^2 = d1^2
		// ( x2 - x0 )^2 + ( y2 - y0 )^2 = d2^2
		// ( x3 - x0 )^2 + ( y3 - y0 )^2 = d3^2
		double tmp1 = Math.pow(beacon1.getX(), 2) - Math.pow(beacon2.getX(), 2) + Math.pow(beacon1.getY(), 2)
				- Math.pow(beacon2.getY(), 2) + Math.pow(beacon2.getDist(), 2) - Math.pow(beacon1.getDist(), 2);
		double tmp2 = Math.pow(beacon2.getX(), 2) - Math.pow(beacon3.getX(), 2) + Math.pow(beacon2.getY(), 2)
				- Math.pow(beacon3.getY(), 2) + Math.pow(beacon3.getDist(), 2) - Math.pow(beacon2.getDist(), 2);
		double tmp3 = beacon2.getX() - beacon3.getX();
		double tmp4 = beacon1.getX() - beacon2.getX();
		double tmp5 = beacon1.getY() - beacon2.getY();
		double tmp6 = beacon2.getY() - beacon3.getY();

		double tmp7 = tmp1 * tmp3 - tmp2 * tmp4;
		double tmp8 = 2 * tmp3 * tmp5 - 2 * tmp4 * tmp6;
		yPos = tmp7 / tmp8;

		double tmp9 = 2 * tmp5 * yPos;
		double tmp10 = tmp1 - tmp9;
		xPos = tmp10 / (2 * tmp4);
		return true;
	}

	void refreshMinMaxX() {
		double x1 = beacon1.getX();
		double x2 = beacon2.getX();
		double x3 = beacon3.getX();

		if (x1 > x2) {
			maxX = (x1 > x3) ? x1 : x3;
			minX = (x2 > x3) ? x3 : x2;
		} else {
			maxX = (x2 > x3) ? x2 : x3;
			minX = (x1 > x3) ? x3 : x1;
		}
	}

	void refreshMinMaxY() {
		double y1 = beacon1.getY();
		double y2 = beacon2.getY();
		double y3 = beacon3.getY();

		if (y1 > y2) {
			maxY = (y1 > y3) ? y1 : y3;
			minY = (y2 > y3) ? y3 : y2;
		} else {
			maxY = (y2 > y3) ? y2 : y3;
			minY = (y1 > y3) ? y3 : y1;
		}
	}

    public void searchPoint() {
		double minDiff = 0;
		double searchX = 0;
		double searchY = 0;
		boolean pointFound = false;
		
		if ((beacon1 == null) || (beacon2 == null) || (beacon3 == null)) {
			return;
		}
		
		updateBeaconDist();
		
		refreshMinMaxX();
		refreshMinMaxY();
		
		logger.info("beacon{} x {} y {} dist {}", beacon1.getMinor(), beacon1.getX(), beacon1.getY(), beacon1.getDist());
		logger.info("beacon{} x {} y {} dist {}", beacon2.getMinor(), beacon2.getX(), beacon2.getY(), beacon2.getDist());
		logger.info("beacon{} x {} y {} dist {}", beacon3.getMinor(), beacon3.getX(), beacon3.getY(), beacon3.getDist());
		logger.info("minX {}, maxX {}, minY {}, maxY {}, step {}", minX, maxX, minY, maxY, step);
		
		//防止被扫到的beacon地图上布置在很远的地方，导致长时间计算
		if(maxX -minX > 50 || maxY - minY > 50)
		{
			logger.info("error: beacon too far away, > 50meter, ignor");
			xPos = yPos = 0;//忽略此次扫描结果
			return;
		}
		
		for (double tmpX = minX; tmpX <= maxX; tmpX += step) {
			for (double tmpY = minY; tmpY <= maxY; tmpY += step) {
				if (!isPointInTriangle(tmpX, tmpY)) {
					continue;
				}
				
				double dist1 = Math.sqrt(Math.pow(tmpX-beacon1.getX(), 2) + Math.pow(tmpY-beacon1.getY(), 2));
				if (dist1 < 0.00001) {
					dist1 = 0.00001;
				}
				
				double dist2 = Math.sqrt(Math.pow(tmpX-beacon2.getX(), 2) + Math.pow(tmpY-beacon2.getY(), 2));
				if (dist2 < 0.00001) {
					dist2 = 0.00001;
				}
				double dist3 = Math.sqrt(Math.pow(tmpX-beacon3.getX(), 2) + Math.pow(tmpY-beacon3.getY(), 2));
				if (dist3 < 0.00001) {
					dist3 = 0.00001;
				}
				
				double div1 = dist1/dist2;
				double div2 = dist1/dist3;
				double div3 = dist2/dist3;
				
				double div1Comp = beacon1.getDist()/beacon2.getDist();
				double div2Comp = beacon1.getDist()/beacon3.getDist();
				double div3Comp = beacon2.getDist()/beacon3.getDist();
				
				double diff = Math.pow(div1-div1Comp, 2)
									+Math.pow(div2-div2Comp, 2)
									+Math.pow(div3-div3Comp, 2);
				
				if ((minDiff == 0) || (minDiff > diff)) {
					minDiff = diff;
					searchX = tmpX;
					searchY = tmpY;
					pointFound = true;
					//logger.info("better point: dist1 {} dist2 {} dist3 {} {}-{} ", dist1, dist2, dist3, tmpX, tmpY);
				}
			}
		}
		
		if (pointFound == false) {
			logger.info("triangle point not found, use average point");
			searchX = (maxX + minX)/2;
			searchY = (maxY + minY)/2;
		}
		
		xPos = searchX;
		yPos = searchY;
		logger.info("final point x {} y {}", xPos, yPos);
	}

	private void updateBeaconDist()
	{
		double tmp1 = beacon1.getX() - beacon2.getX();
		double tmp2 = beacon1.getY() - beacon2.getY();
		double dist1 = Math.sqrt(Math.pow(tmp1, 2) + Math.pow(tmp2, 2));
		tmp1 = beacon1.getX() - beacon3.getX();
		tmp2 = beacon1.getY() - beacon3.getY();
		double dist2 = Math.sqrt(Math.pow(tmp1, 2) + Math.pow(tmp2, 2));
		tmp1 = beacon2.getX() - beacon3.getX();
		tmp2 = beacon2.getY() - beacon3.getY();
		double dist3 = Math.sqrt(Math.pow(tmp1, 2) + Math.pow(tmp2, 2));
		
		double bigDist =	dist1 > dist2 ? (dist1 > dist3 ? dist1 : dist3) : (dist2 > dist3 ? dist2 : dist3);
		if(beacon1.getDist() > bigDist)
			beacon1.setDist(bigDist);
		if(beacon2.getDist() > bigDist)
			beacon2.setDist(bigDist);
		if(beacon3.getDist() > bigDist)
			beacon3.setDist(bigDist);
	}
	public double getXPos() {
		return xPos;
	}

	public double getYPos() {
		return yPos;
	}

	public double getBeacon1Dist() {
		return beacon1.getDist();
	}
	
	public double getBeacon2Dist() {
		return beacon2.getDist();
	}
	
	public double getBeacon3Dist() {
		return beacon3.getDist();
	}
	
	public boolean isPointInTriangle(double x, double y) {
		
		boolean ret = isInTriangle(new POINT(beacon1.getX(), beacon1.getY()),
							new POINT(beacon2.getX(), beacon2.getY()),
							new POINT(beacon3.getX(), beacon3.getY()),
							new POINT(x, y));
		//logger.info("point {}-{} in triangle {}", x, y, ret);
		return ret;
	}
	
	private boolean isInTriangle(POINT A, POINT B, POINT C, POINT P) {  
		/*利用叉乘法进行判断,假设P点就是M点*/  
		double a = 0, b = 0, c = 0;  
          
		/*向量减法*/  
		POINT MA = new POINT(P.x - A.x,P.y - A.y);  
		POINT MB = new POINT(P.x - B.x,P.y - B.y);  
		POINT MC = new POINT(P.x - C.x,P.y - C.y);  
          
		/*向量叉乘*/  
		a = MA.x * MB.y - MA.y * MB.x;  
		b = MB.x * MC.y - MB.y * MC.x;  
		c = MC.x * MA.y - MC.y * MA.x;  
          
		if((a <= 0 && b <= 0 && c <= 0)||  
				(a > 0 && b > 0 && c > 0))  
		{  
			return true;  
		}  

		return false;  
	}  
}

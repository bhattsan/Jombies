package org.jombie.common;

public class Vector {
	int xCoord, yCoord;

	public int getxCoord() {
		return xCoord;
	}

	public void setxCoord(int xCoord) {
		this.xCoord = xCoord;
	}

	public int getyCoord() {
		return yCoord;
	}

	public void setyCoord(int yCoord) {
		this.yCoord = yCoord;
	}

	public double findAngle(Vector other) {
		int xHit = other.xCoord;
		int yHit = other.yCoord;
		double angle;
		int dX, dY;
		dX = xHit - xCoord;
		dY = yCoord - yHit;
		if (xHit == 0) {
			angle = Math.PI / 2 * (yHit > 0 ? 1 : -1);
		} else {
			angle = Math.atan((double) dY / dX);
			if (dX < 0) {
				angle += Math.PI;
			}
		}
		return angle;
	}
	public double findAngle(){
		double angle;
		int dX, dY;
		dX = xCoord;
		dY = yCoord;
		if (dX == 0) {
			angle = Math.PI / 2 * (yCoord> 0 ? 1 : -1);
		} else {
			angle = Math.atan((double) dY / dX);
			if (dX < 0) {
				angle += Math.PI;
			}
		}
		return angle;
	}
	
	public void addScalarVector(Vector direction, int speed){
		setxCoord((int)(xCoord+speed*Math.cos(direction.findAngle())));
		setyCoord((int)(yCoord-speed*Math.sin(direction.findAngle())));
	}
	@Override
	public String toString() {
		return xCoord+","+yCoord;
	}
	public double distanceBetween(Vector other){
		return Math.sqrt(Math.pow(Math.abs(other.xCoord-xCoord),2) + Math.pow(Math.abs(other.yCoord-yCoord),2));
	}
}

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
		System.out.printf("(%d, %d)\t", xHit, yHit);
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
}

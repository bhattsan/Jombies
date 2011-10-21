package org.jombie.common;

public class Vector {
	double xCoord, yCoord;

	public double getxCoord() {
		return xCoord;
	}

	public void setxCoord(double xCoord) {
		this.xCoord = xCoord;
	}

	public double getyCoord() {
		return yCoord;
	}

	public void setyCoord(double yCoord) {
		this.yCoord = yCoord;
	}

	public static Vector getAngledVector(double angle) {
		Vector s = new Vector();
		s.setxCoord(10 * Math.cos(angle));
		s.setyCoord(10 * Math.sin(angle));
		return s;
	}

	public void setAngle(double angle) {
		setxCoord(10 * Math.cos(angle));
		setyCoord(10 * Math.sin(angle));
	}

	public double findAngle(Vector other) {
		double xHit = other.getxCoord();
		double yHit = other.getyCoord();
		double angle;
		double dX, dY;
		dX = xHit - getxCoord();
		dY = getyCoord() - yHit;
		if (xHit == 0) {
			angle = Math.PI / 2 * (yHit > 0 ? 1 : -1);
		} else {
			angle = Math.atan(dY / dX);
			if (dX < 0) {
				angle += Math.PI;
			}
		}
		return angle;
	}

	public double findAngle() {
		double angle;
		double dX, dY;
		dX = getxCoord();
		dY = getyCoord();
		if (dX == 0) {
			angle = Math.PI / 2 * (getyCoord() > 0 ? 1 : -1);
		} else {
			angle = Math.atan(dY / dX);
			if (dX < 0) {
				angle += Math.PI;
			}
		}
		return angle;
	}

	public void addScalarVector(Vector direction, int speed) {
		setxCoord(getxCoord() + speed * Math.cos(direction.findAngle()));
		setyCoord(getyCoord() - speed * Math.sin(direction.findAngle()));
	}

	public double distanceTo(Vector other) {
		return Math.sqrt(Math.pow(other.getxCoord() - getxCoord(), 2)
				+ Math.pow(other.getyCoord() - getyCoord(), 2));
	}

	@Override
	public String toString() {
		return getxCoord() + "," + getyCoord();
	}

	public double distanceBetween(Vector other) {
		return Math.sqrt(Math.pow(Math.abs(other.xCoord - xCoord), 2)
				+ Math.pow(Math.abs(other.yCoord - yCoord), 2));
	}
}

package org.jombie.projectile;

import org.jombie.common.Vector;

public abstract class Projectile {
	int speed;
	int radius;
	private String owner;
	public int getRadius() {
		return radius;
	}
	public void setRadius(int radius) {
		this.radius = radius;
	}
	public int getSpeed() {
		return speed;
	}
	public void setSpeed(int speed) {
		this.speed = speed;
	}
	public Vector getDirection() {
		return direction;
	}
	public void setDirection(Vector direction) {
		this.direction = direction;
	}
	public Vector getPosition() {
		return position;
	}
	public void setPosition(Vector position) {
		this.position = position;
	}
	public void updatePosition(){
		position.addScalarVector(direction, speed);
//		System.out.println(position);
		System.out.println(direction.findAngle());
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	Vector direction = new Vector();
	Vector position = new Vector();
	
}

package org.jombie.projectile;

import org.jombie.common.Vector;
import org.jombie.unit.Unit;

public abstract class Projectile {
	int speed;
	int radius;
	int damage;
	public static int projectileId = 0;
	public int id;
	Unit owner;
	
	public Unit getOwner() {
		return owner;
	}
	public void setOwner(Unit owner) {
		this.owner = owner;
	}
	public int getDamage() {
		return damage;
	}
	public void setDamage(int damage) {
		this.damage = damage;
	}
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
	}
	
	public boolean hasCollided(Unit other){
		if(position.distanceTo(other.location)<=other.size/2+radius/2){
			System.out.println("Collided into "+other.userId);
			return true;
		}
		return false;
	}
	
	Vector direction = new Vector();
	Vector position = new Vector();
	
}

package org.jombie.unit;

import org.jombie.common.Vector;
import org.jombie.weapon.Weapon;

public abstract class Unit {
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public Vector getLocation() {
		return location;
	}
	public void setLocation(Vector location) {
		this.location = location;
	}
	public int getHealth() {
		return health;
	}
	public void setHealth(int health) {
		this.health = health;
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
	public Weapon getMyWeapon() {
		return myWeapon;
	}
	public void setMyWeapon(Weapon myWeapon) {
		this.myWeapon = myWeapon;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public String userId;
	public Vector location;
	public int health;
	public int healthCapacity;
	public int speed;
	public int getHealthCapacity() {
		return healthCapacity;
	}
	public void setHealthCapacity(int healthCapacity) {
		this.healthCapacity = healthCapacity;
	}
	public Vector direction;
	public Weapon myWeapon;
	public int size;
	public enum Team{TEAM_A, TEAM_B};
	public Team myTeam;
	public abstract boolean attackPosition(Vector targetPosition);
}

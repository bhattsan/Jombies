package org.jombie.weapon;

import javax.swing.Timer;

import org.jombie.projectile.Projectile;


public abstract class RangedWeapon extends Weapon {
	Timer reloadTimer;
	
	public int getRange() {
		return range;
	}

	public void setRange(int range) {
		this.range = range;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public long getReloadTime() {
		return reloadTime;
	}

	public void setReloadTime(long reloadTime) {
		this.reloadTime = reloadTime;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public int getShootSpeed() {
		return shootSpeed;
	}

	public void setShootSpeed(int shootSpeed) {
		this.shootSpeed = shootSpeed;
	}

	public int getCurrentClip() {
		return currentClip;
	}

	public void setCurrentClip(int currentClip) {
		this.currentClip = currentClip;
	}

	int range;
	int capacity;
	long reloadTime;
	int weight;
	int shootSpeed;
	int currentClip;
	Projectile shell;

	public Projectile getShell() {
		return shell;
	}

	public void setShell(Projectile shell) {
		this.shell = shell;
	}

	public abstract boolean attack();
}

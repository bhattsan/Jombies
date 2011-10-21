package org.jombie.weapon;

public abstract class Weapon {

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getRadius() {
		return radius;
	}

	public void setRadius(int radius) {
		this.radius = radius;
	}

	String name;
	int radius;
	
	public abstract boolean attack();
}

package org.jombie.weapon;

public class RangedWeapon extends Weapon {
	int range;
	int capacity;
	int reloadTime;
	int weight;
	int shootSpeed;

	@Override
	public boolean attack() {
		return true;
	}
}

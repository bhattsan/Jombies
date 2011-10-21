package org.jombie.unit;

import org.jombie.common.Vector;
import org.jombie.weapon.Weapon;

public abstract class Unit {
	String userId;
	Vector location;
	int health;
	int speed;
	Vector direction;
	Weapon myWeapon;
	enum team{TEAM_A, TEAM_B};
	public abstract boolean attackPosition(Vector targetPosition);
}

package org.jombie.unit.marines;

import org.jombie.common.Vector;
import org.jombie.unit.Unit;
import org.jombie.weapon.Pistol;

public class Marine extends Unit {
	
	public Marine(){
		healthCapacity = 100;
		health = healthCapacity;
		speed = 5;
		userId = "New-001";
		location = new Vector();
		myWeapon = new Pistol();
		myTeam = Team.TEAM_A;
		size = 20;
	}

	@Override
	public boolean attackPosition(Vector targetPosition) {
		// TODO Auto-generated method stub
		return false;
	}

}

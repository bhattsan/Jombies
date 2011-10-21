package org.jombie.projectile;

import org.jombie.common.Vector;


public class PistolBullet extends Projectile {
	public PistolBullet(){
		speed = 4;
		radius = 5;
		damage = 5;
		id = projectileId++;
	}
	public void fire(Vector starting, Vector direction){ 
		
	}
}

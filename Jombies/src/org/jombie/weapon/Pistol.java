package org.jombie.weapon;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import org.jombie.projectile.PistolBullet;
import org.jombie.projectile.Projectile;

public class Pistol extends RangedWeapon {
	long millisLastEmpty;
	public Pistol() {
		reloadTime = 1000;
		reloadTimer = new Timer((int) reloadTime, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				currentClip = capacity;
				reloadTimer.stop();
			}
		});
		capacity = 64;
		currentClip = capacity;
		name = "Automatic Pistol";
		radius = 10;
		setShell(new PistolBullet());
	}

	@Override
	public Projectile getBullet() {
		long currentTime = System.currentTimeMillis();
		if(currentClip==0){
			if(currentTime-millisLastEmpty>reloadTime){
				currentClip = capacity-1;
				return new PistolBullet();
			}
			else
				return null;
		}
		currentClip--;
		if(currentClip==0){
			millisLastEmpty = currentTime;
			reloadTimer.start();
		}
		return new PistolBullet();
	}
}

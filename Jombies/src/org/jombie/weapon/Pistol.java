package org.jombie.weapon;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import org.jombie.projectile.PistolBullet;


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
		capacity = 16;
		currentClip = capacity;
		name = "Automatic Pistol";
		radius = 10;
		setShell(new PistolBullet());
	}

	@Override
	public boolean attack() {
		long currentTime = System.currentTimeMillis();
		if(currentClip==0){
			if(currentTime-millisLastEmpty>reloadTime){
				currentClip = capacity-1;
				return true;
			}
			else
				return false;
		}
		currentClip--;
		if(currentClip==0){
			millisLastEmpty = currentTime;
			reloadTimer.start();
		}
		return true;
	}
}

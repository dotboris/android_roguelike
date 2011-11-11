/*
 * Class Name:			Weapon.java
 * Class Purpose:		A template generated weapon
 * Created by:			boris on 2011-11-11
 */
package name.bobnet.android.rl.core.ents.tiles;

import name.bobnet.android.rl.core.ents.Entity;
import name.bobnet.android.rl.core.message.Message;

public class Weapon implements Entity {

	// variables
	private int dmg_low, dmg_high;
	private int req_str, req_int, req_dex;

	/**
	 * Create a weapons
	 * 
	 * @param dmg_low
	 *            the minimum damage the weapon does
	 * @param dmg_high
	 *            the maximum damage the weapon does
	 * @param req_str
	 *            the strength required to use this weapons
	 * @param req_int
	 *            the intellect requited to use this weapon
	 * @param req_dex
	 *            the dexterity required to use this weapon
	 */
	public Weapon(int dmg_low, int dmg_high, int req_str, int req_int,
			int req_dex) {
		this.dmg_low = dmg_low;
		this.dmg_high = dmg_high;
		this.req_str = req_str;
		this.req_int = req_int;
		this.req_dex = req_dex;
	}

	/**
	 * @return the dmg_low
	 */
	public int getDmg_low() {
		return dmg_low;
	}

	/**
	 * @return the dmg_high
	 */
	public int getDmg_high() {
		return dmg_high;
	}

	/**
	 * @return the req_str
	 */
	public int getReq_str() {
		return req_str;
	}

	/**
	 * @return the req_int
	 */
	public int getReq_int() {
		return req_int;
	}

	/**
	 * @return the req_dex
	 */
	public int getReq_dex() {
		return req_dex;
	}

	/* (non-Javadoc)
	 * @see name.bobnet.android.rl.core.ents.Entity#tick()
	 */
	@Override
	public void tick() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see name.bobnet.android.rl.core.ents.Entity#processMessage(name.bobnet.android.rl.core.message.Message)
	 */
	@Override
	public void processMessage(Message message) {
		// TODO Auto-generated method stub

	}

}

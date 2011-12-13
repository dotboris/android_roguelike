/*
 * Class Name:			Equipment.java
 * Class Purpose:		Equipment that can be worn
 * Created by:			boris on 2011-11-15
 */
package name.bobnet.android.rl.core.ents;

import name.bobnet.android.rl.core.ents.Creature.EquipSlots;

public abstract class Equipment extends Item {

	// variables
	protected EquipSlots slot;

	public Equipment(int weight, String display, String name, int tileSheet,
			int tileSheet_x, int tileSheet_y, EquipSlots slot) {
		super(weight, display, name, tileSheet, tileSheet_x, tileSheet_y);

		this.slot = slot;
	}

	/**
	 * @return the slot
	 */
	public EquipSlots getSlot() {
		return slot;
	}

	/**
	 * @param slot
	 *            the slot to set
	 */
	public void setSlot(EquipSlots slot) {
		this.slot = slot;
	}

}

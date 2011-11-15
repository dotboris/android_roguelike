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

	public Equipment(int weight, String display, String name, EquipSlots slot) {
		super(weight, display, name);

		this.slot = slot;
	}

}

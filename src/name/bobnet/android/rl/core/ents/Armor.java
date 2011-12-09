/*
 * Class Name:			Armor.java
 * Class Purpose:		Armor with defence
 * Created by:			boris on 2011-11-15
 */
package name.bobnet.android.rl.core.ents;

import name.bobnet.android.rl.core.ents.Creature.EquipSlots;
import name.bobnet.android.rl.core.message.Message;

public class Armor extends Equipment {

	// variables
	protected int defence;

	public Armor(int weight, String display, String name, EquipSlots slot,
			int defence) {
		super(weight, display, name, slot);

		this.defence = defence;
	}

	/* (non-Javadoc)
	 * @see name.bobnet.android.rl.core.ents.Entity#tick()
	 */
	@Override
	public void tick() {
		// do nothing
	}

	/* (non-Javadoc)
	 * @see name.bobnet.android.rl.core.ents.Entity#processMessage(name.bobnet.android.rl.core.message.Message)
	 */
	@Override
	public void processMessage(Message message) {
		// We don't deal with messages

	}

	/**
	 * @return the defence
	 */
	public int getDefence() {
		return defence;
	}

	/**
	 * @param defence
	 *            the defence to set
	 */
	public void setDefence(int defence) {
		this.defence = defence;
	}

}

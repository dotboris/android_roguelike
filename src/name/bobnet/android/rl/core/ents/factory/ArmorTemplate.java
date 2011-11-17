/*
 * Class Name:			ArmorTemplate.java
 * Class Purpose:		Template that loads and generates armors
 * Created by:			boris on 2011-11-17
 */
package name.bobnet.android.rl.core.ents.factory;

import java.util.Random;

import org.json.JSONException;
import org.json.JSONObject;

import name.bobnet.android.rl.core.ents.Armor;
import name.bobnet.android.rl.core.ents.Entity;
import name.bobnet.android.rl.core.ents.Creature.EquipSlots;

public class ArmorTemplate extends ItemTemplate {

	// variables
	protected int min_defence, max_defence;
	protected EquipSlots slot;

	/* (non-Javadoc)
	 * @see name.bobnet.android.rl.core.ents.factory.Template#generate(java.util.Random)
	 */
	@Override
	public Entity generate(Random rnd) {
		// generate a new armon piece
		return new Armor(weight, display, name, slot, rndIntRange(rnd,
				min_defence, max_defence));
	}

	@Override
	public void load(JSONObject self, String[] path) throws JSONException,
			NullPointerException {
		super.load(self, path);

		// load defence
		max_defence = self.getInt("max_defence");
		min_defence = self.getInt("min_defence");

		// figure out what slot we are in
		String sSlot = path[path.length - 1];
		if (sSlot.equals("head")) {
			slot = EquipSlots.HEAD;
		} else if (sSlot.equals("chest")) {
			slot = EquipSlots.CHEST;
		} else if (sSlot.equals("legs")) {
			slot = EquipSlots.LEGS;
		} else if (sSlot.equals("hands")) {
			slot = EquipSlots.HANDS;
		} else if (sSlot.equals("feet")) {
			slot = EquipSlots.FEET;
		} else if (sSlot.equals("shield")) {
			slot = EquipSlots.SHIELD;
		} else if (sSlot.equals("back")) {
			slot = EquipSlots.BACK;
		}
	}
}

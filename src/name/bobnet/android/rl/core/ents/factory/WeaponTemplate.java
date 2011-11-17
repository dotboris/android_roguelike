/*
 * Class Name:			WeaponTemplate.java
 * Class Purpose:		A template that generates weapons
 * Created by:			boris on 2011-11-11
 */
package name.bobnet.android.rl.core.ents.factory;

import java.util.Random;

import org.json.JSONException;
import org.json.JSONObject;

import name.bobnet.android.rl.core.ents.Creature.EquipSlots;
import name.bobnet.android.rl.core.ents.Entity;
import name.bobnet.android.rl.core.ents.Weapon;

public class WeaponTemplate extends ItemTemplate {

	private int dmg_low_min, dmg_low_max, dmg_high_min, dmg_high_max;
	private int req_int, req_dex, req_str;
	private boolean allow_curse, allow_band, allow_enchant;

	/* (non-Javadoc)
	 * @see name.bobnet.android.rl.core.ents.factory.Template#generate(java.util.Random)
	 */
	@Override
	public Entity generate(Random rnd) {
		// resulting entity
		Weapon res;

		// variables
		int dmg_low, dmg_high;

		// pick random damage
		dmg_low = rndIntRange(rnd, dmg_low_min, dmg_low_max);
		dmg_high = rndIntRange(rnd, dmg_high_min, dmg_high_max);

		// create new weapon
		res = new Weapon(weight, display, name, EquipSlots.WEAPON, dmg_low,
				dmg_high, req_str, req_int, req_dex);

		// return the weapon
		return res;
	}

	@Override
	public void load(JSONObject self, String[] path) throws JSONException,
			NullPointerException {
		super.load(self, path);

		// get all the fields

		// item info
		weight = self.getInt("weight");
		display = self.getString("display");

		// damage
		dmg_low_min = self.getInt("dmg_low_min");
		dmg_low_max = self.getInt("dmg_low_max");
		dmg_high_min = self.getInt("dmg_high_min");
		dmg_high_max = self.getInt("dmg_high_max");

		// requirements
		req_int = self.getInt("req_int");
		req_dex = self.getInt("req_dex");
		req_str = self.getInt("req_str");

		// flags
		allow_band = self.getBoolean("allow_band");
		allow_curse = self.getBoolean("allow_curse");
		allow_enchant = self.getBoolean("allow_enchant");
	}

}

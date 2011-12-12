/*
 * Class Name:			CreatureTemplate.java
 * Class Purpose:		Template used to load creature
 * Created by:			boris on 2011-11-15
 */
package name.bobnet.android.rl.core.ents.factory;

import java.util.Random;

import org.json.JSONException;
import org.json.JSONObject;

import name.bobnet.android.rl.core.ai.HumanoidAI;
import name.bobnet.android.rl.core.ents.Creature;
import name.bobnet.android.rl.core.ents.Entity;

public class CreatureTemplate extends Template {

	protected int strength, intellect, dexterity, vitality;
	protected int res_frost, res_fire, res_air, res_earth, res_holy, res_evil;
	protected int xpWorth, invSize;

	@Override
	public void load(JSONObject self, String[] path) throws JSONException,
			NullPointerException {
		super.load(self, path);

		// load content

		// stats
		strength = self.getInt("strength");
		dexterity = self.getInt("dexterity");
		vitality = self.getInt("vitality");
		intellect = self.getInt("intellect");

		// resistances
		res_air = self.getInt("res_air");
		res_earth = self.getInt("res_earth");
		res_fire = self.getInt("res_fire");
		res_frost = self.getInt("res_frost");
		res_evil = self.getInt("res_evil");
		res_holy = self.getInt("res_holy");

		// other information
		xpWorth = self.getInt("xpworth");
		invSize = self.getInt("invsize");
	}

	/* (non-Javadoc)
	 * @see name.bobnet.android.rl.core.ents.factory.Template#generate(java.util.Random)
	 */
	@Override
	public Entity generate(Random rnd) {
		return new Creature(display, name, tileSheet, tileSheet_x, tileSheet_y,
				strength, intellect, dexterity, vitality, res_frost, res_fire,
				res_air, res_earth, res_holy, res_evil, xpWorth, invSize,
				new HumanoidAI());
	}

}

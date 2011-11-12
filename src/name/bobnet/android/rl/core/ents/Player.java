package name.bobnet.android.rl.core.ents;

import name.bobnet.android.rl.core.GameEngine;
import name.bobnet.android.rl.core.message.Message;

/**
 * The player
 * 
 * @author boris
 */
public class Player extends Creature {

	// constants
	private static final int XP_BASE = 80;
	private static final int XP_INCREMENT = 20;

	// variables
	private int level, experience, nextLevelXP;

	public Player(int strength, int intellect, int dexterity, int vitality,
			int res_frost, int res_fire, int res_air, int res_earth,
			int res_holy, int res_evil) {
		super(strength, intellect, dexterity, vitality, res_frost, res_fire,
				res_air, res_earth, res_holy, res_evil, 0);

		// set experience values
		setLevel(1);

		// make sure we tick
		GameEngine.getEngine().regEntTick(this);
	}

	@Override
	public void processMessage(Message message) {
		super.processMessage(message);

		// parse messages
		switch (message.getMessageType()) {
		case M_KILLED:
			// get xp
			Object oXP = message.getArgument("xp");
			if (oXP != null && oXP instanceof Integer)
				giveXP((Integer) oXP);
			break;
		case M_DESTROY:
			// unregister from ticks (not that it matters since the game stops
			// when the player dies, but better be sure)
			GameEngine.getEngine().unregEntTick(this);
			break;
		}
	}

	public void giveXP(int amount) {
		// add to the pool
		experience += amount;

		// hit new level
		if (experience >= nextLevelXP) {
			// set the new level
			setLevel(level + 1);
		}
	}

	private void setLevel(int level) {
		this.level = level;
		experience = 0;
		nextLevelXP = XP_BASE + this.level * XP_INCREMENT;
	}

	/**
	 * @return the level
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * @return the experience
	 */
	public int getExperience() {
		return experience;
	}

	/**
	 * @return the nextLevelXP
	 */
	public int getNextLevelXP() {
		return nextLevelXP;
	}

}

package name.bobnet.android.rl.core.ents;

import name.bobnet.android.rl.core.Action;
import name.bobnet.android.rl.core.ActionsManager;
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
	private String cAction;
	private int cActionTime;
	private int tickCount;
	private Entity aEnt;

	public Player(int strength, int intellect, int dexterity, int vitality,
			int res_frost, int res_fire, int res_air, int res_earth,
			int res_holy, int res_evil) {
		super(strength, intellect, dexterity, vitality, res_frost, res_fire,
				res_air, res_earth, res_holy, res_evil, 0);

		// set experience values
		setLevel(1);

		// set tick management
		tickCount = 0;
		cActionTime = 0;
		cAction = null;

		// make sure we tick
		GameEngine.getEngine().regEntTick(this);

	}

	/**
	 * Get the number of ticks an action will take
	 * 
	 * @param actionName
	 *            the string name of the action
	 * @return the time it takes to do said action
	 */
	public int getActionTicks(String actionName) {
		// get the base ticks for the action
		int bTicks = ActionsManager.getActionManager().getActionTicks(
				actionName);

		// TODO: change speed depending on stats

		// return the ticks to use
		return bTicks;
	}

	public boolean setNextAction(Action a) {
		// check if we can do the action
		if (a.name.equals("A_WALK")) {
			if (!(a.ent != null && a.ent instanceof Tile && ((Tile) a.ent)
					.isPassthrough())) {
				return false;
			}
		} else if (a.name.equals("A_ATTACK")) {

		} else if (a.name.equals("A_PICKUP")) {

		} else if (a.name.equals("A_EAT")) {

		} else if (a.name.equals("A_READ")) {

		} else if (a.name.equals("A_WAIT")) {

		} else
			// I don't know how to do this
			return false;

		// set action information
		cAction = a.name;
		cActionTime = a.ticks;
		this.aEnt = a.ent;

		// everything checks in start ticking
		return true;
	}

	@Override
	public void tick() {
		super.tick();

		// check if we are doing anything
		if (cAction != null) {
			// update the counter
			tickCount++;

			// check if we waited long enough
			if (tickCount >= cActionTime) {
				// reset the counter and action
				tickCount = 0;
				cActionTime = 0;

				// TODO: Do the action
				if (cAction.equals("A_WALK")) {
					GameEngine.getEngine().getCurrentDungeon()
							.moveCreature((Tile) aEnt, this);
				} else if (cAction.equals("A_ATTACK")) {

				} else if (cAction.equals("A_PICKUP")) {

				} else if (cAction.equals("A_EAT")) {

				} else if (cAction.equals("A_READ")) {

				} else if (cAction.equals("A_WAIT")) {
					// don't do anything
				}

				// clear the tile
				aEnt = null;
				cAction = null;
			}
		}
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

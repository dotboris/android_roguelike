package name.bobnet.android.rl.core.ents;

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
				res_air, res_earth, res_holy, res_evil);

		// set experience values
		setLevel(1);
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

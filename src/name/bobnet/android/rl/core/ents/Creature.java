package name.bobnet.android.rl.core.ents;

import name.bobnet.android.rl.core.message.Message;

/**
 * A creature that can move and take damage
 * 
 * @author boris
 */
public class Creature implements Entity {

	// variables
	protected int health, mana;
	protected int strength, intellect, dexterity, vitality;
	protected int res_frost, res_fire, res_air, res_earth, res_holy, res_evil;

	/**
	 * @param strength
	 *            the strength of the creature
	 * @param intellect
	 *            the intellect of the creature
	 * @param dexterity
	 *            the dexterity of the creature
	 * @param vitality
	 *            the vitality of the creature
	 * @param res_frost
	 *            the creature's resistance to frost damage
	 * @param res_fire
	 *            the creature's resistance to fire damage
	 * @param res_air
	 *            the creature's resistance to air damage
	 * @param res_earth
	 *            the creature's resistance to earth damage
	 * @param res_holy
	 *            the creature's resistance to holy damage
	 * @param res_evil
	 *            the creature's resistance to evil damage
	 */
	public Creature(int strength, int intellect, int dexterity, int vitality,
			int res_frost, int res_fire, int res_air, int res_earth,
			int res_holy, int res_evil) {
		this.strength = strength;
		this.intellect = intellect;
		this.dexterity = dexterity;
		this.vitality = vitality;
		this.res_frost = res_frost;
		this.res_fire = res_fire;
		this.res_air = res_air;
		this.res_earth = res_earth;
		this.res_holy = res_holy;
		this.res_evil = res_evil;
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

	/**
	 * @return the res_frost
	 */
	public int getRes_frost() {
		return res_frost;
	}

	/**
	 * @param res_frost
	 *            the res_frost to set
	 */
	public void setRes_frost(int res_frost) {
		this.res_frost = res_frost;
	}

	/**
	 * @return the res_fire
	 */
	public int getRes_fire() {
		return res_fire;
	}

	/**
	 * @param res_fire
	 *            the res_fire to set
	 */
	public void setRes_fire(int res_fire) {
		this.res_fire = res_fire;
	}

	/**
	 * @return the res_air
	 */
	public int getRes_air() {
		return res_air;
	}

	/**
	 * @param res_air
	 *            the res_air to set
	 */
	public void setRes_air(int res_air) {
		this.res_air = res_air;
	}

	/**
	 * @return the res_earth
	 */
	public int getRes_earth() {
		return res_earth;
	}

	/**
	 * @param res_earth
	 *            the res_earth to set
	 */
	public void setRes_earth(int res_earth) {
		this.res_earth = res_earth;
	}

	/**
	 * @return the res_holy
	 */
	public int getRes_holy() {
		return res_holy;
	}

	/**
	 * @param res_holy
	 *            the res_holy to set
	 */
	public void setRes_holy(int res_holy) {
		this.res_holy = res_holy;
	}

	/**
	 * @return the res_evil
	 */
	public int getRes_evil() {
		return res_evil;
	}

	/**
	 * @param res_evil
	 *            the res_evil to set
	 */
	public void setRes_evil(int res_evil) {
		this.res_evil = res_evil;
	}

	/**
	 * @return the health
	 */
	public int getHealth() {
		return health;
	}

	/**
	 * @param health
	 *            the health to set
	 */
	public void setHealth(int health) {
		this.health = health;
	}

	/**
	 * @return the mana
	 */
	public int getMana() {
		return mana;
	}

	/**
	 * @param mana
	 *            the mana to set
	 */
	public void setMana(int mana) {
		this.mana = mana;
	}

	/**
	 * @return the strength
	 */
	public int getStrength() {
		return strength;
	}

	/**
	 * @param strength
	 *            the strength to set
	 */
	public void setStrength(int strength) {
		this.strength = strength;
	}

	/**
	 * @return the intellect
	 */
	public int getIntellect() {
		return intellect;
	}

	/**
	 * @param intellect
	 *            the intellect to set
	 */
	public void setIntellect(int intellect) {
		this.intellect = intellect;
	}

	/**
	 * @return the dexterity
	 */
	public int getDexterity() {
		return dexterity;
	}

	/**
	 * @param dexterity
	 *            the dexterity to set
	 */
	public void setDexterity(int dexterity) {
		this.dexterity = dexterity;
	}

	/**
	 * @return the vitality
	 */
	public int getVitality() {
		return vitality;
	}

	/**
	 * @param vitality
	 *            the vitality to set
	 */
	public void setVitality(int vitality) {
		this.vitality = vitality;
	}

}

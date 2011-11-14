package name.bobnet.android.rl.core.ents;

import java.util.ArrayList;
import java.util.Iterator;

import android.util.Log;
import name.bobnet.android.rl.core.MessageManager;
import name.bobnet.android.rl.core.message.Message;
import name.bobnet.android.rl.core.message.Message.MessageType;

/**
 * A creature that can move and take damage
 * 
 * @author boris
 */
public class Creature extends TemplateEntity {

	/**
	 * Types of elemental damage to be used with the M_DO_DAMAGE Message
	 * 
	 * @author boris
	 */
	public enum Elements {
		FROST, FIRE, AIR, EARTH, HOLY, EVIL
	}

	// variables
	protected int health, mana;
	protected int strength, intellect, dexterity, vitality;
	protected int res_frost, res_fire, res_air, res_earth, res_holy, res_evil;
	protected int xpWorth;
	protected ArrayList<Item> inventory;
	protected int invSize;

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
	 * @param xpWorth
	 *            how much XP this creature is worth
	 */
	public Creature(String display, String name, int strength, int intellect,
			int dexterity, int vitality, int res_frost, int res_fire,
			int res_air, int res_earth, int res_holy, int res_evil,
			int xpWorth, int invSize) {
		super(display, name);

		// set attributes
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
		this.xpWorth = xpWorth;
		this.health = getMaxHealth();
		this.mana = getMaxMana();

		// create inventory
		inventory = new ArrayList<Item>();

		// set the size
		this.invSize = invSize;
	}

	public void pickUpItem(Item i) {
		// variables
		Tile tile = (Tile) getParent();
		boolean res = false;

		// check if we can pick up the item
		Iterator<Item> it = tile.getItemsIterator();
		while (it.hasNext()) {
			if (it.next() == i) {
				// found it
				res = true;
				break;
			}
		}

		if (res) {
			// add it to our inventory
			inventory.add(i);

			// remove the item from the tile
			it.remove();

			// inform that tile that we removed the object
			Message m = new Message(this, tile, MessageType.M_ENT_LEAVE_TILE);
			m.setArgument("what", i);
			MessageManager.getMessenger().sendMessage(m);
		}
	}

	public void dropItem(Item i) {
		// variables
		Tile tile = (Tile) getParent();
		boolean good = false;

		// check if the item exists
		Iterator<Item> it = inventory.iterator();
		while (it.hasNext()) {
			if (it.next() == i) {
				// it's there
				good = true;
				break;
			}
		}

		if (good) {
			// add the item to the tile
			tile.addItem(i);

			// remove the item from the inventory
			it.remove();

			// inform that tile of the new item
			Message m = new Message(this, tile, MessageType.M_ENT_ENTER_TILE);
			m.setArgument("what", i);
			MessageManager.getMessenger().sendMessage(m);
		}
	}

	/* (non-Javadoc)
	 * @see name.bobnet.android.rl.core.ents.Entity#tick()
	 */
	@Override
	public void tick() {
		// TODO Do AI stuff

	}

	/* (non-Javadoc)
	 * @see name.bobnet.android.rl.core.ents.Entity#processMessage(name.bobnet.android.rl.core.message.Message)
	 */
	@Override
	public void processMessage(Message message) {
		// process damage messages
		switch (message.getMessageType()) {
		case M_DO_DAMAGE:
			// elemental damage
			try {
				// get damage
				Object oDmg = message.getArgument("elemental_dmg");

				// check if we have elemental damage
				if (oDmg != null && oDmg instanceof Integer) {
					// get magic damage
					int eDmg = (Integer) oDmg;
					int nDmg = 0;

					// add damage to resistance
					switch ((Elements) message
							.getArgument("elemental_dmg_type")) {
					case AIR:
						nDmg = (int) (eDmg - ((float) res_air / 100) * eDmg);
						break;
					case EARTH:
						nDmg = (int) (eDmg - ((float) res_earth / 100) * eDmg);
						break;
					case EVIL:
						nDmg = (int) (eDmg - ((float) res_evil / 100) * eDmg);
						break;
					case FIRE:
						nDmg = (int) (eDmg - ((float) res_fire / 100) * eDmg);
						break;
					case FROST:
						nDmg = (int) (eDmg - ((float) res_frost / 100) * eDmg);
						break;
					case HOLY:
						nDmg = (int) (eDmg - ((float) res_holy / 100) * eDmg);
						break;
					}

					// do elemental damage
					if (nDmg > 0) {
						health -= nDmg;
					}

				}
			} catch (Exception e) {
				// No elemental damage
			}

			// normal damage

			// get damage
			Object oDmg = message.getArgument("dmg");

			// TODO: Implement defence

			// check if it's good
			if (oDmg != null && oDmg instanceof Integer) {
				// get the damage value
				int dmg = (Integer) oDmg;

				// do damage
				if (dmg > 0) {
					health -= dmg;
				}
			}

			// check if we died
			if (health <= 0) {
				// send a message to the killer with the XP they got
				Message m = new Message(this, message.getSender(),
						MessageType.M_KILLED);
				m.setArgument("xp", xpWorth);
				MessageManager.getMessenger().sendMessage(m);

				// remove ourselves from the tile
				((Tile) getParent()).delMob();

				// send a message saying that we left the tile
				m = new Message(this, getParent(), MessageType.M_ENT_LEAVE_TILE);
				m.setArgument("what", this);
				MessageManager.getMessenger().sendMessage(m);
			}

			// debug messages
			Log.d("RL", "Got hit health now: " + getHealth() + "/"
					+ getMaxHealth());

			break;
		}
	}

	/**
	 * @param size
	 *            the size of the inventory
	 */
	public void setInventorySize(int size) {
		invSize = size;
	}

	/**
	 * @param item
	 *            item to be added to the in inventory
	 */
	public void addToInventory(Item item) {
		inventory.add(item);
	}

	/**
	 * @return the iterator for the inventory
	 */
	public Iterator<Item> getInventoryIterator() {
		return inventory.iterator();
	}

	/**
	 * @return the xpWorth
	 */
	public int getXpWorth() {
		return xpWorth;
	}

	/**
	 * @param xpWorth
	 *            the xpWorth to set
	 */
	public void setXpWorth(int xpWorth) {
		this.xpWorth = xpWorth;
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
	 * @return the maximum health of the creature
	 */
	public int getMaxHealth() {
		return 2 + vitality * 2;
	}

	/**
	 * @return the macimum mana of the creature
	 */
	public int getMaxMana() {
		return intellect * 2;
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

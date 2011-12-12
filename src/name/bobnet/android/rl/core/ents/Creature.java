package name.bobnet.android.rl.core.ents;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Random;

import android.util.Log;
import name.bobnet.android.rl.core.ActionsManager;
import name.bobnet.android.rl.core.GameEngine;
import name.bobnet.android.rl.core.MessageManager;
import name.bobnet.android.rl.core.ai.AI;
import name.bobnet.android.rl.core.ai.PathNode;
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

	/**
	 * The slot in which equipment can be put
	 * 
	 * @author boris
	 */
	public enum EquipSlots {
		HEAD, CHEST, LEGS, BACK, HANDS, FEET, NECK, FINGERS, WEAPON, SHIELD
	}

	// constants
	public static final int LOS_SIZE = 10;

	// variables
	protected int health, mana;
	protected int strength, intellect, dexterity, vitality;
	protected int res_frost, res_fire, res_air, res_earth, res_holy, res_evil;
	protected int xpWorth;
	protected ArrayList<Item> inventory;
	protected int invSize;
	protected HashMap<EquipSlots, Equipment> equipment;
	protected PathNode[][] lineOfSight;
	protected AI ai;
	private boolean discoverer, firstTick;
	private PriorityQueue<PathNode> open;
	private ArrayList<PathNode> closed;
	private LinkedList<PathNode> path;

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
			int xpWorth, int invSize, AI ai) {
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

		// create equipment
		equipment = new HashMap<Creature.EquipSlots, Equipment>();

		// set the size
		this.invSize = invSize;

		// create a new LOS array
		lineOfSight = new PathNode[LOS_SIZE][LOS_SIZE];
		for (int x = 0; x < lineOfSight.length; x++) {
			for (int y = 0; y < lineOfSight[x].length; y++) {
				lineOfSight[x][y] = new PathNode();
			}
		}

		// create open and closed sets and the path
		open = new PriorityQueue<PathNode>();
		closed = new ArrayList<PathNode>();
		path = new LinkedList<PathNode>();

		// set the ai
		this.ai = ai;

		// determine if we should mark discovered tiles
		discoverer = this instanceof Player;

		// set the first tick
		firstTick = false;
		
		// init the ai
		ai.init(this);
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

	/* (non-Javadoc)
	 * @see name.bobnet.android.rl.core.ents.Entity#tick()
	 */
	@Override
	public void tick() {
		// first tick
		if (!firstTick) {
			// calculate the LOS for the first time
			calcLOS();
			
			// set the flag
			firstTick = true;
		}
		
		// let the AI tick
		ai.tick();
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

			// check if it's good
			if (oDmg != null && oDmg instanceof Integer) {
				// get the damage value
				int dmg = (Integer) oDmg;

				// absorbs damage
				dmg -= getDefence();

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

	public void calcPath(Tile goal) {
		// log start
		Log.d("RL", "Starting path calculation");

		// variables
		PathNode goalNode, startNode, currNode, neighbourNode;
		int gx, gy, ox, oy;
		boolean stopShort;

		// empty our sets
		open.clear();
		closed.clear();
		path.clear();

		// find the current node
		startNode = lineOfSight[LOS_SIZE / 2][LOS_SIZE / 2];

		// find the goal node
		gx = goal.getX();
		gy = goal.getY();
		ox = startNode.getTile().getX() - LOS_SIZE / 2;
		oy = startNode.getTile().getY() - LOS_SIZE / 2;
		goalNode = lineOfSight[gx - ox][gy - oy];
		stopShort = !goalNode.getTile().isPassthrough();

		// add our node to open
		open.add(startNode);

		// start searching
		search_loop: while (open.peek() != goalNode) {
			// get the current node and add it to closed
			currNode = open.poll();
			closed.add(currNode);

			// check the neighbours
			for (int x = -1; x <= 1; x++)
				for (int y = -1; y <= 1; y++) {
					// don't do 0,0
					if (x == 0 && y == 0)
						continue;

					// get the node
					try {
						neighbourNode = lineOfSight[(currNode.getTile().getX() + x)
								- ox][(currNode.getTile().getY() + y) - oy];
					} catch (Exception e) {
						// bad node, skip it
						continue;
					}

					// do a few checks
					if (stopShort && neighbourNode == goalNode) {
						// set the goal node to be the current node (we are
						// stopping short)
						goalNode = currNode;

						// bail out
						break search_loop;
					}

					if (neighbourNode.getTile() == null
							|| !neighbourNode.getTile().isPassthrough())
						continue;

					if (open.contains(neighbourNode)
							&& neighbourNode.getgScore() > PathNode.calcGScore(
									neighbourNode, currNode)) {
						// remove it from the open list as we have a better path
						// It'll be added to open later
						open.remove(neighbourNode);
					}
					if (closed.contains(neighbourNode)
							&& neighbourNode.getgScore() > PathNode.calcGScore(
									neighbourNode, currNode)) {
						// remove it from the closed list as we have a better
						// it'll be added to open later
						closed.remove(neighbourNode);
					}
					if (!open.contains(neighbourNode)
							&& !closed.contains(neighbourNode)) {
						// set the new parent for the neighbour
						neighbourNode.setParentNode(currNode);

						// recalculate the scores
						neighbourNode.calcScores(goalNode);

						// add neighbour to open
						open.add(neighbourNode);
					}
				}

			// check for partial path
			if (open.isEmpty()) {
				// We couldn't make a path, abort
				Log.d("RL", "Couldn't find path, aborting");
				return;
			}
		}

		// fill the path
		currNode = goalNode;
		while (currNode != startNode) {
			// add from the begining
			path.add(0, currNode);

			// get the parent
			currNode = currNode.getParentNode();
		}

		// log path end
		Log.d("RL", "Stopped claculating path");
	}

	/**
	 * Calculate the Line of sight with shadow casting
	 */
	public void calcLOS() {
		// variables
		int originX, originY;

		// get origin
		Tile t = (Tile) getParent();
		originX = t.getX();
		originY = t.getY();

		// empty the LOS matrix
		for (int x = 0; x < LOS_SIZE; x++)
			for (int y = 0; y < LOS_SIZE; y++)
				lineOfSight[x][y].setTile(null);

		// loop through all the direction
		for (int dirX = -1; dirX < 2; dirX += 2)
			for (int dirY = -1; dirY < 2; dirY += 2) {
				// set the slopes
				float slope = dirX * -dirY;

				// scan the vertical ones
				scanSector(slope, 0, 0, originX, originY, dirX, dirY, true);

				// scan the horizontal one
				scanSector(slope, 0, 0, originX, originY, dirX, dirY, false);
			}

	}

	private void scanSector(double slope1, double slope2, int startX,
			int originX, int originY, int dirX, int dirY, boolean scanVert) {
		// variables
		Tile cTile;
		boolean lastLine = false;
		boolean s1Set, s2Set;
		int tX = 0, tY = 0;

		// loop through the tiles to scan
		for (int x = startX; x < LOS_SIZE / 2 && x > -LOS_SIZE / 2 && !lastLine; x += dirX) {
			// set a few things for the new row
			s1Set = false;
			s2Set = false;
			double nSlope1 = slope1, nSlope2 = slope2;

			// loop in the row
			for (int y = (int) (dirY == -1 ? Math.ceil(x * slope1) : Math
					.floor(x * slope1)); y != (int) (x * slope2) + dirY; y += dirY) {
				// find the actual x and y pos
				int gX, gY, lX, lY;

				if (!scanVert) {
					// swap x and y
					tX = y;
					tY = x;
				} else {
					// standard x/y
					tX = x;
					tY = y;
				}

				// figure out global values
				gX = tX + originX;
				gY = tY + originY;
				lX = LOS_SIZE / 2 + tX;
				lY = LOS_SIZE / 2 + tY;

				try {
					// get the tile
					cTile = GameEngine.getEngine().getCurrentDungeon()
							.getTile(gX, gY);
				} catch (Exception e) {
					// out of bound get out
					continue;
				}

				// add it to our LOS
				lineOfSight[lX][lY].setTile(cTile);

				// set the tile to visible
				if (discoverer)
					cTile.setVisible(true);

				// check if we're done scanning
				if (!cTile.isSeeThrough())
					lastLine = true;

				// rules for tile hit
				if (!s1Set && cTile.isSeeThrough()) {
					// get the start slope

					// get the offset point (top left corner)
					float sX, sY;
					sX = x + dirX * 0.5f;
					sY = y - dirY * 0.5f;

					// find the new slope
					nSlope1 = sY / sX;

					// first slope set
					s1Set = true;
				} else if (!s2Set && s1Set && !cTile.isSeeThrough()) {
					// get the end slope

					// get the offset point (top left corner)
					float sX, sY;
					sX = x - dirX * 0.5f;
					sY = y - dirY * 0.5f;

					// find the new slope
					nSlope2 = sY / sX;

					// second slope set
					s2Set = true;

					// search the new area
					scanSector(nSlope1, nSlope2, x + dirX, originX, originY,
							dirX, dirY, scanVert);

					// unset slope flags
					s1Set = false;
					s2Set = false;
				}
			}

			// no last slope
			if (lastLine && s1Set && !s2Set) {
				// search the new area
				scanSector(nSlope1, slope2, x + dirX, originX, originY, dirX,
						dirY, scanVert);

				// unset slope flags
				s1Set = false;
				s2Set = false;
			}
		}
	}

	/**
	 * @return the ai
	 */
	public AI getAi() {
		return ai;
	}

	/**
	 * @param ai
	 *            the ai to set
	 */
	public void setAi(AI ai) {
		this.ai = ai;
	}

	public int getDefence() {
		// variables
		int defence = 0;

		// add up the defence
		try {
			defence += ((Armor) getEquipment(EquipSlots.CHEST)).getDefence();
		} catch (Exception e) {
		}
		try {
			defence += ((Armor) getEquipment(EquipSlots.BACK)).getDefence();
		} catch (Exception e) {
		}
		try {
			defence += ((Armor) getEquipment(EquipSlots.FEET)).getDefence();
		} catch (Exception e) {
		}
		try {
			defence += ((Armor) getEquipment(EquipSlots.HANDS)).getDefence();
		} catch (Exception e) {
		}
		try {
			defence += ((Armor) getEquipment(EquipSlots.HEAD)).getDefence();
		} catch (Exception e) {
		}
		try {
			defence += ((Armor) getEquipment(EquipSlots.LEGS)).getDefence();
		} catch (Exception e) {
		}

		// return the defence
		return defence;
	}

	public int getDamage() {
		// figure out how much damage we should do
		Random rnd = new Random();
		int dmg = (int) (strength / 2);

		// weapon damage
		Equipment e = getEquipment(EquipSlots.WEAPON);
		if (e != null) {
			Weapon w = (Weapon) e;
			dmg += rnd.nextInt(w.getDmg_high() - w.getDmg_low())
					+ w.getDmg_low() + 1;
		}

		return dmg;
	}

	/**
	 * Get equipment in a slot
	 */
	public Equipment getEquipment(EquipSlots slot) {
		return equipment.get(slot);
	}

	/**
	 * Put a piece of equipment on
	 */
	public void putEquipment(EquipSlots slot, Equipment equipment) {
		this.equipment.put(slot, equipment);
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

	public Tile getLOSTile(int x, int y) {
		return lineOfSight[x][y].getTile();
	}

	public Iterator<PathNode> getPathIterator() {
		return path.iterator();
	}

}

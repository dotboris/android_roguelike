/*
 * Class Name:			Tile.java
 * Class Purpose:		Game tile that contains entities
 * Created by:			boris on 2011-09-30
 */
package name.bobnet.android.rl.core.ents;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;

import name.bobnet.android.rl.core.ents.tiles.TileType;
import name.bobnet.android.rl.core.ents.tiles.Wall;
import name.bobnet.android.rl.core.ents.tiles.TileType.TileStyle;
import name.bobnet.android.rl.core.message.Message;

public class Tile implements Entity {

	/*
	 * Entities that can be on the tile
	 * 
	 * - Super Entities (Spells, Arrows, Rocks, etc.) 
	 * - Monster or player 
	 * - Items on the floor 
	 * - The type of the tile itself 
	 * 		(Floor, Wall, Stairs, Shallow Water, Deep Water, Lava, etc.)
	 */
	private ArrayList<Entity> superEnts;
	private Entity mob;
	private Stack<Entity> items;
	private TileType tileType;
	private boolean visible;

	public Tile() {
		this(new Wall(TileStyle.ROCK));
	}

	public Tile(TileType tileType) {
		setTileType(tileType);

		// create the items stack
		superEnts = new ArrayList<Entity>();
		items = new Stack<Entity>();
		setVisible(false);
	}

	@Override
	public void tick() {
		// call the tick method of our children

		// general purpose iterator
		Iterator<Entity> it;

		// projectiles
		it = superEnts.iterator();
		while (it.hasNext()) {
			it.next().tick();
		}

		// mob
		if (mob != null)
			mob.tick();

		// items
		it = items.iterator();
		while (it.hasNext())
			it.next().tick();

		// tileType
		tileType.tick();
	}

	@Override
	public void processMessage(Message message) {
		// TODO Auto-generated method stub

	}

	/**
	 * @return the iterator of the projectiles
	 */
	public Iterator<Entity> getProjectilesIterator() {
		return superEnts.iterator();
	}

	/**
	 * Add a projectile
	 * 
	 * @throws NullPointerException
	 *             thrown when proj is null
	 */
	public void addProjectile(Entity proj) {
		if (proj == null)
			throw new NullPointerException("proj cannot be null");

		// add the projectile
		superEnts.add(proj);
	}

	/**
	 * @return the iterator of the items
	 */
	public Iterator<Entity> getItemsIterator() {
		return items.iterator();
	}

	/**
	 * @param item
	 *            item to be added
	 */
	public void addItem(Entity item) {
		if (item == null)
			throw new NullPointerException("item cannot be null");

		// add the item
		items.push(item);
	}

	/**
	 * @return the mob
	 */
	public Entity getMob() {
		return mob;
	}

	/**
	 * @param mob
	 *            the mob to set
	 * @throws RuntimeException
	 *             thrown when there's already a mob on the tile
	 */
	public void setMob(Entity mob) {
		if (mob != null)
			throw new RuntimeException("Mob already exists");

		// set the new mob
		this.mob = mob;
	}

	/**
	 * Delete the current mob
	 * 
	 * @return the mob that was deleted
	 */
	public Entity delMob() {
		Entity tMob = mob;
		mob = null;
		return tMob;
	}

	/**
	 * @return the tileType
	 */
	public TileType getTileType() {
		return tileType;
	}

	/**
	 * @param tileType
	 *            the tileType to set
	 * 
	 * @throws NullPointerException
	 *             thrown when tileType is null
	 */
	public void setTileType(TileType tileType) {
		if (tileType == null)
			throw new NullPointerException("tileType cannot be null");

		// set the tile type
		this.tileType = tileType;
	}

	/**
	 * @return the visible
	 */
	public boolean isVisible() {
		return visible;
	}

	/**
	 * @param visible
	 *            the visible to set
	 */
	public void setVisible(boolean visible) {
		this.visible = visible;
	}

}

package name.bobnet.android.rl.core.ents.tiles;

import name.bobnet.android.rl.core.message.Message;

/**
 * A basic wall
 * 
 * @author boris
 */
public class Wall extends TileType {

	private boolean breakable;

	/**
	 * @param style
	 *            the style of the tile
	 * @param seeThrough
	 *            is the wall see through
	 * @param breakable
	 *            is the wall breakable (diggable)
	 */
	public Wall(TileStyle style, boolean breakable) {
		super(style, false, false);
		this.breakable = breakable;
	}

	public Wall(TileStyle style) {
		this(style, false);
	}

	/**
	 * @return the seeThrough
	 */
	public boolean isSeeThrough() {
		return seeThrough;
	}

	/**
	 * @param seeThrough
	 *            the seeThrough to set
	 */
	public void setSeeThrough(boolean seeThrough) {
		this.seeThrough = seeThrough;
	}

	/**
	 * @return the breakable
	 */
	public boolean isBreakable() {
		return breakable;
	}

	/**
	 * @param breakable
	 *            the breakable to set
	 */
	public void setBreakable(boolean breakable) {
		this.breakable = breakable;
	}

	/* (non-Javadoc)
	 * @see name.bobnet.android.rl.core.ents.Entity#tick()
	 */
	@Override
	public void tick() {
		// do nothing
	}

	/* (non-Javadoc)
	 * @see name.bobnet.android.rl.core.ents.Entity#processMessage(name.bobnet.android.rl.core.message.Message)
	 */
	@Override
	public void processMessage(Message message) {
		// TODO: Process damage
	}

}

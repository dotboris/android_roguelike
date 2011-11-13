package name.bobnet.android.rl.core.ents.tiles;

import name.bobnet.android.rl.core.ents.Entity;

/**
 * A basic tile type
 * 
 * @author boris
 */
public abstract class TileType extends Entity implements Cloneable {

	/**
	 * The style of the tile
	 * 
	 * For example, a wall could be made of rocks, metal, wood, etc.
	 * 
	 * @author boris
	 */
	public enum TileStyle {
		ROCK, WOOD, GLASS, METAL
	}

	// variables
	protected TileStyle style;
	protected boolean passThrough;

	public TileType(TileStyle style, boolean passThrough) {
		setStyle(style);
		setPassThrough(passThrough);
	}

	/**
	 * @return the style
	 */
	public TileStyle getStyle() {
		return style;
	}

	/**
	 * @param style
	 *            the style to set
	 * @throws NullPointerException
	 *             thrown when style is null
	 */
	public void setStyle(TileStyle style) {
		if (style == null)
			throw new NullPointerException("style connot be null");

		// set style
		this.style = style;
	}

	/**
	 * @return the passThrough
	 */
	public boolean isPassThrough() {
		return passThrough;
	}

	/**
	 * @param passThrough
	 *            the passThrough to set
	 */
	public void setPassThrough(boolean passThrough) {
		this.passThrough = passThrough;
	}

	@Override
	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}

}

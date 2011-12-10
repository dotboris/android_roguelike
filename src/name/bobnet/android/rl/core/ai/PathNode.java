package name.bobnet.android.rl.core.ai;

import name.bobnet.android.rl.core.ents.Tile;

/**
 * A node used in pathfinding
 * 
 * @author boris
 */
public class PathNode {

	// constants
	public static final int G_INC = 1;

	// variables
	private int f, g, h;
	private Tile tile;
	private PathNode parentNode;

	/**
	 * Calculate the fScore of the node
	 * 
	 * @param goal
	 *            the end node
	 */
	public void calcFScore(PathNode goal) {
		// calculate g and h
		calcGScore();
		calcHScore(goal);

		// calculate the fScore
		f = g + h;
	}

	/**
	 * Calculate the gScore of the node
	 */
	public void calcGScore() {
		// calculate the g score
		g = parentNode.g + G_INC;
	}

	/**
	 * Calculate the hScore of the node
	 * 
	 * @param goal
	 *            the end node
	 */
	public void calcHScore(PathNode goal) {
		h = Math.max(Math.abs(tile.getX() - parentNode.getTile().getX()),
				Math.abs(tile.getY() - parentNode.getTile().getY()));
	}

	/**
	 * @return the fScore
	 */
	public int getfScore() {
		return f;
	}

	/**
	 * @param fScore
	 *            the fScore to set
	 */
	public void setfScore(int fScore) {
		this.f = fScore;
	}

	/**
	 * @return the gScore
	 */
	public int getgScore() {
		return g;
	}

	/**
	 * @param gScore
	 *            the gScore to set
	 */
	public void setgScore(int gScore) {
		this.g = gScore;
	}

	/**
	 * @return the hScore
	 */
	public int gethScore() {
		return h;
	}

	/**
	 * @param hScore
	 *            the hScore to set
	 */
	public void sethScore(int hScore) {
		this.h = hScore;
	}

	/**
	 * @return the tile
	 */
	public Tile getTile() {
		return tile;
	}

	/**
	 * @param tile
	 *            the tile to set
	 */
	public void setTile(Tile tile) {
		this.tile = tile;
	}

	/**
	 * @return the parentNode
	 */
	public PathNode getParentNode() {
		return parentNode;
	}

	/**
	 * @param parentNode
	 *            the parentNode to set
	 */
	public void setParentNode(PathNode parentNode) {
		this.parentNode = parentNode;
	}

}

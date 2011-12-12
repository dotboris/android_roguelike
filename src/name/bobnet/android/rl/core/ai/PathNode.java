package name.bobnet.android.rl.core.ai;

import name.bobnet.android.rl.core.ents.Tile;

/**
 * A node used in pathfinding
 * 
 * @author boris
 */
public class PathNode implements Comparable<PathNode> {

	// constants
	public static final double G_INC = 1;
	public static final double DIAG_BUMP = 0.1;
	public static final double H_BUMP = 1.0001;

	// variables
	private double f, g, h;
	private Tile tile;
	private PathNode parentNode;

	/**
	 * Calculate the fScore of the node
	 * 
	 * @param g
	 *            the g score
	 * @param h
	 *            the h score
	 * @return the f score
	 */
	public static double calcFScore(double g, double h) {
		// calculate the fScore
		return g + h;
	}

	/**
	 * Calculate the gScore of the node
	 * 
	 * @param parent
	 *            the parent node
	 * @return the g score
	 */
	public static double calcGScore(PathNode node, PathNode parent) {
		// calculate the g score
		if (node.getTile().getX() - parent.getTile().getX() != 0
				&& node.getTile().getY() - parent.getTile().getY() != 0)
			// diagonal
			return parent.g + G_INC + DIAG_BUMP;
		else
			// straight
			return parent.g + G_INC;
	}

	/**
	 * Calculate the hScore of the node
	 * 
	 * @param goal
	 *            the end node
	 * @param node
	 *            the current node
	 * @return the h score
	 */
	public static double calcHScore(PathNode node, PathNode goal) {
		return Math.max(Math.abs(node.tile.getX() - goal.getTile().getX()),
				Math.abs(node.tile.getY() - goal.getTile().getY()));
	}

	public void calcScores(PathNode goal) {
		g = calcGScore(this, parentNode);
		h = calcHScore(this, goal);
		f = calcFScore(g, h);
	}

	@Override
	public int compareTo(PathNode another) {
		if (f == another.getfScore())
			return 0;
		else if (f > another.getfScore())
			return 1;
		else
			return -1;
	}

	/**
	 * @return the fScore
	 */
	public double getfScore() {
		return f;
	}

	/**
	 * @param fScore
	 *            the fScore to set
	 */
	public void setfScore(double fScore) {
		this.f = fScore;
	}

	/**
	 * @return the gScore
	 */
	public double getgScore() {
		return g;
	}

	/**
	 * @param gScore
	 *            the gScore to set
	 */
	public void setgScore(double gScore) {
		this.g = gScore;
	}

	/**
	 * @return the hScore
	 */
	public double gethScore() {
		return h;
	}

	/**
	 * @param hScore
	 *            the hScore to set
	 */
	public void sethScore(double hScore) {
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

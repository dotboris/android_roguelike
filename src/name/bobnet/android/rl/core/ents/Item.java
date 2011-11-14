/*
 * Class Name:			Item.java
 * Class Purpose:		An Item that can be held
 * Created by:			boris on 2011-11-14
 */
package name.bobnet.android.rl.core.ents;

public abstract class Item extends Entity {

	// variables
	private int weight;
	private String display;

	public Item(int weight, String display) {
		this.weight = weight;
		this.display = display;
	}

	/**
	 * @return the display
	 */
	public String getDisplay() {
		return display;
	}

	/**
	 * @param display
	 *            the display to set
	 */
	public void setDisplay(String display) {
		this.display = display;
	}

	/**
	 * @return the weight
	 */
	public int getWeight() {
		return weight;
	}

	/**
	 * @param weight
	 *            the weight to set
	 */
	public void setWeight(int weight) {
		this.weight = weight;
	}

}

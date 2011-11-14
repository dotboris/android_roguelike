/*
 * Class Name:			Item.java
 * Class Purpose:		An Item that can be held
 * Created by:			boris on 2011-11-14
 */
package name.bobnet.android.rl.core.ents;

public abstract class Item extends TemplateEntity {

	// variables
	private int weight;

	public Item(int weight, String display, String name) {
		super(display, name);
		this.weight = weight;
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

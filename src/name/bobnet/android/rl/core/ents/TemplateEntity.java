/*
 * Class Name:			TemplateEntity.java
 * Class Purpose:		A basic template generated entity
 * Created by:			boris on 2011-11-14
 */
package name.bobnet.android.rl.core.ents;

public abstract class TemplateEntity extends Entity {

	// variables
	protected String display;
	protected String name;
	protected int tileSheet, tileSheet_x, tileSheet_y;

	public TemplateEntity(String display, String name, int tileSheet,
			int tileSheet_x, int tileSheet_y) {
		this.display = display;
		this.name = name;
		this.tileSheet = tileSheet;
		this.tileSheet_x = tileSheet_x;
		this.tileSheet_y = tileSheet_y;
	}

	/**
	 * @return the tileSheet
	 */
	public int getTileSheet() {
		return tileSheet;
	}

	/**
	 * @param tileSheet
	 *            the tileSheet to set
	 */
	public void setTileSheet(int tileSheet) {
		this.tileSheet = tileSheet;
	}

	/**
	 * @return the tileSheet_x
	 */
	public int getTileSheet_x() {
		return tileSheet_x;
	}

	/**
	 * @param tileSheet_x
	 *            the tileSheet_x to set
	 */
	public void setTileSheet_x(int tileSheet_x) {
		this.tileSheet_x = tileSheet_x;
	}

	/**
	 * @return the tileSheet_y
	 */
	public int getTileSheet_y() {
		return tileSheet_y;
	}

	/**
	 * @param tileSheet_y
	 *            the tileSheet_y to set
	 */
	public void setTileSheet_y(int tileSheet_y) {
		this.tileSheet_y = tileSheet_y;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
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

}
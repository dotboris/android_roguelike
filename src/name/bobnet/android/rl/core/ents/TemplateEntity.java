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

	public TemplateEntity(String display, String name) {
		this.display = display;
		this.name = name;
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
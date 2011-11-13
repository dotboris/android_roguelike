/*
 * Class Name:			Entity.java
 * Class Purpose:		Basic multi purpose game entity
 * Created by:			boris on 2011-09-30
 */
package name.bobnet.android.rl.core.ents;

import name.bobnet.android.rl.core.message.Message;

public abstract class Entity {

	// variables
	protected Entity parent;

	/**
	 * @return the parent
	 */
	public Entity getParent() {
		return parent;
	}

	/**
	 * @param parent
	 *            the parent to set
	 */
	public void setParent(Entity parent) {
		this.parent = parent;
	}

	/**
	 * Called whenever the game ticks
	 */
	public abstract void tick();

	/**
	 * Called whenever a message is sent to the entity
	 * 
	 * @param message
	 *            the message in question
	 */
	public abstract void processMessage(Message message);
}

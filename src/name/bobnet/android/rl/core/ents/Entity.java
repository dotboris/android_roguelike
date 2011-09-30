/*
 * Class Name:			Entity.java
 * Class Purpose:		Basic multi purpose game entity
 * Created by:			boris on 2011-09-30
 */
package name.bobnet.android.rl.core.ents;

import name.bobnet.android.rl.core.message.Message;

public interface Entity {

	/**
	 * Called whenever the game ticks
	 */
	public void tick();

	public void processMessage(Message message);
}

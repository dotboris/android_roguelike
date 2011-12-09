/*
 * Class Name:			Floor.java
 * Class Purpose:		A floor
 * Created by:			boris on 2011-10-14
 */
package name.bobnet.android.rl.core.ents.tiles;

import name.bobnet.android.rl.core.message.Message;

public class Floor extends TileType {

	public Floor(TileStyle style) {
		super(style, true, true);
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
		// No messages to handle

	}

}

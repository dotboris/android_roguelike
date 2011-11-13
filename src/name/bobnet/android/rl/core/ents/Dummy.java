package name.bobnet.android.rl.core.ents;

import android.util.Log;
import name.bobnet.android.rl.core.GameEngine;
import name.bobnet.android.rl.core.message.Message;

/**
 * Dummy entity for testing
 * 
 * @author boris
 */
public class Dummy extends Entity {

	public Dummy() {
		// register
		GameEngine.getEngine().regEntTick(this);
		
		Log.d("RL", "Registered Dummy for ticks list");
	}
	
	/* (non-Javadoc)
	 * @see name.bobnet.android.rl.core.ents.Entity#tick()
	 */
	@Override
	public void tick() {
		// TODO Auto-generated method stub
		Log.d("RL", "Ticking Dummy Entity: " + this);
	}

	/* (non-Javadoc)
	 * @see name.bobnet.android.rl.core.ents.Entity#processMessage(name.bobnet.android.rl.core.message.Message)
	 */
	@Override
	public void processMessage(Message message) {
		switch (message.getMessageType()) {
		case M_DESTROY:
			// unregister
			GameEngine.getEngine().unregEntTick(this);
			
			Log.d("RL", "UnRegistered Dummy from ticks list");
			break;
		}

	}

}

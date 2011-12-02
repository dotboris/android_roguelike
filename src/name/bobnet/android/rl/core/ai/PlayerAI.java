/*
 * Class Name:			PlayerAI.java
 * Class Purpose:		AI for the player
 * Created by:			boris on 2011-12-02
 */
package name.bobnet.android.rl.core.ai;

public class PlayerAI extends AI {

	/* (non-Javadoc)
	 * @see name.bobnet.android.rl.core.ai.AI#nextAction()
	 */
	@Override
	public void nextAction() {
		// set the action to null, we're picking it manually
		currAction = null;
	}

}

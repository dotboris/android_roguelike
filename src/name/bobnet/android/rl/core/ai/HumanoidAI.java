/*
 * Class Name:			HumanoidAI.java
 * Class Purpose:		The AI for humanoids
 * Created by:			boris on 2011-12-02
 */
package name.bobnet.android.rl.core.ai;

import name.bobnet.android.rl.core.Action;
import name.bobnet.android.rl.core.GameEngine;
import name.bobnet.android.rl.core.ents.Player;
import name.bobnet.android.rl.core.ents.Tile;

public class HumanoidAI extends AI {

	/* (non-Javadoc)
	 * @see name.bobnet.android.rl.core.ai.AI#nextAction()
	 */
	@Override
	public void nextAction() {
		// variables
		Action nextAction = new Action();
		Tile cTile = (Tile) parent.getParent();
		Player player = (Player) GameEngine.getEngine().getPlayer();
		Tile pTile = (Tile) player.getParent();

		// determine the next action.
		if (pTile.getX() - cTile.getX() <= 1
				&& pTile.getX() - cTile.getX() >= -1
				&& pTile.getY() - cTile.getY() <= 1
				&& pTile.getY() - cTile.getY() >= -1) {
			// player is close, we can attack

			// get the name of the action
			nextAction.name = "A_ATTACK";

			// get the tile of the player
			nextAction.ent = pTile;

			// get the number of ticks
			nextAction.ticks = parent.getActionTicks(nextAction.name);
			
			// set the next action
			setNextAction(nextAction);
		}
	}
}

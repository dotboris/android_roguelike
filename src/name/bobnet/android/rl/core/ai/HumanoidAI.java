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
		} else if (pTile.getX() - cTile.getX() >= -4
				&& pTile.getX() - cTile.getX() <= 4
				&& pTile.getY() - cTile.getY() >= -4
				&& pTile.getY() - cTile.getY() <= 4) {
			// check if the player is in sight
			Tile t = parent.getLOSTile(pTile.getX() - (cTile.getX() - 5),
					pTile.getY() - (cTile.getY() - 5));
			if (t != null) {
				// next action is to move towards the player

				// set the name
				nextAction.name = "A_WALK";

				// set the number of ticks
				nextAction.ticks = parent.getActionTicks(nextAction.name);

				// calculate the path for the next tile
				parent.calcPath(pTile);

				// get the next tile
				Tile next = parent.getPathIterator().next().getTile();

				// set the tile to move to
				nextAction.ent = next;

				// set it as the next action
				setNextAction(nextAction);
			}
		}
	}
}

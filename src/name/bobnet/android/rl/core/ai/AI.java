/*
 * Class Name:			AI.java
 * Class Purpose:		Basic AI class
 * Created by:			boris on 2011-12-02
 */
package name.bobnet.android.rl.core.ai;

import name.bobnet.android.rl.core.Action;
import name.bobnet.android.rl.core.GameEngine;
import name.bobnet.android.rl.core.MessageManager;
import name.bobnet.android.rl.core.ents.Creature;
import name.bobnet.android.rl.core.ents.Item;
import name.bobnet.android.rl.core.ents.Tile;
import name.bobnet.android.rl.core.message.Message;
import name.bobnet.android.rl.core.message.Message.MessageType;

public abstract class AI {

	// variables
	protected Creature parent;
	protected Action prevAction, currAction;
	protected int tickCount;

	/**
	 * Set the next action
	 * 
	 * @param a
	 *            the action
	 * @return whether or not the action is valid and we can start ticking
	 */
	public boolean setNextAction(Action a) {
		// check if we can do the action
		if (a.name.equals("A_WALK")) {
			if (!(a.ent != null && a.ent instanceof Tile && ((Tile) a.ent)
					.isPassthrough())) {
				return false;
			}
		} else if (a.name.equals("A_ATTACK")) {
			if (!(a.ent != null && a.ent instanceof Tile))
				return false;
		} else if (a.name.equals("A_PICKUP")) {
			if (!(a.ent != null && a.ent instanceof Item)) {
				return false;
			}
		} else if (a.name.equals("A_DROP")) {
			if (!(a.ent != null && a.ent instanceof Item)) {
				return false;
			}
		} else if (a.name.equals("A_EAT")) {

		} else if (a.name.equals("A_READ")) {

		} else if (a.name.equals("A_WAIT")) {

		} else
			// I don't know how to do this
			return false;

		// set action information
		prevAction = currAction;
		currAction = a;

		// everything checks in start ticking
		return true;
	}

	/**
	 * Called to initialise the AI
	 */
	public void init(Creature parent) {
		// set the parent
		this.parent = parent;

		// register the parent for ticking
		GameEngine.getEngine().regEntTick(parent);
	}

	/**
	 * Figures out what action it will do next
	 */
	public abstract void nextAction();

	/**
	 * tick handling
	 */
	public void tick() {
		// check if we are doing anything
		if (currAction != null) {
			// update the counter
			tickCount++;

			// check if we waited long enough
			if (tickCount >= currAction.ticks) {
				// reset the counter and action
				tickCount = 0;
				currAction.ticks = 0;

				// TODO: Do the action
				if (currAction.name.equals("A_WALK")) {
					// move ourselves
					GameEngine.getEngine().getCurrentDungeon()
							.moveEntity((Tile) currAction.ent, parent);

					// update LOS
					parent.calcLOS();
				} else if (currAction.name.equals("A_ATTACK")) {
					// check if the entity has a creature in it
					if (currAction.ent != null) {
						// create a message to do damage
						Message aMessage = new Message(parent, currAction.ent,
								MessageType.M_DO_DAMAGE);

						// set damage
						aMessage.setArgument("dmg", parent.getDamage());

						// send the message
						MessageManager.getMessenger().sendMessage(aMessage);
					}
				} else if (currAction.name.equals("A_PICKUP")) {
					// pick up the item
					parent.pickUpItem((Item) currAction.ent);
				} else if (currAction.name.equals("A_DROP")) {
					// drop the item
					parent.dropItem((Item) currAction.ent);
				} else if (currAction.name.equals("A_EAT")) {

				} else if (currAction.name.equals("A_READ")) {

				} else if (currAction.name.equals("A_WAIT")) {
					// don't do anything
				}

				// set the next action to null
				currAction = null;

				// pick the next action
				nextAction();
			}
		} else
			// we don't have an action, see what we can do
			nextAction();
	}

}

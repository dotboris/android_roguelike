/*
 * Class Name:			GameEngine.java
 * Class Purpose:		Basic game engine handling most game operations
 * Created by:			boris on 2011-09-30
 */
package name.bobnet.android.rl.core;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

import android.content.res.Resources;
import android.util.Log;
import name.bobnet.android.rl.core.ents.Dungeon;
import name.bobnet.android.rl.core.ents.Entity;
import name.bobnet.android.rl.core.ents.tiles.TileType.TileStyle;
import name.bobnet.android.rl.core.gen.Generator;
import name.bobnet.android.rl.core.gen.Generator.DungeonType;

public class GameEngine {

	// engine singleton
	private static GameEngine engine;

	// variables
	private MessageManager messageManager;
	private Dungeon currentDungeon;
	private ActionsManager actionsManager;
	private Thread gameRunnerThread;
	private Queue<String> actionsQueue;
	private volatile boolean gameRunnerRunning;
	private LinkedList<Entity> tickList;

	private GameEngine() {
		// create the actions queue
		actionsQueue = new LinkedList<String>();

		// create the list of entities to tick
		tickList = new LinkedList<Entity>();

		// set gameRunnerRunning
		gameRunnerRunning = false;
	}

	/**
	 * Starts the game
	 */
	public void init(Resources res) {
		// TODO: Init engine

		// get an action manager
		ActionsManager.initActionManager(res);
		actionsManager = ActionsManager.getActionManager();
		Log.d("RL", "Created ActionsManager: " + actionsManager.toString());

		// create a message manager
		messageManager = MessageManager.getMessageManager();
		Log.d("RL", "Created MessageManager: " + messageManager.toString());

		// create a new dungeon (for testing)
		currentDungeon = Generator.GenerateDungeon(DungeonType.STANDARD,
				TileStyle.ROCK);

	}

	// TESTING generate a new dungeon and set it as the current dungeon
	public void genDugeon() {
		currentDungeon = Generator.GenerateDungeon(DungeonType.STANDARD,
				TileStyle.ROCK);
	}

	/**
	 * Register an entity to be ticked when the world ticks
	 * 
	 * @param ent
	 *            entity to register
	 */
	public void regEntTick(Entity ent) {
		// add the ent to the list if it doens't exist
		if (!tickList.contains(ent))
			tickList.add(ent);
	}

	/**
	 * Unregister an entity so it's not ticked anymore when the world ticks
	 * 
	 * @param ent
	 *            entity to be unregistered
	 */
	public void unregEntTick(Entity ent) {
		// remove the ent from the list
		tickList.remove(ent);
	}

	/**
	 * Performs a specified action
	 * 
	 * @param name
	 *            the name of the action to be performed
	 */
	public void doAction(String name) {
		// queue the action to be done
		actionsQueue.add(name);

		// process the queue
		processActions();
	}

	private synchronized void processActions() {
		if (!gameRunnerRunning && !actionsQueue.isEmpty()) {
			// variables
			String aName;
			int aTicks;

			// try to process the action in the queue
			while (true) {
				// get the action name
				aName = actionsQueue.poll();

				try {
					// get the ticks for the action
					aTicks = actionsManager.getActionTicks(aName);

					// validate the tick
					if (aTicks > 0) {
						// do the action
						gameRunnerThread = new Thread(new GameRunner(aTicks),
								"Game Runner");
						gameRunnerThread.start();

						// exit the method to prevent looping
						return;
					} else
						// display error
						Log.d("RL", "Couldn't do action " + aName
								+ ". Number of ticks is invalid.");

				} catch (Exception e) {
					// display error
					Log.d("RL", "Couldn't do action " + aName
							+ ". Action doesn't doesn't exist.");
				}
			}
		}
	}

	/**
	 * @return the currentDungeon
	 */
	public Dungeon getCurrentDungeon() {
		return currentDungeon;
	}

	/**
	 * @return the game engine singleton
	 */
	public static GameEngine getEngine() {
		if (engine == null)
			engine = new GameEngine();

		return engine;
	}

	/**
	 * Class handling game ticks
	 * 
	 * @author boris
	 */
	private class GameRunner implements Runnable {

		// variables
		private int tickCount;

		/**
		 * @param ticks
		 *            the number to ticks
		 */
		public GameRunner(int ticks) {
			this.tickCount = ticks;
		}

		@Override
		public void run() {
			// set the game runner to running
			gameRunnerRunning = true;

			Log.d("RL", "starting ticks: " + tickCount);

			// start ticking
			while (tickCount > 0) {
				// tick the entities who want to be ticked
				for (Iterator<Entity> i = tickList.iterator(); i.hasNext();)
					i.next().tick();

				// process messages
				messageManager.processMessages();

				// update the counter
				tickCount--;
			}

			Log.d("RL", "stopped ticking");

			// set game runner to not running
			gameRunnerRunning = false;

			// continue to process the queue
			processActions();
		}

	}

}

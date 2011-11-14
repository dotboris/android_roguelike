/*
 * Class Name:			GameEngine.java
 * Class Purpose:		Basic game engine handling most game operations
 * Created by:			boris on 2011-09-30
 */
package name.bobnet.android.rl.core;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import android.content.res.Resources;
import android.util.Log;
import name.bobnet.android.rl.core.ents.Creature;
import name.bobnet.android.rl.core.ents.Dungeon;
import name.bobnet.android.rl.core.ents.Entity;
import name.bobnet.android.rl.core.ents.Item;
import name.bobnet.android.rl.core.ents.Player;
import name.bobnet.android.rl.core.ents.TemplateEntity;
import name.bobnet.android.rl.core.ents.Tile;
import name.bobnet.android.rl.core.ents.factory.ContentLoader;
import name.bobnet.android.rl.core.ents.factory.EntityFactory;
import name.bobnet.android.rl.core.ents.tiles.TileType.TileStyle;
import name.bobnet.android.rl.core.gen.Generator;
import name.bobnet.android.rl.core.gen.Generator.DungeonType;
import name.bobnet.android.rl.core.message.Message;
import name.bobnet.android.rl.core.message.Message.MessageType;

public class GameEngine {

	// engine singleton
	private static GameEngine engine;

	// variables
	private MessageManager messageManager;
	private Dungeon currentDungeon;
	private ActionsManager actionsManager;
	private ContentLoader contentLoader;
	private Thread gameRunnerThread;
	private Queue<Action> actionsQueue;
	private volatile boolean gameRunnerRunning;
	private LinkedList<Entity> tickList;
	private Player player;

	private GameEngine() {
		// create the actions queue
		actionsQueue = new LinkedList<Action>();

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

		// load content from JSON
		contentLoader = ContentLoader.getContentLoader();
		contentLoader.loadContent(res);
		Log.d("RL", "Loaded Resources from JSON files");

		// create a new dungeon (for testing)
		currentDungeon = Generator.GenerateDungeon(DungeonType.STANDARD,
				TileStyle.ROCK);

		// create a player and put him in the middle of the dungeon
		player = new Player(0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		currentDungeon.getTile(40, 40).setMob(player);
		Log.d("RL", "Created player");

		// create an item on the floor
		Item i = (Item) EntityFactory.getEntityFactory().getRndEntity(
				ContentLoader.P_WEPAONS, new Random());
		currentDungeon.getTile(40, 41).addItem(i);

		// create a creature and put it in the dungeon
		Creature c = new Creature("dummy",
				"The one looking for a can of whoopass", 1, 3, 5, 6, 0, 0, 0,
				0, 0, 0, 10, 100);
		currentDungeon.getTile(40, 42).setMob(c);

	}

	// TESTING generate a new dungeon and set it as the current dungeon
	public void genDugeon() {
		currentDungeon = Generator.GenerateDungeon(DungeonType.STANDARD,
				TileStyle.ROCK);
	}

	public void pickUpItem() {
		// get player tile
		Tile t = (Tile) player.getParent();

		// find the first item on the floor
		Iterator<Item> it = t.getItemsIterator();
		if (it.hasNext()) {
			Item i = it.next();
			Message m = new Message(player, player, MessageType.M_PICKUP_ENT);
			m.setArgument("what", i);
			messageManager.sendMessage(m);

			Log.d("RL", "Picked up item " + i);
		}
	}

	public void dropItem() {
		// get the first item in the inventory
		Iterator<Item> it = player.getInventoryIterator();
		if (it.hasNext()) {
			Item i = it.next();
			Message m = new Message(player, player, MessageType.M_DROP_ENT);
			m.setArgument("what", i);
			messageManager.sendMessage(m);

			Log.d("RL", "Dropped item: " + i);
		}
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
	public void doAction(String name, Entity aEnt) {
		// get tick number from player
		int ticks = player.getActionTicks(name);

		// construct Action
		Action a = new Action();
		a.ent = aEnt;
		a.name = name;
		a.ticks = ticks;

		// queue the action to be done
		actionsQueue.add(a);

		// process the queue
		processActions();
	}

	public void doMoveAction(int x, int y) {
		// get the tile for the action
		Tile cTile = (Tile) player.getParent();
		Tile nTile = currentDungeon.getTile(cTile.getX() + x, cTile.getY() - y);

		// check the new tile and determine what to do
		if (nTile.isPassthrough() && nTile.getMob() == null) {
			doAction("A_WALK", nTile);
		} else if (nTile.getMob() != null) {
			doAction("A_ATTACK", nTile.getMob());
		}
	}

	private synchronized void processActions() {
		if (!gameRunnerRunning && !actionsQueue.isEmpty()) {
			// variables
			Action a;
			int aTicks;

			// try to process the action in the queue
			while (true) {
				// get the action
				a = actionsQueue.poll();

				if (a != null) {
					// give the action to the player
					boolean res = player.setNextAction(a);

					if (res)
						try {
							// get the ticks for the action
							aTicks = a.ticks;

							// validate the tick
							if (aTicks > 0) {
								// do the action
								gameRunnerThread = new Thread(new GameRunner(
										aTicks), "Game Runner");
								gameRunnerThread.start();

								// exit the method to prevent looping
								return;
							} else
								// display error
								Log.d("RL", "Couldn't do action " + a.name
										+ ". Number of ticks is invalid.");

						} catch (Exception e) {
							// display error
							Log.d("RL", "Couldn't do action " + a.name
									+ ". Action doesn't doesn't exist.");
						}
				} else
					// queue is empty
					return;
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

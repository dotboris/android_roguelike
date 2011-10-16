package name.bobnet.android.rl.core;

import java.util.HashMap;
import java.util.StringTokenizer;

import name.bobnet.android.rl.R;

import android.content.res.Resources;

/**
 * Class that manages all actions
 * 
 * The class loads all the actions from the XML resources and stores them.
 * 
 * @author boris
 */
public class ActionsManager {

	// variables
	private HashMap<String, Integer> actions;
	private static ActionsManager actionsManager;

	private ActionsManager(Resources res) {
		// create the actions hashmap
		actions = new HashMap<String, Integer>();

		// get raw actions
		String[] rawActions = res.getStringArray(R.array.actions);

		// parse said actions
		StringTokenizer tokenizer;
		for (String rawAct : rawActions) {
			// tokenize out string
			tokenizer = new StringTokenizer(rawAct, " ");

			// add the action
			actions.put(tokenizer.nextToken(),
					Integer.parseInt(tokenizer.nextToken()));
		}
		
		// set the singleton
		actionsManager = this;
	}

	/**
	 * @return the action manager singleton
	 */
	public static ActionsManager getActionManager() {
		return actionsManager;
	}

	/**
	 * Init the action manager
	 * 
	 * This must be called at least one before calling getActionManager()
	 * otherwise it will return null
	 * 
	 * @param res
	 *            the resource object to pull the actions from
	 */
	public static void initActionManager(Resources res) {
		// create the new action manager
		if (actionsManager == null)
			actionsManager = new ActionsManager(res);
	}

	public int getActionTicks(String name) {
		return actions.get(name);
	}

}

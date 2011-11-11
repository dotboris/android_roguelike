/*
 * Class Name:			ContentLoader.java
 * Class Purpose:		Class in charge of loading content from JSON files 
 * Created by:			boris on 2011-11-04
 */
package name.bobnet.android.rl.core.ents.factory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.util.Log;

import name.bobnet.android.rl.R;
import name.bobnet.android.rl.core.util.TreeNode;

public class ContentLoader {

	// singleton
	private static ContentLoader contentLoader;

	// variables
	private TreeNode classTree;
	private EntityFactory factory;

	private ContentLoader() {
		factory = EntityFactory.getEntityFactory();
	}

	/**
	 * Get the content loader singleton
	 * 
	 * @return the content loader singleton
	 */
	public static ContentLoader getContentLoader() {
		// create a new content loader if needed
		if (contentLoader == null) {
			contentLoader = new ContentLoader();
		}

		// return the content loader
		return contentLoader;
	}

	/**
	 * Load the content from file and give it to the Factory
	 * 
	 * @param context
	 *            the context of the application to that data can be loaded
	 * @return true if loading was successful false if it failed
	 */
	public boolean loadContent(Context context) {
		// variables
		JSONObject tmpJson;
		JSONArray tmpJsonArray;

		// create the tree of classes
		classTree = new TreeNode("root");

		// load the classes into the tree
		try {
			tmpJsonArray = ((JSONObject) new JSONTokener(context.getResources()
					.openRawResource(R.raw.classes).toString()).nextValue())
					.getJSONArray("classes");

			loadClasses(tmpJsonArray, classTree, "classes");
		} catch (NotFoundException e) {
			Log.d("RL", "Classes file not found, can't load content");
			return false;
		} catch (JSONException e) {
			Log.d("RL", "Classes file malformed, can't load content");
			return false;
		}

		// load items
		try {
			tmpJsonArray = ((JSONObject) new JSONTokener(context.getResources()
					.openRawResource(R.raw.items).toString()).nextValue())
					.getJSONArray("item");
		} catch (NotFoundException e) {
			// Items file not found aborting load
			Log.d("RL", "Items file not found, can't load content");
		} catch (JSONException e) {
			// Items file malformed aborting load
			Log.d("RL", "Items file malformed, can't load content");
		}

		// success
		return true;
	}

	// load templates form file
	private <T extends Template> void loadTemplates(JSONObject root,
			String[] path) {

	}

	// load classes into the tree
	private void loadClasses(JSONArray classes, TreeNode cNode, String className) {
		// variable
		JSONObject tmpObj;

		// load all the classes in this array (recursively)
		for (int i = 0; i < classes.length(); i++) {
			try {
				// load the object
				tmpObj = (JSONObject) classes.getJSONObject(i);

				// add it to the tree
				TreeNode nNode = new TreeNode(tmpObj, tmpObj.getString("name"));
				cNode.addChild(nNode);

				// check if it has subclasses
				JSONArray subClasses = tmpObj.optJSONArray("sub_classes");

				if (subClasses != null) {
					// load all the subclasses
					loadClasses(subClasses, nNode, nNode.getName());
				}
			} catch (JSONException e) {
				// something is wrong with this object log it
				Log.d("RL", "Coudln't load object #" + i + " in " + className
						+ ", " + e.getMessage());
			}
		}

	}

}

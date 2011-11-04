/*
 * Class Name:			EntityFactory.java
 * Class Purpose:		Factory used to generate entities form templates
 * Created by:			boris on 2011-11-04
 */
package name.bobnet.android.rl.core.ents.factory;

import name.bobnet.android.rl.core.util.TreeNode;

public class EntityFactory {

	// singleton
	private static EntityFactory entityFactory;

	// variables
	private TreeNode templates;

	private EntityFactory() {
		// create the templates tree
		templates = new TreeNode("root");
	}

	/**
	 * Get the Entity factory singleton
	 * 
	 * @return the EntityFactory singleton
	 */
	public static EntityFactory getEntityFactory() {
		// create a new factory if needed
		if (entityFactory == null)
			entityFactory = new EntityFactory();

		// return the factory
		return entityFactory;
	}

	/**
	 * Add a template to the factory
	 */
	public void addTemplate(Template t) {
		// variables
		TreeNode cNode = templates;
		String[] classes = new String[3];
		classes[0] = t.getEntClass();
		classes[1] = t.getEntSubClass();
		classes[2] = t.getEntSpecClass();

		// get to the right place in the tree to add the template
		// create nodes if needed
		for (String cClass : classes)
			if (cClass != null) {
				// check if the class exists
				if (cNode.getChild(cClass) == null) {
					// create it
					cNode.addChild(new TreeNode(cClass));
				}

				// set that as the current node
				cNode = cNode.getChild(cClass);
			} else
				// hit a null class stop searching
				break;

		// add the template to the node
		cNode.addChild(new TreeNode(t, t.getName()));
	}
}

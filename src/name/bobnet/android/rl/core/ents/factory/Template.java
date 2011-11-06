/*
 * Class Name:			Template.java
 * Class Purpose:		A template for generating entities
 * Created by:			boris on 2011-11-04
 */
package name.bobnet.android.rl.core.ents.factory;

import java.util.Random;

import org.json.JSONException;
import org.json.JSONObject;

import name.bobnet.android.rl.core.ents.Entity;

/**
 * A template used to generate entities by a factory
 * 
 * @author boris
 */
public abstract class Template {

	// variables
	private int spawnOdd;
	private String name, entClass, entSubClass, entSpecClass;

	/**
	 * Create a template from the data in the JSON objects for the class, sub
	 * class, spec class and template
	 * 
	 * @param entClass
	 *            the JSON object holding the definition of the class
	 * @param entSubClass
	 *            the JSON object holding the definition of the sub class
	 * @param entSpecClass
	 *            the JSON object holding the definition of the spec class
	 * @param self
	 *            the JSON object holding the definition of the template
	 * @throws JSONException
	 *             Thrown when content has failed to load
	 * @throws NullPointerException
	 *             thrown if entClass, entSubClass or self are null
	 */
	public Template(JSONObject entClass, JSONObject entSubClass,
			JSONObject entSpecClass, JSONObject self) throws JSONException,
			NullPointerException {
		// get the values from the JSON objects
		
		// load class name
		setEntClass(entClass.getString("name"));
		
		// load subclass name
		setEntSubClass(entSubClass.getString("name"));
		
		// load spec class name (if used)
		if (entSpecClass != null)
			setEntSpecClass(entSpecClass.getString("name"));
		
		// load spawning odds
		setSpawnOdd(self.getInt("spawnodds"));
		
		// load the name of the object
		setName(self.getString("name"));
	}

	/**
	 * Check whether the entity can be created randomly with the given roll
	 * 
	 * @param roll
	 *            the number between 1 and 100 that was rolled
	 * @return whether or not this entity can be spawned
	 */
	public boolean checkRoll(int roll) {
		return spawnOdd <= roll;
	}

	/**
	 * Generate an entity based on this template
	 * 
	 * @param rnd
	 *            the random number generator to be used
	 * @return the entity generated from the template
	 */
	public abstract Entity generate(Random rnd);

	/**
	 * @return the entClass
	 */
	public String getEntClass() {
		return entClass;
	}

	/**
	 * @param entClass
	 *            the entClass to set
	 * @throws NullPointerException
	 *             entClassCannot be null
	 */
	public void setEntClass(String entClass) {
		if (entClass == null)
			throw new NullPointerException("Entity Class cannot be null");

		this.entClass = entClass;
	}

	/**
	 * @return the entSubClass
	 */
	public String getEntSubClass() {
		return entSubClass;
	}

	/**
	 * @param entSubClass
	 *            the entSubClass to set
	 * @throws NullPointerException
	 *             entSubClass cannot be null
	 */
	public void setEntSubClass(String entSubClass) {
		if (entSubClass == null)
			throw new NullPointerException("Entity Subclass cannot be null");

		this.entSubClass = entSubClass;
	}

	/**
	 * @return the entSpecClass
	 */
	public String getEntSpecClass() {
		return entSpecClass;
	}

	/**
	 * @param entSpecClass
	 *            the entSpecClass to set
	 */
	public void setEntSpecClass(String entSpecClass) {
		this.entSpecClass = entSpecClass;
	}

	/**
	 * @return the spawnOdd
	 */
	public int getSpawnOdd() {
		return spawnOdd;
	}

	/**
	 * @param spawnOdd
	 *            the spawnOdd to set
	 */
	public void setSpawnOdd(int spawnOdd) {
		this.spawnOdd = spawnOdd;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

}

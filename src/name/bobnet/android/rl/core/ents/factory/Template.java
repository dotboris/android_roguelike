/*
 * Class Name:			Template.java
 * Class Purpose:		A template for generating entities
 * Created by:			boris on 2011-11-04
 */
package name.bobnet.android.rl.core.ents.factory;

import java.util.Random;

import org.json.JSONObject;

import name.bobnet.android.rl.core.ents.Entity;

/**
 * A template used to generate entities by a factory
 * 
 * @author boris
 */
public abstract class Template {

	// variables
	protected int spawnOdd;
	protected String name, entClass, entSubClass, entSpecClass;

	/**
	 * @param spawnOdd
	 *            the chance of the entity being spawned
	 * @param name
	 *            the name of the entity
	 * @param entClass
	 *            the class of the entity (item, monster, etc.)
	 * @param entSubClass
	 *            the sub class of the entClass. e.g. Items can be weapons,
	 *            armour, potions, etc.
	 * @param entSpecClass
	 *            Specialisation class. e.g. Weapons can be swords, maces,
	 *            staves
	 */
	public Template(int spawnOdd, String name, String entClass,
			String entSubClass, String entSpecClass) {
		setEntClass(entClass);
		setEntSubClass(entSubClass);
		setEntSpecClass(entSpecClass);
		setSpawnOdd(spawnOdd);
		setName(name);
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
	 * Load our own content using the JSON data
	 * 
	 * @param entClass
	 *            the JSON object representing the class
	 * @param entSubClass
	 *            the JSON object representing the subclass
	 * @param entSpecClass
	 *            the JSON object representing the specialisation class
	 * @param self
	 *            the JSON object representing ourselves
	 */
	public abstract void loadContent(JSONObject entClass,
			JSONObject entSubClass, JSONObject entSpecClass, JSONObject self);

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

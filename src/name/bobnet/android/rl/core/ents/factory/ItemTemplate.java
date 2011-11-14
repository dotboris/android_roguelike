/*
 * Class Name:			ItemTemplate.java
 * Class Purpose:		Basic item template holding weight
 * Created by:			boris on 2011-11-14
 */
package name.bobnet.android.rl.core.ents.factory;

import org.json.JSONException;
import org.json.JSONObject;

public abstract class ItemTemplate extends Template {

	// variable
	protected int weight;

	@Override
	public void load(JSONObject self) throws JSONException,
			NullPointerException {
		super.load(self);

		// load weight
		weight = self.getInt("weight");
	}

}

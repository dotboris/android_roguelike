/*
 * Class Name:			ContentLoader.java
 * Class Purpose:		Class in charge of loading content from JSON files 
 * Created by:			boris on 2011-11-04
 */
package name.bobnet.android.rl.core.ents.factory;

public class ContentLoader {

	// singleton
	private static ContentLoader contentLoader;

	private ContentLoader() {
		
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
}

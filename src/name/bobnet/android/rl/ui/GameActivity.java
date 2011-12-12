package name.bobnet.android.rl.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

/**
 * Activity that holds the game being played
 * 
 * @author boris
 */
public class GameActivity extends Activity {

	// variables
	private GameView gameView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// set fullscreen
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		// create my game view
		gameView = new GameView(this);

		// show the view
		setContentView(gameView);
	}
}

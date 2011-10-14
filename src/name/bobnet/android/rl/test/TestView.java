/*
 * Class Name:			TestView.java
 * Class Purpose:		testing display
 * Created by:			boris on 2011-10-14
 */
package name.bobnet.android.rl.test;

import name.bobnet.android.rl.core.GameEngine;
import name.bobnet.android.rl.core.ents.Dungeon;
import name.bobnet.android.rl.core.ents.tiles.Wall;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class TestView extends View {

	// variables
	private Activity pActivity;
	private GameEngine engine;

	public TestView(Context context) {
		super(context);
		pActivity = (Activity) context;
		engine = GameEngine.getEngine();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		invalidate();
		return true;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		// values
		float tW, tH;
		tW = getWidth() / (float) Dungeon.D_WIDTH;
		tH = getHeight() / (float) Dungeon.D_HEIGHT;

		Log.d("RL", "w: " + tW + " h: " + tH);

		// paints
		Paint pBlue, pRed;
		pBlue = new Paint();
		pRed = new Paint();
		pRed.setColor(Color.RED);
		pBlue.setColor(Color.BLUE);

		for (int x = 0; x < Dungeon.D_WIDTH; x++)
			for (int y = 0; y < Dungeon.D_HEIGHT; y++) {
				if (engine.getCurrentDungeon().getTile(x, y).getTileType() instanceof Wall)
					canvas.drawRect(x * tW, y * tW, (x + 1) * tW, (y + 1) * tH,
							pRed);
				else
					canvas.drawRect(x * tW, y * tW, (x + 1) * tW, (y + 1) * tH,
							pBlue);
			}

	}
}

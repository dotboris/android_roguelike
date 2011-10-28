/*
 * Class Name:			TestView.java
 * Class Purpose:		testing display
 * Created by:			boris on 2011-10-14
 */
package name.bobnet.android.rl.test;

import java.util.Iterator;

import name.bobnet.android.rl.core.GameEngine;
import name.bobnet.android.rl.core.MessageManager;
import name.bobnet.android.rl.core.ents.Dummy;
import name.bobnet.android.rl.core.ents.Dungeon;
import name.bobnet.android.rl.core.ents.Entity;
import name.bobnet.android.rl.core.ents.tiles.Wall;
import name.bobnet.android.rl.core.message.Message;
import name.bobnet.android.rl.core.message.Message.MessageType;
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

		engine.getCurrentDungeon().getTile(10, 10).addSuperEntity(new Dummy());
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			// generate a new dungeon and redraw
			engine.genDugeon();
			invalidate();
			
			
			for (int i = 0; i < 10; i++)
				engine.doAction("A_DUMMY_10");
			try {
				// delete the dummy ent
				Iterator<Entity> i = engine.getCurrentDungeon().getTile(10, 10)
						.getSuperEntsIterator();
				Entity dummy = i.next();

				// send destroy message
				MessageManager.getMessenger().sendMessage(
						new Message(null, engine.getCurrentDungeon().getTile(
								10, 10), MessageType.M_DESTROY));

				// remove it from the tile
//				i.remove();
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		return true;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		Log.d("RL", "Started drawing");
		
		// values
		float tW, tH;
		tW = 3.0f;
		tH = 3.0f;

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
		
		Log.d("RL", "done drawing");
	}
}

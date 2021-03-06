/*
 * Class Name:			TestView.java
 * Class Purpose:		testing display
 * Created by:			boris on 2011-10-14
 */
package name.bobnet.android.rl.test;

import java.util.Iterator;

import name.bobnet.android.rl.core.GameEngine;
import name.bobnet.android.rl.core.MessageManager;
import name.bobnet.android.rl.core.ai.PathNode;
import name.bobnet.android.rl.core.ents.Creature;
import name.bobnet.android.rl.core.ents.Dummy;
import name.bobnet.android.rl.core.ents.Dungeon;
import name.bobnet.android.rl.core.ents.Entity;
import name.bobnet.android.rl.core.ents.Player;
import name.bobnet.android.rl.core.ents.Tile;
import name.bobnet.android.rl.core.ents.tiles.Wall;
import name.bobnet.android.rl.core.message.Message;
import name.bobnet.android.rl.core.message.Message.MessageType;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

public class TestView extends View {

	// variables
	private Activity pActivity;
	private GameEngine engine;
	private Creature p;
	private Paint pBlue, pRed, pGreen, pOrange, pPurple, pGray, pWhite;
	private Iterator<PathNode> path;

	public TestView(Context context) {
		super(context);
		pActivity = (Activity) context;
		engine = GameEngine.getEngine();

		setFocusable(true);
		setFocusableInTouchMode(true);

		// paints
		pBlue = new Paint();
		pRed = new Paint();
		pGreen = new Paint();
		pOrange = new Paint();
		pPurple = new Paint();
		pGray = new Paint();
		pWhite = new Paint();
		pRed.setColor(Color.RED);
		pBlue.setColor(Color.BLUE);
		pGreen.setColor(Color.GREEN);
		pOrange.setColor(Color.rgb(255, 127, 0));
		pPurple.setColor(Color.MAGENTA);
		pGray.setColor(Color.DKGRAY);
		pGray.setAlpha(180);
		pWhite.setColor(Color.WHITE);
		pWhite.setAlpha(127);

		// get player
		p = engine.getPlayer();

		// engine.getCurrentDungeon().getTile(10, 10).addSuperEntity(new
		// Dummy());
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		// check the key
		switch (keyCode) {
		case KeyEvent.KEYCODE_1:
			engine.doMoveAction(-1, -1);
			break;
		case KeyEvent.KEYCODE_2:
			engine.doMoveAction(0, -1);
			break;
		case KeyEvent.KEYCODE_3:
			engine.doMoveAction(1, -1);
			break;
		case KeyEvent.KEYCODE_4:
			engine.doMoveAction(-1, 0);
			break;
		case KeyEvent.KEYCODE_6:
			engine.doMoveAction(1, 0);
			break;
		case KeyEvent.KEYCODE_7:
			engine.doMoveAction(-1, 1);
			break;
		case KeyEvent.KEYCODE_8:
			engine.doMoveAction(0, 1);
			break;
		case KeyEvent.KEYCODE_9:
			engine.doMoveAction(1, 1);
			break;
		case KeyEvent.KEYCODE_P:
			engine.pickUpItem();
			break;
		case KeyEvent.KEYCODE_D:
			engine.dropItem();
			break;
		case KeyEvent.KEYCODE_R:
			invalidate();
			break;
		case KeyEvent.KEYCODE_DPAD_CENTER:
			engine.genDugeon();
			break;
		default:
			break;
		}

		// redraw
		invalidate();

		return true;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			try {
				// null the path
				path = null;

				// pick a tile for path testing
				int x, y;
				x = (int) (event.getX() / 5);
				y = (int) (event.getY() / 5);
				Tile t = engine.getCurrentDungeon().getTile(x, y);

				// calculate the path
				p.calcPath(t);

				// get the path
				path = p.getPathIterator();

				// redraw this
				invalidate();

			} catch (RuntimeException e) {
				// something went wrong
				Log.d("RL", "Something failed (on touch): " + e.getMessage());
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
		tW = 5.0f;
		tH = 5.0f;

		Log.d("RL", "w: " + tW + " h: " + tH);

		for (int x = 0; x < Dungeon.D_WIDTH; x++)
			for (int y = 0; y < Dungeon.D_HEIGHT; y++) {
				// get the player tile
				int pX, pY;
				boolean useLOS = true;
				pX = x
						- (((Tile) p.getParent()).getX() - Creature.LOS_SIZE / 2);
				pY = y
						- (((Tile) p.getParent()).getY() - Creature.LOS_SIZE / 2);
				Tile pTile = null;
				try {
					pTile = p.getLOSTile(pX, pY);
				} catch (Exception e) {
					// out of bounds don't use LOS
					useLOS = false;
				}
				if (pTile == null)
					// tile isn't seen by the player don't use LOS
					useLOS = false;

				Tile t;
				if (useLOS)
					t = pTile;
				else
					t = engine.getCurrentDungeon().getTile(x, y);

				if (t.isVisible()) {
					if (t.getTileType() instanceof Wall)
						canvas.drawRect(x * tW, y * tW, (x + 1) * tW, (y + 1)
								* tH, pRed);
					else if (useLOS && t.getMob() instanceof Player)
						canvas.drawRect(x * tW, y * tW, (x + 1) * tW, (y + 1)
								* tH, pGreen);
					else if (useLOS && t.getMob() instanceof Creature)
						canvas.drawRect(x * tW, y * tW, (x + 1) * tW, (y + 1)
								* tH, pOrange);
					else if (useLOS && t.getItemsIterator().hasNext()) {
						canvas.drawRect(x * tW, y * tW, (x + 1) * tW, (y + 1)
								* tH, pPurple);
					} else
						canvas.drawRect(x * tW, y * tW, (x + 1) * tW, (y + 1)
								* tH, pBlue);
					if (!useLOS)
						canvas.drawRect(x * tW, y * tW, (x + 1) * tW, (y + 1)
								* tH, pGray);
				}
			}

		// paint the path
		if (path != null) {
			for (; path.hasNext();) {
				PathNode n = path.next();
				int px = n.getTile().getX();
				int py = n.getTile().getY();

				canvas.drawRect(px * tW, py * tW, (px + 1) * tW, (py + 1) * tH,
						pWhite);
			}
		}

		Log.d("RL", "done drawing");
	}
}

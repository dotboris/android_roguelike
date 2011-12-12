package name.bobnet.android.rl.ui;

import name.bobnet.android.rl.R;
import name.bobnet.android.rl.core.GameEngine;
import name.bobnet.android.rl.core.ents.Creature;
import name.bobnet.android.rl.core.ents.Item;
import name.bobnet.android.rl.core.ents.Player;
import name.bobnet.android.rl.core.ents.Tile;
import name.bobnet.android.rl.core.ents.tiles.Floor;
import name.bobnet.android.rl.core.ents.tiles.Wall;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * View that handles game operations (AKA interaction with the user)
 * 
 * @author boris
 */
public class GameView extends SurfaceView {

	// constants
	public static final int TILE_SHEETS[] = { R.drawable.ts1 };
	public static final int TILE_SIZE = 32;
	public static final int SHEET_TILES_X = 20;
	public static final int SHEET_TILES_Y = 10;

	// variables
	private GameEngine engine;
	private Player player;
	private Bitmap[] tileSheets;
	private int colorShadow;
	private int w, h;
	private int minx, miny, maxx, maxy, cx, cy, tx, ty;
	private Paint paint;
	private SurfaceHolder holder;

	public GameView(Context context) {
		super(context);

		// allow keyboard input
		setFocusable(true);
		setFocusableInTouchMode(true);

		// get the holder
		holder = getHolder();

		// get engine
		engine = GameEngine.getEngine();

		// load tile sheets
		tileSheets = new Bitmap[TILE_SHEETS.length];
		BitmapFactory.Options ops = new BitmapFactory.Options();
		ops.inScaled = false;
		for (int t = 0; t < TILE_SHEETS.length; t++) {
			tileSheets[t] = BitmapFactory.decodeResource(getResources(),
					TILE_SHEETS[t]);
		}

		// load colours
		colorShadow = getResources().getColor(R.color.shadow);

		// set the paint
		paint = new Paint();
		paint.setAntiAlias(true);

		// init the engine
		engine.init(getResources(), this);

		// get the player instance
		player = (Player) engine.getPlayer();
	}

	public void paintSelf() {
		// repaint ourselfves
		Thread t = new Thread(new GameDrawer(), "render");
		t.start();
	}

	private void drawTile(Canvas canvas, int tileSheet, int tx, int ty, int px,
			int py) {
		canvas.drawBitmap(tileSheets[tileSheet], new Rect(tx * TILE_SIZE, ty
				* TILE_SIZE, tx * TILE_SIZE + TILE_SIZE, ty * TILE_SIZE
				+ TILE_SIZE), new Rect(px, py, px + TILE_SIZE, py + TILE_SIZE),
				paint);
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
			paintSelf();
			break;
		case KeyEvent.KEYCODE_DPAD_CENTER:
			engine.genDugeon();
			break;
		default:
			break;
		}

		return true;
	}

	private class GameDrawer implements Runnable {

		@Override
		public void run() {
			if (holder.getSurface().isValid()) {
				// get the canvas
				Canvas canvas = holder.lockCanvas();

				// figure out the range of tiles to draw
				w = getWidth();
				h = getHeight();
				cx = ((Tile) player.getParent()).getX();
				cy = ((Tile) player.getParent()).getY();
				minx = cx - (w / 2 / TILE_SIZE);
				miny = cy - (h / 2 / TILE_SIZE);
				maxx = cx + (w / 2 / TILE_SIZE);
				maxy = cy + (h / 2 / TILE_SIZE);
				tx = (w - (maxx - minx) * TILE_SIZE) / 2 - TILE_SIZE / 2;
				ty = (h - (maxy - miny) * TILE_SIZE) / 2 - TILE_SIZE / 2;

				// clear the surface
				canvas.drawColor(Color.BLACK);

				// draw the tiles
				int px, py;
				Tile cTile;
				boolean visible;
				for (int x = minx; x <= maxx; x++)
					for (int y = miny; y <= maxy; y++) {
						// figure out the position of the tile on screen
						px = (x - minx) * TILE_SIZE + tx;
						py = (y - miny) * TILE_SIZE + ty;

						// rest the current tile
						cTile = null;
						visible = true;

						// reset the paint
						paint.setAlpha(255);

						// check if the tile is in the player's LOS
						try {
							cTile = player.getLOSTile(x - cx
									+ Creature.LOS_SIZE / 2, y - cy
									+ Creature.LOS_SIZE / 2);
						} catch (ArrayIndexOutOfBoundsException e) {
						}

						// check if we're seeing the tile or not
						if (cTile == null) {
							visible = false;
							try {
								cTile = engine.getCurrentDungeon()
										.getTile(x, y);
							} catch (ArrayIndexOutOfBoundsException e) {
								// tile doesn't exist, skip it
								continue;
							}
						}

						// draw the tile
						if (cTile.isVisible()) {
							// draw the tile type
							if (cTile.getTileType() instanceof Wall) {
								drawTile(canvas, 0, 0, 0, px, py);
							} else if (cTile.getTileType() instanceof Floor) {
								drawTile(canvas, 0, 1, 0, px, py);
							}

							// draw the rest
							if (visible) {
								if (cTile.getMob() != null) {
									// draw creatures
									drawTile(canvas, cTile.getMob()
											.getTileSheet(), cTile.getMob()
											.getTileSheet_x(), cTile.getMob()
											.getTileSheet_y(), px, py);
								} else if (cTile.getNumItems() > 0) {
									// display the first item
									Item i = cTile.getItemsIterator().next();

									// draw it
									drawTile(canvas, i.getTileSheet(),
											i.getTileSheet_x(),
											i.getTileSheet_y(), px, py);
								}
							} else {
								// draw the shadow on top of the tile
								paint.setColor(colorShadow);
								canvas.drawRect(px, py, px + TILE_SIZE, py
										+ TILE_SIZE, paint);
							}

						}
					}

				// release the canvas
				holder.unlockCanvasAndPost(canvas);
			}

		}
	}
}

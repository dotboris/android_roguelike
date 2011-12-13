package name.bobnet.android.rl.ui;

import java.util.Iterator;

import name.bobnet.android.rl.R;
import name.bobnet.android.rl.core.GameEngine;
import name.bobnet.android.rl.core.ents.Creature;
import name.bobnet.android.rl.core.ents.Equipment;
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
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * View that handles game operations (AKA interaction with the user)
 * 
 * @author boris
 */
public class GameView extends SurfaceView implements SurfaceHolder.Callback {

	// constants
	public static final int TILE_SHEETS[] = { R.drawable.ts1 };
	public static final int TILE_SIZE = 32;
	public static final int SHEET_TILES_X = 20;
	public static final int SHEET_TILES_Y = 10;
	public static final int D_WIDTH = 150;
	public static final int D_HEIGHT = 150;
	public static final int D_OFFSET = 10;
	public static final int S_OFFSET = 10;
	public static final int S_WIDTH = 150;
	public static final int S_HEIGHT = 20;
	public static final int H_WIDTH = 352;
	public static final int H_HEIGHT = 224;
	public static final int H_PADDING = 5;
	public static final int H_CONTROLS_WIDTH = 32;

	// variables
	private GameEngine engine;
	private Player player;
	private Bitmap[] tileSheets;
	private Bitmap dpad;
	private int colorShadow, colorHealth, colorMana, colorHUDItems,
			colorHUDControls, colorHUDBack, colorEquipped;
	private int w, h;
	private int minx, miny, maxx, maxy, cx, cy, tx, ty;
	private int hx, hy, hcx, hcy, hix, hiy;
	private Paint paint;
	private SurfaceHolder holder;
	private boolean drawPickupHUD, drawInvHUD;
	private int hudSelect;

	public GameView(Context context) {
		super(context);

		// allow keyboard input
		setFocusable(true);
		setFocusableInTouchMode(true);

		// get the holder
		holder = getHolder();

		// listen to holder callbacks
		holder.addCallback(this);

		// get engine
		engine = GameEngine.getEngine();

		// load bitmaps
		BitmapFactory.Options ops = new BitmapFactory.Options();
		ops.inScaled = false;

		// load tile sheets
		tileSheets = new Bitmap[TILE_SHEETS.length];
		for (int t = 0; t < TILE_SHEETS.length; t++) {
			tileSheets[t] = BitmapFactory.decodeResource(getResources(),
					TILE_SHEETS[t]);
		}

		// load dpad
		dpad = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
				getResources(), R.drawable.dpadlight, ops), D_WIDTH, D_HEIGHT,
				false);

		// load colours
		colorShadow = getResources().getColor(R.color.shadow);
		colorHealth = getResources().getColor(R.color.health);
		colorMana = getResources().getColor(R.color.mana);
		colorHUDItems = getResources().getColor(R.color.HUD_items);
		colorHUDControls = getResources().getColor(R.color.HUD_control);
		colorHUDBack = getResources().getColor(R.color.HUD_back);
		colorEquipped = getResources().getColor(R.color.equipped);

		// set the paint
		paint = new Paint();
		paint.setAntiAlias(true);

		// init the engine
		engine.init(getResources(), this);

		// get the player instance
		player = (Player) engine.getPlayer();

		// set if the huds should be drawn
		drawInvHUD = false;
		drawPickupHUD = false;

		// set the hud selection
		hudSelect = -1;
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

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			// check for the DPAD
			if (event.getX() >= w - D_WIDTH - D_OFFSET
					&& event.getX() <= w - D_OFFSET
					&& event.getY() >= h - D_HEIGHT - D_OFFSET
					&& event.getY() <= h - D_OFFSET) {
				// figure out the direction that was clicked
				int dirX = (int) (Math
						.floor((event.getX() - (w - D_WIDTH - D_OFFSET))
								/ (D_WIDTH / 3)) - 1);
				int dirY = -(int) (Math
						.floor((event.getY() - (h - D_HEIGHT - D_OFFSET))
								/ (D_HEIGHT / 3)) - 1);

				// go in that direction
				if (!(dirX == 0 && dirY == 0)) {
					engine.doMoveAction(dirX, dirY);
				} else {
					// wait
					engine.doAction("A_WAIT", null);
				}

			}
			// check for the inventory button
			else if (event.getX() >= w - 2 * D_OFFSET - 2 * TILE_SIZE
					&& event.getX() <= w - 2 * D_OFFSET - TILE_SIZE
					&& event.getY() >= h - D_OFFSET * 2 - D_HEIGHT - TILE_SIZE
					&& event.getY() <= h - D_OFFSET * 2 - D_HEIGHT) {
				// show / hide what is needed
				drawPickupHUD = false;
				drawInvHUD = !drawInvHUD;

				// clear the selection
				hudSelect = -1;

				// redraw
				paintSelf();
			}
			// check for the pickup button
			else if (event.getX() >= w - 2 * D_OFFSET - TILE_SIZE
					&& event.getX() <= w - 2 * D_OFFSET
					&& event.getY() >= h - D_OFFSET * 2 - D_HEIGHT - TILE_SIZE
					&& event.getY() <= h - D_OFFSET * 2 - D_HEIGHT) {
				// show / hide what is needed
				drawInvHUD = false;
				drawPickupHUD = !drawPickupHUD;

				// clear the selection
				hudSelect = -1;

				// redraw
				paintSelf();
			}
			// checks for hud
			else if (drawInvHUD || drawPickupHUD) {
				// selecting items
				if (event.getX() >= hix
						&& event.getX() <= hix + H_WIDTH - H_CONTROLS_WIDTH
						&& event.getY() >= hiy
						&& event.getY() <= hiy + H_HEIGHT) {
					// find the selected item
					int sx = (int) Math.floor((event.getX() - hix) / TILE_SIZE);
					int sy = (int) Math.floor((event.getY() - hiy) / TILE_SIZE);
					hudSelect = sy * (H_WIDTH - H_CONTROLS_WIDTH) / TILE_SIZE
							+ sx;

					// redraw
					paintSelf();
				}
				// selecting controls
				else if (event.getX() >= hcx && event.getX() <= hcx + TILE_SIZE
						&& event.getY() >= hcy
						&& event.getY() <= hcy + H_HEIGHT + 2 * H_PADDING) {
					// find the selected control
					int cy = (int) Math.floor((event.getY() - hcy) / TILE_SIZE);

					// do something about it
					switch (cy) {
					case 0:
						// top most control
						if (drawInvHUD) {
							try {
								// equip the item
								Item i = player.getInventoryItem(hudSelect);

								// only equip equipment
								if (i instanceof Equipment) {
									// put the equipment on
									player.putEquipment((Equipment) i);

									// redraw
									paintSelf();
								}
							} catch (ArrayIndexOutOfBoundsException e) {
							}
						} else {
							try {
								// pickup the item
								Item i = ((Tile) player.getParent())
										.getItem(hudSelect);

								// do the action
								engine.doAction("A_PICKUP", i);
							} catch (ArrayIndexOutOfBoundsException e) {
							}
						}
						break;
					case 1:
						// second control
						if (drawInvHUD) {
							try {
								// drop the item
								Item i = player.getInventoryItem(hudSelect);

								// do the action
								engine.doAction("A_DROP", i);
							} catch (ArrayIndexOutOfBoundsException e) {
							}
						}
						break;
					}
				}
			}

		}

		return true;
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// set the holder
		this.holder = holder;

		// set window dimentions
		w = width;
		h = height;
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// set the holder
		this.holder = holder;

		// draw ourselves
		paintSelf();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// set the holder
		this.holder = holder;
	}

	private class GameDrawer implements Runnable {

		@Override
		public void run() {
			if (holder.getSurface().isValid()) {
				// get the canvas
				Canvas canvas = holder.lockCanvas();

				// figure out the range of tiles to draw
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
						paint.setStyle(Style.FILL_AND_STROKE);

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

				// draw the health and mana bars

				// outside
				paint.setStyle(Style.STROKE);
				paint.setColor(colorHealth);
				canvas.drawRect(w - S_OFFSET - S_WIDTH, S_OFFSET, w - S_OFFSET,
						S_OFFSET + S_HEIGHT, paint);
				paint.setColor(colorMana);
				canvas.drawRect(w - S_OFFSET - S_WIDTH,
						2 * S_OFFSET + S_HEIGHT, w - S_OFFSET, 2 * S_OFFSET + 2
								* S_HEIGHT, paint);

				// inside
				paint.setStyle(Style.FILL);
				if (player.getHealth() > 0) {
					paint.setColor(colorHealth);
					canvas.drawRect(w - S_OFFSET - S_WIDTH * player.getHealth()
							/ player.getMaxHealth(), S_OFFSET, w - S_OFFSET,
							S_OFFSET + S_HEIGHT, paint);
				}
				if (player.getMana() > 0) {
					paint.setColor(colorMana);
					canvas.drawRect(w - S_OFFSET - S_WIDTH * player.getMana()
							/ player.getMaxMana(), 2 * S_OFFSET + S_HEIGHT, w
							- S_OFFSET, 2 * S_OFFSET + 2 * S_HEIGHT, paint);
				}

				// draw the dpad
				canvas.drawBitmap(dpad, null, new Rect(w - D_WIDTH - D_OFFSET,
						h - D_HEIGHT - D_OFFSET, w - D_OFFSET, h - D_OFFSET),
						paint);

				// draw buttons to display the hud
				paint.setAlpha(200);
				drawTile(canvas, 0, 10, 0, w - D_OFFSET - TILE_SIZE, h
						- D_OFFSET * 2 - D_HEIGHT - TILE_SIZE);
				drawTile(canvas, 0, 7, 0, w - 2 * D_OFFSET - 2 * TILE_SIZE, h
						- D_OFFSET * 2 - D_HEIGHT - TILE_SIZE);

				// draw the huds
				if (drawInvHUD || drawPickupHUD) {
					// determine the positions of elements
					hx = (w - (H_WIDTH + H_PADDING * 2)) / 2;
					hy = (h - (H_HEIGHT + H_PADDING * 2)) / 2;
					hcx = hx + H_WIDTH + H_PADDING * 2 - H_CONTROLS_WIDTH;
					hcy = hy;
					hix = hx + H_PADDING;
					hiy = hy + H_PADDING;

					// draw the background
					paint.setColor(colorHUDBack);
					paint.setAlpha(255);
					paint.setStyle(Style.FILL_AND_STROKE);
					canvas.drawRect(hx, hy, hx + H_WIDTH + H_PADDING * 2, hy
							+ H_HEIGHT + H_PADDING * 2, paint);

					// draw the area for the items
					paint.setColor(colorHUDItems);
					canvas.drawRect(hix, hiy, hix + H_WIDTH - H_CONTROLS_WIDTH,
							hiy + H_HEIGHT, paint);

					// draw the area for the controls
					paint.setColor(colorHUDControls);
					canvas.drawRect(hcx, hcy, hcx + H_CONTROLS_WIDTH, hcy
							+ H_HEIGHT + H_PADDING * 2, paint);

					// draw the controls depending on the type of hud
					if (drawInvHUD) {
						drawTile(canvas, 0, 9, 0, hcx, hcy);
						drawTile(canvas, 0, 10, 0, hcx, hcy + TILE_SIZE);
					} else if (drawPickupHUD) {
						drawTile(canvas, 0, 10, 0, hcx, hcy);
					}

					// draw the items
					Iterator<Item> it;
					Item i;

					// get the iterator
					if (drawInvHUD) {
						it = player.getInventoryIterator();
					} else {
						it = ((Tile) player.getParent()).getItemsIterator();
					}

					// go through the items
					int rowCount = 0;
					int iCount = 0;
					int sCount = 0;
					while (it.hasNext()) {
						// get an item
						i = it.next();

						// check if equipped
						if (drawInvHUD && i instanceof Equipment
								&& player.isEquipped((Equipment) i)) {
							paint.setColor(colorEquipped);
							paint.setAlpha(255);
							canvas.drawRect(hix + TILE_SIZE * iCount, hiy
									+ TILE_SIZE * rowCount, hix + TILE_SIZE
									* iCount + TILE_SIZE, hiy + TILE_SIZE
									* rowCount + TILE_SIZE, paint);
						}

						// draw the item
						drawTile(canvas, i.getTileSheet(), i.getTileSheet_x(),
								i.getTileSheet_y(), hix + TILE_SIZE * iCount,
								hiy + TILE_SIZE * rowCount);

						// check if selected
						if (sCount == hudSelect) {
							drawTile(canvas, 0, 0, 1, hix + TILE_SIZE * iCount,
									hiy + TILE_SIZE * rowCount);
						}

						// update the counter
						iCount++;
						sCount++;
						if (iCount >= (H_WIDTH - H_CONTROLS_WIDTH) / TILE_SIZE) {
							// new row
							iCount = 0;
							rowCount++;
						}
					}
				}

				// release the canvas
				holder.unlockCanvasAndPost(canvas);
			}
		}
	}
}

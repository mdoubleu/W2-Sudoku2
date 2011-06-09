package org.example.sudoku;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;


public class PuzzleView extends View {
	private static final String TAG="Sudoku";
	private final Game game;
	
	public PuzzleView(Context context){
		super(context);
		this.game=(Game)context;
		setFocusable(true);//allows user input in view...?
		setFocusableInTouchMode(true);
	}
	private float width;
	private float height;
	private int selectionX;
	private int selectionY;
	private final Rect selectionRect=new Rect();
	
	protected void onSizeChanged(int w, int h, int oldw, int oldh){
		width=w/9f;//whats f? something unique
		height=h/9f;
		getRect(selectionX, selectionY, selectionRect);
		Log.d(TAG, "onSizeChanged: width"+width+", height"+height);
		super.onSizeChanged(w, h, oldw, oldh);
	}
	private void getRect(int x, int y, Rect rect){
		rect.set((int)(x*width), (int)(y*height), (int)(x*width+width), (int)(y*height+height));
		
	}
	protected void onDraw(Canvas canvas){
		Paint background=new Paint();
		background.setColor(getResources().getColor(R.color.puzzle_background));
		canvas.drawRect(0,0, getWidth(), getHeight(), background);
		
		Paint dark=new Paint();
		dark.setColor(getResources().getColor(R.color.puzzle_dark));
		
		Paint highlite=new Paint();
		highlite.setColor(getResources().getColor(R.color.puzzle_highlite));
		
		Paint light=new Paint();
		light.setColor(getResources().getColor(R.color.puzzle_light));
		
		for(int i=0; i<9; i++){
			canvas.drawLine(0, i*height, getWidth(), i*height, light);
			canvas.drawLine(0, i*height+1, getWidth(), i*height+1, highlite);
			canvas.drawLine(i*width, 0, i*width, getHeight(), light);
			canvas.drawLine(i*width+1, 0, i*width+1, getHeight(), highlite);
		}
		for(int i=0; i<9; i++){
			if(i%3!=0)
				continue;
				canvas.drawLine(0, i*height, getWidth(), i*height, dark);
				canvas.drawLine(0, i*height+1, getWidth(), i*height+1, highlite);
				canvas.drawLine(i*width, 0, i*width, getHeight(), dark);
				canvas.drawLine(i*width+1, 0, i*width+1, getHeight(), highlite); 
		}
		Paint foreground=new Paint(Paint.ANTI_ALIAS_FLAG);
		foreground.setColor(getResources().getColor(R.color.puzzle_foreground));
		foreground.setStyle(Style.FILL);
		foreground.setTextSize(height*0.75f);
		foreground.setTextScaleX(width/height);
		foreground.setTextAlign(Paint.Align.CENTER);
		
		FontMetrics fm=foreground.getFontMetrics();//draws number in center of tile
		float x=width/2;
		float y=height/2-(fm.ascent+fm.descent)/2;
		for(int i=0; i<9; i++){
			for(int j=0; j<9; j++){
				canvas.drawText(this.game.getTileString(i,j), i*width+x, j*height+y, foreground);
				
			}
		}

		
		Paint hint=new Paint();
		int c[] = { getResources().getColor(R.color.puzzle_hint_0),
				getResources().getColor(R.color.puzzle_hint_1),
				getResources().getColor(R.color.puzzle_hint_2), };
		Rect r = new Rect();
		for(int i=0; i<9; i++) {
			for(int j=0; j<9; j++) {
					int movesleft=9-game.getUsedTiles(i, j).length;
					if (movesleft<c.length) {
						getRect(i, j, r);
						hint.setColor(c[movesleft]);
						canvas.drawRect(r, hint);
					}
			}
		}
		Log.d(TAG, "selectionRect="+selectionRect);
		Paint selected=new Paint();
		selected.setColor(getResources().getColor(R.color.puzzle_selected));
		canvas.drawRect(selectionRect, selected);
		
	}
	public boolean onKeyDown(int keyCode, KeyEvent event){
		Log.d(TAG, "onKeyDown: keycode="+keyCode+", event="+event);
		if(keyCode==KeyEvent.KEYCODE_DPAD_UP){
			select(selectionX, selectionY-1);
		}else if(keyCode==KeyEvent.KEYCODE_DPAD_DOWN){
			select(selectionX, selectionY+1);
		}else if(keyCode==KeyEvent.KEYCODE_DPAD_LEFT){
			select(selectionX-1, selectionY);
		}else if(keyCode==KeyEvent.KEYCODE_DPAD_RIGHT){
			select(selectionX+1, selectionY);
		}else if(keyCode==KeyEvent.KEYCODE_0){
			setSelectedTile(0);
		}else if(keyCode==KeyEvent.KEYCODE_SPACE){
			setSelectedTile(0);
		}else if(keyCode==KeyEvent.KEYCODE_1){
			setSelectedTile(1);
		}else if(keyCode==KeyEvent.KEYCODE_2){
			setSelectedTile(2);
		}else if(keyCode==KeyEvent.KEYCODE_3){
			setSelectedTile(3);
		}else if(keyCode==KeyEvent.KEYCODE_4){
			setSelectedTile(4);
		}else if(keyCode==KeyEvent.KEYCODE_5){
			setSelectedTile(5);
		}else if(keyCode==KeyEvent.KEYCODE_6){
			setSelectedTile(6);
		}else if(keyCode==KeyEvent.KEYCODE_7){
			setSelectedTile(7);
		}else if(keyCode==KeyEvent.KEYCODE_8){
			setSelectedTile(8);
		}else if(keyCode==KeyEvent.KEYCODE_9){
			setSelectedTile(9);
		}else if(keyCode==KeyEvent.KEYCODE_ENTER ||keyCode==KeyEvent.KEYCODE_DPAD_CENTER){
			game.showKeypadOrError(selectionX,selectionY);
		}
		else{
			return super.onKeyDown(keyCode, event);
		}
		return true;
	}
	private void select (int x, int y){
		invalidate(selectionRect);
		selectionX=Math.min(Math.max(x, 0), 8);
		selectionY=Math.min(Math.max(y,0), 8);
		getRect(selectionX,selectionY, selectionRect);
		invalidate(selectionRect);
	}
	public boolean onTouchEvent(MotionEvent event){
		if(event.getAction()!=MotionEvent.ACTION_DOWN){
			return super.onTouchEvent(event);
		}
			select((int)(event.getX()/width),(int)(event.getY()/height));
			game.showKeypadOrError(selectionX, selectionY);
			Log.d(TAG, "onTouchEvent:x"+selectionX+", y "+selectionY);
			return true;
		}
	public void setSelectedTile(int tile){
		if(game.setTileIfValid(selectionX, selectionY, tile)){
			invalidate();
		}else{
			Log.d(TAG, "setSelectedTile: invalid: " +tile);
			startAnimation(AnimationUtils.loadAnimation(game, R.anim.shake));
		}
		
		
	}
}
	


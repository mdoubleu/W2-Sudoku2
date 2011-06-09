package org.example.sudoku;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;

public class Sudoku extends Activity implements OnClickListener{

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        View continueButton=findViewById(R.id.continue_button);
        continueButton.setOnClickListener(this);
        View newButton=findViewById(R.id.new_button);
        newButton.setOnClickListener(this);
        View aboutButton=findViewById(R.id.about_button);
        aboutButton.setOnClickListener(this);
        View exitButton=findViewById(R.id.exit_button);
        exitButton.setOnClickListener(this);
        View donateButton=findViewById(R.id.donate_button);
        donateButton.setOnClickListener(this);
    }
	public void onClick(View v) {
		if(v.getId()==R.id.about_button){
			Intent i=new Intent(this, About.class);
			startActivity(i);
		}else if(v.getId()==R.id.new_button){
			openNewGameDialog();
		}else if(v.getId()==R.id.exit_button){
			finish();
		}else if(v.getId()==R.id.donate_button){
			Intent x=new Intent(this, Donate.class);
			startActivity(x);
		}
		
	}
	private static final String TAG="Sudoku";
	public void openNewGameDialog(){
		new AlertDialog.Builder(this)
		.setTitle(R.string.new_game_title)
		.setItems(R.array.difficulty, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int i) {
				startGame(i);
			}
		})
		.show();
		
	}
	private void startGame(int i){
		Log.d(TAG, "clicked on "+ i);
		Intent intent=new Intent(this, Game.class);
		intent.putExtra(Game.KEY_DIFFICULTY, i);
		startActivity(intent);
	}
	public boolean onCreateOptionsMenu(Menu menu){
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater=getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}
	public boolean onOptionsItemSelected(MenuItem item){
		if(item.getItemId()==R.id.settings){
			startActivity(new Intent(this, Prefs.class));
			return true;
		}
		return false;
	}
}
package com.example.hhvanet;

import com.example.slidrlayout.fragment.ContentFragment;
import com.example.slidrlayout.interfaces.Resourceble;
import com.example.slidrlayout.util.ViewAnimator;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class DriveAssess extends AppCompatActivity{
	private Button btn1,btn2;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_service);
		
		setActionBar();
//		ActionBar actionBar = getActionBar();
//		actionBar.setDisplayHomeAsUpEnabled(true);
		
		btn1 = (Button)findViewById(R.id.button1);
		btn2 = (Button)findViewById(R.id.button2);
		btn1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				startActivity(new Intent(DriveAssess.this,RecognizeResult.class));
			}
		});
		btn2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				startActivity(new Intent(DriveAssess.this,AssessAll.class));
			}
		});

	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.driveassess, menu);
	    return true;
	}
	
    private void setActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("VANET");
        toolbar.setLogo(R.drawable.gemini);
        
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.drihome:
                        Toast.makeText(DriveAssess.this, "return home !", Toast.LENGTH_SHORT).show();
                        finish();
                        break;
                    default:
                        Toast.makeText(DriveAssess.this, "No Correct enter !", Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });
    }
}

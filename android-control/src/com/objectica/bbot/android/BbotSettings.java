package com.objectica.bbot.android;

import com.objectica.bbot.android.util.SettingTypes;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class BbotSettings extends Activity {
	
	private EditText hostUrlField;
	private SharedPreferences settings;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);
		init();
	}
	
	public void init(){
		settings = getSharedPreferences(SettingTypes.BBOT_APP_NAME.getValue(), MODE_PRIVATE);
		hostUrlField = (EditText)findViewById(R.id.bbotHostUrl);		
		hostUrlField.setText(settings.getString(SettingTypes.BBOT_HOSTNAME.getValue(), ""));		
	}
	
	/**
	 * Called when user clicks 'Save' button
	 * @param view
	 */
	public void onSettingsSave(View view)
	{
	      SharedPreferences.Editor editor = settings.edit();
	      editor.putString(SettingTypes.BBOT_HOSTNAME.getValue(), hostUrlField.getText().toString());
	      editor.commit();	      
          startActivityForResult(new Intent(this, BbotControl.class), 0);
	}
	
}

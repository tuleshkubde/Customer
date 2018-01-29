package com.smartdatainc.activities;

import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

import com.smartdatainc.dataobject.AppInstance;
import com.smartdatainc.fudo.R;
import com.smartdatainc.utils.Constants;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;




/**
 * Created by Anurag Sethi
 * The activity is used for handling the splash screen operations along with redirection to login screen
 * after the splash screen delay is exhausted  
 */
public class MainActivity extends AppActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Fabric.with(this, new Crashlytics() );
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);


        //initializing the data
        initData();

        //handler to close the splash activity after the set time
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

               
                
                //Intent to call the DashboardActivity Activity
               Intent intentObj = new Intent(MainActivity.this, HotelListActivity.class);
                
                startActivity(intentObj);
                MainActivity.this.finish();

                MainActivity.this.overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            }
        }, Constants.SplashScreen.SPLASH_DELAY_LENGTH);
    }


    /**
     * The method is used to initialize the objects
     * @return none
     */
    public void initData() {

        AppInstance.getAppInstance();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

package onur.timey;


import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.View;


/**
 * Created by onurh on 24.07.2016.
 */
public class Settings extends PreferenceActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //try to use ZaaTheme
        /*
        or you can use this
                View someView = findViewById(R.id.screen);
                View root = someView.getRootView();
                root.setBackgroundColor(getResources().getColor(color.white));
         */

        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferencescreen);
    }



}
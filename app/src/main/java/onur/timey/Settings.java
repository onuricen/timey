package onur.timey;


import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;


/**
 * Created by onurh on 24.07.2016.
 */
public class Settings extends PreferenceActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferencescreen);
    }



}
package onur.timey;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Toast;


/**
 * Created by onurh on 24.07.2016.
 */
public class Settings extends PreferenceActivity  {


    public ListPreference listPreference=(ListPreference)findPreference("circular_bar_color_setting_key");
    public boolean keyIsOn;
    String keyPreference;

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

        listPreference=(ListPreference)findPreference("circular_bar_color_setting_key");

        getProKey();
        checkProKey();
        getCpbColorPreference();




    }

    public void setListPreferenceSummary(String summary){
        listPreference=(ListPreference)findPreference("circular_bar_color_setting_key");
        listPreference.setSummary(summary);
    }


    private void plusTimesShownToast(){
        SharedPreferences.Editor editor=getSharedPreferences("toastKey",MODE_PRIVATE).edit();
        editor.putInt("toastIntKey",1);
        editor.commit();

    }

    public void checkProKey (){
        SharedPreferences sharedPreferences=getSharedPreferences("proKey",MODE_PRIVATE);
        String getKey=sharedPreferences.getString("key","");
        if(getKey.equals("onispecial")) {
            listPreference.setEnabled(true);

            SharedPreferences prf=getSharedPreferences("toastKey",MODE_PRIVATE);
            int timesShownToast=prf.getInt("toastIntKey",0);
            if(timesShownToast==0) {
                Toast.makeText(this, "Pro Özellikler Açıldı", Toast.LENGTH_LONG).show();
                plusTimesShownToast();
            }




        }

    }

    public void getProKey (){
        SharedPreferences getKeyPreference= PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        keyPreference=getKeyPreference.getString("key_for_pro_pKey",null);

        SharedPreferences.Editor editor=getSharedPreferences("proKey",MODE_PRIVATE).edit();
        editor.putString("key",keyPreference);
        editor.commit();

    }

    public void getCpbColorPreference(){
        SharedPreferences getColorPreference=PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String colorPreference=getColorPreference.getString("circular_bar_color_setting_key","Kırmızı");
        Log.i("Renk","Seçilen"+colorPreference);
    }



}
package onur.timey;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Created by onurh on 17.07.2016.
 */
public class TimeActivity extends AppCompatActivity {
    int lenght;
    String output;
    long seconds;

   

    Snackbar snackbar;
    CountDownTimer countDownTimer;
    private TextView minText;
    private Button stopButton;


    @Override
    protected void onPause() {
        //write there later
        super.onPause();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setTheme(R.style.ZaaTheme);
        setContentView(R.layout.time_activity);

        final View view=findViewById(android.R.id.content);

        stopButton=(Button)findViewById(R.id.stopButton);

        minText=(TextView)findViewById(R.id.minuteText);




        snackbar=Snackbar.make(view,"Unutma!   eğer uygulamadan çıkarsan süren sıfırlanır! ",Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("Tamam", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });
        snackbar.show();







        countDownTimer=new CountDownTimer(2401000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                minText.setText((formatTime(millisUntilFinished)));


            }

            @Override
            public void onFinish() {
                Intent after40MinIntent=new Intent(TimeActivity.this.getApplicationContext(),after40Activity.class);
                startActivity(after40MinIntent);
            }



        }.start();

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backIntent=new Intent(TimeActivity.this.getApplicationContext(),MainActivity.class);
                output="0  :  0";
                countDownTimer.cancel();
            }
        });



        }




    public String formatTime(long mlSeconds){
        output="";
        seconds=mlSeconds/1000;
        long minutes=seconds/60;
        long hours=minutes/60;

        seconds=seconds%60;
        minutes=minutes%60;
        hours=hours%60;


        String secondsD=String.valueOf(seconds);
        String minutesD=String.valueOf(minutes);
        String hoursD=String.valueOf(hours);


        if (seconds<10) {
            secondsD = "0" + seconds;
        }


        if (minutes<10) {
            minutesD = "0" + minutes;
        }
            //saat olayını yazdım ama return etmedim çünkü şuan sadece 40 dkizin veriyorum kullancı custom bir süre ayarlayamıyor //ileride açarsam custom time diye
        if (hours<10) {
            hoursD = "0" + hours;
        }

        output = minutesD + " : " + secondsD;

        return output;


    }



    }







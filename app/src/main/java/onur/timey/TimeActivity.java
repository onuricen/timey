package onur.timey;



import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;




import com.mikhaellopez.circularprogressbar.CircularProgressBar;


// i know this Class sucks little bit, but i promise i will do some refactoring (e.g setters)

/**
 * Created by onurh on 17.07.2016.
 */
public class TimeActivity extends AppCompatActivity {

    //  dont forget to consider re-writing in service instead of using PARTIAL_WAKE_LOCK


    //Check here for bug http://stackoverflow.com/questions/2614719/how-do-i-get-the-sharedpreferences-from-a-preferenceactivity-in-android

    PowerManager.WakeLock wakeLock;
    PowerManager powerManager;




    @BindView(R.id.breakOrMainTimer)
    TextView breakOrMainTimerText;

    @BindView(R.id.secondText)
    TextView secondsText;

    @BindView(R.id.minuteText)
    TextView minText;

    @BindView(R.id.stopButton)
    Button stopButton;

    @BindView(R.id.continueButton)
    Button continueButton;

    @BindView(R.id.startButton)
    Button startButton;

    @BindView(R.id.cancelButton)
    Button cancelButton;

    @BindView(R.id.circularProgressBar)
    CircularProgressBar circularProgressBar;



    float timeNormal;
    float timeBreak;


    //look circularprogressbar s code to understand how animationwith timing works
    public CountDownTimerWithPause  countDownTimerWithPause = new CountDownTimerWithPause(1500000, 1) {
        // 1500000
        @Override
        public void onTick(long millisUntilFinished) {

            minText.setText(formatTimeMinutes(millisUntilFinished));
            secondsText.setText(formatTimeSeconds(millisUntilFinished));
            timeNormal=circularProgressBar.getProgress();

        }


        @Override
        public void onFinish() {
            onBreak = true;
            startBreakTimeTimer();

        }

    };



   public CountDownTimerWithPause countDownTimerWithPauseBreak = new CountDownTimerWithPause(300000, 1) {
        //300000


    //add a RelativeLayout inside

        @Override
        public void onTick(long millisUntilFinished) {

            minText.setText(formatTimeMinutes(millisUntilFinished));
            secondsText.setText(formatTimeSeconds(millisUntilFinished));
            timeBreak=circularProgressBar.getProgress();


        }

        @Override
        public void onFinish() {

            SharedPreferences getPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());


            boolean loop = getPrefs.getBoolean("countdowntimer_loop_pKey", true);


            boolean notifSetting = getPrefs.getBoolean("countdowntimer_notifications_pKey", true);
            if (notifSetting){
                if(!loop){
                    breakFinishNotfSentence="Mola Süren Bitti";
                }
                onBreakFinishNotification();
            }


            if (loop == true) {
                onBreak = false;
                startMainTimer();
                Log.i("CdtLoop", "CountDownTimer Loop true");
            } else {
                Intent goBackMainIntent = new Intent(getApplicationContext(), TimeActivity.class);
                startActivity(goBackMainIntent);
                finish();
            }
        }
    };









    long seconds;
    public boolean onBreak;

    String breakFinishNotfSentence="Mola Süren Bitti Çalışmana Devam Et";
   




    @Override
    protected void onPause() {
        super.onPause();
        if (wakeLock.isHeld()) {
            Log.i("onPause", "Wakelock didn't released (still works)");
        } else {
            Log.i("onPause", "Wakelock released!!! (doesnt work)");
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.time_activity);

        ButterKnife.bind(this);

        //changing actionbar's color to background color
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xff222222));

        //removing actionbar's shadow
        getSupportActionBar().setElevation(0);

        getSupportActionBar().setTitle(null);



        powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "TimeyWakeLock");
        wakeLock.acquire();

        if (wakeLock.isHeld()) {
            Log.i("onCreate", "WakeLock didn't released (still works)");
        } else {
            Log.i("onCreate", "WakeLock released!!!");
        }







    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()) {

            case R.id.feedbackItem: {

                Intent feedbackIntent = new Intent(this, FeedbackActivity.class);
                startActivity(feedbackIntent);
                return true;
            }

            case R.id.preferenceItem: {

                Intent preferenceIntent = new Intent(this, Settings.class);
                startActivity(preferenceIntent);
                return true;
            }
            case R.id.aboutItem:{

                Intent aboutIntent=new Intent(this,AboutActivity.class);
                startActivity(aboutIntent);
                return true;
            }



        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    private void startBreakTimeTimer() {


        SharedPreferences getPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        breakOrMainTimerText.setText("Mola");
        boolean notifSetting = getPrefs.getBoolean("countdowntimer_notifications_pKey", true);

        if (notifSetting == true) {
            onBreakTimeNotification();

        }

        countDownTimerWithPauseBreak.start();
        circularProgressBar.setProgress(0);
        circularProgressBar.setProgressWithAnimation(100,300000);

    }




    private void startMainTimer() {


        breakOrMainTimerText.setText("Çalışma");
        countDownTimerWithPause.start();
        circularProgressBar.setProgressWithAnimation(100,1500000);


    }

    public String formatTimeMinutes(long mlSeconds) {
        seconds = mlSeconds / 1000;
        long minutes = seconds / 60;
        minutes = minutes % 60;
        String minutesD = String.valueOf(minutes);
        if (minutes < 10) {
            minutesD = "0" + minutes;
        }
        return minutesD;
    }

    public String formatTimeSeconds(long mlSeconds) {
        seconds=mlSeconds/1000;


        seconds=seconds%60;


        String secondsD=String.valueOf(seconds);


        if (seconds<10) {
            secondsD = "0" + seconds;
        }


        return secondsD;
    }




    @OnClick(R.id.stopButton)
    void stopButtonAction() {
        if (wakeLock.isHeld()) {
            Log.i("stopButton", "Wakelock didn't released (still works)");
        } else {
            Log.i("stopButton", "Wakelock released!!! (doesnt work)");
        }
        circularProgressBar.setProgressWithAnimation(0);
        if (onBreak) {
            countDownTimerWithPauseBreak.pause();
            stopButton.setVisibility(View.INVISIBLE);
            continueButton.setVisibility(View.VISIBLE);
        }
        else if(!onBreak) {
            countDownTimerWithPause.pause();
            stopButton.setVisibility(View.INVISIBLE);
            continueButton.setVisibility(View.VISIBLE);
        }

    }
    @OnClick(R.id.continueButton)void continueButton(){
        if (wakeLock.isHeld()){
            Log.i("Cont Button","Wakelock didn't released (still works)");
        }
        else {
            Log.i("Cont Button","Wakelock released!!! (doesnt work)");
        }
        if (onBreak){
            countDownTimerWithPauseBreak.resume();
            circularProgressBar.setProgressWithAnimation(timeNormal);
            //dont get the time of circular progress bar ,instead of get the time from countdowntimer
            stopButton.setVisibility(View.VISIBLE);

            continueButton.setVisibility(View.INVISIBLE);
        }
        circularProgressBar.setProgressWithAnimation(timeBreak);

        countDownTimerWithPause.resume();

        stopButton.setVisibility(View.VISIBLE);

        continueButton.setVisibility(View.INVISIBLE);

    }
    @OnClick(R.id.startButton)void startButton(){
        startButton.setVisibility(View.INVISIBLE);

        stopButton.setVisibility(View.VISIBLE);

        startMainTimer();
    }
    @OnClick(R.id.cancelButton)void cancelButton(){
        Intent gobackIntent=new Intent(TimeActivity.this,TimeActivity.class);

        circularProgressBar.setProgress(0);

        if (onBreak) {
            countDownTimerWithPauseBreak.cancel();

            startActivity(gobackIntent);

            finish();

        }
        else if(!onBreak) {
            countDownTimerWithPause.cancel();

            startActivity(gobackIntent);

            finish();
        }
    }



    public void onBreakTimeNotification() {

        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_timer_black_48dp);


        //check wear notf again
        Intent intent = new Intent(this, onur.timey.TimeActivity.class);

        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


        intent.putExtra("startBreakTimer", "startBreakTimeTimer()");
        PendingIntent pendingIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, 0);
        Notification breakTimeNotification = new Notification.Builder(this)


                .setContentTitle("Timey")
                .setContentText("Mola süren başladı!")
                //fix the ıcon it doesnt work
                .setLargeIcon(largeIcon)
                .setDefaults(Notification.DEFAULT_ALL)
                .setSmallIcon(R.drawable.ic_timer_white_36dp)
                .setSound(notification)
                .build();


        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);


        notificationManager.notify(0, breakTimeNotification);


    }



    public void onBreakFinishNotification() {



        NotificationCompat.WearableExtender wearableExtender =
                new NotificationCompat.WearableExtender()
                        .setHintHideIcon(true);


        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_timer_black_48dp);


        Intent intent = new Intent(this, onur.timey.TimeActivity.class);

        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        Notification breakTimeNotification = new NotificationCompat.Builder(this)
                .setLargeIcon(largeIcon)
                .setContentTitle("Timey")
                .setContentText(breakFinishNotfSentence)
                .setDefaults(Notification.DEFAULT_ALL)
                .setSound(notification)
                .setSmallIcon(R.drawable.ic_timer_white_36dp)
                .extend(wearableExtender)
                .build();


        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);


        notificationManager.notify(0, breakTimeNotification);


    }

}









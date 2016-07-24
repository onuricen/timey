package onur.timey;



import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.NotificationCompat.WearableExtender;


// i know this activity sucks little bit but i promise i will refactor some methods with butterknife libary bla bla bla (being a beginner sucks)

/**
 * Created by onurh on 17.07.2016.
 */
public class TimeActivity extends AppCompatActivity {

      //  dont forget to consider re-writing in service instead of using PARTIAL_WAKE_LOCK



    PowerManager.WakeLock wakeLock;
    PowerManager powerManager;

    CountDownTimerWithPause countDownTimerWithPause;
    CountDownTimerWithPause countDownTimerWithPauseBreak;





    @BindView(R.id.secondText) TextView secondsText;

    @BindView(R.id.minuteText)TextView minText;

    @BindView(R.id.stopButton)Button stopButton;

    @BindView(R.id.continueButton)Button continueButton;

    @OnClick(R.id.stopButton) void stopButtonAction(){
        wakeLock.release();
        if (wakeLock.isHeld()){
            Log.i("stopButton","Wakelock didn't released (still works)");
        }
        else {
            Log.i("stopButton","Wakelock released!!! (doesnt work)");
        }

        countDownTimerWithPause.pause();
        stopButton.setVisibility(View.INVISIBLE);
        continueButton.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.continueButton)void continueButton(){

        wakeLock.acquire();
        if (wakeLock.isHeld()){
            Log.i("Cont Button","Wakelock didn't released (still works)");
        }
        else {
            Log.i("Cont Button","Wakelock released!!! (doesnt work)");
        }
        countDownTimerWithPause.resume();
        stopButton.setVisibility(View.VISIBLE);
        continueButton.setVisibility(View.INVISIBLE);
    }



    long seconds;



    public boolean startAgainAnswer;


    @Override
    protected void onPause() {
        super.onPause();
        if (wakeLock.isHeld()){
            Log.i("onPause","Wakelock didn't released (still works)");
        }
        else {
            Log.i("onPause","Wakelock released!!! (doesnt work)");
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.time_activity);

        ButterKnife.bind(this);

        powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,"TimeyWakeLock");
        wakeLock.acquire();

        if(wakeLock.isHeld()){
            Log.i("onCreate","WakeLock didn't released (still works)");
        } else {
            Log.i("onCreate","WakeLock released!!!");
        }


        startMainTimer();






    }



    private void onBreakFinishNotification(){
        Intent intent=new Intent(this,TimeActivity.class);
        intent.putExtra("startBreakTimer","startBreakTimeTimer()");
        PendingIntent pendingIntent=PendingIntent.getActivity(this,(int)System.currentTimeMillis(),intent,0);
        Notification breakTimeNotification=new NotificationCompat.Builder(this)

                .setContentTitle("Timey")
                .setContentText("Mola süren bitti çalışmaya geri dön")
                .setContentIntent(pendingIntent)
                //fix the ıcon
                .setSmallIcon(getApplicationInfo().icon)
                .extend(new NotificationCompat.WearableExtender())
                .build();


        NotificationManager notificationManager=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        breakTimeNotification.flags |= Notification.FLAG_AUTO_CANCEL;

        notificationManager.notify(0, breakTimeNotification);

    }


       //write something to when notf clicked go break activity
    private void onBreakTimeNotification(){

        NotificationCompat.WearableExtender wearableExtender =
                new NotificationCompat.WearableExtender()
                        .setHintHideIcon(true);




        Intent intent=new Intent(this,TimeActivity.class);
        PendingIntent pendingIntent=PendingIntent.getActivity(this, 0, intent,PendingIntent.FLAG_UPDATE_CURRENT);
        Notification breakTimeNotification=new NotificationCompat.Builder(this)
                .setSmallIcon(getApplicationInfo().icon)
                .setContentTitle("Timey")
                .setContentText("Mola Süren Bitti Çalışmana Devam Et")
                .setContentIntent(pendingIntent)
                .extend(wearableExtender)
                .build();




        NotificationManager notificationManager=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);


        notificationManager.notify(0, breakTimeNotification);

    }


    private void startBreakTimeTimer(){
        onBreakTimeNotification();
        //300000

        countDownTimerWithPauseBreak=new CountDownTimerWithPause(10000,1) {
            @Override
            public void onTick(long millisUntilFinished) {
                minText.setText(formatTimeMinutes(millisUntilFinished));
                secondsText.setText(formatTimeSeconds(millisUntilFinished));

            }

            @Override
            public void onFinish() {
                onBreakTimeNotification();
                          /*open a dialog and ask do you want to contiune working and while user wants to work loop
                           countDownTimerWithPause */

                startAgainAnswer=true;
                if (startAgainAnswer==true){
                      startMainTimer();
                }
            }
        }.start();

    }


    private void startMainTimer() {

        // 1500000

        countDownTimerWithPause = new CountDownTimerWithPause(10000, 1) {
            @Override
            public void onTick(long millisUntilFinished) {


                minText.setText(formatTimeMinutes(millisUntilFinished));
                secondsText.setText(formatTimeSeconds(millisUntilFinished));


            }


            @Override
            public void onFinish() {
                //write notification
                startBreakTimeTimer();

            }

        }.start();
    }



    public String formatTimeMinutes(long mlSeconds){
        seconds=mlSeconds/1000;
        long minutes=seconds/60;
        minutes=minutes%60;
        String minutesD=String.valueOf(minutes);
        if (minutes<10) {
            minutesD = "0" + minutes;
        }
        return minutesD;
    }

    public String formatTimeSeconds(long mlSeconds){
        seconds=mlSeconds/1000;
        seconds=seconds%60;
        String secondsD=String.valueOf(seconds);
        if (seconds<10) {
            secondsD = "0" + seconds;
        }
        return secondsD;
    }



}









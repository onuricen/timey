package onur.timey;


import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.util.Map;
import java.util.Set;


public class CountDownTimerService extends Service {

    static final public String RESULT = "onur.timey.Service.REQUEST_PROCESSED";
    static final public String MESSAGE = "onur.timey.Service.COPA_MSG";

    private boolean timerStarted;
    private LocalBroadcastManager broadcast;
    protected static boolean onBreak;


    @Override
    public void onCreate() {
        super.onCreate();
        broadcast=LocalBroadcastManager.getInstance(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        cancelMainTimer();
        cancelBreakTimer();
    }

    public void sendResult(String message) {
        Intent intent = new Intent(RESULT);
        if(message != null)
            intent.putExtra(MESSAGE, message);
        broadcast.sendBroadcast(intent);
    }

    private void timerStarted(){
        timerStarted=true;
        SharedPreferences t=getSharedPreferences("timerStarted",MODE_PRIVATE);
        t.edit().putBoolean("timerStarted",timerStarted).apply();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startMainTimer();
        timerStarted();
        return START_STICKY;
    }



    private CountDownTimerWithPause countDownTimerWithPause = new CountDownTimerWithPause(1500000, 1) {
        // 1500000
        @Override
        public void onTick(long millisUntilFinished) {


            if (TimeActivity.cancelButtonClicked){
                countDownTimerWithPauseBreak.cancel();
                stopSelf();
               TimeActivity.cancelButtonClicked=false;

            }

            else {
               String milis=String.valueOf(millisUntilFinished);
                sendResult(milis);
            }
        }


        @Override
        public void onFinish() {
            SharedPreferences p=getSharedPreferences("onBreak",MODE_PRIVATE);
            p.edit().putBoolean("onBreak",onBreak);
            onBreak = true;
            startBreakTimer();
        }

    };

    private void onTickDebug(boolean tof){
        if (tof==true)
        Log.i("onTick","Still Works"+" "+System.currentTimeMillis()/100);
        else { return; }
    }


    private CountDownTimerWithPause countDownTimerWithPauseBreak = new CountDownTimerWithPause(300000, 1) {
        //300000




        @Override
        public void onTick(long millisUntilFinished) {

            //listening for a cancel click
            if (TimeActivity.cancelButtonClicked){
                countDownTimerWithPauseBreak.cancel();
                stopSelf();
                TimeActivity.cancelButtonClicked=false;

            }

            else {
                String milis=String.valueOf(millisUntilFinished);
                sendResult(milis);
                }
            }





        @Override
        public void onFinish() {



            SharedPreferences getPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());


            boolean loop = getPrefs.getBoolean("countdowntimer_loop_pKey", true);


            boolean notifSetting = getPrefs.getBoolean("countdowntimer_notifications_pKey", true);
            if (notifSetting){
                if(!loop){
                    setBreakFinishNotfSentence("Mola Süren Bitti");
                }


                //insert notf here
            }

            onBreak = false;

            if (loop == true) {
                startMainTimer();
                Log.i("CdtLoop", "CountDownTimer Loop true");
            } else {
                Intent goBackMainIntent = new Intent(getApplicationContext(), TimeActivity.class);
                startActivity(goBackMainIntent);

            }
        }
    };

    private void setBreakFinishNotfSentence(String sentence){
        SharedPreferences prefs=getSharedPreferences("notf_sentence",MODE_PRIVATE);
        prefs.edit().putString("breakNotf",sentence).commit();
    }

    private void startMainTimer(){
        countDownTimerWithPause.start();
    }
    private void startBreakTimer(){
        countDownTimerWithPauseBreak.start();
    }



    protected  void cancelMainTimer(){
        countDownTimerWithPause.cancel();
    }

    protected  void cancelBreakTimer(){
        countDownTimerWithPauseBreak.cancel();
    }

    @Override
    public IBinder onBind(Intent intent) {
       return null;
    }
}

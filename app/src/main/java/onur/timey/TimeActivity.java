package onur.timey;



import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
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



import android.os.Handler;




// i know this Class sucks little bit, but i promise i will do some refactoring (e.g setters)

/**
 * Created by onurh on 17.07.2016.
 */
public class TimeActivity extends AppCompatActivity {

    //  dont forget to consider re-writing in service instead of using PARTIAL_WAKE_LOCK




    PowerManager.WakeLock wakeLock;
    PowerManager powerManager;

    CountDownTimerService serviceObj=new CountDownTimerService();

    private long seconds;
    public static Handler handler;

    private String breakFinishNotfSentence="Mola Süren Bitti Çalışmana Devam Et";
    private int getCurrentProgress;
    private  String keyPreference;
    public static boolean cancelButtonClicked;



    @BindView(R.id.breakOrMainTimer)
     TextView breakOrMainTimerText;

    @BindView(R.id.secondText)
    TextView secondsText;

    @BindView(R.id.minuteText)
     TextView minText;


    @BindView(R.id.continueButton)
    Button continueButton;

    @BindView(R.id.startButton)
    FloatingActionButton startButton;



    @BindView(R.id.stopButton)
    FloatingActionButton cancelButton;

    @BindView(R.id.circularProgressBar)
    CircularProgressBar circularProgressBar;





    @OnClick(R.id.startButton)void startButton(){
        startButton.setVisibility(View.INVISIBLE);
        cancelButton.setVisibility(View.VISIBLE);
        startMainTimer();
    }



    @OnClick(R.id.stopButton)void cancelButton(){

        cancelButtonClicked=true;


        Intent gobackIntent=new Intent(TimeActivity.this,TimeActivity.class);
        gobackIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
        setTimerBoolean(false);

        if (CountDownTimerService.onBreak) {
            serviceObj.cancelBreakTimer();

            TimeActivity.this.startActivity(gobackIntent);

            Intent service=new Intent(TimeActivity.this,CountDownTimerService.class);
            stopService(service);
            startActivity(gobackIntent);

            finish();


        }
        else if(!CountDownTimerService.onBreak) {

            serviceObj.cancelMainTimer();

            startActivity(gobackIntent);

            Intent service=new Intent(TimeActivity.this,CountDownTimerService.class);
            stopService(service);

            finish();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver((receiver),
                new IntentFilter(CountDownTimerService.RESULT)
        );
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.time_activity);

        ButterKnife.bind(this);

        AudioManager a=(AudioManager)getSystemService(Context.AUDIO_SERVICE);


        //changing actionbar's color to background color
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xff222222));

        //removing actionbar's shadow
        getSupportActionBar().setElevation(0);

        //removing title of actionbar
        getSupportActionBar().setTitle(null);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        overridePendingTransition(R.anim.fadein,R.anim.fadeout);




        SharedPreferences prefs = getSharedPreferences("zaa", MODE_PRIVATE);
        if (prefs.getBoolean("firstrun", true)) {
            prefs.edit().putBoolean("firstrun", false).commit();
            Intent appIntroIntent=new Intent(this,AppIntro.class);
            startActivity(appIntroIntent);
        }



        powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "TimeyWakeLock");
        wakeLock.acquire();

        if (wakeLock.isHeld()) {
            Log.i("onCreate", "WakeLock didn't released (still works)");
        } else {
            Log.i("onCreate", "WakeLock released!!!");
        }


        if (timerStarted()){
            startButton.setVisibility(View.INVISIBLE);
            cancelButton.setVisibility(View.VISIBLE);
        }



    }


    private boolean timerStarted(){
        SharedPreferences s=getSharedPreferences("timerStarted",MODE_PRIVATE);
          return s.getBoolean("timerStarted",false);
    }

    private void setTimerBoolean(boolean tf){
        SharedPreferences s=getSharedPreferences("timerStarted",MODE_PRIVATE);
        s.edit().putBoolean("timerStarted",tf).apply();
    }


    public  boolean isCancelButtonClicked() {
        return cancelButtonClicked;
    }

    public void setCancelButtonClicked(boolean cancelButtonClicked) {
        this.cancelButtonClicked = cancelButtonClicked;
    }

    private void setCpbColor(){

        SharedPreferences getColorPreference= PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String selectedColor=getColorPreference.getString("circular_bar_color_setting_key","Kırmızı");


        if(selectedColor.equals("Kırmızı")){
            circularProgressBar.setColor(ContextCompat.getColor(this,R.color.Kırmızı));
        }
        if (selectedColor.equals("Mor")){
            circularProgressBar.setColor(ContextCompat.getColor(this,R.color.Mor));
        }
        if(selectedColor.equals("Yeşil")){
            circularProgressBar.setColor(ContextCompat.getColor(this,R.color.Yeşil));
        }
        if(selectedColor.equals("Turuncu")){
            circularProgressBar.setColor(ContextCompat.getColor(this,R.color.Turuncu));
        }
        if(selectedColor.equals("Mavi")){
            circularProgressBar.setColor(ContextCompat.getColor(this,R.color.Mavi));
        }
        if(selectedColor.equals("Zeytin Yeşili")){
            circularProgressBar.setColor(ContextCompat.getColor(this,R.color.zeytinYesili));
        }
        if (selectedColor.equals("Beyaz")){
            circularProgressBar.setColor(ContextCompat.getColor(this,R.color.Beyaz));
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
            case R.id.staticsItem:{
                Intent staticsIntent=new Intent(this,StaticsActivity.class);
                startActivity(staticsIntent);
                return true;
            }







        }
        return super.onOptionsItemSelected(item);
    }

    private void startBreakTimeTimer() {


        SharedPreferences getPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        breakOrMainTimerText.setText("Mola");
        boolean notifSetting = getPrefs.getBoolean("countdowntimer_notifications_pKey", true);

        if (notifSetting == true) {
            onBreakTimeNotification();

        }

        startService(new Intent(this,CountDownTimerService.class)); //todo start service here
        circularProgressBar.setProgress(0);


        circularProgressBar.setProgressWithAnimation(100, 300000);

    }

    private void startMainTimer() {


        breakOrMainTimerText.setText("Çalışma");
        startService(new Intent(this,CountDownTimerService.class));  //todo start service here
        circularProgressBar.setProgressWithAnimation(100, 1500000);
        setCpbColor();

    }

    private String formatTimeMinutes(long mlSeconds) {
        seconds = mlSeconds / 1000;
        long minutes = seconds / 60;
        minutes = minutes % 60;
        String minutesD = String.valueOf(minutes);
        if (minutes < 10) {
            minutesD = "0" + minutes;
        }
        return minutesD;
    }

    private String formatTimeSeconds(long mlSeconds) {
        seconds=mlSeconds/1000;


        seconds=seconds%60;


        String secondsD=String.valueOf(seconds);


        if (seconds<10) {
            secondsD = "0" + seconds;
        }


        return secondsD;
    }


    private void onBreakTimeNotification() {

        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_timer_black_48dp);

        if (!cancelButtonClicked) {
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
        else {
            return;
        }
    }

    private boolean isServiceRunning(){
         String serviceName="onur.timey.CountDownTimerService";
        ActivityManager manager =(ActivityManager)getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service:manager.getRunningServices(Integer.MAX_VALUE)){
            if (serviceName.equals(service.service.getClassName())){
                return true;
            }
        }
        return false;
    }

    private String getBreakFinishNotfSentence(){
        SharedPreferences prefs=getSharedPreferences("notf_sentence",MODE_PRIVATE);
        String firstSentence="Mola süren bitti";
        if (!isServiceRunning()){
            firstSentence="Mola süren bitti çalışmana geri dön";
        }
        String sentence=prefs.getString("breakNotf",firstSentence);
        return sentence;
    }

    protected  void onBreakFinishNotification() {


        if (!cancelButtonClicked) {
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
                    .setContentText(getBreakFinishNotfSentence())
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setSound(notification)
                    .setSmallIcon(R.drawable.ic_timer_white_36dp)
                    .extend(wearableExtender)
                    .build();


            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);


            notificationManager.notify(0, breakTimeNotification);


        }
        else {
            return;
        }
    }

        public BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String s = intent.getStringExtra(CountDownTimerService.MESSAGE);
                long milis = Long.parseLong(s);
                minText.setText(formatTimeMinutes(milis));
                secondsText.setText(formatTimeSeconds(milis));
            }
        };

    }










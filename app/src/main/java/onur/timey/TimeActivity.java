package onur.timey;



import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


// i know this activity sucks little bit but i promise i will refactor some methods with butterknife libary bla bla bla (being a beginner sucks)

/**
 * Created by onurh on 17.07.2016.
 */
public class TimeActivity extends AppCompatActivity {

      //  dont forget to consider re-writing in service or just provide to start  TimeActivity's CountDownTimer in service



    CountDownTimerWithPause countDownTimerWithPause;
    CountDownTimerWithPause countDownTimerWithPauseBreak;


    @BindView(R.id.secondText) TextView secondsText;

    @BindView(R.id.minuteText)TextView minText;

    @BindView(R.id.stopButton)Button stopButton;

    @BindView(R.id.continueButton)Button continueButton;

    @OnClick(R.id.stopButton) void stopButtonAction(){
        countDownTimerWithPause.pause();
        stopButton.setVisibility(View.INVISIBLE);
        continueButton.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.continueButton)void continueButton(){
        countDownTimerWithPause.resume();
        stopButton.setVisibility(View.VISIBLE);
        continueButton.setVisibility(View.INVISIBLE);
    }

    int secondsLeft=0;

    long seconds;


    public boolean startAgainAnswer;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.time_activity);

        ButterKnife.bind(this);


        startMainTimer();






    }


    private void startBreakTimeTimer(){
        countDownTimerWithPauseBreak=new CountDownTimerWithPause(300000,1) {
            @Override
            public void onTick(long millisUntilFinished) {
                minText.setText(formatTimeMinutes(millisUntilFinished));
                secondsText.setText(formatTimeSeconds(millisUntilFinished));

            }

            @Override
            public void onFinish() {

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

        countDownTimerWithPause = new CountDownTimerWithPause(1500000, 1) {
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









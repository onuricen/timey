package onur.timey;

import android.app.Service;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;

public abstract class CountDownTimerWithPause  {

    private long mMillisInFuture;
    private long mCountdownInterval;
    private long mStopTimeInFuture;
    private long mValuePause;
    private boolean timerIsFinish = true;

    public CountDownTimerWithPause(long millisInFuture, long countDownInterval) {
        mMillisInFuture = millisInFuture;
        mCountdownInterval = countDownInterval;
    }

    public void setMillisInFuture(long millisInFuture) {
        this.mMillisInFuture = millisInFuture;
    }

    public void setCountdownInterval(long countdownInterval) {
        this.mCountdownInterval = countdownInterval;
    }

    public synchronized final void pause() {
        if(!mHandler.hasMessages(MSG)) return;
        mValuePause = mStopTimeInFuture - SystemClock.elapsedRealtime();
        mHandler.removeMessages(MSG);
    }

    public synchronized final void resume() {
        if(mHandler.hasMessages(MSG)) return;
        if(timerIsFinish) return;
        mStopTimeInFuture = SystemClock.elapsedRealtime() + mValuePause;
        mHandler.sendMessage(mHandler.obtainMessage(MSG));
    }

    public synchronized final boolean isActive() {
        if (mHandler.hasMessages(MSG))
            return true;
        return false;
    }

    public final void cancel() {
        mHandler.removeMessages(MSG);
    }

    public synchronized final CountDownTimerWithPause start() {
        if (mMillisInFuture <= 0) {
            onFinish();
            timerIsFinish = true;
            return this;
        }
        mStopTimeInFuture = SystemClock.elapsedRealtime() + mMillisInFuture;
        mHandler.sendMessage(mHandler.obtainMessage(MSG));
        timerIsFinish = false;
        return this;
    }

    public abstract void onTick(long millisUntilFinished);
    public abstract void onFinish();
    private static final int MSG = 1;

    // handles counting down
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            synchronized (CountDownTimerWithPause.this) {
                final long millisLeft = mStopTimeInFuture - SystemClock.elapsedRealtime();

                if (millisLeft <= 0) {
                    timerIsFinish = true;
                    onFinish();
                } else if (millisLeft < mCountdownInterval) {
                    // no tick, just delay until done
                    sendMessageDelayed(obtainMessage(MSG), millisLeft);
                } else {
                    long lastTickStart = SystemClock.elapsedRealtime();
                    onTick(millisLeft);

                    // take into account user's onTick taking time to execute
                    long delay = lastTickStart + mCountdownInterval - SystemClock.elapsedRealtime();

                    // special case: user's onTick took more than interval to
                    // complete, skip to next interval
                    while (delay < 0) delay += mCountdownInterval;

                    sendMessageDelayed(obtainMessage(MSG), delay);
                }
            }
        }
    };
}
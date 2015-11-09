package youtubebackgroundplayer.youtubebackgroundplayer;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import java.util.Date;

/**
 * Created by Vuki on 9.11.2015..
 */
public class CallReceiver extends PhonecallReceiver {

    @Override
    protected void onIncomingCallStarted(Context ctx, String number, Date start) {
        Log.d("call","onIncomingCallStarted");
        MainActivity.webView.onPause();
        MainActivity.webView.pauseTimers();
    }

    @Override
    protected void onOutgoingCallStarted(Context ctx, String number, Date start) {
        Log.d("call","onOutgoingCallStarted");
        MainActivity.webView.onPause();
        MainActivity.webView.pauseTimers();
    }

    @Override
    protected void onIncomingCallEnded(Context ctx, String number, Date start, Date end) {
        Log.d("call", "onIncomingCallEnded");
       // timeHolder(1000);
        MainActivity.webView.resumeTimers();
        MainActivity.webView.onResume();
    }

    @Override
    protected void onOutgoingCallEnded(Context ctx, String number, Date start, Date end) {
        Log.d("call", "onOutgoingCallEnded");
       // timeHolder(1000);
        MainActivity.webView.resumeTimers();
        MainActivity.webView.onResume();

    }

    @Override
    protected void onMissedCall(Context ctx, String number, Date start) {
    }

    private void timeHolder(int duration){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

            }
        }, duration);
    }

}
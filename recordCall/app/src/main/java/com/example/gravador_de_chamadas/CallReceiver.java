package com.example.gravador_de_chamadas;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.util.Log;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CallReceiver extends BroadcastReceiver {

    private static String lastState = TelephonyManager.EXTRA_STATE_IDLE;
    private static long startTime = 0;

    @Override
    public void onReceive(Context context, Intent intent) {
        String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
        String phoneNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);

        if (phoneNumber == null) {
            phoneNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER); // Outgoing calls
        }

        if (state != null) {
            if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                Log.d("CallReceiver", "Incoming call from: " + phoneNumber);
            } else if (state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
                startTime = System.currentTimeMillis();
                if (lastState.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                    Log.d("CallReceiver", "Call answered: " + phoneNumber);
                } else {
                    Log.d("CallReceiver", "Outgoing call started: " + phoneNumber);
                }
            } else if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
                if (lastState.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
                    long duration = (System.currentTimeMillis() - startTime) / 1000;
                    Log.d("CallReceiver", "Call ended: " + phoneNumber + ", duration: " + duration + " seconds");

                    String type = lastState.equals(TelephonyManager.EXTRA_STATE_RINGING) ? "Received" : "Outgoing";
                    SimpleDateFormat sdfDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                    SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
                    String date = sdfDate.format(new Date());
                    String time = sdfTime.format(new Date());

                    // Enviar as informações da chamada para a MainActivity
                    Intent callIntent = new Intent(context, MainActivity.class);
                    callIntent.putExtra("number", phoneNumber);
                    callIntent.putExtra("type", type);
                    callIntent.putExtra("date", date);
                    callIntent.putExtra("time", time);
                    callIntent.putExtra("duration", String.valueOf(duration));
                    callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(callIntent);
                }
            }
            lastState = state;
        }
    }
}



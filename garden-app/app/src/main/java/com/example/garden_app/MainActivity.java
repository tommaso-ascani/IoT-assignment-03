package com.example.garden_app;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.garden_app.utility.BluetoothChannel;
import com.example.garden_app.utility.CommChannel;
import com.example.garden_app.utility.ConnectToEmulatedBluetoothServerTask;
import com.example.garden_app.utility.EmulatedBluetoothChannel;
import com.example.garden_app.utility.ConnectionTask;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private volatile BluetoothChannel btChannel;

    int led3_count = 0;
    int led4_count = 0;
    int irrigation_count = 0;
    boolean irrigation_state = false;
    boolean led1_state = false;
    boolean led2_state = false;
    boolean someStateChange = false;
    ArrayList<String> state= new ArrayList<String>(6);

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
    }

    private void initUI() {
        findViewById(R.id.manualControlButton).setOnClickListener(l -> {
            try {
                connectToBTServer();
            } finally {
                findViewById(R.id.buttonLed1).setEnabled(true);

                findViewById(R.id.buttonLed2).setEnabled(true);

                findViewById(R.id.buttonLed3_più).setEnabled(true);
                findViewById(R.id.buttonLed3_meno).setEnabled(true);

                findViewById(R.id.buttonLed4_più).setEnabled(true);
                findViewById(R.id.buttonLed4_meno).setEnabled(true);

                findViewById(R.id.irrigationButton).setEnabled(true);
                findViewById(R.id.irrigationButton_più).setEnabled(true);
                findViewById(R.id.irrigationButton_meno).setEnabled(true);

                l.setEnabled(false);
                sendMessage("S");
            }

        });

    }

    private void connectToBTServer(){

        new ConnectToEmulatedBluetoothServerTask(new ConnectionTask.EventListener() {

            @Override
            public void onConnectionActive(final BluetoothChannel channel) {

                btChannel = channel;
                btChannel.registerListener(new EmulatedBluetoothChannel.Listener() {

                    @Override
                    public void onMessageReceived(String receivedMessage) {
                        Log.i("onMessageReceived", receivedMessage);
                        state.add(receivedMessage);
                        if (state.lastIndexOf(receivedMessage) == 5) {
                            setCurrentState();
                        }
                    }

                    @Override
                    public void onMessageSent(String sentMessage) {
                        Log.i("onMessageSent", sentMessage);
                    }
                });
            }

            @Override
            public void onConnectionCanceled() {
            }
        }).execute();
    }

    private void setCurrentState() {
        Log.i("state", state.toString());
        String tempState = state.get(0);
        String g = tempState.replace("\r", "");
        if (g.equals("true")){
            findViewById(R.id.buttonLed1).setBackgroundColor(getResources().getColor(R.color.on));
            led1_state = true;
        } else {
            findViewById(R.id.buttonLed1).setBackgroundColor(getResources().getColor(R.color.off));
        }

        tempState = state.get(1);
        g = tempState.replace("\r", "");
        if (g.equals("true")){
            findViewById(R.id.buttonLed2).setBackgroundColor(getResources().getColor(R.color.on));
            led2_state = true;
        } else {
            findViewById(R.id.buttonLed2).setBackgroundColor(getResources().getColor(R.color.off));
        }

        led3_count = Integer.parseInt(String.valueOf(state.get(2).charAt(0)));
        TextView Led3_textView = findViewById(R.id.valueLed_3);
        Led3_textView.setText(String.valueOf(led3_count));

        led4_count = Integer.parseInt(String.valueOf(state.get(3).charAt(0)));
        TextView Led4_textView = findViewById(R.id.valueLed_4);
        Led4_textView.setText(String.valueOf(led4_count));

        tempState = state.get(4);
        g = tempState.replace("\r", "");
        if (g.equals("true")){
            findViewById(R.id.irrigationButton).setBackgroundColor(getResources().getColor(R.color.on));
        } else {
            findViewById(R.id.irrigationButton).setBackgroundColor(getResources().getColor(R.color.off));
        }

        irrigation_count = Integer.parseInt(String.valueOf(state.get(5).charAt(0)));
        TextView irrigation_textView = findViewById(R.id.irrigationValue);
        irrigation_textView.setText(String.valueOf(irrigation_count));
    }

    @Override
    protected void onStop() {
        super.onStop();
        btChannel.close();
    }

    // --------------------- Ring --------------------------------------------------------------

    public void clickOnRing(View view) {
        ImageButton ring = findViewById(R.id.ringButton);
        ring.setClickable(false);
        ring.setColorFilter(getResources().getColor(R.color.ring_yellow));
    }

    // --------------------- Led 1 -------------------------------------------------------------

    public void clickOnButtonLed1(View view) {
        if (!led1_state) {
            led1_state = true;
            findViewById(R.id.buttonLed1).setBackgroundColor(getResources().getColor(R.color.on));
        } else {
            led1_state = false;
            findViewById(R.id.buttonLed1).setBackgroundColor(getResources().getColor(R.color.off));
        }
        this.someStateChange = !this.someStateChange;
        String message = "1" + led1_state;
        sendMessage(message);
    }

    // --------------------- Led 2 -------------------------------------------------------------

    public void clickOnButtonLed2(View view) {
        if (!led2_state) {
            led2_state = true;
            findViewById(R.id.buttonLed2).setBackgroundColor(getResources().getColor(R.color.on));
        } else {
            led2_state = false;
            findViewById(R.id.buttonLed2).setBackgroundColor(getResources().getColor(R.color.off));
        }
        this.someStateChange = !this.someStateChange;
        String message = "2" + led2_state;
        sendMessage(message);

    }

    // --------------------- Led 3 -------------------------------------------------------------

    public void clickOnButtonLed3Plus(View view) {
        if (led3_count == 4) {
            return;
        }
        led3_count++;
        TextView Led3_textView = findViewById(R.id.valueLed_3);
        Led3_textView.setText(String.valueOf(led3_count));
        this.someStateChange = !this.someStateChange;
        String message = "3" + led3_count;
        sendMessage(message);
    }

    public void clickOnButtonLed3Minus(View view) {
        if (led3_count == 0){
            return;
        }
        led3_count--;
        TextView Led3_textView = findViewById(R.id.valueLed_3);
        Led3_textView.setText(String.valueOf(led3_count));
        this.someStateChange = !this.someStateChange;
        String message = "3" + led3_count;
        sendMessage(message);
    }

    // --------------------- Led 4 -------------------------------------------------------------

    public void clickOnButtonLed4Plus(View view) {
        if (led4_count == 4) {
            return;
        }
        led4_count++;
        TextView Led4_textView = findViewById(R.id.valueLed_4);
        Led4_textView.setText(String.valueOf(led4_count));
        this.someStateChange = !this.someStateChange;
        String message = "4" + led4_count;
        sendMessage(message);
    }

    public void clickOnButtonLed4Minus(View view) {
        if (led4_count == 0){
            return;
        }
        led4_count--;
        TextView Led4_textView = findViewById(R.id.valueLed_4);
        Led4_textView.setText(String.valueOf(led4_count));
        this.someStateChange = !this.someStateChange;
        String message = "4" + led4_count;
        sendMessage(message);
    }

    // --------------------- Irrigation System -------------------------------------------------

    public void clickOnButtonIrrigation(View view) {
        if (irrigation_state == false) {
            irrigation_state = true;
            findViewById(R.id.irrigationButton).setBackgroundColor(getResources().getColor(R.color.on));
        } else {
            irrigation_state = false;
            findViewById(R.id.irrigationButton).setBackgroundColor(getResources().getColor(R.color.off));
        }
        this.someStateChange = !this.someStateChange;
        String message = "I" + irrigation_state;
        sendMessage(message);
    }

    public void clickOnButtonIrrigationMinus(View view) {
        irrigation_count--;
        TextView irrigation_textView = findViewById(R.id.irrigationValue);
        if (irrigation_count < 0) {
            irrigation_count = 0;
        }
        irrigation_textView.setText(String.valueOf(irrigation_count));
        this.someStateChange = !this.someStateChange;
        String message = "I" + irrigation_count;
        sendMessage(message);
    }

    public void clickOnButtonIrrigationPlus(View view) {
        if (irrigation_count == 4) {
            return;
        }
        irrigation_count++;
        TextView irrigation_textView = findViewById(R.id.irrigationValue);
        irrigation_textView.setText(String.valueOf(irrigation_count));
        this.someStateChange = !this.someStateChange;
        String message = "I" + irrigation_count;
        sendMessage(message);
    }

    private void sendMessage(final String message) {
        Runnable r = () -> {
            while (btChannel == null){}
            btChannel.sendMessage(message);
        };
        Thread t= new Thread(r);
        t.start();
    }
}
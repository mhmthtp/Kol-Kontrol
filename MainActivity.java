package com.mhmthtp.kol_kontrol2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    Button button1,button2,button3,button4,button5,button6,button7,button8;
    TextView txt;
    MyThread myThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        button4 = findViewById(R.id.button4);
        button5 = findViewById(R.id.button5);
        button6 = findViewById(R.id.button6);
        button7 = findViewById(R.id.button7);
        button8 = findViewById(R.id.button8);
        txt    = findViewById(R.id.textView);
        txt.setVisibility(View.INVISIBLE);

        myThread = new MyThread();
        new Thread(myThread).start();

    }

    private class MyThread implements Runnable
    {

        Socket socket;
        DataOutputStream dos;
        private String msg;


        @Override
        public void run() {
            try {
                if (msg==null)
                {
                    return;
                }
                socket = new Socket("192.168.1.3",5678);
                dos = new DataOutputStream(socket.getOutputStream());
                dos.writeUTF(String.valueOf(msg));
                dos.close();
                dos.flush();
                socket.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void sendMsgParam(String msg)
        {
            this.msg = msg;
            run();
        }


    }

    public void mainBtnClick(View view) {
        switch (view.getId()) {
            case R.id.button1: txt.setVisibility(View.VISIBLE);
                txt.setText("MERKEZ MOTOR +10");
                myThread.sendMsgParam("00000001");
                break;
            case R.id.button2: txt.setVisibility(View.VISIBLE);
                txt.setText("MERKEZ MOTOR -10");
                myThread.sendMsgParam("00000002");
                break;
            case R.id.button3: txt.setVisibility(View.VISIBLE);
                txt.setText("SOL MOTOR +10");
                myThread.sendMsgParam("00000003");
                break;
            case R.id.button4: txt.setVisibility(View.VISIBLE);
                txt.setText("SOL MOTOR -10");
                myThread.sendMsgParam("00000004");
                break;
            case R.id.button5: txt.setVisibility(View.VISIBLE);
                txt.setText("SAG MOTOR +10");
                myThread.sendMsgParam("00000005");
                break;
            case R.id.button6: txt.setVisibility(View.VISIBLE);
                txt.setText("SAG MOTOR -10");
                myThread.sendMsgParam("00000006");
                break;
            case R.id.button7: txt.setVisibility(View.VISIBLE);
                txt.setText("PENCE MOTOR +10");
                myThread.sendMsgParam("00000007");
                break;
            case R.id.button8: txt.setVisibility(View.VISIBLE);
                txt.setText("PENCE MOTOR -10");
                myThread.sendMsgParam("00000010");
                break;
        }

    }

}
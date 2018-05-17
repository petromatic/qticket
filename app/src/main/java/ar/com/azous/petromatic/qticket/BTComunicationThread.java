package ar.com.azous.petromatic.qticket;

import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class BTComunicationThread extends Thread {
    private final InputStream inStream;
    private final OutputStream outStream;
    private Looper looper;

    public BTComunicationThread(BluetoothSocket socket) {
        InputStream tmpIn = null;
        OutputStream tmpOut = null;

        try {
            tmpIn = socket.getInputStream();
            tmpOut = socket.getOutputStream();
        } catch (IOException e) {

        }

        inStream = tmpIn;
        outStream = tmpOut;
    }

    public void run() {
        Looper.prepare();
        looper = Looper.myLooper();
    }

    public void write(String input) {
        try {
            outStream.write(input.getBytes());
        } catch (IOException e) {
            // TODO: Show error
        }
    }

    public void exit()  {
        if(looper!=null) {
            looper.quit();
        }
        try {
            outStream.close();
        } catch (IOException e) {}
    }
}

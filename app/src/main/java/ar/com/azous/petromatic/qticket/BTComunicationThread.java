package ar.com.azous.petromatic.qticket;

import android.bluetooth.BluetoothDevice;
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

import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;


public class BTComunicationThread extends Thread {
    // Identificador unico de servicio - SPP UUID
    private static final UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    // String para la direccion MAC
    private static String address;
    private BluetoothAdapter btAdapter = null;
    private BluetoothSocket btSocket = null;

    private final InputStream inStream;
    private final OutputStream outStream;
    private Looper looper;

    public BTComunicationThread(String address) {
        this.address = address;

        BluetoothDevice device = btAdapter.getRemoteDevice(address);

        InputStream inStream = null;
        OutputStream outStream = null;

        try{
            device.createRfcommSocketToServiceRecord(BTMODULEUUID);
        }catch (IOException e) {
            // TODO: Show error
            // Toast.makeText(getBaseContext(), "La creacción del Socket fallo", Toast.LENGTH_LONG).show();
        }
        try {
            btSocket.connect();
        } catch (IOException e) {
            try {
                btSocket.close();
            } catch (IOException e2) {
            }
        }


        try {
            inStream = btSocket.getInputStream();
            outStream = btSocket.getOutputStream();
        } catch (IOException e) {

        }

        this.inStream = inStream;
        this.outStream = outStream;
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

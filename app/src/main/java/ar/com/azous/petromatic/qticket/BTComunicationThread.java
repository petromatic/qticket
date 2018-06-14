package ar.com.azous.petromatic.qticket;

import android.app.Service;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.ParcelUuid;
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
import android.widget.Toast;


public class BTComunicationThread extends Thread {
    // Identificador unico de servicio - SPP UUID
    private static final UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    // String para la direccion MAC
    private BluetoothSocket btSocket = null;

    private final InputStream inStream;
    private final OutputStream outStream;
    private Looper looper;

    public BTComunicationThread(String address) {
        BluetoothAdapter mBtAdapter = BluetoothAdapter.getDefaultAdapter();

        BluetoothDevice device = mBtAdapter.getRemoteDevice(address);
        for (ParcelUuid uuid :device.getUuids()) {
            Log.d("BT", uuid.toString());
        }

        if(device.getBondState()==device.BOND_BONDED){
            try {
                btSocket = device.createRfcommSocketToServiceRecord(BTMODULEUUID);
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                Log.d("BT","socket not created");
                e1.printStackTrace();
            }
        }else{
            Log.d("BT","Device not paired");
            // Toast.makeText(getBaseContext(), "El dispositivo no está pareado", Toast.LENGTH_LONG).show();
        }

        InputStream inStream = null;
        OutputStream outStream = null;

        try {
            Log.d("BT","Connecting");
            btSocket.connect();
        } catch (IOException e) {
            Log.d("BT","Connecting error: " + e.getMessage());
            try {
                Log.d("BT","trying fallback...");

                btSocket =(BluetoothSocket) device.getClass().getMethod("createRfcommSocket", new Class[] {int.class}).invoke(device,1);
                btSocket.connect();

                Log.d("BT","Connected");
            }
            catch (Exception e2) {
                Log.d("BT", "Couldn't establish Bluetooth connection!");
                try {
                    btSocket.close();
                } catch (IOException e3) {
                }
            }
        }


        try {
            Log.d("BT","Getting sockets");
            inStream = btSocket.getInputStream();
            outStream = btSocket.getOutputStream();
        } catch (IOException e) {
            Log.d("BT","Getting sockets error: " + e.getMessage());
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
            Log.d("BT", "BT write error: "+e.getMessage());
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

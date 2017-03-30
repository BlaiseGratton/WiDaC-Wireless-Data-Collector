package widac.cis350.upenn.edu.widac;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

/**
 * Created by Matt on 3/24/2017.
 */

public class MyBluetoothService {
    private static final String TAG = "WiDaC DEBUG";
    private Handler mHandler; // handler that gets info from Bluetooth service
    Context context;

    // Defines several constants used when transmitting messages between the
    // service and the UI.
    private interface MessageConstants {
        public static final int MESSAGE_READ = 0;
        public static final int MESSAGE_WRITE = 1;
        public static final int MESSAGE_TOAST = 2;

        // ... (Add other message types here as needed.)
    }

    public MyBluetoothService(Context context) {
        this.context = context;
        Toast.makeText(context, "here 1", Toast.LENGTH_SHORT).show();
    }

    public void runService(UUID uuid) {
        AcceptThread acceptThread = new AcceptThread(uuid);
        //acceptThread.run();
    }

    private class AcceptThread extends Thread {
        private final BluetoothServerSocket mmServerSocket;
        private final int REQUEST_ENABLE_BT = 1;

        public AcceptThread(UUID uuid) {

            BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (mBluetoothAdapter == null) {
                // Device does not support Bluetooth
                Log.e(TAG, "Device does not support bluetooth");
            }

            if (!mBluetoothAdapter.isEnabled()) {
                /*
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                */
                Log.e(TAG, "Bluetooth is not enabled");
            }

            // Use a temporary object that is later assigned to mmServerSocket
            // because mmServerSocket is final.
            BluetoothServerSocket tmp = null;

            UUID DEFAULT_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

            try {
                // Use the UUID of the device that discovered // TODO Maybe need extra device object
                if (uuid != null)
                {
                    tmp = mBluetoothAdapter.listenUsingRfcommWithServiceRecord("WiDaC", uuid);
                    Toast.makeText(context, "worked!!!", Toast.LENGTH_SHORT).show();
                }
            } catch (NullPointerException e)
            {
                try {
                    tmp = mBluetoothAdapter.listenUsingRfcommWithServiceRecord("WiDaC", DEFAULT_UUID);
                } catch (IOException e1) {
                    Toast.makeText(context, "socket listen failed", Toast.LENGTH_SHORT).show();
                }
            }
            catch (IOException e) { Toast.makeText(context, "socket listen failed", Toast.LENGTH_SHORT).show(); }

            mmServerSocket = tmp;
            if (tmp == null) {
                Toast.makeText(context, "NULL!!", Toast.LENGTH_SHORT).show();
            }

            try {
                BluetoothSocket socket = mmServerSocket.accept();
            } catch (IOException e) {
                Toast.makeText(context, "XDDD", Toast.LENGTH_SHORT).show();
            }

            Toast.makeText(context, "here 2", Toast.LENGTH_SHORT).show();
        }

        public void run() {
            BluetoothSocket socket = null;
            // Keep listening until exception occurs or a socket is returned.
            Toast.makeText(context, "here 3", Toast.LENGTH_SHORT).show();
            for (int count = 0; count < 20; count++) {
                try {
                    Toast.makeText(context, "socket accepted", Toast.LENGTH_SHORT).show();
                    socket = mmServerSocket.accept();
                } catch (IOException e) {
                    Toast.makeText(context, "socket failed to accept", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Socket's accept() method failed", e);
                    break;
                }

                if (socket != null) {
                    // A connection was accepted. Perform work associated with
                    // the connection in a separate thread.
                    Toast.makeText(context, "try to make connectedthread", Toast.LENGTH_SHORT).show();
                    ConnectedThread connectedThread = new ConnectedThread(socket);
                    //connectedThread.run();
                    this.cancel();
                    break;
                }
            }
        }

        // Closes the connect socket and causes the thread to finish.
        public void cancel() {
            try {
                mmServerSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "Could not close the connect socket", e);
            }
        }
    }

    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;
        private byte[] mmBuffer; // mmBuffer store for the stream

        public ConnectedThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the input and output streams; using temp objects because
            // member streams are final.
            try {
                tmpIn = socket.getInputStream();
            } catch (IOException e) {
                Log.e(TAG, "Error occurred when creating input stream", e);
            }
            try {
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
                Log.e(TAG, "Error occurred when creating output stream", e);
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;

            Toast.makeText(context, "here 4", Toast.LENGTH_SHORT).show();
        }

        public void run() {
            mmBuffer = new byte[1024];
            int numBytes; // bytes returned from read()

            Toast.makeText(context, "here 5", Toast.LENGTH_SHORT).show();

            // Keep listening to the InputStream until an exception occurs.
            //while (true) {
                try {
                    // Read from the InputStream.
                    numBytes = mmInStream.read(mmBuffer);
                    //Log.d(TAG, "BYTES: " + Integer.toString(numBytes));
                    // Send the obtained bytes to the UI activity.
                    /*Message readMsg = mHandler.obtainMessage(
                            MessageConstants.MESSAGE_READ, numBytes, -1,
                            mmBuffer);*/
                    //readMsg.sendToTarget();
                    Toast.makeText(context, Integer.toString(numBytes), Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    Log.d(TAG, "Input stream was disconnected", e);
                    //break;
                }
            //}
        }

        // Call this from the main activity to send data to the remote device.
        public void write(byte[] bytes) {
            try {
                mmOutStream.write(bytes);

                // Share the sent message with the UI activity.
                /*Message writtenMsg = mHandler.obtainMessage(
                        MessageConstants.MESSAGE_WRITE, -1, -1, mmBuffer);*/
                //writtenMsg.sendToTarget();
            } catch (IOException e) {
                Log.e(TAG, "Error occurred when sending data", e);

                // Send a failure message back to the activity.
                /*Message writeErrorMsg =
                        mHandler.obtainMessage(MessageConstants.MESSAGE_TOAST);*/
                Bundle bundle = new Bundle();
                bundle.putString("toast",
                        "Couldn't send data to the other device");
                //writeErrorMsg.setData(bundle);
                //mHandler.sendMessage(writeErrorMsg);
            }
        }

        // Call this method from the main activity to shut down the connection.
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "Could not close the connect socket", e);
            }
        }
    }
}
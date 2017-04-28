package widac.cis350.upenn.edu.widac;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.UUID;

import widac.cis350.upenn.edu.widac.models.BluetoothHelper;

/**
 * Created by Matt on 3/29/2017.
 */

public class BluetoothService {
    private static final String TAG = "WiDaC DEBUG";
    private Handler mHandler; // handler that gets info from Bluetooth service
    Context context;
    public static ConnectedThread connectedThread = null;
    public static int currWeight;

    // Defines several constants used when transmitting messages between the
    // service and the UI.
    private interface MessageConstants {
        public static final int MESSAGE_READ = 0;
        public static final int MESSAGE_WRITE = 1;
        public static final int MESSAGE_TOAST = 2;

        // ... (Add other message types here as needed.)
    }

    public BluetoothService(Context context) {
        this.context = context;
    }

    public void runService(BluetoothDevice device) {
        // Check if have an existing connection, otherwise alert user
        if (connectedThread == null) {
            Toast.makeText(context, "Device not found: try to reconnect", Toast.LENGTH_SHORT).show();
        } else {
            // Call to pull information from bluetooth
            connectedThread.run();
        }
    }

    // Needs to be called whenever you want to restart a connection
    // Can't reconnect to device if device thinks it is still connected (will get busy error)
    public void closeThread () {
        connectedThread.cancel();
        connectedThread = null;
    }

    public void reconnect(BluetoothDevice device) {
        try {
            // Close any existing connection to reopen it
            if (connectedThread != null) {
                closeThread();
            }

            // Create new connection with device
            ConnectThread connectThread = new ConnectThread(device);
            connectThread.run();
        } catch (Exception e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;

        public ConnectThread(BluetoothDevice device) {
            // Use a temporary object that is later assigned to mmSocket
            // because mmSocket is final.
            BluetoothSocket tmp = null;
            mmDevice = device;

            UUID DEFAULT_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

            try {
                // Use the UUID of the device that discovered
                if (mmDevice != null)
                {
                    Log.i(TAG, "Device Name: " + mmDevice.getName());
                    Log.i(TAG, "Device UUID: " + mmDevice.getUuids()[0].getUuid());
                    //tmp = device.createInsecureRfcommSocketToServiceRecord(mmDevice.getUuids()[0].getUuid());
                    try {
                        // Opens a socket with the bluetooth device
                        // MAGIC CODE: http://stackoverflow.com/a/3397739
                        Method m = device.getClass().getMethod("createRfcommSocket", new Class[] {int.class});
                        tmp = (BluetoothSocket) m.invoke(device, 1);
                    } catch (Exception e) {
                        Toast.makeText(context, "Error: " + e.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
                else Log.d(TAG, "Device is null.");
            } catch (NullPointerException e)
            {
                Log.d(TAG, " UUID from device is null, Using Default UUID, Device name: " + device.getName());
                try {
                    // Attempt to create connection using default
                    tmp = device.createInsecureRfcommSocketToServiceRecord(DEFAULT_UUID);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }

            mmSocket = tmp;
        }

        public void run() {
            BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            // Cancel discovery because it otherwise slows down the connection.
            mBluetoothAdapter.cancelDiscovery();

            try {
                // Connect to the remote device through the socket. This call blocks
                // until it succeeds or throws an exception.
                mmSocket.connect();
            } catch (IOException connectException) {
                // Unable to connect; close the socket and return.
                Toast.makeText(context, "UNABLE TO CONNECT", Toast.LENGTH_SHORT).show();
                Toast.makeText(context, connectException.toString(), Toast.LENGTH_SHORT).show();
                try {
                    // Close the socket so device is not kept busy
                    mmSocket.close();
                } catch (IOException closeException) {
                    Toast.makeText(context, "COULD NOT CLOSE SOCKET", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Could not close the client socket", closeException);
                }
                return;
            }

            // The connection attempt succeeded. Perform work associated with
            // the connection in a separate thread.

            // All work pulling information done through the connected thread
            connectedThread = new ConnectedThread(mmSocket);
        }

        // Closes the client socket and causes the thread to finish.
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "Could not close the client socket", e);
            }
        }
    }

    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;
        private byte[] mmBuffer; // stores the data pulled from the scale
        public ConnectedThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the input and output streams; using temp objects because
            // member streams are final.
            try {
                tmpIn = socket.getInputStream();
            } catch (IOException e) {
                Toast.makeText(context, "Error occurred when creating input stream", Toast.LENGTH_SHORT).show();
            }
            try {
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
                Toast.makeText(context, "Error occurred when creating output stream", Toast.LENGTH_SHORT).show();
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            mmBuffer = new byte[1024];
            int numBytes; // bytes returned from read()

            // Keep listening to the InputStream until an exception occurs.
            try {
                // Read from the InputStream.
                // can speed up pull time by checking if data is available
                // TODO: Below is functionality for pulling and parsing data from a bluetooth scale
                if (mmInStream.available() >= 2) {
                    numBytes = mmInStream.read(mmBuffer);   // Tells how many bytes were pulled from the input stream

                    // TODO: Any changes to parsing of data from scale should be done in BluetoothHelper
                    currWeight = BluetoothHelper.parseBytesNutriscale(mmBuffer, numBytes);  // TODO: <--- Change this line to parse different streams
                    Toast.makeText(context, "WEIGHT: " + currWeight, Toast.LENGTH_SHORT).show();
                    } else {
                        // Alert that reading was not taken.
                        Toast.makeText(context, "No change detected. Please check if the scale is on and re-weigh the item.", Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    Log.d(TAG, "Input stream was disconnected", e);
                    Toast.makeText(context, "Disconnected from scale: please restart the scale", Toast.LENGTH_SHORT).show();
                    connectedThread.cancel();
                    connectedThread = null;
                } catch (NullPointerException e) {
                    Toast.makeText(context, "input stream null " + e.toString(), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(context, "jesus " + e.toString(), Toast.LENGTH_SHORT).show();
                }
        }

        // Call this method to shut down the connection.
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "Could not close the connect socket", e);
            }
        }
    }
}
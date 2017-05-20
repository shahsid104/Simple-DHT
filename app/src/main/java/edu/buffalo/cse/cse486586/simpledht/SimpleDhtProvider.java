package edu.buffalo.cse.cse486586.simpledht;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.app.Activity;
import android.app.Application;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.MergeCursor;
import android.net.Uri;
import android.os.*;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.util.Log;

public class SimpleDhtProvider extends ContentProvider {

    static final int SERVER_PORT = 10000;
    static final String TAG = SimpleDhtProvider.class.getSimpleName();
    static String myPort = "";
    int successor_port = 0;
    int predecessor_port = 0;
    String myHash, pre_hash;
    int number_of_active_ports = 0;
    static final String REMOTE_PORT0 = "11108";
    static final String REMOTE_PORT1 = "11112";
    static final String REMOTE_PORT2 = "11116";
    static final String REMOTE_PORT3 = "11120";
    static final String REMOTE_PORT4 = "11124";
    //<Boolean> activePort = Collections.synchronizedList(new ArrayList<Boolean>());
    ArrayList<Boolean> activePort = new ArrayList<Boolean>();
    String activePorts = null;
    HashMap<String,Integer> portMapping = new HashMap<>();
    HashMap<Integer,String> reversePortMapping = new HashMap<>();
    HashMap<Integer,Integer> reverseAVDMapping = new HashMap<>();
    final Uri mUri = buildUri("content", "edu.buffalo.cse.cse486586.simpledht.provider");
    private Uri buildUri(String scheme, String authority) {
        Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.authority(authority);
        uriBuilder.scheme(scheme);
        return uriBuilder.build();
    }

    private void fillHashMap()
    {
        portMapping.put(REMOTE_PORT0,2);
        reversePortMapping.put(2,REMOTE_PORT0);
        reverseAVDMapping.put(2,5554);
        activePort.add(0,false);

        portMapping.put(REMOTE_PORT1,1);
        reversePortMapping.put(1,REMOTE_PORT1);
        reverseAVDMapping.put(1,5556);
        activePort.add(1,false);

        portMapping.put(REMOTE_PORT2,3);
        reversePortMapping.put(3,REMOTE_PORT2);
        reverseAVDMapping.put(3,5558);
        activePort.add(2,false);

        portMapping.put(REMOTE_PORT3,4);
        reversePortMapping.put(4,REMOTE_PORT3);
        reverseAVDMapping.put(4,5560);
        activePort.add(3,false);

        portMapping.put(REMOTE_PORT4,0);
        reversePortMapping.put(0,REMOTE_PORT4);
        reverseAVDMapping.put(0,5562);
        activePort.add(4,false);

    }


    private void sendToAll()
    {

        Log.d("Sending a message","to all");
        Message msg = new Message();
        msg.query = 3;
        msg.activePorts = activePorts;
        try {
            if (activePort.get(portMapping.get(REMOTE_PORT1))) {
                Socket socket = new Socket(InetAddress.getByAddress(new byte[]{10, 0, 2, 2}),
                        Integer.parseInt(REMOTE_PORT1));
                ObjectOutputStream msgToSend = new ObjectOutputStream(socket.getOutputStream());
                msgToSend.writeObject(msg);
            }

            if(activePort.get(portMapping.get(REMOTE_PORT2))){
                Socket socket = new Socket(InetAddress.getByAddress(new byte[]{10, 0, 2, 2}),
                        Integer.parseInt(REMOTE_PORT2));
                ObjectOutputStream msgToSend = new ObjectOutputStream(socket.getOutputStream());
                msgToSend.writeObject(msg);
            }

            if(activePort.get(portMapping.get(REMOTE_PORT3))){
                Socket socket = new Socket(InetAddress.getByAddress(new byte[]{10, 0, 2, 2}),
                        Integer.parseInt(REMOTE_PORT3));
                ObjectOutputStream msgToSend = new ObjectOutputStream(socket.getOutputStream());
                msgToSend.writeObject(msg);

            }

            if(activePort.get(portMapping.get(REMOTE_PORT4))){
                Socket socket = new Socket(InetAddress.getByAddress(new byte[]{10, 0, 2, 2}),
                        Integer.parseInt(REMOTE_PORT4));
                ObjectOutputStream msgToSend = new ObjectOutputStream(socket.getOutputStream());
                msgToSend.writeObject(msg);
            }
        }catch (Exception e){

        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // TODO Auto-generated method stub
        if (selection.equals("@")) {
            PreferenceManager.getDefaultSharedPreferences(getContext()).edit().clear().commit();
            return 0;
        }

        if (selection.equals("*")) {

            PreferenceManager.getDefaultSharedPreferences(getContext()).edit().clear().commit();
            try{

                if(!myPort.equals(REMOTE_PORT0) && activePort.get(portMapping.get(REMOTE_PORT0)))
                {
                    Socket socket = new Socket(InetAddress.getByAddress(new byte[]{10, 0, 2, 2}),
                            Integer.parseInt(REMOTE_PORT0));
                    Message toSend = new Message("@", myPort, 2);
                    ObjectOutputStream msgToSend = new ObjectOutputStream(socket.getOutputStream());
                    Log.d("Sending message delete","port0");
                    msgToSend.writeObject(toSend);
                }

                if(!myPort.equals(REMOTE_PORT1) && activePort.get(portMapping.get(REMOTE_PORT1)))
                {
                    Socket socket = new Socket(InetAddress.getByAddress(new byte[]{10, 0, 2, 2}),
                            Integer.parseInt(REMOTE_PORT1));
                    Message toSend = new Message("@", myPort, 2);
                    ObjectOutputStream msgToSend = new ObjectOutputStream(socket.getOutputStream());
                    Log.d("Sending message delete","port1");
                    msgToSend.writeObject(toSend);
                }

                if(!myPort.equals(REMOTE_PORT2) && activePort.get(portMapping.get(REMOTE_PORT2)))
                {
                    Socket socket = new Socket(InetAddress.getByAddress(new byte[]{10, 0, 2, 2}),
                            Integer.parseInt(REMOTE_PORT2));
                    Message toSend = new Message("@", myPort, 2);
                    ObjectOutputStream msgToSend = new ObjectOutputStream(socket.getOutputStream());
                    Log.d("Sending message delete","port2");
                    msgToSend.writeObject(toSend);
                }

                if(!myPort.equals(REMOTE_PORT3) && activePort.get(portMapping.get(REMOTE_PORT3)))
                {
                    Socket socket = new Socket(InetAddress.getByAddress(new byte[]{10, 0, 2, 2}),
                            Integer.parseInt(REMOTE_PORT3));
                    Message toSend = new Message("@", myPort, 2);
                    ObjectOutputStream msgToSend = new ObjectOutputStream(socket.getOutputStream());
                    Log.d("Sending message delete","port3");
                    msgToSend.writeObject(toSend);
                }

                if(!myPort.equals(REMOTE_PORT4) && activePort.get(portMapping.get(REMOTE_PORT4)))
                {
                    Socket socket = new Socket(InetAddress.getByAddress(new byte[]{10, 0, 2, 2}),
                            Integer.parseInt(REMOTE_PORT4));
                    Message toSend = new Message("@", myPort, 2);
                    ObjectOutputStream msgToSend = new ObjectOutputStream(socket.getOutputStream());
                    Log.d("Sending message delete","port4");
                    msgToSend.writeObject(toSend);
                }

            }catch (Exception e){

            }
        }

        PreferenceManager.getDefaultSharedPreferences(getContext()).edit().remove(selection).commit();


        return 0;
    }

    @Override
    public String getType(Uri uri) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        try {

            Set<Map.Entry<String,Object>> set = values.valueSet();
            Iterator<Map.Entry<String,Object>> it = set.iterator();
            String value = it.next().getValue().toString();
            String keys = it.next().getValue().toString();
            Log.d("Keys",keys);
            Log.d("value",value);
            String keyHash = genHash(keys);
            Log.d("key Hash",keyHash);
            Log.d("compare value",String.valueOf(keyHash.compareTo(myHash)));
            if(number_of_active_ports > 1) {
                if (pre_hash.compareTo(myHash) < 0) {
                    if ((keyHash.compareTo(myHash) <= 0 && keyHash.compareTo(pre_hash) > 0) || number_of_active_ports == 1) {
                        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
                        SharedPreferences.Editor edit = sp.edit();
                        edit.putString(keys, value);
                        edit.commit();
                        Log.d("inserted in", myPort);
                    } else {
                        try {
                            Socket socket = new Socket(InetAddress.getByAddress(new byte[]{10, 0, 2, 2}),
                                    successor_port);
                            Log.d("Sending message to port", String.valueOf(successor_port));
                            Message toSend = new Message(keys, value, 0, myPort);
                            ObjectOutputStream msgToSend = new ObjectOutputStream(socket.getOutputStream());
                            msgToSend.writeObject(toSend);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    if ((keyHash.compareTo(myHash) <= 0 || keyHash.compareTo(pre_hash) > 0) || number_of_active_ports == 1) {
                        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
                        SharedPreferences.Editor edit = sp.edit();
                        edit.putString(keys, value);
                        edit.commit();
                        Log.d("inserted in", myPort);
                    } else {
                        try {
                            Socket socket = new Socket(InetAddress.getByAddress(new byte[]{10, 0, 2, 2}),
                                    successor_port);
                            Log.d("Sending message to port", String.valueOf(successor_port));
                            Message toSend = new Message(keys, value, 0, myPort);
                            ObjectOutputStream msgToSend = new ObjectOutputStream(socket.getOutputStream());
                            msgToSend.writeObject(toSend);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            else{
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
                SharedPreferences.Editor edit = sp.edit();
                edit.putString(keys, value);
                edit.commit();
                Log.d("inserted in", myPort);
            }

        }catch(NoSuchAlgorithmException e){

        }
        return uri;
    }

    @Override
    public boolean onCreate() {
        try {
            // TODO Auto-generated method stub
            fillHashMap();
            TelephonyManager tel = (TelephonyManager) getContext().getSystemService(Context.TELEPHONY_SERVICE);
            String portStr = tel.getLine1Number().substring(tel.getLine1Number().length() - 4);
            Log.d("Port String",portStr);
            myPort = String.valueOf((Integer.parseInt(portStr) * 2));
            Log.d("My Port",myPort);
            activePort.remove(portMapping.get(myPort));
            Log.d("Port Mapping",String.valueOf(portMapping.get(myPort)));
            activePort.add(portMapping.get(myPort), true);
            number_of_active_ports++;
            activePorts = myPort;
            myHash = genHash(portStr);
            Log.d("My hash",myHash);

            try {
                ServerSocket serverSocket = new ServerSocket(SERVER_PORT);
                new ServerTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, serverSocket);
            } catch (IOException e) {
                Log.e(TAG, "Can't create a ServerSocket");
            }

            /*if(myPort.equals(REMOTE_PORT0))
            {
                successor_port = 11116;
                predecessor_port = 5556;
            }

            else if(myPort.equals(REMOTE_PORT1))
            {
                successor_port = 11108;
                predecessor_port = 5562;
            }

            else if(myPort.equals(REMOTE_PORT2))
            {
                successor_port = 11120;
                predecessor_port = 5554;
            }

            else if(myPort.equals(REMOTE_PORT3))
            {
                successor_port = 11124;
                predecessor_port = 5558;
            }

            else if(myPort.equals(REMOTE_PORT4))
            {
                successor_port = 11112;
                predecessor_port = 5560;
            }

            pre_hash = genHash(String.valueOf(predecessor_port));*/
            if(!myPort.equals(REMOTE_PORT0)) {
                new ClientTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
            }


        }catch (Exception e)
        {

        }
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
            String sortOrder) {
        if(selection.equals("*"))
        {
            Map<String,String> allKeys = (Map<String,String>) PreferenceManager.getDefaultSharedPreferences(getContext()).getAll();
            MatrixCursor cursor = new MatrixCursor(new String[]{"key","value"});
            for(Map.Entry<String,String> entry:allKeys.entrySet()){
                Log.d("Shared Preference value",entry.getKey()+ ":" + entry.getValue());
                MatrixCursor.RowBuilder build;
                build =  cursor.newRow();
                build.add("key",entry.getKey());
                build.add("value",entry.getValue());
            }
            cursor.moveToFirst();
            Cursor mergedCursor = cursor;
            try {
                if (!REMOTE_PORT0.equals(myPort) && activePort.get(portMapping.get(REMOTE_PORT0))) {
                    Socket socket = new Socket(InetAddress.getByAddress(new byte[]{10, 0, 2, 2}),
                            Integer.parseInt(REMOTE_PORT0));
                    Message toSend = new Message(selection, myPort, 1);
                    ObjectOutputStream msgToSend = new ObjectOutputStream(socket.getOutputStream());
                    Log.d("Sending message cursors","port0");
                    msgToSend.writeObject(toSend);
                    ObjectInputStream msgToReceieve = new ObjectInputStream(socket.getInputStream());
                    HashMap<String,String> port0 = (HashMap<String,String>)msgToReceieve.readObject();
                    socket.close();
                    MatrixCursor port0Cursor = new MatrixCursor(new String[]{"key","value"});
                    for(Map.Entry<String,String> entry:port0.entrySet()){
                        Log.d("Shared Preference value",entry.getKey()+ ":" + entry.getValue());
                        MatrixCursor.RowBuilder build;
                        build =  port0Cursor.newRow();
                        build.add("key",entry.getKey());
                        build.add("value",entry.getValue());
                    }
                    port0Cursor.moveToFirst();
                    Log.d("Curosor recieved",String.valueOf(port0Cursor.getCount()));
                    mergedCursor = new MergeCursor(new Cursor[]{mergedCursor,port0Cursor});

                }

                if (!REMOTE_PORT1.equals(myPort) && activePort.get(portMapping.get(REMOTE_PORT1))) {
                    Socket socket = new Socket(InetAddress.getByAddress(new byte[]{10, 0, 2, 2}),
                            Integer.parseInt(REMOTE_PORT1));
                    Message toSend = new Message("*P", myPort, 1);
                    Log.d("Sending message cursors","port1");
                    ObjectOutputStream msgToSend = new ObjectOutputStream(socket.getOutputStream());
                    msgToSend.writeObject(toSend);
                    ObjectInputStream msgToReceieve = new ObjectInputStream(socket.getInputStream());
                    HashMap<String,String> port1 = (HashMap<String, String>) msgToReceieve.readObject();
                    socket.close();
                    MatrixCursor port1Cursor = new MatrixCursor(new String[]{"key","value"});
                    for(Map.Entry<String,String> entry:port1.entrySet()){
                        Log.d("Shared Preference value",entry.getKey()+ ":" + entry.getValue());
                        MatrixCursor.RowBuilder build;
                        build =  port1Cursor.newRow();
                        build.add("key",entry.getKey());
                        build.add("value",entry.getValue());
                    }
                    port1Cursor.moveToFirst();
                    Log.d("Curosor recieved",String.valueOf(port1Cursor.getCount()));
                    mergedCursor = new MergeCursor(new Cursor[]{mergedCursor,port1Cursor});
                }

                if (!REMOTE_PORT2.equals(myPort) && activePort.get(portMapping.get(REMOTE_PORT2))) {
                    Socket socket = new Socket(InetAddress.getByAddress(new byte[]{10, 0, 2, 2}),
                            Integer.parseInt(REMOTE_PORT2));
                    Message toSend = new Message("*P", myPort, 1);
                    Log.d("Sending message cursors","port2");
                    ObjectOutputStream msgToSend = new ObjectOutputStream(socket.getOutputStream());
                    msgToSend.writeObject(toSend);
                    ObjectInputStream msgToReceieve = new ObjectInputStream(socket.getInputStream());
                    HashMap<String,String> port2 = (HashMap<String, String>) msgToReceieve.readObject();
                    socket.close();
                    MatrixCursor port2Cursor = new MatrixCursor(new String[]{"key","value"});
                    for(Map.Entry<String,String> entry:port2.entrySet()){
                        Log.d("Shared Preference value",entry.getKey()+ ":" + entry.getValue());
                        MatrixCursor.RowBuilder build;
                        build =  port2Cursor.newRow();
                        build.add("key",entry.getKey());
                        build.add("value",entry.getValue());
                    }
                    port2Cursor.moveToFirst();
                    Log.d("Curosor recieved",String.valueOf(port2Cursor.getCount()));
                    mergedCursor = new MergeCursor(new Cursor[]{mergedCursor,port2Cursor});

                }

                if (!REMOTE_PORT3.equals(myPort) && activePort.get(portMapping.get(REMOTE_PORT3))) {
                    Socket socket = new Socket(InetAddress.getByAddress(new byte[]{10, 0, 2, 2}),
                            Integer.parseInt(REMOTE_PORT3));
                    Message toSend = new Message("*P", myPort, 1);
                    Log.d("Sending message cursors","port3");
                    ObjectOutputStream msgToSend = new ObjectOutputStream(socket.getOutputStream());
                    msgToSend.writeObject(toSend);
                    ObjectInputStream msgToReceieve = new ObjectInputStream(socket.getInputStream());
                    HashMap<String,String> port3 = (HashMap<String, String>) msgToReceieve.readObject();
                    socket.close();
                    MatrixCursor port3Cursor = new MatrixCursor(new String[]{"key","value"});
                    for(Map.Entry<String,String> entry:port3.entrySet()){
                        Log.d("Shared Preference value",entry.getKey()+ ":" + entry.getValue());
                        MatrixCursor.RowBuilder build;
                        build =  port3Cursor.newRow();
                        build.add("key",entry.getKey());
                        build.add("value",entry.getValue());
                    }
                    port3Cursor.moveToFirst();
                    Log.d("Curosor recieved",String.valueOf(port3Cursor.getCount()));
                    mergedCursor = new MergeCursor(new Cursor[]{mergedCursor,port3Cursor});

                }

                if (!REMOTE_PORT4.equals(myPort) && activePort.get(portMapping.get(REMOTE_PORT4))) {
                    Socket socket = new Socket(InetAddress.getByAddress(new byte[]{10, 0, 2, 2}),
                            Integer.parseInt(REMOTE_PORT4));
                    Message toSend = new Message("*P", myPort, 1);
                    Log.d("Sending message cursors","port4");
                    ObjectOutputStream msgToSend = new ObjectOutputStream(socket.getOutputStream());
                    msgToSend.writeObject(toSend);
                    ObjectInputStream msgToReceieve = new ObjectInputStream(socket.getInputStream());
                    HashMap<String,String> port4 = (HashMap<String, String>) msgToReceieve.readObject();
                    socket.close();
                    MatrixCursor port4Cursor = new MatrixCursor(new String[]{"key","value"});
                    for(Map.Entry<String,String> entry:port4.entrySet()){
                        Log.d("Shared Preference value",entry.getKey()+ ":" + entry.getValue());
                        MatrixCursor.RowBuilder build;
                        build =  port4Cursor.newRow();
                        build.add("key",entry.getKey());
                        build.add("value",entry.getValue());
                    }
                    port4Cursor.moveToFirst();
                    Log.d("Curosor recieved",String.valueOf(port4Cursor.getCount()));
                    mergedCursor = new MergeCursor(new Cursor[]{mergedCursor,port4Cursor});

                }
            }catch (Exception e){
                Log.d("Exception Received",e.toString());
                mergedCursor.moveToFirst();
                return mergedCursor;
            }

            mergedCursor.moveToFirst();
            Log.d("Merge cursor count",String .valueOf(mergedCursor.getCount()));
            return mergedCursor;
        }

        else if(selection.equals("*P")){
            Map<String,String> allKeys = (Map<String,String>) PreferenceManager.getDefaultSharedPreferences(getContext()).getAll();
            MatrixCursor cursor = new MatrixCursor(new String[]{"key","value"});
            if(allKeys.size() == 0) {
                Log.d("Sending null",String.valueOf(cursor.getCount()));
                return cursor;
            }
            for(Map.Entry<String,String> entry:allKeys.entrySet()){
                Log.d("Shared Preference value",entry.getKey()+ ":" + entry.getValue());
                MatrixCursor.RowBuilder build;
                build =  cursor.newRow();
                build.add("key",entry.getKey());
                build.add("value",entry.getValue());
            }

            cursor.moveToFirst();
            return cursor;
        }

        else if(selection.equals("@"))
        {
            Map<String,String> allKeys = (Map<String,String>) PreferenceManager.getDefaultSharedPreferences(getContext()).getAll();
            MatrixCursor cursor = new MatrixCursor(new String[]{"key","value"});
            for(Map.Entry<String,String> entry:allKeys.entrySet()){
                Log.d("Shared Preference value",entry.getKey()+ ":" + entry.getValue());
                MatrixCursor.RowBuilder build;
                build =  cursor.newRow();
                build.add("key",entry.getKey());
                build.add("value",entry.getValue());
            }
            Log.d("cursor size",String.valueOf(cursor.getCount()));
            cursor.moveToFirst();
            return cursor;

        }

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
        String value = sp.getString(selection,"NULL");
        if(value.equals("NULL"))
        {
            Log.d("Value is NULL","NULL");
            try {
                Log.d("Active Port 0",String.valueOf(activePort.get(portMapping.get(REMOTE_PORT0))));
                if (!REMOTE_PORT0.equals(myPort) && activePort.get(portMapping.get("11108"))) {
                    Socket socket = new Socket(InetAddress.getByAddress(new byte[]{10, 0, 2, 2}),
                            Integer.parseInt(REMOTE_PORT0));
                    Message toSend = new Message(selection, myPort, 4);
                    ObjectOutputStream msgToSend = new ObjectOutputStream(socket.getOutputStream());
                    Log.d("Sending message cursors","port0");
                    msgToSend.writeObject(toSend);
                    ObjectInputStream msgToReceieve = new ObjectInputStream(socket.getInputStream());
                    HashMap<String,String> port0 = (HashMap<String,String>)msgToReceieve.readObject();
                    socket.close();
                    if(!port0.get(selection).equals("NULL")) {
                        MatrixCursor port0Cursor = new MatrixCursor(new String[]{"key", "value"});
                        Log.d("Shared Preference value", selection + ":" + port0.get(selection));
                        MatrixCursor.RowBuilder build;
                        build = port0Cursor.newRow();
                        build.add("key",selection);
                        build.add("value", port0.get(selection));
                        port0Cursor.moveToFirst();
                        Log.d("Curosor recieved", String.valueOf(port0Cursor.getCount()));
                        return port0Cursor;
                    }

                }

                Log.d("Active Port 1",String.valueOf(activePort.get(portMapping.get("11112"))));
                if (!REMOTE_PORT1.equals(myPort) && activePort.get(1)) {
                    Socket socket = new Socket(InetAddress.getByAddress(new byte[]{10, 0, 2, 2}),
                            Integer.parseInt(REMOTE_PORT1));
                    Message toSend = new Message(selection, myPort, 4);
                    Log.d("Sending message cursors","port1");
                    ObjectOutputStream msgToSend = new ObjectOutputStream(socket.getOutputStream());
                    msgToSend.writeObject(toSend);
                    ObjectInputStream msgToReceieve = new ObjectInputStream(socket.getInputStream());
                    HashMap<String,String> port1 = (HashMap<String, String>) msgToReceieve.readObject();
                    socket.close();
                    if(!port1.get(selection).equals("NULL")) {
                        MatrixCursor port1Cursor = new MatrixCursor(new String[]{"key", "value"});
                        Log.d("Shared Preference value", selection + ":" + port1.get(selection));
                        MatrixCursor.RowBuilder build;
                        build = port1Cursor.newRow();
                        build.add("key",selection);
                        build.add("value", port1.get(selection));
                        port1Cursor.moveToFirst();
                        Log.d("Curosor recieved", String.valueOf(port1Cursor.getCount()));
                        return port1Cursor;
                    }
                }

                Log.d("Active Port 2",String.valueOf(activePort.get(portMapping.get("11116"))));
                if (!REMOTE_PORT2.equals(myPort) && activePort.get(2)) {
                    Socket socket = new Socket(InetAddress.getByAddress(new byte[]{10, 0, 2, 2}),
                            Integer.parseInt(REMOTE_PORT2));
                    Message toSend = new Message(selection, myPort, 4);
                    Log.d("Sending message cursors","port2");
                    ObjectOutputStream msgToSend = new ObjectOutputStream(socket.getOutputStream());
                    msgToSend.writeObject(toSend);
                    ObjectInputStream msgToReceieve = new ObjectInputStream(socket.getInputStream());
                    HashMap<String,String> port2 = (HashMap<String, String>) msgToReceieve.readObject();
                    socket.close();
                    if(!port2.get(selection).equals("NULL")) {
                        MatrixCursor port2Cursor = new MatrixCursor(new String[]{"key", "value"});
                        Log.d("Shared Preference value", selection + ":" + port2.get(selection));
                        MatrixCursor.RowBuilder build;
                        build = port2Cursor.newRow();
                        build.add("key",selection);
                        build.add("value", port2.get(selection));
                        port2Cursor.moveToFirst();
                        Log.d("Curosor recieved", String.valueOf(port2Cursor.getCount()));
                        return port2Cursor;
                    }

                }

                Log.d("Active Port 3",String.valueOf(activePort.get(portMapping.get("11120"))));
                if (!REMOTE_PORT3.equals(myPort) && activePort.get(3)) {
                    Socket socket = new Socket(InetAddress.getByAddress(new byte[]{10, 0, 2, 2}),
                            Integer.parseInt(REMOTE_PORT3));
                    Message toSend = new Message(selection, myPort, 4);
                    Log.d("Sending message cursors","port3");
                    ObjectOutputStream msgToSend = new ObjectOutputStream(socket.getOutputStream());
                    msgToSend.writeObject(toSend);
                    ObjectInputStream msgToReceieve = new ObjectInputStream(socket.getInputStream());
                    HashMap<String,String> port3 = (HashMap<String, String>) msgToReceieve.readObject();
                    socket.close();
                    if(!port3.get(selection).equals("NULL")) {
                        MatrixCursor port3Cursor = new MatrixCursor(new String[]{"key", "value"});
                        Log.d("Shared Preference value", selection + ":" + port3.get(selection));
                        MatrixCursor.RowBuilder build;
                        build = port3Cursor.newRow();
                        build.add("key",selection);
                        build.add("value", port3.get(selection));
                        port3Cursor.moveToFirst();
                        Log.d("Curosor recieved", String.valueOf(port3Cursor.getCount()));
                        return port3Cursor;
                    }

                }

                Log.d("Active Port 4",String.valueOf(activePort.get(portMapping.get("11124"))));
                if (!REMOTE_PORT4.equals(myPort) && activePort.get(4)) {
                    Socket socket = new Socket(InetAddress.getByAddress(new byte[]{10, 0, 2, 2}),
                            Integer.parseInt(REMOTE_PORT4));
                    Message toSend = new Message(selection, myPort, 4);
                    Log.d("Sending message cursors","port4");
                    ObjectOutputStream msgToSend = new ObjectOutputStream(socket.getOutputStream());
                    msgToSend.writeObject(toSend);
                    ObjectInputStream msgToReceieve = new ObjectInputStream(socket.getInputStream());
                    HashMap<String,String> port4 = (HashMap<String, String>) msgToReceieve.readObject();
                    socket.close();
                    if(!port4.get(selection).equals("NULL")) {
                        MatrixCursor port4Cursor = new MatrixCursor(new String[]{"key", "value"});
                        Log.d("Shared Preference value", selection + ":" + port4.get(selection));
                        MatrixCursor.RowBuilder build;
                        build = port4Cursor.newRow();
                        build.add("key",selection);
                        build.add("value", port4.get(selection));
                        port4Cursor.moveToFirst();
                        Log.d("Curosor recieved", String.valueOf(port4Cursor.getCount()));
                        return port4Cursor;
                    }

                }
            }catch (Exception e){

            }
        }
        Log.d("QueryKey",selection);
        Log.d("QueryValue",value);
        MatrixCursor cursor = new MatrixCursor(new String[]{"key","value"});
        MatrixCursor.RowBuilder build;
        build =  cursor.newRow();
        build.add("key",selection);
        build.add("value",value);
        cursor.moveToFirst();
        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        // TODO Auto-generated method stub
        return 0;
    }

    private String genHash(String input) throws NoSuchAlgorithmException {
        MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
        byte[] sha1Hash = sha1.digest(input.getBytes());
        Formatter formatter = new Formatter();
        for (byte b : sha1Hash) {
            formatter.format("%02x", b);
        }
        return formatter.toString();
    }

    private class ServerTask extends  AsyncTask<ServerSocket,String,Void>
    {

        @Override
        protected Void doInBackground(ServerSocket... sockets) {
            ServerSocket serverSocket = sockets[0];
            try {
                while (true) {

                    Socket clientSocket = serverSocket.accept();
                    ObjectInputStream msgReceived = new ObjectInputStream(clientSocket.getInputStream());
                    Message receivedObject = (Message) msgReceived.readObject();
                    Log.d("Message received",String.valueOf(receivedObject.query));
                    if(receivedObject.requestingPort.equals(myPort))
                        continue;

                    if(receivedObject.query == 2)
                        delete(mUri,receivedObject.selection,null);

                    else if(receivedObject.query == 1) {
                        Log.d("Query",receivedObject.selection);
                        Cursor q = query(mUri, null, receivedObject.selection, null, null);
                        HashMap<String,String> allValues = new HashMap<>();
                        if(q.getCount() != 0) {
                            do {
                                int keyIndex = q.getColumnIndex("key");
                                int valueIndex = q.getColumnIndex("value");
                                allValues.put(q.getString(keyIndex), q.getString(valueIndex));
                                Log.d(q.getString(keyIndex), q.getString(valueIndex));
                            } while (q.moveToNext());
                        }
                        ObjectOutputStream msgToSend = new ObjectOutputStream(clientSocket.getOutputStream());
                        msgToSend.writeObject(allValues);
                    }

                    else if(receivedObject.query == 3){
                        Log.d("Reached here","3");
                        if(!receivedObject.requestingPort.equals("-1")) {
                            Log.d("VALUE IN ACTIVE",String.valueOf(portMapping.get(receivedObject.requestingPort)));
                            activePorts += "&&" + receivedObject.requestingPort;
                            Log.d("Active Ports", activePorts);
                            number_of_active_ports++;
                            String[] parts = activePorts.split("&&");
                            Log.d("parts size",String.valueOf(parts.length));
                            for(int i = 0; i < parts.length; i++) {
                                //activePort.remove(portMapping.get(parts[i]));
                                activePort.set(portMapping.get(parts[i]), true);
                                number_of_active_ports++;
                                Log.d("Activating Port",parts[i]);
                            }
                            if (myPort.equals(REMOTE_PORT0)){
                                sendToAll();
                            }
                        }

                        else{
                            Log.d("Splitting string","split");
                            String[] parts = receivedObject.activePorts.split("&&");
                            Log.d("parts size",String.valueOf(parts.length));
                            for(int i = 0; i < parts.length;i++) {
                                //activePort.remove(portMapping.get(parts[i]));
                                activePort.set(portMapping.get(parts[i]), true);
                                number_of_active_ports++;
                                Log.d("Activating Port",parts[i]);
                            }
                        }

                        for(int i = portMapping.get(myPort) + 1; i != portMapping.get(myPort); i++)
                        {
                            if(i > 4)
                                i = 0;

                            if(activePort.get(i))
                            {
                                successor_port = Integer.parseInt(reversePortMapping.get(i));
                                break;
                            }
                        }

                        for(int i = portMapping.get(myPort) - 1; i != portMapping.get(myPort); i--)
                        {
                            if(i < 0)
                                i = 4;

                            if(activePort.get(i))
                            {
                                predecessor_port = reverseAVDMapping.get(i) ;
                                pre_hash = genHash(String.valueOf(predecessor_port));
                                break;
                            }
                        }

                        Log.d("Successor Port",String.valueOf(successor_port));
                        Log.d("Predecessor Port",String.valueOf(predecessor_port));
                    }

                    else if(receivedObject.query == 4)
                    {
                        Log.d("Query type","4");
                        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
                        String value = sp.getString(receivedObject.selection,"NULL");
                        Log.d("Sending this value",value);
                        HashMap<String,String> single_value_hash = new HashMap<>();
                        single_value_hash.put(receivedObject.selection,value);
                        ObjectOutputStream msgToSend = new ObjectOutputStream(clientSocket.getOutputStream());
                        msgToSend.writeObject(single_value_hash);
                    }

                    else {
                        Log.d("Key received", receivedObject.key);
                        Log.d("Value received", receivedObject.value);
                        ContentValues cv = new ContentValues();
                        cv.put("key", receivedObject.key);
                        cv.put("value", receivedObject.value);
                        insert(mUri, cv);
                    }

                }
            }catch (Exception e)
            {

            }
            return null;
        }
    }

    private class ClientTask extends AsyncTask<Void, Void, Void>
    {

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Log.d("Sending message", "msg");
                Message active = new Message();
                active.requestingPort = myPort;
                active.query = 3;
                Socket socket = new Socket(InetAddress.getByAddress(new byte[]{10, 0, 2, 2}),
                        Integer.parseInt(REMOTE_PORT0));
                ObjectOutputStream activeMessage = new ObjectOutputStream(socket.getOutputStream());
                activeMessage.writeObject(active);
                Log.d("Message sent", "Ho jaa send");
            }catch (Exception e){

            }
            return null;
        }
    }

}

package edu.buffalo.cse.cse486586.simpledht;

import java.io.Serializable;

/**
 * Created by shahsid104 on 3/21/2017.
 */

public class Message implements Serializable {
    String key;
    String value;
    String selection = null;
    int query;
    String requestingPort = "-1";
    String activePorts = null;

    Message(){}

    Message(String k, String v, int q, String port)
    {
        key = k;
        value = v;
        query = q;
        requestingPort = port;
    }

    Message(String selection, String port, int q)
    {
        key = null;
        value= null;
        query = q;
        requestingPort = port;
        this.selection = selection;
    }
}

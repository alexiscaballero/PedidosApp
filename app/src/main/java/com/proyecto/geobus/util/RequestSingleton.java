package com.proyecto.geobus.util;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by lkloster on 8/26/2016.
 */
public class RequestSingleton {
    private static RequestSingleton mInstance;
    private RequestQueue mRequestQueue;
    private static Context mCtx;

    private RequestSingleton(Context context) {
        mCtx = context;
        mRequestQueue = getRequestQueue();
    }

    public static synchronized RequestSingleton getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new RequestSingleton(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String token) {
        //Aqui agregar el token a la llamada menos las de login y register

        try {
            if(token != null) {
                req.getHeaders().put("AuthToken", token);
            }
        } catch (AuthFailureError authFailureError) {
            authFailureError.printStackTrace();
        }

        getRequestQueue().add(req);
    }
}
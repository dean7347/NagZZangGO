package com.example.android;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;


public class LoginRequest extends StringRequest {

    final static private String URL = "http://androidrf.dothome.co.kr/Login.php";
    private Map<String, String> map;

    public LoginRequest(String userId, String userPw, Response.Listener<String> listener){

        super(Method.POST, URL, listener,null);
        map = new HashMap<>();
        map.put("userId", userId);
        map.put("userPw", userPw);

    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}

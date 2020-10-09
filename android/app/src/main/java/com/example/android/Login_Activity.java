package com.example.android;

import android.app.Activity;
import androidx.appcompat.app.AppCompatActivity;
import java.lang.String;
import com.android.volley.toolbox.Volley;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.CheckBox;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import org.json.JSONException;
import org.json.JSONObject;

public class Login_Activity extends AppCompatActivity {

    private EditText login_Id, login_Pw;
    private Button login, register;
    private CheckBox autologin;
    SharedPreferences setting;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_);

        login_Id = findViewById(R.id.login_Id);
        login_Pw = findViewById(R.id.login_Pw);
        login = findViewById(R.id.login);
        register = findViewById(R.id.register);
        autologin = findViewById(R.id.autologin);
        ////////////////////////////////////////////////////////////////////////

        setting = getSharedPreferences("setting", 0);
        editor= setting.edit();

        if(setting.getBoolean("Auto_Login_enabled", false)){
            login_Id.setText(setting.getString("ID", ""));
            login_Pw.setText(setting.getString("PW", ""));
            autologin.setChecked(true);
        }

        autologin.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO Auto-generated method stub
                if(isChecked){
                    String ID = login_Id.getText().toString();
                    String PW = login_Pw.getText().toString();

                    editor.putString("ID", ID);
                    editor.putString("PW", PW);
                    editor.putBoolean("Auto_Login_enabled", true);
                    editor.commit();
                }else{
                    /**
                     * remove로 지우는것은 부분삭제
                     * clear로 지우는것은 전체 삭제 입니다
                     */
//					editor.remove("ID");
//					editor.remove("PW");
//					editor.remove("Auto_Login_enabled");
                    editor.clear();
                    editor.commit();
                }
            }
        });

        //////////////////////////////////////////////////////////////////////////
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login_Activity.this, Register_Activity.class);
                startActivity(intent);
            }
        });

        login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String userId = login_Id.getText().toString();
                String userPw = login_Pw.getText().toString();

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if (success) {
                                String userId = jsonObject.getString("userID");
                                String userPw = jsonObject.getString("userPassword");
                                Toast.makeText(getApplicationContext(), "로그인에 성공하였습니다", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(Login_Activity.this, MainActivity.class);
                                intent.putExtra("userID", userId);
                                intent.putExtra("userPassword", userPw);
                                startActivity(intent);
                            } else {
                                Toast.makeText(getApplicationContext(), "로그인에 실패하였습니다", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                LoginRequest loginRequest = new LoginRequest(userId, userPw, responseListener);
                RequestQueue queue = Volley.newRequestQueue(Login_Activity.this);
                queue.add(loginRequest);
            }
        });


    }



}
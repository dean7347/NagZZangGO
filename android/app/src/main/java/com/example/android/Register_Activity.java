package com.example.android;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import com.android.volley.toolbox.Volley;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import com.android.volley.RequestQueue;
import com.android.volley.Response;

import org.json.JSONException;
import org.json.JSONObject;

public class Register_Activity extends AppCompatActivity {

    private EditText register_Id, register_Pw, register_Pw2, register_Name;
    private Button register, check_Id;
    private boolean validate = false;
    private AlertDialog dialog;
    private int checking = 0;
    private ImageView imageCheck, imageCheck2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_);

        final ImageView imageCheck = (ImageView) findViewById(R.id.imageCheck);
        final ImageView imageCheck2 = (ImageView) findViewById(R.id.imageCheck2);
        imageCheck2.setVisibility(View.INVISIBLE);
        imageCheck.setVisibility(View.INVISIBLE);

        register_Id = findViewById(R.id.register_Id);
        register_Pw = findViewById(R.id.register_Pw);
        register_Pw2 = findViewById(R.id.register_Pw2);
        register_Name = findViewById(R.id.register_Name);


        final EditText register_Id = findViewById(R.id.register_Id);
        register_Id.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                imageCheck2.setVisibility(View.VISIBLE);
                imageCheck.setVisibility(View.INVISIBLE);
                checking = 0;
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });



        final Button check_Id = (Button)findViewById(R.id.check_Id);
        check_Id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userID = register_Id.getText().toString();
                if(validate){
                    return;//검증 완료
                }
                //ID 값을 입력하지 않았다면
                if(userID.equals("")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(Register_Activity.this);
                    dialog = builder.setMessage("아이디를 입력해주세요")
                            .setPositiveButton("OK", null)
                            .create();
                    dialog.show();
                    return;
                }


                //검증시작
                Response.Listener<String> responseListener = new Response.Listener<String>(){

                    @Override
                    public void onResponse(String response) {
                        try{
                       //     Toast.makeText(Register_Activity.this, response, Toast.LENGTH_LONG).show();

                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if(success){//사용할 수 있는 아이디라면
                                imageCheck.setVisibility(View.VISIBLE);
                                imageCheck2.setVisibility(View.INVISIBLE);
                                checking = 1;
                                AlertDialog.Builder builder = new AlertDialog.Builder(Register_Activity.this);
                          /*      dialog = builder.setMessage("아이디 사용가능합니다.")
                                        .setPositiveButton("OK", null)
                                        .create();*/
                                dialog.show();

                                //      register_Id.setEnabled(false);//아이디값을 바꿀 수 없도록 함
                                validate = true;//검증완료

                                //      register_Id.setBackgroundColor(getResources().getColor(R.color.colorGray));
                                //      check_Id.setBackgroundColor(getResources().getColor(R.color.colorGray));
                            }else{//사용할 수 없는 아이디라면
                                AlertDialog.Builder builder = new AlertDialog.Builder(Register_Activity.this);
                           /*     dialog = builder.setMessage("이미 사용중인 아이디입니다.")
                                        .setNegativeButton("OK", null)
                                        .create();*/
                                imageCheck2.setVisibility(View.VISIBLE);
                                imageCheck.setVisibility(View.INVISIBLE);
                                //     register_Id.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                                //     check_Id.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                                dialog.show();
                                checking = 0;
                            }

                        }
                        catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                };//Response.Listener 완료

                //Volley 라이브러리를 이용해서 실제 서버와 통신을 구현하는 부분
                Check_IdRequest validateRequest = new Check_IdRequest(userID, responseListener);
                RequestQueue queue = Volley.newRequestQueue(Register_Activity.this);
                queue.add(validateRequest);
            }
        });



        register = findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                //현재 입력되어 있는 값 가져오기
                String userId = register_Id.getText().toString();
                String userPw = register_Pw.getText().toString();
                String userPw2 = register_Pw2.getText().toString();
                String userName = register_Name.getText().toString();

                if(checking == 0){
                    Toast.makeText(Register_Activity.this, "아이디 중복확인을 해주세요", Toast.LENGTH_SHORT).show();
                    register_Id.requestFocus();
                    return;
                }


                if(userPw.length() == 0){
                    Toast.makeText(Register_Activity.this, "패스워드를 입력해주세요", Toast.LENGTH_SHORT).show();
                    register_Pw.requestFocus();
                    return;
                }

                if(userPw2.length() == 0){
                    Toast.makeText(Register_Activity.this, "패스워드를 입력해주세요", Toast.LENGTH_SHORT).show();
                    register_Pw2.requestFocus();
                    return;
                }

                if(!userPw.equals(userPw2)){
                    Toast.makeText(Register_Activity.this, "패스워드가 일치하지 않습니다", Toast.LENGTH_SHORT).show();
                    register_Pw.requestFocus();
                    return;
                }


                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if(success){
                                Toast.makeText(getApplicationContext(),"회원등록에 성공하였습니다", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(Register_Activity.this, Login_Activity.class);
                                startActivity(intent);
                            }
                            else{
                                Toast.makeText(getApplicationContext(), "회원등록에 실패하였습니다", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                };

                RegisterRequest registerRequest = new RegisterRequest(userId, userPw, userName, responseListener);
                RequestQueue queue = Volley.newRequestQueue(Register_Activity.this);
                queue.add(registerRequest);
            }


        });


    }
}
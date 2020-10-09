package com.example.android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private TextView main_Id, main_Pw;

    private BottomNavigationView bottomNavigationView;
    private FragmentManager fm;
    private FragmentTransaction ft;
    private menu1 menu1;
    private menu2 menu2;
    private menu3 menu3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        main_Id = findViewById(R.id.login_Id);
        main_Pw = findViewById(R.id.login_Pw);
/*

        Intent intent = getIntent();
        String userId = intent.getStringExtra("userId");
        String userPw = intent.getStringExtra("userPw");

        main_Id.setText(userId);
        main_Pw.setText(userPw);

*/
        bottomNavigationView = findViewById(R.id.bottomNavi);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch(menuItem.getItemId()){

                    case R.id.action_airplane1:
                        setFrag(0);
                        break;
                    case R.id.action_airplane2:
                        setFrag(1);
                        break;
                    case R.id.action_airplane3:
                        setFrag(2);
                        break;
                }
                return true;
            }
        });

        menu1 = new menu1();
        menu2 = new menu2();
        menu3 = new menu3();
        setFrag(0);


    }


    private long lastTimeBackPressed;

    @Override
    public void onBackPressed(){
        if(System.currentTimeMillis() - lastTimeBackPressed < 1500){
            finish();
            return;
        }
        Toast.makeText(this, "뒤로 버튼을 한 번 더 눌러 종료합니다", Toast.LENGTH_SHORT);
        lastTimeBackPressed = System.currentTimeMillis();

    }

    private void setFrag(int n){

        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();

        switch(n){
            case 0:
                ft.replace(R.id.main_frame, menu1);
                ft.commit();
                break;
            case 1:
                ft.replace(R.id.main_frame, menu2);
                ft.commit();
                break;
            case 2:
                ft.replace(R.id.main_frame, menu3);
                ft.commit();
                break;
        }
    }



}
package com.example.hatio.insidepositioning;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Login extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_login);



        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

//        new Thread(new Runnable() {
//            public void run() {
                String url;
                url = "jdbc:postgresql://factory.hatiolab.com:5432/mes";
                try {
                    Connection conn = DriverManager.getConnection(url, "postgres", "elidom");
                    Toast.makeText(getApplicationContext(), "Connection", Toast.LENGTH_LONG).show();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

//            }
//        }).start();

    }
}

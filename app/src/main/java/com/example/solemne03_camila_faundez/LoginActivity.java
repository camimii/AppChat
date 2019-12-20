package com.example.solemne03_camila_faundez;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.os.Handler;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPass;
    private Button ingresar, registrar;
    private boolean doubleBackToExitPressedOnce;
    private Handler mHandler;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mProgressBar = findViewById(R.id.progressBar);

        mHandler = new Handler(); //handler para cerrar app
        doubleBackToExitPressedOnce = false; //variable para cerrar app
        etEmail = findViewById(R.id.etEmail);
        etPass = findViewById(R.id.etPassword);
        ingresar = findViewById(R.id.btnLogin);
        registrar = findViewById(R.id.btnRegLog);
        mProgressBar.setVisibility(ProgressBar.INVISIBLE);

        ingresar.setOnClickListener(v -> procesoDeLogin(
                etEmail.getText().toString(),
                etPass.getText().toString()
        ));

        registrar.setOnClickListener(v -> trasladarReg());
    }

    private void procesoDeLogin(String email, String pass){
        if(email.isEmpty()){
            Toast.makeText(this, "Ingresa el email", Toast.LENGTH_SHORT).show();
            return;
        }
        if (pass.isEmpty()){
            Toast.makeText(this, "Ingresa la contraseña", Toast.LENGTH_SHORT).show();
            return;
        }

        //Autenticación firebase
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithEmailAndPassword(email, pass)
                .addOnSuccessListener(auth -> {
                    mProgressBar.setVisibility(ProgressBar.VISIBLE);
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                })
                .addOnFailureListener(auth -> {
                    Toast.makeText(this, "Error al ingresar", Toast.LENGTH_SHORT).show();
                    System.out.println("ERROR_AUTH_FIREBASE: "+auth.getMessage());
                });

    }

    private void trasladarReg(){
        Intent intent = new Intent(this,RegisterActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private final Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            doubleBackToExitPressedOnce = false;
        }
    };

    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        if (mHandler != null) { mHandler.removeCallbacks(mRunnable); }
    }


    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Presiona otra vez para salir", Toast.LENGTH_SHORT).show();

        mHandler.postDelayed(mRunnable, 2000);
    }
}


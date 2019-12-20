package com.example.solemne03_camila_faundez;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {
    private Button btnRegistro;
    private EditText etEmail;
    private EditText etPass;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etEmail = findViewById(R.id.etEmailReg);
        etPass = findViewById(R.id.etPassReg);

        btnRegistro = findViewById(R.id.btnRegistro);
        btnRegistro.setOnClickListener(v -> Registro(
                etEmail.getText().toString(),etPass.getText().toString()
        ));

        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();


    }

    private void Registro(String email, String pass) {
        //Registro firebase

        if (pass.length() >= 6){
            mAuth.createUserWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(RegisterActivity.this, "Exito al registrar.",
                                        Toast.LENGTH_SHORT).show();
                                Handler hd = new Handler();
                                hd.postDelayed(() -> {
                                    Intent intent = new Intent(RegisterActivity.this
                                            , LoginActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                    finish();
                                }, 2000); // Delay de 2 seg.

                            } else {
                                // If sign in fails, display a message to the user.
                                String msj = task.getException().toString();
                                Toast.makeText(RegisterActivity.this, "Error: " + msj,
                                        Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
        }
        else{Toast.makeText(RegisterActivity.this, "Error: Contraseña con menos de 6 caracteres.",
                Toast.LENGTH_SHORT).show();}
    }

    //devuelve a la activity login
    @Override
    public void onBackPressed() {
        Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
    }



}

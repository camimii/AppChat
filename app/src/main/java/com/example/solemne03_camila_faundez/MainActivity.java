package com.example.solemne03_camila_faundez;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.solemne03_camila_faundez.DAO.ControladorMensaje;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private Button salida,enviar;
    private EditText msj;
    private EditText msjs;
    private TextView saludo;
    private DatabaseReference mFirebaseDatabaseReference;
    private boolean doubleBackToExitPressedOnce;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mHandler = new Handler(); //handler para cerrar app
        doubleBackToExitPressedOnce = false; //variable para cerrar app
        msj = findViewById(R.id.etMensaje);
        msjs = findViewById(R.id.mtMensajes);
        saludo = findViewById(R.id.tvBienvenida);
        enviar = findViewById(R.id.btnEnviar);
        salida = findViewById(R.id.btnSalir);
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance()
                .getReference("solemne03-camila-faundez");

        //variable que obtiene el usuario logeado si es que lo hay
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String name = user.getEmail();

            saludar(name);

            salida.setOnClickListener(v -> salir());
            enviar.setOnClickListener(v -> enviarMensaje());

            obtenerMensajes();
        }
        //si no está logeado retorna a la activity login
        else{
            Handler hd = new Handler();
            hd.postDelayed(() -> {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }, 1000); // Delay de 1 seg.
        }


    }

    private void saludar(String nombre){
        saludo.append(" "+nombre);
    }

    private void salir(){
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();

    }
    //envía msje a db de firebase incluyendo el nombre del usuario que lo envió.
    private void enviarMensaje(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String name = user.getEmail();
        String mensaje = msj.getText().toString();

        mFirebaseDatabaseReference.child("mensaje_user")
                .push().setValue(name+": "+mensaje);
        msj.setText("");
    }

    private void obtenerMensajes(){

        mFirebaseDatabaseReference.child("mensaje_user").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Consulta si existen registros en la db de firebase, si no los hay notifica un toast
                if(dataSnapshot.getValue() == null){
                    Toast.makeText(MainActivity.this,"No hay mensajes previos.",Toast.LENGTH_SHORT).show();
                }
                //Habiendo registros de mensajes serán obtenidos.
                else{
                    Iterable<DataSnapshot> todoMensajes = dataSnapshot.getChildren();
                    ArrayList<String> mensajes = new ArrayList<>();

                    for(DataSnapshot todo : todoMensajes){
                        String mj = todo.getValue().toString();
                        mensajes.add(mj);
                    }

                    ControladorMensaje c = new ControladorMensaje(MainActivity.this);

                    c.eliminarTabla();

                    for(int x=0;x<mensajes.size();x++){

                        c.insertar(mensajes.get(x));}

                    if(!c.obtener().isEmpty()){
                        if(msjs.getText().length() == 0){
                            for(int x=0;x<c.obtener().size();x++){

                                msjs.append(c.obtener().get(x)+"\n");}
                        }
                    else{msjs.append(c.obtener().get(c.obtener().size()-1)+"\n");}
                    }
                    else{c.insertar(mensajes.get(mensajes.size()-1));
                        if(msjs.getText().length() == 0){
                            for(int x=0;x<c.obtener().size();x++){

                                msjs.append(c.obtener().get(x)+"\n");}
                        }

                    else{msjs.append(c.obtener().get(c.obtener().size()-1)+"\n");}
                    }
                }
/*
                //Obtener msjes firebase a multitext
                if(msjs.getText().length() == 0){
                for(int x=0;x<mensajes.size();x++){

                    msjs.append(mensajes.get(x)+"\n");}
            }

                else{msjs.append(mensajes.get(mensajes.size()-1)+"\n");}*/
        }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivity.this,"error db: "+databaseError.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
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
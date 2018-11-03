package com.example.aidas.pspejercicio1process;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    Button bt1;
    Button bt2;
    Runtime rt;
    Process proceso;
    TextView tv1, tv2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv1 = (TextView) findViewById(R.id.tv1);
        tv2 = findViewById(R.id.tvIPS);

        rt = Runtime.getRuntime();

        String cadena = "ping -c 3 8.8.8.8";

        try {
            proceso = rt.exec(cadena);
        } catch (IOException e) {
            e.printStackTrace();
        }

        InputStream is = proceso.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        final BufferedReader br = new BufferedReader(isr);

        Thread th = new Thread(new Runnable() {

            @Override
            public void run() {

                String linea;
                Boolean conexion = false;

                try {

                    while ((linea = br.readLine()) != null) {

                        if (linea.contains("0% packet loss")) {

                            conexion = true;
                        }
                    }

                    if (conexion) {

                        mostrarResultado("Hay conexión a internet");

                    }else{

                        mostrarResultado("Ni hay conexion");
                    }

                } catch (IOException ex) {

                    System.out.println(ex.toString());
                }

            }


        });

        //th.start();

        cadena ="ifconfig";

        try {
            proceso = rt.exec(cadena);
        } catch (IOException e) {
            e.printStackTrace();
        }

        is = proceso.getInputStream();
        isr = new InputStreamReader(is);
        final BufferedReader br1 = new BufferedReader(isr);

        Thread th2 = new Thread(new Runnable() {

            @Override
            public void run() {

                String linea;
                Boolean conexion = false;

                try {

                    while ((linea = br1.readLine()) != null) {

                        if(linea.contains("inet addr:")){

                            int pos = linea.indexOf("inet addr:");

                            Log.v("MITAG", "posicion: " + pos);

                            char[] ip = new char[15];

                            linea.getChars(pos + 10, pos + 25, ip, 0);

                            String a = "";

                            for(Character c: ip){

                                a += c.charValue();

                            }

                            if(a.contains(" ")){

                                Log.v("MITAG", "Espacio en: " + a.indexOf(" "));

                                a = a.substring(0, a.indexOf(" "));

                            }

                            mostrarIP(a);

                        }

                        mostrarResultado(linea);
                    }

                } catch (IOException ex) {

                    System.out.println(ex.toString());
                }

            }


        });

        th2.start();
    }


    public void mostrarResultado(String linea){

        tv1.append(linea + "\n\n\n");
    }

    public void mostrarIP(String linea){

        tv2.append(linea + "\n");
    }

}

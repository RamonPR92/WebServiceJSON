package com.perez.ramon.webservicejson;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

//AGREGAR LAS LIBRERIAS DE VOLLEY
//AGREGAR EL PERMISO DE INTERNET
// IMPLEMENTAR LAS INTERFACES DE LOS ESCUCHADORES
public class MainActivity extends AppCompatActivity  implements  Response.Listener<JSONObject>, Response.ErrorListener{

    //VARIABLES DE LAS VISTAS EN LA INTERFAZ
    private EditText usuario, password;
    private TextView salida;
    private Button btnValidar;

    //COLA DE PETICIONES QUE SERAN RESUELTAS
    private RequestQueue requestQueue;
    //REPRESENTA LA UNIDAD COMO PETICION
    private JsonObjectRequest jsonObjectRequest;
    //PARAMETROS EN EL CASO DE SER POST, PUT
    private JSONObject postParametros;

    //LA URL O DIRECCION DEL SERVICIO PUBLICADO
    private String requestService = "http://192.168.100.11:8080/wsrest/services/Servicios/validarUsuario";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //link graphical items to variables
        usuario = (EditText)findViewById(R.id.usuario);
        password = (EditText)findViewById(R.id.password);
        btnValidar = (Button)findViewById(R.id.validar);
        salida = (TextView)findViewById(R.id.salida);

        //INICIALIZA LA COLA DE PETICIONES
        requestQueue = Volley.newRequestQueue(this);

    //DISPARA EL PROCESO
        btnValidar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    //DEFINIR LOS PARAMETROS EN JSON A ENVIAR AL WS
                    postParametros = new JSONObject();
                    postParametros.put("usuario", usuario.getText().toString());
                    postParametros.put("password", password.getText().toString());

                    //CARGAMOS LA PETICION, INDICANDO QUE ES POST O GET
                    //DIRECCION DEL WS, LOS PARAMETROS, LOS DOS ESCUCHADORES
                    jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                            requestService, postParametros,
                            MainActivity.this,MainActivity.this);
                    //LA PETICION ES CARGADA A LA COLA DE PETICIONES
                    requestQueue.add(jsonObjectRequest);

                }catch (JSONException je){
                    Toast.makeText(MainActivity.this,
                            "Error en los datos ingresados",
                            Toast.LENGTH_LONG).show();
                    je.printStackTrace();
                }


            }
        });
    }

    //CUANDO EL EVENTO ERROR SEA LANZADO
    @Override
    public void onErrorResponse(VolleyError error) {

    }

    //CUANDO LA PETICION SEA CORRECTA
    @Override
    public void onResponse(JSONObject response){
        try{
            boolean valido = response.getBoolean("userValido");

            if(valido){
                salida.setTextColor(Color.GREEN);
                salida.setText("USUARIO VALIDO");
            }else{
                salida.setTextColor(Color.RED);
                salida.setText("USUARIO NOOO!!! VALIDO");
            }
        }catch (JSONException je){
            Toast.makeText(MainActivity.this,
                    "Error en los datos devueltos",
                    Toast.LENGTH_LONG).show();
            je.printStackTrace();
        }
    }


}

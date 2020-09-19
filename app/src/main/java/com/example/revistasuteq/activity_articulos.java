package com.example.revistasuteq;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.revistasuteq.adaptadores.AdaptadorArticulo;
import com.example.revistasuteq.adaptadores.AdaptadorRevista;
import com.example.revistasuteq.objetos.Articulo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class activity_articulos extends AppCompatActivity {
    @SuppressLint("TrulyRandom")
    public static void handleSSLHandshake() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                @Override
                public void checkClientTrusted(java.security.cert.X509Certificate[] x509Certificates, String s) throws CertificateException {
                }

                @Override
                public void checkServerTrusted(java.security.cert.X509Certificate[] x509Certificates, String s) throws CertificateException {
                }

                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }

            }};

            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String arg0, SSLSession arg1) {
                    return true;
                }
            });
        } catch (Exception ignored) {
        }
    }
    RecyclerView articuloRcl;
    ProgressDialog progress;
    ArrayList<Articulo> lstArticulo;
    TextView txt;
    RecyclerView rclArticulos;
  //  ArrayList<String> arrayListGroup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_articulos);
        rclArticulos=findViewById(R.id.rcvArticulos);

        articuloRcl=new RecyclerView(this);
        articuloRcl=(RecyclerView)findViewById(R.id.rcvArticulos);
        //añadir un Divider a los elementos de la lista->Diseño de la linea de separacion de los items
        //  revistaRcl.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        //Establecer el LayoutManager para definir la forma en la que se muestran los items en este caso en  forma de lista vertical
        articuloRcl.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        // revistaRcl.setLayoutManager(new GridLayoutManager(this,2));
        txt=(TextView)findViewById(R.id.textView4);

        lstArticulo=new ArrayList<Articulo>();
        handleSSLHandshake();
        progress=new ProgressDialog(this);
        progress.setMessage("Consultando...");
        progress.show();
        String url="https://revistas.uteq.edu.ec/ws/journals.php";
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest jobReq = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Articulo rev=null;
                        try {
                            for (int i=0;i<response.length();i++){
                                rev=new Articulo();
                                JSONObject obj = response.getJSONObject(i);
                                rev.setPublication_id(obj.getString("publication_id"));
                                rev.setAbstracts(obj.getString("abstract"));
                                rev.setTitle(obj.getString("title"));
                                rev.setDate_published(obj.getString("date_published"));
                                lstArticulo.add(rev);
                            }
                            progress.hide();
                            AdaptadorArticulo adapter=new AdaptadorArticulo(activity_articulos.this,lstArticulo);
                            adapter.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    int opcselec=articuloRcl.getChildAdapterPosition(view);
                                    String nombreselec= lstArticulo.get(opcselec).getPublication_id();
                                    Intent intent = new Intent(activity_articulos.this,Ediciones.class);

                                    Bundle b = new Bundle();
                                    b.putString("revistaID", nombreselec);
                                    intent.putExtras(b);

                                    startActivity(intent);

                                }
                            });
                            articuloRcl.setAdapter(adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            txt.setText(response.toString());
                            Toast.makeText(activity_articulos.this, "No se ha podido establecer conexión con el servidor" +
                                    " "+response, Toast.LENGTH_LONG).show();
                            progress.hide();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        VolleyLog.e("Error: ", volleyError.getMessage());
                        System.out.println();
                        txt.setText(volleyError.toString());
                        Toast.makeText(activity_articulos.this, "No se ha podido establecer conexión con el servidor" +
                                " "+volleyError.toString(), Toast.LENGTH_LONG).show();
                        Log.d("ERROR: ", volleyError.toString());
                        progress.hide();
                    }
                });
        queue.add(jobReq);


        handleSSLHandshake();


    }

}

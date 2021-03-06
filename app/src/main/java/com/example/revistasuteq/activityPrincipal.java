package com.example.revistasuteq;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
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
import com.example.revistasuteq.adaptadores.AdaptadorRevista;
import com.example.revistasuteq.objetos.Revista;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.util.ArrayList;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.X509Certificate;

public class activityPrincipal extends AppCompatActivity{
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
    RecyclerView revistaRcl;
    ProgressDialog progress;
    ArrayList<Revista> lstRevista;
    TextView txt;
    NavigationView navView;
    DrawerLayout drawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        revistaRcl=new RecyclerView(this);
        revistaRcl=(RecyclerView)findViewById(R.id.rclRevista);
        //añadir un Divider a los elementos de la lista->Diseño de la linea de separacion de los items
      //  revistaRcl.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        //Establecer el LayoutManager para definir la forma en la que se muestran los items en este caso en  forma de lista vertical
        revistaRcl.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
       // revistaRcl.setLayoutManager(new GridLayoutManager(this,2));
        txt=(TextView)findViewById(R.id.textView4);

        lstRevista=new ArrayList<Revista>();
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
                        Revista rev=null;
                        try {
                            for (int i=0;i<response.length();i++){
                                rev=new Revista();
                                JSONObject obj = response.getJSONObject(i);
                                rev.setNombre(obj.getString("name"));
                                rev.setPortada_url(obj.getString("portada"));
                                rev.setJournal_id(obj.getString("journal_id"));
                                lstRevista.add(rev);
                            }
                            progress.hide();
                            AdaptadorRevista adapter=new AdaptadorRevista(activityPrincipal.this,lstRevista);
                            adapter.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    int opcselec=revistaRcl.getChildAdapterPosition(view);
                                    String nombreselec= lstRevista.get(opcselec).getJournal_id();
                                    Intent intent = new Intent(activityPrincipal.this,Ediciones.class);

                                    Bundle b = new Bundle();
                                    b.putString("revistaID", nombreselec);
                                    intent.putExtras(b);

                                    startActivity(intent);

                                }
                            });
                            revistaRcl.setAdapter(adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            txt.setText(response.toString());
                            Toast.makeText(activityPrincipal.this, "No se ha podido establecer conexión con el servidor" +
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
                        Toast.makeText(activityPrincipal.this, "No se ha podido establecer conexión con el servidor" +
                                " "+volleyError.toString(), Toast.LENGTH_LONG).show();
                        Log.d("ERROR: ", volleyError.toString());
                        progress.hide();
                    }
                });
        queue.add(jobReq);


        handleSSLHandshake();


    }

}
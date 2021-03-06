package com.example.revistasuteq;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.example.revistasuteq.adaptadores.AdaptadorEdicion;
import com.example.revistasuteq.objetos.Edicion;

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

public class Ediciones extends AppCompatActivity {
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
    RecyclerView edicionRcl;
    ProgressDialog progress;
    ArrayList<Edicion> lstEdicion;
    String revistaID;
    TextView txt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ediciones);

        Bundle b = this.getIntent().getExtras();
        revistaID=b.getString("revistaID");
        Toast toast1=Toast.makeText(getApplicationContext(),
                "Seleccionaste: " + revistaID, Toast.LENGTH_SHORT);
        toast1.show();

        edicionRcl=new RecyclerView(this);
        edicionRcl=(RecyclerView)findViewById(R.id.rcvEdicion);
        edicionRcl.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));

        lstEdicion=new ArrayList<Edicion>();
        handleSSLHandshake();
        progress=new ProgressDialog(this);
        progress.setMessage("Consultando...");
        progress.show();
        String url="https://revistas.uteq.edu.ec/ws/issues.php?j_id="+revistaID+"";
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest jobReq = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Edicion rev=null;
                        try {
                            for (int i=0;i<response.length();i++){
                                rev=new Edicion();
                                JSONObject obj = response.getJSONObject(i);
                                rev.setId(obj.getString("issue_id"));
                                rev.setVol(obj.getString("volume"));
                                rev.setNumero(obj.getString("number"));
                                rev.setTitulo(obj.getString("title"));
                                rev.setDoi(obj.getString("doi"));
                                rev.setFechapublicac(obj.getString("date_published"));
                                rev.setImagen(obj.getString("cover"));
                                lstEdicion.add(rev);
                            }
                            progress.hide();
                            AdaptadorEdicion adapter=new AdaptadorEdicion(Ediciones.this,lstEdicion);
                            adapter.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    int opcselec=edicionRcl.getChildAdapterPosition(view);
                                    String nombreselec= lstEdicion.get(opcselec).getId();
                                    Intent intent = new Intent(Ediciones.this,activity_articulos.class);

                                    Bundle b = new Bundle();
                                    b.putString("edicionID", nombreselec);
                                    intent.putExtras(b);

                                    startActivity(intent);

                                }
                            });
                            edicionRcl.setAdapter(adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            txt.setText(response.toString());
                            Toast.makeText(Ediciones.this, "No se ha podido establecer conexión con el servidor" +
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
                        Toast.makeText(Ediciones.this, "No se ha podido establecer conexión con el servidor" +
                                " "+volleyError.toString(), Toast.LENGTH_LONG).show();
                        Log.d("ERROR: ", volleyError.toString());
                        progress.hide();
                    }
                });
        queue.add(jobReq);


        handleSSLHandshake();


    }
}
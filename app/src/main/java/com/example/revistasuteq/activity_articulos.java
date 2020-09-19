package com.example.revistasuteq;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

public class activity_articulos extends AppCompatActivity {

    RecyclerView rclArticulos;
  //  ArrayList<String> arrayListGroup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_articulos);
        rclArticulos=findViewById(R.id.rcvArticulos);

        Bundle b = this.getIntent().getExtras();
        String edicionID=b.getString("edicionID");
        Toast toast1=Toast.makeText(getApplicationContext(),
                "Revista: " + edicionID, Toast.LENGTH_SHORT);
        toast1.show();
    }
}
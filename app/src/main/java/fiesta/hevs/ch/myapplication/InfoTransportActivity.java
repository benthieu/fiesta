package fiesta.hevs.ch.myapplication;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import fiesta.hevs.ch.backend.transportApi.model.Transport;

public class InfoTransportActivity extends AppCompatActivity  {

    private Long transport_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_transport);

        Intent myIntent = getIntent(); // gets the previously created intent
        transport_id = myIntent.getLongExtra("transport_id", 0);

        final TextView driverName = (TextView) findViewById(R.id.driver_name);
        final TextView leave = (TextView) findViewById(R.id.driver_leave_value);
        final TextView numPlace = (TextView) findViewById(R.id.nb_place_value);
        final TextView destination = (TextView) findViewById(R.id.destination_value);

        /*driverName.setText(myIntent.getStringExtra("driver_name"));
        leave.setText(myIntent.getStringExtra("time"));
        numPlace.setText(myIntent.getIntExtra("num_place",-1));
        destination.setText(myIntent.getStringExtra("destination"));*/

        //Temporaire
        driverName.setText("Contacter ... nom");
        leave.setText("10:20");
        numPlace.setText("4");
        destination.setText("Sion");

    }


}

package fiesta.hevs.ch.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import fiesta.hevs.ch.backend.transportApi.model.Transport;

public class UpdateTransportActivity extends AppCompatActivity implements TransportTimeInterface {

    private Transport transport = new Transport();
    private int numPlace = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_transport);

        //Get data from transport
        //Intent en commentaire provisoirement
        //Intent myIntent = getIntent();
        TextView driverLeave = (TextView) findViewById(R.id.driver_leave_update);
       // driverLeave.setText(myIntent.getStringExtra("hour")+":"+myIntent.getStringExtra("minutes"));
        TextView nbPlaces = (TextView) findViewById(R.id.nb_place_update);
        //nbPlaces.setText(myIntent.getIntExtra("nb_place",-1));
        //numPlace = myIntent.getIntExtra("nb_place",-1);
        TextView destination = (TextView) findViewById(R.id.destination_update);
       // destination.setText(myIntent.getStringExtra("destination"));

        driverLeave.setText("01:45");
        nbPlaces.setText(""+numPlace);
        destination.setText("Martigny");
    }

    public void clicDecreaseButton(View v){

        if(numPlace>0){
            numPlace-=1;
            TextView nbPlaces = (TextView) findViewById(R.id.nb_place_update);
            nbPlaces.setText(""+numPlace);
        }
    }

    public void clicIncreaseButton(View v){

        numPlace+=1;
        TextView nbPlaces = (TextView) findViewById(R.id.nb_place_update);
        nbPlaces.setText(""+numPlace);

    }

    public void showTimePickerDialog(View v){
        DialogFragment time = new TimePickerFragment(this);
        time.show(getSupportFragmentManager(), "Time picker");

    }
    @Override
    public void setTime(int hour, int minutes) {
        transport.setHourStart(hour);
        transport.setMinuteStart(minutes);
        TextView driverLeave = (TextView) findViewById(R.id.driver_leave_update);
         driverLeave.setText(""+hour+":"+minutes);
    }
}

package fiesta.hevs.ch.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import fiesta.hevs.ch.backend.transportApi.model.Transport;

public class UpdateTransportActivity extends AppCompatActivity implements TransportTimeInterface {

    private Transport transport = new Transport();
    private int numPlace;
    private int hour;
    private int minutes;
    private ImageButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_transport);

        //Get data from transport

        Intent fromTransport = getIntent();
        TextView transport_top_name = (TextView) findViewById(R.id.transport_top_name);
        TextView transport_top_date = (TextView) findViewById(R.id.transport_top_date);
        transport_top_name.setText(fromTransport.getStringExtra("festival_name"));
        transport_top_date.setText(fromTransport.getStringExtra("festival_date"));
        backButton = (ImageButton) findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        transport.setId(fromTransport.getLongExtra("transport_id",-1));
        TextView driverLeave = (TextView) findViewById(R.id.driver_leave_update);
        TextView nbPlaces = (TextView) findViewById(R.id.nb_place_update);
        TextView destination = (TextView) findViewById(R.id.destination_update);
        transport.setDestination(fromTransport.getStringExtra("transport_destination"));
        transport.setDriver(fromTransport.getStringExtra("driver_name"));
        transport.setFestivalId(fromTransport.getLongExtra("festval_id", -1));

        hour = Integer.parseInt(fromTransport.getStringExtra("transport_hourStart"));
        minutes = Integer.parseInt(fromTransport.getStringExtra("transport_minuteStart"));
        driverLeave.setText(""+hour+":"+minutes);
        numPlace = Integer.parseInt(fromTransport.getStringExtra("transport_numFreeSpace"));
        nbPlaces.setText(""+numPlace);
        destination.setText(fromTransport.getStringExtra("transport_destination"));
        //------------------------------------------------------------------------------------------------------------------------------------
     /*   driverLeave.setText("01:45");
        nbPlaces.setText(""+numPlace);
        destination.setText("Martigny");*/
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

    public void clicSendButton(View v){
        //Get num free place
        transport.setNumFreeSpace(this.numPlace);
        transport.setHourStart(hour);
        transport.setMinuteStart(minutes);
        updateTransport();

        Intent endUpdate = new Intent(this, FestivalActivity.class);
        startActivity(endUpdate);
    }
    public void updateTransport(){
        TransportEndpointsAsyncTask teas = new TransportEndpointsAsyncTask(this.transport);
        teas.setUpdateTransport(true);
        teas.execute();
    }
    public void showTimePickerDialog(View v){
        DialogFragment time = new TimePickerFragment(this);
        time.show(getSupportFragmentManager(), "Time picker");

    }
    @Override
    public void setTime(int hour, int minutes) {
        this.hour=hour;
        TextView driverLeave = (TextView) findViewById(R.id.driver_leave_update);
         driverLeave.setText(""+hour+":"+minutes);
    }


}

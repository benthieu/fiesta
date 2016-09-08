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

/**
 * This class is used by the driver for update his transport (num place and hour)
 *(it is also used to communicate withe the future passengers)
 * @author Sylvain
 */
public class UpdateTransportActivity extends AppCompatActivity implements TransportTimeInterface {

    private Transport transport = new Transport();
    private int numPlace;
    private int hour;
    private int minutes;
    private ImageButton backButton;

    /**
     * In this method we got some informations on the transport to update, from TransportActivity
     * And these informations are used to be displayed on the layout.
     * We check also the state of this Activity
     * @see TransportActivity
     */
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

        // Check whether we're recreating a previously destroyed instance
        if (savedInstanceState != null) {
            // Restore value from saved state
            hour = savedInstanceState.getInt("hour");
            minutes = savedInstanceState.getInt("minutes");
            numPlace = savedInstanceState.getInt("numPlace");
            driverLeave.setText(""+hour+":"+minutes);
            nbPlaces.setText(""+numPlace);
        }
        //------------------------------------------------------------------------------------------------------------------------------------
     /*   driverLeave.setText("01:45");
        nbPlaces.setText(""+numPlace);
        destination.setText("Martigny");*/
    }

    /**
     * Method to save the new information(s) if the state of this Activity change
     */
    public void onSaveInstanceState(Bundle savedInstanceState) {

        // Save time and numPlace
        savedInstanceState.putInt("hour", hour);
        savedInstanceState.putInt("minutes", minutes);
        savedInstanceState.putInt("numPlace", numPlace);

        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

    /**
    * Method used by the "-" button to decrease the number of places.
    * There is a test to not be under 0 place.
     */
    public void clicDecreaseButton(View v){

        if(numPlace>0){
            numPlace-=1;
            TextView nbPlaces = (TextView) findViewById(R.id.nb_place_update);
            nbPlaces.setText(""+numPlace);
        }
    }

    /**
     Method used by the "+" button to increase the number of places.
     */
    public void clicIncreaseButton(View v){

        numPlace+=1;
        TextView nbPlaces = (TextView) findViewById(R.id.nb_place_update);
        nbPlaces.setText(""+numPlace);

    }

    /**
     * Method to get all informations who can be changed and put them
    *  in the new object transport. And return on the festivalActivity
    *
     */
    public void clicSendButton(View v){
        //Get num free place
        transport.setNumFreeSpace(this.numPlace);
        transport.setHourStart(hour);
        transport.setMinuteStart(minutes);
        updateTransport();

        Intent endUpdate = new Intent(this, FestivalActivity.class);
        startActivity(endUpdate);
    }
    /**
     Method to send the new transport updated to the database on the cloud
     @see TransportEndpointsAsyncTask
     */
    public void updateTransport(){
        TransportEndpointsAsyncTask teas = new TransportEndpointsAsyncTask(this.transport);
        teas.setUpdateTransport(true);
        teas.execute();
    }
    /**
     * Method to display the time picker
     * @param v
     */
    public void showTimePickerDialog(View v){
        DialogFragment time = new TimePickerFragment(this);
        time.show(getSupportFragmentManager(), "Time picker");

    }
    /**
     * Method to get the time selected by the user, on the time picker
     * We use this hour to set on the updated transport and display on the activity
     * @param hour hour selected
     * @param minutes minutes selected
     */
    @Override
    public void setTime(int hour, int minutes) {
        this.hour=hour;
        this.minutes = minutes;
        TextView driverLeave = (TextView) findViewById(R.id.driver_leave_update);
        driverLeave.setText(""+hour+":"+minutes);
    }


}

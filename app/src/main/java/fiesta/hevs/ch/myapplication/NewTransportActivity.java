package fiesta.hevs.ch.myapplication;

import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import fiesta.hevs.ch.backend.transportApi.model.Transport;



/**
 * Created by Pascal on 30.08.2016.
 */
public class NewTransportActivity extends AppCompatActivity implements View.OnClickListener, TransportTimeInterface {
    private DateFormat dateFormat;
    private DateFormat dateFormatInput;
    private ProgressDialog mDialog;
    private Transport addTransport;
    private long idFestival;
    private int hour = 0;
    private int minutes = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_transport);

        Intent fromTransport = getIntent();
        idFestival = fromTransport.getLongExtra("festival_id",0);
        Button create = (Button)findViewById(R.id.save_transport);
        create.setOnClickListener(this);
    }

   // @Override
   /* public void onResume() {
        super.onResume();
        mDialog.show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mDialog.dismiss();
    }*/


    @Override
    public void onClick(View v) {
        String driverName =  ((EditText) findViewById(R.id.editText_driver_name)).getText().toString();
        String destination =  ((EditText) findViewById(R.id.editText_destination)).getText().toString();
        String nbPlace =  ((EditText) findViewById(R.id.editText_places)).getText().toString();
        String email =  ((EditText) findViewById(R.id.editText_email)).getText().toString();
        //String time =  ((EditText) findViewById(R.id.editText_time)).getText().toString();

        addTransport = new Transport();

        addTransport.setDriver(driverName);
        addTransport.setDestination(destination);
        addTransport.setNumFreeSpace(3);
        addTransport.setDriverEmail(email);
        addTransport.setHourStart(this.hour);
        addTransport.setMinuteStart(this.minutes);
        addTransport.setFestivalId(this.idFestival);
        insertTransport(addTransport);
        Intent myIntent = new Intent(this, FestivalActivity.class);
        startActivity(myIntent);
    }

    public void insertTransport(Transport transport){
        new TransportEndpointsAsyncTask(transport).execute();
    }

    public void showTimePickerDialog(View v){
        DialogFragment time = new TimePickerFragment(this);
        time.show(getSupportFragmentManager(), "Time picker");
    }

    @Override
    public void setTime(int hour, int minutes) {
        this.hour=hour;
        this.minutes=minutes;
    }
}



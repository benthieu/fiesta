package fiesta.hevs.ch.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;

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
    private int hour = 25;
    private int minutes = 60;
    private ImageButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_transport);

        backButton = (ImageButton) findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent fromTransport = getIntent();
        idFestival = fromTransport.getLongExtra("festival_id",0);
        TextView transport_top_name = (TextView) findViewById(R.id.transport_top_name);
        TextView transport_top_date = (TextView) findViewById(R.id.transport_top_date);
        transport_top_name.setText(fromTransport.getStringExtra("festival_name"));
        transport_top_date.setText(fromTransport.getStringExtra("festival_date"));
        Button create = (Button)findViewById(R.id.save_transport);
        create.setOnClickListener(this);


        // Check whether we're recreating a previously destroyed instance
        if (savedInstanceState != null) {

            // Restore value from saved state
            hour = savedInstanceState.getInt("hour");
            minutes = savedInstanceState.getInt("minutes");
            boolean isChecked = savedInstanceState.getBoolean("isChecked");
            CheckBox checkbox = (CheckBox)findViewById(R.id.checkbox_conditions);

            if(isChecked == true){
                checkbox.setChecked(true);
            }else{
                checkbox.setChecked(false);
            }
            TextView textHour = (TextView)findViewById(R.id.editText_time);
            if(hour!=25)
            textHour.setText(""+hour+":"+minutes);
        }

    }

    public void onSaveInstanceState(Bundle savedInstanceState) {

        CheckBox checkbox = (CheckBox)findViewById(R.id.checkbox_conditions);
        boolean isChecked = false;

        if (checkbox.isChecked()){
            isChecked = true;
        }else{
            isChecked = false;
        }

        // Save the form fields checkbox and time
        savedInstanceState.putInt("hour", hour);
        savedInstanceState.putInt("minutes", minutes);

        savedInstanceState.putBoolean("isChecked", isChecked);

        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
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


    //Click to add a new transport
    @Override
    public void onClick(View v) {
        String driverName =  ((EditText) findViewById(R.id.editText_driver_name)).getText().toString();
        String destination =  ((EditText) findViewById(R.id.editText_destination)).getText().toString();
        String nbPlaceString =  ((EditText) findViewById(R.id.editText_places)).getText().toString();
        String email =  ((EditText) findViewById(R.id.editText_email)).getText().toString();
        //Boolean isSelected = findViewById(R.id.checkbox_conditions).isSelected();
        CheckBox checkbox = (CheckBox)findViewById(R.id.checkbox_conditions);
        Boolean isChecked = checkbox.isChecked();
        //String time =  ((EditText) findViewById(R.id.editText_time)).getText().toString();

        //Test empty text fields
        if(driverName.isEmpty() || destination.isEmpty() ||nbPlaceString.isEmpty()|| email.isEmpty() || hour==25 || minutes==60){
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(getApplicationContext(), "Tous les champs doivent être remplis.", duration);
            toast.show();
        //Test the checkbox is checked
        }else if(isChecked==false){
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(getApplicationContext(), "N'oublie pas, tu dois être sobre et responsable ;) ", duration);
            toast.show();
        //Add a new transport
        }else{
            int nbplace = Integer.parseInt(nbPlaceString);
            addTransport = new Transport();

            addTransport.setDriver(driverName);
            addTransport.setDestination(destination);
            addTransport.setNumFreeSpace(nbplace);
            addTransport.setDriverEmail(email);
            addTransport.setHourStart(this.hour);
            addTransport.setMinuteStart(this.minutes);
            addTransport.setFestivalId(this.idFestival);
            insertTransport(addTransport);
            Intent myIntent = new Intent(this, FestivalActivity.class);
            startActivity(myIntent);
        }
    }

    public void insertTransport(Transport transport){
        String android_id = Settings.Secure.getString(getBaseContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        transport.setDeviceId(android_id);
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

        TextView textHour = (TextView)findViewById(R.id.editText_time);
        textHour.setText(""+hour+":"+minutes);
    }
}



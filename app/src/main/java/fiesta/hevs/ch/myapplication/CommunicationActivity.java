package fiesta.hevs.ch.myapplication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.List;

import fiesta.hevs.ch.backend.communicationApi.model.Communication;

public class CommunicationActivity extends AppCompatActivity implements ConversationListEndPointsInterface {
    private ProgressDialog mDialog;
    private EditText communication_name;
    private EditText communication_message;
    private Button send_communication;
    private String device_id_from;
    private String device_id_to;
    private Long festival_transport_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_communication);

        Intent myIntent = getIntent(); // gets the previously created intent

        communication_name = (EditText) findViewById(R.id.communication_name);
        communication_message = (EditText) findViewById(R.id.communication_message);
        send_communication = (Button) findViewById(R.id.send_communication);

        device_id_from = myIntent.getStringExtra("device_id_from");
        device_id_to = myIntent.getStringExtra("device_id_to");
        Log.d("device_id_from", device_id_from);
        Log.d("device_id_to", device_id_to);
        festival_transport_id = myIntent.getLongExtra("transport_id", 0);

        send_communication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                mDialog = new ProgressDialog(CommunicationActivity.this);
                mDialog.setMessage("Sending...");
                mDialog.setCancelable(false);
                mDialog.show();

                Communication new_com = new Communication();
                new_com.setFestivalTransportId(festival_transport_id);
                new_com.setDeviceIdFrom(device_id_from);
                new_com.setDeviceIdTo(device_id_to);
                new_com.setName(communication_name.getText().toString());
                new_com.setMessage(communication_message.getText().toString());

                ConversationListEndPointsAsyncTask cLAT = new ConversationListEndPointsAsyncTask(CommunicationActivity.this);
                cLAT.setType(CommunicationActivity.INSERT);
                cLAT.setTempCommunication(new_com);
                cLAT.execute();
            }
        });




    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mDialog.dismiss();
    }

    @Override
    public void updateListView(List<Communication> communications) {

    }

    @Override
    public void insertedCommunication() {
        mDialog.hide();
        finish();
    }
}
package fiesta.hevs.ch.myapplication;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import fiesta.hevs.ch.backend.communicationApi.model.Communication;
import fiesta.hevs.ch.backend.transportApi.model.Transport;

/**
 * This class display some informations for a selected transport.
 * @see TransportActivity
 * @author Sylvain
 */
public class InfoTransportActivity extends AppCompatActivity implements ConversationListEndPointsInterface {
    private ProgressDialog mDialog;

    private Long transport_id;
    private ImageButton backButton;
    private String android_id;
    private String transport_device_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_transport);

        Intent myIntent = getIntent(); // gets the previously created intent
        transport_id = myIntent.getLongExtra("transport_id", 0);


        android_id = Settings.Secure.getString(getBaseContext().getContentResolver(), Settings.Secure.ANDROID_ID);

        final TextView driverName = (TextView) findViewById(R.id.driver_name);
        final TextView leave = (TextView) findViewById(R.id.driver_leave_value);
        final TextView numPlace = (TextView) findViewById(R.id.nb_place_value);
        final TextView destination = (TextView) findViewById(R.id.destination_value);
        final TextView festivalName = (TextView) findViewById(R.id.transport_top_name);
        final TextView festivalDate = (TextView) findViewById(R.id.transport_top_date);

        transport_device_id = myIntent.getStringExtra("transport_device_id");
        backButton = (ImageButton) findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        Button newComButton = (Button) findViewById(R.id.new_communication);
        newComButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent;

                myIntent = new Intent(InfoTransportActivity.this, CommunicationActivity.class);

                myIntent.putExtra("transport_id", transport_id);
                myIntent.putExtra("device_id_to", transport_device_id);
                myIntent.putExtra("device_id_from", android_id);
                startActivity(myIntent);
            }
        });


        mDialog = new ProgressDialog(InfoTransportActivity.this);
        mDialog.setMessage("Charger les conversations...");
        mDialog.setCancelable(false);
        mDialog.show();

        //Temporaire
        driverName.setText(myIntent.getStringExtra("driver_name"));
        leave.setText(myIntent.getStringExtra("transport_hourStart") + ":"  + (myIntent.getStringExtra("transport_minuteStart")));
        numPlace.setText(myIntent.getStringExtra("transport_numFreeSpace"));
        destination.setText(myIntent.getStringExtra("transport_destination"));
        festivalName.setText(myIntent.getStringExtra("festival_name"));
        festivalDate.setText(myIntent.getStringExtra("festival_date"));

        getConversations();
    }

    @Override
    public void onResume() {
        super.onResume();
        mDialog.show();
        getConversations();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mDialog.dismiss();
    }

    public void getConversations() {
        ConversationListEndPointsAsyncTask cLAT = new ConversationListEndPointsAsyncTask(this);
        cLAT.setType(this.LIST);
        cLAT.setDevice_id_1(android_id);
        cLAT.setFestival_transport_id(transport_id);
        cLAT.execute();
    }

    @Override
    public void updateListView(final List<Communication> communications) {
        mDialog.hide();

        if (communications == null) {
            return;
        }

        final ListView listView = (ListView) findViewById(R.id.conversation_list);
        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.list_elem_conversation, R.id.textview_message, communications) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text_message = (TextView) view.findViewById(R.id.textview_message);
                TextView text_name = (TextView) view.findViewById(R.id.textview_name);
                LinearLayout message_layout = (LinearLayout) view.findViewById(R.id.layout_message);
                LinearLayout message_parent = (LinearLayout) view.findViewById(R.id.layout_message_parent);
                if (communications.get(position).getDeviceIdFrom().equals(android_id)) {
                    message_parent.setGravity(Gravity.RIGHT);
                    message_layout.setGravity(Gravity.RIGHT);
                }

                text_message.setText(communications.get(position).getMessage());
                text_name.setText(communications.get(position).getName());

                return view;
            }
        };
        listView.setAdapter(adapter);
        final Context context = this;
    }

    @Override
    public void insertedCommunication() {

    }
}

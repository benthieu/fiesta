package fiesta.hevs.ch.myapplication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import fiesta.hevs.ch.backend.communicationApi.model.Communication;
import fiesta.hevs.ch.backend.transportApi.model.Transport;

/**
 * This class is used by the driver for update his transport (num place and hour)
 *(it is also used to communicate withe the future passengers)
 * @author Sylvain
 */
public class UpdateTransportActivity extends AppCompatActivity implements TransportTimeInterface, ConversationListEndPointsInterface{
    private ProgressDialog mDialog;
    private Transport transport = new Transport();
    private int numPlace;
    private int hour;
    private int minutes;
    private ImageButton backButton;
    private String android_id;
    private DateFormat dateFormat;
    private DateFormat dateFormatInput;
    private final List<comObj> comList = new ArrayList<comObj>();

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
        android_id = Settings.Secure.getString(getBaseContext().getContentResolver(), Settings.Secure.ANDROID_ID);

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


        dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        dateFormatInput = new SimpleDateFormat("yyyy-MM-dd");

        mDialog = new ProgressDialog(UpdateTransportActivity.this);
        mDialog.setMessage("Charger les conversations...");
        mDialog.setCancelable(false);
        mDialog.show();

        getConversations();
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
        cLAT.setFestival_transport_id(transport.getId());
        cLAT.execute();
    }


    public void updateListView(final List<Communication> communications) {
        mDialog.hide();
        if (communications == null || communications.size() == 0) {
            return ;
        }
        comList.clear();
        for (Communication c : communications) {
            String otherdeviceid = c.getDeviceIdFrom();
            if (c.getDeviceIdFrom().equals(android_id)) {
                otherdeviceid = c.getDeviceIdTo();
            }
            boolean is_in_list = false;
            for (comObj o : comList) {
                if (o.other_device_id.equals(otherdeviceid)) {
                    is_in_list = true;
                    Date formatDate = null;
                    try {
                        formatDate = dateFormatInput.parse(c.getTimeSend().toString());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if (formatDate != null) {
                        if (o.last_date.before(formatDate)) {
                            o.last_date = formatDate;
                        }
                    }
                    o.msg_count++;
                }
            }
            if (!is_in_list) {
                comObj addList = new comObj();
                Date formatDate = null;
                try {
                    formatDate = dateFormatInput.parse(c.getTimeSend().toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                addList.last_date = formatDate;
                addList.name = c.getName();
                addList.other_device_id = otherdeviceid;
                addList.msg_count = 1;
                comList.add(addList);
            }
        }

        final ListView listView = (ListView) findViewById(R.id.listView);
        setListViewHeightBasedOnChildren(listView);
        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.list_elem_communication, R.id.comm_name, comList) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView name = (TextView) view.findViewById(R.id.comm_name);
                TextView msg_count = (TextView) view.findViewById(R.id.comm_message_count);
                TextView date = (TextView) view.findViewById(R.id.comm_date);

                name.setText(comList.get(position).name);
                msg_count.setText(comList.get(position).msg_count+" Messages");
                date.setText(dateFormat.format(comList.get(position).last_date));

                return view;
            }
        };

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // show order edit/modify of this specific order
                Intent myIntent = new Intent(UpdateTransportActivity.this, CommunicationListActivity.class);
                //myIntent.putExtra("transport_id", communications.get(position).getId());
                myIntent.putExtra("other_device_id", comList.get(position).other_device_id);
                myIntent.putExtra("transport_id", transport.getId());

                startActivity(myIntent);
            }
        });

        final Context context = this;
    }

    @Override
    public void insertedCommunication() {

    }

    /**** Method for Setting the Height of the ListView dynamically.
     **** Hack to fix the issue of not showing all the items of the ListView
     **** when placed inside a ScrollView  ****/
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, RelativeLayout.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    private class comObj {
        public String other_device_id;
        public Date last_date;
        public String name;
        public int msg_count;
    }
}

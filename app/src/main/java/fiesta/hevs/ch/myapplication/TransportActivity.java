package fiesta.hevs.ch.myapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import java.util.Date;
import java.util.List;

import fiesta.hevs.ch.backend.transportApi.model.Transport;

/**
 * Class to display a list of transpots for one festival
 * Possibility to create a transport or see
 * some alternatives transport
 */
public class TransportActivity extends AppCompatActivity implements TransportEndpointsInterface {
    private ProgressDialog mDialog;
    private Long festival_id;
    private ImageButton backButton;
    private TextView transport_top_name;
    private TextView transport_top_date;
    private TextView transport_intro;
    private String festival_name;
    private String festival_date;
    private DateFormat dateFormat;
    private DateFormat dateFormatInput;

    //Listener to create a new transport
    private View.OnClickListener clickCreateTransport = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intentForTransport = new Intent(TransportActivity.this, NewTransportActivity.class);
            intentForTransport.putExtra("festival_id", festival_id);
            intentForTransport.putExtra("festival_name", festival_name);
            intentForTransport.putExtra("festival_date", festival_date);
            startActivity(intentForTransport);
        }
    };

    //Listener to view other transports
    private View.OnClickListener clickOtherTransports = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intentForTransport = new Intent(TransportActivity.this, TransportAltActivity.class);
            intentForTransport.putExtra("festival_id", festival_id);
            intentForTransport.putExtra("festival_name", festival_name);
            intentForTransport.putExtra("festival_date", festival_date);
            startActivity(intentForTransport);
        }
    };

    /**
     * Get info (id) of the festival
     * Create the layout
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transport);

        dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        dateFormatInput = new SimpleDateFormat("yyyy-MM-dd");

        Intent myIntent = getIntent(); // gets the previously created intent
        festival_id = myIntent.getLongExtra("festival_id", 0);
        festival_name = myIntent.getStringExtra("festival_name");
        festival_date = myIntent.getStringExtra("festival_date");
        this.setTitle(festival_name);

        transport_top_name = (TextView) findViewById(R.id.transport_top_name);
        transport_top_date = (TextView) findViewById(R.id.transport_top_date);
        transport_top_name.setText(festival_name);
        transport_top_date.setText(festival_date);

        backButton = (ImageButton) findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mDialog = new ProgressDialog(TransportActivity.this);
        mDialog.setMessage("Charger les trajets...");
        mDialog.setCancelable(false);
        mDialog.show();

        Button createTransport = (Button)findViewById(R.id.createTransport);
        createTransport.setOnClickListener(clickCreateTransport);

        Button otherTransports = (Button)findViewById(R.id.otherTransports);
        otherTransports.setOnClickListener(clickOtherTransports);

        getTransports();

    }

    @Override
    public void onResume() {
        super.onResume();
        mDialog.show();
        getTransports();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mDialog.dismiss();
    }

    /**
     * Method to send the query on to database to get a list of transport
     * @see TransportAltEndPointsAsyncTask
     */
    public void getTransports() {
        new TransportEndpointsAsyncTask(festival_id, this).execute();
    }

    /**
     * Method who get the list of transport
     * @param transports
     * @see TransportEndpointsInterface
     * @see TransportEndpointsAsyncTask
     */
    @Override
    public void updateListView(final List<Transport> transports) {
        mDialog.hide();

        final ListView listView = (ListView) findViewById(R.id.listView);
        setListViewHeightBasedOnChildren(listView);

        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.list_elem_transport, R.id.textview_1, transports) {
            @Override

            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text1 = (TextView) view.findViewById(R.id.textview_1);
                TextView text2 = (TextView) view.findViewById(R.id.textview_2);
                TextView text3 = (TextView) view.findViewById(R.id.textview_3);
                TextView text4 = (TextView) view.findViewById(R.id.textview_4);

                text1.setText(transports.get(position).getDriver());
                text2.setText("Destination: "+transports.get(position).getDestination());
                text3.setText(transports.get(position).getNumFreeSpace().toString()+" places");
                text4.setText("Départ ~"+transports.get(position).getHourStart().toString()+":"+transports.get(position).getMinuteStart().toString());
                return view;
            }
        };

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // show order edit/modify of this specific order
               // Intent myIntent = new Intent(TransportActivity.this, InfoTransportActivity.class);

                Intent myIntent;

                String android_id = Settings.Secure.getString(getBaseContext().getContentResolver(), Settings.Secure.ANDROID_ID);
                Log.d("device", android_id);
                if (!android_id.equals(transports.get(position).getDeviceId())) {
                    myIntent = new Intent(TransportActivity.this, InfoTransportActivity.class);
                }
                else {
                    myIntent = new Intent(TransportActivity.this, UpdateTransportActivity.class);
                }
                myIntent.putExtra("transport_id", transports.get(position).getId());
                myIntent.putExtra("driver_name", transports.get(position).getDriver());
                myIntent.putExtra("transport_hourStart", Integer.toString(transports.get(position).getHourStart()));
                myIntent.putExtra("transport_minuteStart", Integer.toString(transports.get(position).getMinuteStart()));
                myIntent.putExtra("transport_numFreeSpace", Integer.toString(transports.get(position).getNumFreeSpace()));
                myIntent.putExtra("transport_device_id", transports.get(position).getDeviceId());
                myIntent.putExtra("transport_destination", transports.get(position).getDestination());
                myIntent.putExtra("festival_name", festival_name);
                myIntent.putExtra("festival_date", festival_date);
                myIntent.putExtra("festval_id", festival_id);
                Date formatDate = null;
                try {
                    formatDate = dateFormatInput.parse(festival_date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (formatDate != null)
                {
                    myIntent.putExtra("festival_date", dateFormat.format(formatDate));
                }

                startActivity(myIntent);
            }
        });

        final Context context = this;
    }

    /**
     * Get information for the messages on the activity
     * @param transports
     * @see TransportEndpointsInterface
     * @see TransportEndpointsAsyncTask
     */
    @Override
    public void updateIntro(List<Transport> transports) {
        int nbTransports = transports.size();
        int nbPlaces = 0;
        for(int i= 0; i<transports.size(); i++){
            nbPlaces += transports.get(i).getNumFreeSpace();
        }
        transport_intro = (TextView) findViewById(R.id.textview_intro);
        transport_intro.setText("Actuellement " + nbTransports + " conducteurs sont inscrits et " +
                nbPlaces + " places disponibles pour rentrer de " + festival_name +
                ". Prenez contact pour la destination qui vous convient!" );

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
}

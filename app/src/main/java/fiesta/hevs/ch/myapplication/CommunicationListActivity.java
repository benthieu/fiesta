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
import android.widget.ListAdapter;
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
public class CommunicationListActivity extends AppCompatActivity implements ConversationListEndPointsInterface {
    private ProgressDialog mDialog;

    private Long transport_id;
    private String android_id;
    private String other_device_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_communication_list);

        Intent myIntent = getIntent(); // gets the previously created intent
        transport_id = myIntent.getLongExtra("transport_id", 0);


        android_id = Settings.Secure.getString(getBaseContext().getContentResolver(), Settings.Secure.ANDROID_ID);

        other_device_id = myIntent.getStringExtra("other_device_id");

        Button newComButton = (Button) findViewById(R.id.new_communication);
        newComButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent;

                myIntent = new Intent(CommunicationListActivity.this, CommunicationActivity.class);
                myIntent.putExtra("transport_id", transport_id);
                myIntent.putExtra("device_id_to", other_device_id);
                myIntent.putExtra("device_id_from", android_id);
                startActivity(myIntent);
            }
        });


        mDialog = new ProgressDialog(CommunicationListActivity.this);
        mDialog.setMessage("Charger les conversations...");
        mDialog.setCancelable(false);
        mDialog.show();

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
        cLAT.setDevice_id_2(other_device_id);
        if (transport_id != 0) {
            cLAT.setFestival_transport_id(transport_id);
        }
        cLAT.execute();
    }

    @Override
    public void updateListView(final List<Communication> communications) {
        mDialog.hide();

        if (communications == null) {
            return;
        }

        final ListView listView = (ListView) findViewById(R.id.conversation_list);
        setListViewHeightBasedOnChildren(listView);
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
                else {
                    message_parent.setGravity(Gravity.LEFT);
                    message_layout.setGravity(Gravity.LEFT);
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


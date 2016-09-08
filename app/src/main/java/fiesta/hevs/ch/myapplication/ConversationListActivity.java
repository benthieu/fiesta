package fiesta.hevs.ch.myapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.api.client.util.DateTime;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import fiesta.hevs.ch.backend.communicationApi.model.Communication;
import fiesta.hevs.ch.backend.festivalApi.model.Festival;

/**
 * Created by Pascal on 01.09.2016.
 */
public class ConversationListActivity extends AppCompatActivity implements ConversationListEndPointsInterface{
    private ProgressDialog mDialog;
    private Long festival_id;
    private ImageButton backButton;
    private String android_id;
    private DateFormat dateFormat;
    private DateFormat dateFormatInput;
    private final List<comObj> comList = new ArrayList<comObj>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation_list);

        android_id = Settings.Secure.getString(getBaseContext().getContentResolver(), Settings.Secure.ANDROID_ID);

        Intent myIntent = getIntent(); // gets the previously created intent

        mDialog = new ProgressDialog(ConversationListActivity.this);
        mDialog.setMessage("Charger les conversations...");
        mDialog.setCancelable(false);
        mDialog.show();

        dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        dateFormatInput = new SimpleDateFormat("yyyy-MM-dd");

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
                    if (!c.getDeviceIdFrom().equals(android_id)) {
                        o.name = c.getName();
                    }
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
                Intent myIntent = new Intent(ConversationListActivity.this, CommunicationListActivity.class);
                //myIntent.putExtra("transport_id", communications.get(position).getId());
                myIntent.putExtra("other_device_id", comList.get(position).other_device_id);

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

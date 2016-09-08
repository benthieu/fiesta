package fiesta.hevs.ch.myapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import fiesta.hevs.ch.backend.communicationApi.model.Communication;

/**
 * Created by Pascal on 01.09.2016.
 */
public class ConversationListActivity extends AppCompatActivity implements ConversationListEndPointsInterface{
    private ProgressDialog mDialog;
    private Long festival_id;
    private ImageButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation_list);

        Intent myIntent = getIntent(); // gets the previously created intent

        mDialog = new ProgressDialog(ConversationListActivity.this);
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
        new ConversationListEndPointsAsyncTask(this).execute();
    }


    public void updateListView(final List<Communication> communications) {
        mDialog.hide();

        final ListView listView = (ListView) findViewById(R.id.listView);
        setListViewHeightBasedOnChildren(listView);
        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.list_elem_conversation, R.id.textview_1, communications) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text1 = (TextView) view.findViewById(R.id.textview_1);

                text1.setText(communications.get(position).getName());

                return view;
            }
        };

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // show order edit/modify of this specific order
                Intent myIntent = new Intent(ConversationListActivity.this, TransportActivity.class);
                myIntent.putExtra("festival_id", communications.get(position).getId());
                myIntent.putExtra("festival_name", communications.get(position).getName());

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

}

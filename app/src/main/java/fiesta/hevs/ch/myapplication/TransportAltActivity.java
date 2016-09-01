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
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import fiesta.hevs.ch.backend.transportAltApi.model.TransportAlt;
import fiesta.hevs.ch.backend.transportApi.model.Transport;

/**
 * Created by Chacha on 01.09.2016.
 */
public class TransportAltActivity extends AppCompatActivity implements TransportAltEndpointsInterface{
    private ProgressDialog mDialog;
    private Long festival_id;
    private ImageButton backButton;
    private TextView transport_top_name;
    private TextView transport_top_date;
    private TextView transport_intro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transport);

        Intent myIntent = getIntent(); // gets the previously created intent
        festival_id = myIntent.getLongExtra("festival_id", 0);
        String festival_name = myIntent.getStringExtra("festival_name");
        String festival_date = myIntent.getStringExtra("festival_date");
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
        mDialog = new ProgressDialog(TransportAltActivity.this);
        mDialog.setMessage("Charger les trajets...");
        mDialog.setCancelable(false);
        mDialog.show();

        getTransportsAlt();

    }

    @Override
    public void onResume() {
        super.onResume();
        mDialog.show();
        getTransportsAlt();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mDialog.dismiss();
    }

    public void getTransportsAlt() {
        new TransportAltEndpointsAsyncTask(festival_id, this).execute();
    }

    @Override
    public void updateListView(final List<TransportAlt> transportsAlt) {
        mDialog.hide();

        final ListView listView = (ListView) findViewById(R.id.listView);

        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.list_elem_transport, R.id.textview_1, transportsAlt) {
            @Override

            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text1 = (TextView) view.findViewById(R.id.textview_1);
                TextView text2 = (TextView) view.findViewById(R.id.textview_2);
                TextView text3 = (TextView) view.findViewById(R.id.textview_3);

                text1.setText(transportsAlt.get(position).getName());
                text2.setText(transportsAlt.get(position).getDescription());
                text3.setText(transportsAlt.get(position).getLink().toString());
                return view;
            }
        };

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

            }
        });

        final Context context = this;
    }

}

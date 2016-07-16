package fiesta.hevs.ch.myapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import java.util.List;

import fiesta.hevs.ch.backend.transportApi.model.Transport;

public class TransportActivity extends AppCompatActivity implements TransportEndpointsInterface {
    private ProgressDialog mDialog;
    private Long festival_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_festival);

        Intent myIntent = getIntent(); // gets the previously created intent
        festival_id = myIntent.getLongExtra("festival_id", 0);
        String festival_name = myIntent.getStringExtra("festival_name");
        this.setTitle(festival_name);

        mDialog = new ProgressDialog(TransportActivity.this);
        mDialog.setMessage("Charger les trajets...");
        mDialog.setCancelable(false);
        mDialog.show();

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

    public void getTransports() {
        new TransportEndpointsAsyncTask(festival_id, this).execute();
    }

    @Override
    public void updateListView(final List<Transport> transports) {
        mDialog.hide();

        final ListView listView = (ListView) findViewById(R.id.listView);

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_2, android.R.id.text1, transports) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                TextView text2 = (TextView) view.findViewById(android.R.id.text2);

                text1.setText(transports.get(position).getDestination());
                text2.setText(transports.get(position).getDriver());
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

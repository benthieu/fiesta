package fiesta.hevs.ch.myapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import fiesta.hevs.ch.backend.festivalApi.model.Festival;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Date;

public class FestivalActivity extends AppCompatActivity implements FestivalEndpointsInterface {
    private DateFormat dateFormat;
    private DateFormat dateFormatInput;
    private ProgressDialog mDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        mDialog = new ProgressDialog(FestivalActivity.this);
        mDialog.setMessage("Charger les festivals...");
        mDialog.setCancelable(false);
        mDialog.show();

        dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        dateFormatInput = new SimpleDateFormat("yyyy-MM-dd");
        getFestivals();
    }

    public void getFestivals() {
        new FestivalEndpointsAsyncTask(this).execute();
    }

    @Override
    public void updateListView(final List<Festival> festivals) {
        mDialog.hide();

        final ListView listView = (ListView) findViewById(R.id.listView);

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_2, android.R.id.text1, festivals) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                TextView text2 = (TextView) view.findViewById(android.R.id.text2);

                text1.setText(festivals.get(position).getName());
                Date formatDate = null;
                try {
                    formatDate = dateFormatInput.parse(festivals.get(position).getDate().toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (formatDate != null)
                {
                    text2.setText(dateFormat.format(formatDate));
                }
                return view;
            }
        };

        listView.setAdapter(adapter);

        final Context context = this;
    }
}

package fiesta.hevs.ch.myapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
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

    //Listener to view other transports
    private View.OnClickListener clickCommunication = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intentForCommunication = new Intent(FestivalActivity.this, ConversationListActivity.class);
            startActivity(intentForCommunication);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_festival);

        mDialog = new ProgressDialog(FestivalActivity.this);
        mDialog.setMessage("Charger les festivals...");
        mDialog.setCancelable(false);
        mDialog.show();

        dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        dateFormatInput = new SimpleDateFormat("yyyy-MM-dd");

        getFestivals();
    }

    @Override
    public void onResume() {
        super.onResume();
        mDialog.show();
        getFestivals();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mDialog.dismiss();
    }


    public void getFestivals() {
        new FestivalEndpointsAsyncTask(this).execute();
    }

    @Override
    public void updateListView(final List<Festival> festivals) {
        mDialog.hide();

        final ListView listView = (ListView) findViewById(R.id.listView);
        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.list_elem_festival, R.id.textview_1, festivals) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text1 = (TextView) view.findViewById(R.id.textview_1);
                TextView text2 = (TextView) view.findViewById(R.id.textview_2);
                ImageView image1 = (ImageView) view.findViewById(R.id.imageview);

                if (festivals.get(position).getImage() != null) {
                    Log.d("setting_image", festivals.get(position).getName());
                    String CurrentString = festivals.get(position).getImage();
                    String[] separated = CurrentString.split(",");
                    byte[] decodedString = Base64.decode(separated[1], Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0,decodedString.length);
                    image1.setImageBitmap(decodedByte);
                }
                else {
                    image1.setImageBitmap(null);
                }

                if (position == 0) {
                    image1.setBackground(ContextCompat.getDrawable(this.getContext(), R.layout.rounded_corners_top_left));
                }
                if (position == (festivals.size()-1)) {
                    image1.setBackground(ContextCompat.getDrawable(this.getContext(), R.layout.rounded_corners_bottom_left));
                }

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

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // show order edit/modify of this specific order
                Intent myIntent = new Intent(FestivalActivity.this, TransportActivity.class);
                myIntent.putExtra("festival_id", festivals.get(position).getId());
                myIntent.putExtra("festival_name", festivals.get(position).getName());
                Date formatDate = null;
                try {
                    formatDate = dateFormatInput.parse(festivals.get(position).getDate().toString());
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

        final Button button= (Button) findViewById(R.id.communicationButton);;

        button.setOnClickListener(clickCommunication);

        final Context context = this;
    }
}

package fiesta.hevs.ch.myapplication;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Created by Pascal on 30.08.2016.
 */
public class NewTransportActivity extends AppCompatActivity {
    private DateFormat dateFormat;
    private DateFormat dateFormatInput;
    private ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_transport);


    }

    @Override
    public void onResume() {
        super.onResume();
        mDialog.show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mDialog.dismiss();
    }
}



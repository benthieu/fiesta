package fiesta.hevs.ch.myapplication;

import android.os.AsyncTask;
import android.util.Log;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import fiesta.hevs.ch.backend.transportAltApi.TransportAltApi;
import fiesta.hevs.ch.backend.transportAltApi.model.TransportAlt;

/**
 * Created by Chacha on 01.09.2016.
 */
/**
 * This class do the link between the activity (Festival) and the backend.
 * it do the operations in the backgroud
 * @author  Chacha on 01.09.2016
 * @see TransportAltActivity
 */
public class TransportAltEndPointsAsyncTask extends AsyncTask<Void, Void, List<TransportAlt>> {
    private static TransportAltApi transportAltApi = null;
    private static final String TAG = TransportAltEndPointsAsyncTask.class.getName();
    private TransportAltEndPointsInterface listener;
    private Long festival_id;

    /**
     * Constructor to get a list of alternative transports with the festival id
     * @param festival_id
     * @param listener
     * @see TransportAltActivity
     */
    TransportAltEndPointsAsyncTask(Long festival_id, TransportAltEndPointsInterface listener) {
        this.festival_id = festival_id;
        this.listener = listener;
    }

    /**
     * This method do the link with the backend to access at the database
     * And return a list of alternatives transports
     * @see TransportAltApi
     */
    @Override
    protected List<TransportAlt> doInBackground(Void... params) {
        List<TransportAlt> returnList = new ArrayList<TransportAlt>();
        if (transportAltApi == null) {
            // Only do this once
            TransportAltApi.Builder builder = new TransportAltApi.Builder(AndroidHttp.newCompatibleTransport(),
                    new AndroidJsonFactory(), null)
                    // options for running against local devappserver
                    // - 10.0.2.2 is localhost's IP address in Android emulator
                    // - turn off compression when running against local devappserver
                    // if you deploy on the cloud backend, use your app name
                    // such as https://<your-app-id>.appspot.com
                    .setRootUrl("https://fiesta-1372.appspot.com/_ah/api/")
                    .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                        @Override
                        public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
                            abstractGoogleClientRequest.setDisableGZipContent(true);
                        }
                    });
            transportAltApi = builder.build();
        }

        try {
            // and for instance return the list
            returnList = transportAltApi.listByFestival().setFestivalId(festival_id).execute().getItems();
        } catch (IOException e) {
            Log.e(TAG, e.toString());
        }
        return returnList;
    }

    /**
     * This method gets executed on the UI thread - The UI can be manipulated directly inside
     * of this method
     * We use the Interface to send the list of alternatives transports to the activity
     * @see TransportAltEndPointsInterface
     * @see TransportAltActivity
     */
    @Override
    protected void onPostExecute(List<TransportAlt> result) {
        if (result != null) {
            listener.updateListView(result);
        }
        else {
            listener.updateListView(new ArrayList<TransportAlt>());
        }
    }
}

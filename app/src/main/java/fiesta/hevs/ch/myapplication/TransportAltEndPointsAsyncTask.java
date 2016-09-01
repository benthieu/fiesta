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
import fiesta.hevs.ch.backend.transportApi.TransportApi;
import fiesta.hevs.ch.backend.transportApi.model.Transport;

/**
 * Created by Chacha on 01.09.2016.
 */
public class TransportAltEndpointsAsyncTask extends AsyncTask<Void, Void, List<TransportAlt>> {
    private static TransportAltApi transportAltApi = null;
    private static final String TAG = TransportAltEndpointsAsyncTask.class.getName();
    private TransportAltEndpointsInterface listener;
    private Long festival_id;
    TransportAltEndpointsAsyncTask(Long festival_id, TransportAltEndpointsInterface listener) {
        this.festival_id = festival_id;
        this.listener = listener;
    }

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

    //This method gets executed on the UI thread - The UI can be manipulated directly inside
    //of this method
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
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

import fiesta.hevs.ch.backend.transportApi.TransportApi;
import fiesta.hevs.ch.backend.transportApi.model.Transport;

/**
 * Created by mathi on 16.07.2016.
 */
public class TransportEndpointsAsyncTask extends AsyncTask<Void, Void, List<Transport>> {
    private static TransportApi transportApi = null;
    private static final String TAG = TransportEndpointsAsyncTask.class.getName();
    private TransportEndpointsInterface listener;
    private Long festival_id;
    private Transport addTransport = null;
    private boolean updateTransport = false;
    TransportEndpointsAsyncTask(Long festival_id, TransportEndpointsInterface listener) {
        this.festival_id = festival_id;
        this.listener = listener;
    }

    //New Constructor to add a new transport and update.
    TransportEndpointsAsyncTask(Transport addTransport) {

        this.addTransport = addTransport;
    }

    //When we use this method, to know if it is for a "add" or a "update" transport
    public void setUpdateTransport(boolean updateTransport){
        this.updateTransport = updateTransport;
    }

    @Override
    protected List<Transport> doInBackground(Void... params) {
        List<Transport> returnList = new ArrayList<Transport>();
        if (transportApi == null) {
            // Only do this once
            TransportApi.Builder builder = new TransportApi.Builder(AndroidHttp.newCompatibleTransport(),
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
            transportApi = builder.build();
        }

        try {
            if(addTransport !=null){//To register a new transport
                if(updateTransport!=false){
                    transportApi.update(addTransport.getId(),addTransport).execute();
                    return null;
                }
                else {
                    transportApi.insert(addTransport).execute();
                    return null;
                }
            }

            // and for instance return the list
            returnList = transportApi.listByFestival().setFestivalId(festival_id).execute().getItems();
        } catch (IOException e) {
            Log.e(TAG, e.toString());
        }
        return returnList;
    }

    //This method gets executed on the UI thread - The UI can be manipulated directly inside
    //of this method
    @Override
    protected void onPostExecute(List<Transport> result) {
        if (result != null) {
            listener.updateListView(result);
            listener.updateIntro(result);
        }
        else {
            if(listener==null){ //The listener is null if we create a new transport !
                return;
            }
            listener.updateListView(new ArrayList<Transport>());
            listener.updateIntro(new ArrayList<Transport>());
        }
    }
}
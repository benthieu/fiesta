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

import fiesta.hevs.ch.backend.communicationApi.CommunicationApi;
import fiesta.hevs.ch.backend.communicationApi.model.Communication;

/**
 * Created by Pascal on 02.09.2016.
 */
public class ConversationListEndPointsAsyncTask extends AsyncTask<Void, Void, List<Communication>> {
    private static CommunicationApi communicationApi = null;
    private static final String TAG = ConversationListEndPointsAsyncTask.class.getName();
    private ConversationListEndPointsInterface listener;

    ConversationListEndPointsAsyncTask(ConversationListEndPointsInterface listener) {
        this.listener = listener;
    }

    @Override
    protected List<Communication> doInBackground(Void... params) {
        List<Communication> returnList = new ArrayList<Communication>();

        if (communicationApi == null) {
            // Only do this once
            CommunicationApi.Builder builder = new CommunicationApi.Builder(AndroidHttp.newCompatibleTransport(),
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
            communicationApi = builder.build();
        }

        try {
            // and for instance return the list of all employees
            return returnList=communicationApi.list().execute().getItems();

        } catch (IOException e) {
            Log.e(TAG, e.toString());
            return new ArrayList<Communication>();
        }
    }

    //This method gets executed on the UI thread - The UI can be manipulated directly inside
    //of this method
    @Override
    protected void onPostExecute(List<Communication> result) {
        if (result != null) {
            listener.updateListView(result);
        }
    }
}
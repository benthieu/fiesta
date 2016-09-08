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

import fiesta.hevs.ch.backend.festivalApi.model.Festival;
import fiesta.hevs.ch.backend.festivalApi.FestivalApi;

/**
 * This class do the link between the activity (Festival) and the backend.
 * it do the operations in the backgroud
 * @author  mathi on 15.07.2016.
 * @see FestivalActivity
 */
public class FestivalEndpointsAsyncTask extends AsyncTask<Void, Void, List<Festival>> {
    private static FestivalApi festivalApi = null;
    private static final String TAG = FestivalEndpointsAsyncTask.class.getName();
    private FestivalEndpointsInterface listener;

    /**
     * Constructor to get a list of Festivals
     * @param listener
     * @see FestivalEndpointsInterface
     * @see FestivalActivity
     */
    FestivalEndpointsAsyncTask(FestivalEndpointsInterface listener) {
        this.listener = listener;
    }

    /**
     * This method do the link with the backend to access at the database
     * And return a list of festivals
     */
    @Override
    protected List<Festival> doInBackground(Void... params) {

        if (festivalApi == null) {
            // Only do this once
            FestivalApi.Builder builder = new FestivalApi.Builder(AndroidHttp.newCompatibleTransport(),
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
            festivalApi = builder.build();
        }

        try {
            // and for instance return the list of all employees
            return festivalApi.list().execute().getItems();

        } catch (IOException e) {
            Log.e(TAG, e.toString());
            return new ArrayList<Festival>();
        }
    }

    /**
     * This method gets executed on the UI thread - The UI can be manipulated directly inside
     * of this method
     * We use the Interface to get the list of festivals to the activity
     * @see FestivalEndpointsInterface
     * @see FestivalActivity
     */
    @Override
    protected void onPostExecute(List<Festival> result) {
        if (result != null) {
            listener.updateListView(result);
        }
    }
}
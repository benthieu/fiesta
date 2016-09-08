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

    private int type;
    private Long festival_transport_id;
    private String device_id_1;
    private String device_id_2;
    private Communication tempCommunication;


    public Communication getTempCommunication() {
        return tempCommunication;
    }

    public void setTempCommunication(Communication tempCommunication) {
        this.tempCommunication = tempCommunication;
    }

    public String getDevice_id_2() {
        return device_id_2;
    }

    public void setDevice_id_2(String device_id_2) {
        this.device_id_2 = device_id_2;
    }

    public Long getFestival_transport_id() {
        return festival_transport_id;
    }

    public void setFestival_transport_id(Long festival_transport_id) {
        this.festival_transport_id = festival_transport_id;
    }

    public String getDevice_id_1() {
        return device_id_1;
    }

    public void setDevice_id_1(String device_id_1) {
        this.device_id_1 = device_id_1;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

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
        if (type == listener.LIST) {
            try {
                // and for instance return the list of all employees
                return returnList = communicationApi.listByTransport().setFestivalTransportId(festival_transport_id).setDeviceId1(device_id_1).setDeviceId2(device_id_2).execute().getItems();

            } catch (IOException e) {
                Log.e(TAG, e.toString());
                return new ArrayList<Communication>();
            }
        }
        if (type == listener.INSERT) {
            if (tempCommunication != null) {
                try {
                    communicationApi.insert(tempCommunication).execute();
                } catch (IOException e) {
                    Log.e(TAG, e.toString());
                }
            }
        }
        return null;
    }

    //This method gets executed on the UI thread - The UI can be manipulated directly inside
    //of this method
    @Override
    protected void onPostExecute(List<Communication> result) {
        if (type == listener.LIST) {
            if (result != null) {
                listener.updateListView(result);
            }
            listener.updateListView(null);
        }

        if (type == listener.INSERT) {
            listener.insertedCommunication();
        }
    }
}
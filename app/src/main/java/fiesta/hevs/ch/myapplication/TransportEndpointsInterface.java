package fiesta.hevs.ch.myapplication;

import java.util.List;

import fiesta.hevs.ch.backend.transportApi.model.Transport;

/**
 * Interface to "communicate" between TransportEndpointsAsyncTask and TransportActivity
 * @author  mathi on 16.07.2016.
 * @see TransportEndpointsAsyncTask
 * @see TransportActivity
 */
public interface TransportEndpointsInterface {
    void updateListView(List<Transport> transports);
    void updateIntro(List<Transport> transports);
}

package fiesta.hevs.ch.myapplication;

import java.util.List;

import fiesta.hevs.ch.backend.transportAltApi.model.TransportAlt;

/**
 * Interface to "communicate" between TransportAltEndpointsAsyncTask and TransportAltActivity
 * @author  Chacha on 01.09.2016.
 * @see TransportAltEndPointsAsyncTask
 * @see TransportAltActivity
 */
public interface TransportAltEndPointsInterface {
    void updateListView(List<TransportAlt> transportsAlt);
}

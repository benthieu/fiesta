package fiesta.hevs.ch.myapplication;

import java.util.List;

import fiesta.hevs.ch.backend.transportApi.model.Transport;

/**
 * Created by mathi on 16.07.2016.
 */
public interface TransportEndpointsInterface {
    void updateListView(List<Transport> transports);
}

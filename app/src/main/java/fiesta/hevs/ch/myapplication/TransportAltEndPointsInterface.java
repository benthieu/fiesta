package fiesta.hevs.ch.myapplication;

import java.util.List;

import fiesta.hevs.ch.backend.transportAltApi.model.TransportAlt;


/**
 * Created by Chacha on 01.09.2016.
 */
public interface TransportAltEndPointsInterface {
    void updateListView(List<TransportAlt> transportsAlt);
}

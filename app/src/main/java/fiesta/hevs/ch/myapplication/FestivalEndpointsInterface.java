package fiesta.hevs.ch.myapplication;

import java.util.List;

import fiesta.hevs.ch.backend.festivalApi.model.Festival;

/**
 * Interface to "communicate" between FestivalEndpointsAsyncTask and FestivalActivity
 * @author mathi on 15.07.2016.
 * @see FestivalEndpointsAsyncTask
 * @see FestivalActivity
 */
public interface FestivalEndpointsInterface {
    void updateListView(List<Festival> festivals);
}

package fiesta.hevs.ch.myapplication;

import java.util.List;

import fiesta.hevs.ch.backend.communicationApi.model.Communication;

/**
 * Created by Pascal on 02.09.2016.
 */
public interface ConversationListEndPointsInterface {
    void updateListView(List<Communication> communications);
}

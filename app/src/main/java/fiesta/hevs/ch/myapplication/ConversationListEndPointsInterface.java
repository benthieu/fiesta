package fiesta.hevs.ch.myapplication;

import java.util.List;

import fiesta.hevs.ch.backend.communicationApi.model.Communication;

/**
 * Created by Pascal on 02.09.2016.
 */
public interface ConversationListEndPointsInterface {
    public static int LIST = 0;
    public static int INSERT = 1;
    public static int UPDATE = 2;
    void updateListView(List<Communication> communications);
    void insertedCommunication();
}

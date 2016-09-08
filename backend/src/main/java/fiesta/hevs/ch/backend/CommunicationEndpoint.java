package fiesta.hevs.ch.backend;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.response.CollectionResponse;
import com.google.api.server.spi.response.NotFoundException;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.QueryResultIterator;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.cmd.Query;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.Nullable;
import javax.inject.Named;

import static com.googlecode.objectify.ObjectifyService.ofy;

/**
 * WARNING: This generated code is intended as a sample or starting point for using a
 * Google Cloud Endpoints RESTful API with an Objectify entity. It provides no data access
 * restrictions and no data validation.
 * <p/>
 * DO NOT deploy this code unchanged as part of a real application to real users.
 */
@Api(
        name = "communicationApi",
        version = "v1",
        resource = "communication",
        namespace = @ApiNamespace(
                ownerDomain = "backend.ch.hevs.fiesta",
                ownerName = "backend.ch.hevs.fiesta",
                packagePath = ""
        )
)
public class CommunicationEndpoint {

    private static final Logger logger = Logger.getLogger(CommunicationEndpoint.class.getName());

    private static final int DEFAULT_LIST_LIMIT = 20;

    static {
        // Typically you would register this inside an OfyServive wrapper. See: https://code.google.com/p/objectify-appengine/wiki/BestPractices
        ObjectifyService.register(Communication.class);
    }

    /**
     * Returns the {@link Communication} with the corresponding ID.
     *
     * @param id the ID of the entity to be retrieved
     * @return the entity with the corresponding ID
     * @throws NotFoundException if there is no {@code Communication} with the provided ID.
     */
    @ApiMethod(
            name = "get",
            path = "communication/{id}",
            httpMethod = ApiMethod.HttpMethod.GET)
    public Communication get(@Named("id") Long id) throws NotFoundException {
        logger.info("Getting Communication with ID: " + id);
        Communication communication = ofy().load().type(Communication.class).id(id).now();
        if (communication == null) {
            throw new NotFoundException("Could not find Communication with ID: " + id);
        }
        return communication;
    }

    /**
     * Inserts a new {@code Communication}.
     */
    @ApiMethod(
            name = "insert",
            path = "communication",
            httpMethod = ApiMethod.HttpMethod.POST)
    public Communication insert(Communication communication) {
        // Typically in a RESTful API a POST does not have a known ID (assuming the ID is used in the resource path).
        // You should validate that communication.id has not been set. If the ID type is not supported by the
        // Objectify ID generator, e.g. long or String, then you should generate the unique ID yourself prior to saving.
        //
        // If your client provides the ID then you should probably use PUT instead.
        Date now = new Date();
        communication.setTime_send(now);
        ofy().save().entity(communication).now();
        logger.info("Created Communication with ID: " + communication.getId());

        return ofy().load().entity(communication).now();
    }

    /**
     * Deletes the specified {@code Communication}.
     *
     * @param id the ID of the entity to delete
     * @throws NotFoundException if the {@code id} does not correspond to an existing
     *                           {@code Communication}
     */
    @ApiMethod(
            name = "remove",
            path = "communication/{id}",
            httpMethod = ApiMethod.HttpMethod.DELETE)
    public void remove(@Named("id") Long id) throws NotFoundException {
        checkExists(id);
        ofy().delete().type(Communication.class).id(id).now();
        logger.info("Deleted Communication with ID: " + id);
    }

    /**
     * List all entities.
     *
     * @param cursor used for pagination to determine which page to return
     * @param limit  the maximum number of entries to return
     * @return a response that encapsulates the result list and the next page token/cursor
     */
    @ApiMethod(
            name = "list",
            path = "communication_list",
            httpMethod = ApiMethod.HttpMethod.GET)
    public CollectionResponse<Communication> list(@Nullable @Named("cursor") String cursor, @Nullable @Named("limit") Integer limit) {
        limit = limit == null ? DEFAULT_LIST_LIMIT : limit;
        Query<Communication> query = ofy().load().type(Communication.class).limit(limit);
        if (cursor != null) {
            query = query.startAt(Cursor.fromWebSafeString(cursor));
        }
        QueryResultIterator<Communication> queryIterator = query.iterator();
        List<Communication> communicationList = new ArrayList<Communication>(limit);
        while (queryIterator.hasNext()) {
            communicationList.add(queryIterator.next());
        }
        return CollectionResponse.<Communication>builder().setItems(communicationList).setNextPageToken(queryIterator.getCursor().toWebSafeString()).build();
    }

    /**
     * List all entities.
     *
     * @param cursor used for pagination to determine which page to return
     * @return a response that encapsulates the result list and the next page token/cursor
     */
    @ApiMethod(
            name = "listByTransport",
            path = "communication_listByTransport",
            httpMethod = ApiMethod.HttpMethod.GET)
    public CollectionResponse<Communication> listByTransport(@Nullable @Named("festival_transport_id") Long festival_transport_id,
                                                             @Nullable @Named("device_id_1") String device_id_1,
                                                             @Nullable @Named("device_id_2") String device_id_2,
                                                             @Nullable @Named("cursor") String cursor) {
        int limit = DEFAULT_LIST_LIMIT;
        Query<Communication> query = ofy().load().type(Communication.class);
        if (festival_transport_id != null) {
            query.filter("festival_transport_id", festival_transport_id);
        }
        if (cursor != null) {
            query = query.startAt(Cursor.fromWebSafeString(cursor));
        }

        QueryResultIterator<Communication> queryIterator = query.iterator();
        List<Communication> communicationList = new ArrayList<Communication>(limit);
        while (queryIterator.hasNext()) {
            Communication thiscom = queryIterator.next();
            if (thiscom.getDevice_id_from() != null && thiscom.getDevice_id_to() != null)  {
                if ((thiscom.getDevice_id_from().equals(device_id_1) && (thiscom.getDevice_id_to().equals(device_id_2) || device_id_2 == null)) ||
                        ((thiscom.getDevice_id_from().equals(device_id_2) || device_id_2 == null) && thiscom.getDevice_id_to().equals(device_id_1))) {
                    communicationList.add(thiscom);
                }
            }
        }
        return CollectionResponse.<Communication>builder().setItems(communicationList).setNextPageToken(queryIterator.getCursor().toWebSafeString()).build();
    }

    private void checkExists(Long id) throws NotFoundException {
        try {
            ofy().load().type(Communication.class).id(id).safe();
        } catch (com.googlecode.objectify.NotFoundException e) {
            throw new NotFoundException("Could not find Communication with ID: " + id);
        }
    }
}
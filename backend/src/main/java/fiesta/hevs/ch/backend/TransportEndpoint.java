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
        name = "transportApi",
        version = "v1",
        resource = "transport",
        namespace = @ApiNamespace(
                ownerDomain = "backend.ch.hevs.fiesta",
                ownerName = "backend.ch.hevs.fiesta",
                packagePath = ""
        )
)
public class TransportEndpoint {

    private static final Logger logger = Logger.getLogger(TransportEndpoint.class.getName());

    private static final int DEFAULT_LIST_LIMIT = 20;

    static {
        // Typically you would register this inside an OfyServive wrapper. See: https://code.google.com/p/objectify-appengine/wiki/BestPractices
        ObjectifyService.register(Transport.class);
    }

    /**
     * Returns the {@link Transport} with the corresponding ID.
     *
     * @param id the ID of the entity to be retrieved
     * @return the entity with the corresponding ID
     * @throws NotFoundException if there is no {@code Transport} with the provided ID.
     */
    @ApiMethod(
            name = "get",
            path = "transport/{id}",
            httpMethod = ApiMethod.HttpMethod.GET)
    public Transport get(@Named("id") Long id) throws NotFoundException {
        logger.info("Getting Transport with ID: " + id);
        Transport transport = ofy().load().type(Transport.class).id(id).now();
        if (transport == null) {
            throw new NotFoundException("Could not find Transport with ID: " + id);
        }
        return transport;
    }

    /**
     * Inserts a new {@code Transport}.
     */
    @ApiMethod(
            name = "insert",
            path = "transport",
            httpMethod = ApiMethod.HttpMethod.POST)
    public Transport insert(Transport transport) {
        // Typically in a RESTful API a POST does not have a known ID (assuming the ID is used in the resource path).
        // You should validate that transport.id has not been set. If the ID type is not supported by the
        // Objectify ID generator, e.g. long or String, then you should generate the unique ID yourself prior to saving.
        //
        // If your client provides the ID then you should probably use PUT instead.
        ofy().save().entity(transport).now();
        logger.info("Created Transport with ID: " + transport.getId());

        return ofy().load().entity(transport).now();
    }

    /**
     * Updates an existing {@code Transport}.
     *
     * @param id        the ID of the entity to be updated
     * @param transport the desired state of the entity
     * @return the updated version of the entity
     * @throws NotFoundException if the {@code id} does not correspond to an existing
     *                           {@code Transport}
     */
    @ApiMethod(
            name = "update",
            path = "transport/{id}",
            httpMethod = ApiMethod.HttpMethod.PUT)
    public Transport update(@Named("id") Long id, Transport transport) throws NotFoundException {
        // TODO: You should validate your ID parameter against your resource's ID here.
        checkExists(id);
        ofy().save().entity(transport).now();
        logger.info("Updated Transport: " + transport);
        return ofy().load().entity(transport).now();
    }

    /**
     * Deletes the specified {@code Transport}.
     *
     * @param id the ID of the entity to delete
     * @throws NotFoundException if the {@code id} does not correspond to an existing
     *                           {@code Transport}
     */
    @ApiMethod(
            name = "remove",
            path = "transport/{id}",
            httpMethod = ApiMethod.HttpMethod.DELETE)
    public void remove(@Named("id") Long id) throws NotFoundException {
        checkExists(id);
        ofy().delete().type(Transport.class).id(id).now();
        logger.info("Deleted Transport with ID: " + id);
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
            path = "transport_list",
            httpMethod = ApiMethod.HttpMethod.GET)
    public CollectionResponse<Transport> list(@Nullable @Named("cursor") String cursor, @Nullable @Named("limit") Integer limit) {
        limit = limit == null ? DEFAULT_LIST_LIMIT : limit;
        Query<Transport> query = ofy().load().type(Transport.class).limit(limit);
        if (cursor != null) {
            query = query.startAt(Cursor.fromWebSafeString(cursor));
        }
        QueryResultIterator<Transport> queryIterator = query.iterator();
        List<Transport> transportList = new ArrayList<Transport>(limit);
        while (queryIterator.hasNext()) {
            transportList.add(queryIterator.next());
        }
        return CollectionResponse.<Transport>builder().setItems(transportList).setNextPageToken(queryIterator.getCursor().toWebSafeString()).build();
    }



    /**
     * List all entities by festival.
     *
     * @param cursor used for pagination to determine which page to return
     * @param limit  the maximum number of entries to return
     * @return a response that encapsulates the result list and the next page token/cursor
     */
    @ApiMethod(
            name = "listByFestival",
            path = "transport_listByFestival",
            httpMethod = ApiMethod.HttpMethod.GET)
    public CollectionResponse<Transport> listByFestival(@Nullable @Named("festival_id") Long festival_id, @Nullable @Named("cursor") String cursor, @Nullable @Named("limit") Integer limit) {
        limit = limit == null ? DEFAULT_LIST_LIMIT : limit;
        Query<Transport> query = ofy().load().type(Transport.class).filter("festival_id", festival_id).limit(limit);
        if (cursor != null) {
            query = query.startAt(Cursor.fromWebSafeString(cursor));
        }
        QueryResultIterator<Transport> queryIterator = query.iterator();
        List<Transport> transportList = new ArrayList<Transport>(limit);
        while (queryIterator.hasNext()) {
            transportList.add(queryIterator.next());
        }
        return CollectionResponse.<Transport>builder().setItems(transportList).setNextPageToken(queryIterator.getCursor().toWebSafeString()).build();
    }

    private void checkExists(Long id) throws NotFoundException {
        try {
            ofy().load().type(Transport.class).id(id).safe();
        } catch (com.googlecode.objectify.NotFoundException e) {
            throw new NotFoundException("Could not find Transport with ID: " + id);
        }
    }
}
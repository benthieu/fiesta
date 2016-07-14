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
        name = "transportAltApi",
        version = "v1",
        resource = "transportAlt",
        namespace = @ApiNamespace(
                ownerDomain = "backend.ch.hevs.fiesta",
                ownerName = "backend.ch.hevs.fiesta",
                packagePath = ""
        )
)
public class TransportAltEndpoint {

    private static final Logger logger = Logger.getLogger(TransportAltEndpoint.class.getName());

    private static final int DEFAULT_LIST_LIMIT = 20;

    static {
        // Typically you would register this inside an OfyServive wrapper. See: https://code.google.com/p/objectify-appengine/wiki/BestPractices
        ObjectifyService.register(TransportAlt.class);
    }

    /**
     * Returns the {@link TransportAlt} with the corresponding ID.
     *
     * @param id the ID of the entity to be retrieved
     * @return the entity with the corresponding ID
     * @throws NotFoundException if there is no {@code TransportAlt} with the provided ID.
     */
    @ApiMethod(
            name = "get",
            path = "transportAlt/{id}",
            httpMethod = ApiMethod.HttpMethod.GET)
    public TransportAlt get(@Named("id") Long id) throws NotFoundException {
        logger.info("Getting TransportAlt with ID: " + id);
        TransportAlt transportAlt = ofy().load().type(TransportAlt.class).id(id).now();
        if (transportAlt == null) {
            throw new NotFoundException("Could not find TransportAlt with ID: " + id);
        }
        return transportAlt;
    }

    /**
     * Inserts a new {@code TransportAlt}.
     */
    @ApiMethod(
            name = "insert",
            path = "transportAlt",
            httpMethod = ApiMethod.HttpMethod.POST)
    public TransportAlt insert(TransportAlt transportAlt) {
        // Typically in a RESTful API a POST does not have a known ID (assuming the ID is used in the resource path).
        // You should validate that transportAlt.id has not been set. If the ID type is not supported by the
        // Objectify ID generator, e.g. long or String, then you should generate the unique ID yourself prior to saving.
        //
        // If your client provides the ID then you should probably use PUT instead.
        ofy().save().entity(transportAlt).now();
        logger.info("Created TransportAlt with ID: " + transportAlt.getId());

        return ofy().load().entity(transportAlt).now();
    }

    /**
     * Updates an existing {@code TransportAlt}.
     *
     * @param id           the ID of the entity to be updated
     * @param transportAlt the desired state of the entity
     * @return the updated version of the entity
     * @throws NotFoundException if the {@code id} does not correspond to an existing
     *                           {@code TransportAlt}
     */
    @ApiMethod(
            name = "update",
            path = "transportAlt/{id}",
            httpMethod = ApiMethod.HttpMethod.PUT)
    public TransportAlt update(@Named("id") Long id, TransportAlt transportAlt) throws NotFoundException {
        // TODO: You should validate your ID parameter against your resource's ID here.
        checkExists(id);
        ofy().save().entity(transportAlt).now();
        logger.info("Updated TransportAlt: " + transportAlt);
        return ofy().load().entity(transportAlt).now();
    }

    /**
     * Deletes the specified {@code TransportAlt}.
     *
     * @param id the ID of the entity to delete
     * @throws NotFoundException if the {@code id} does not correspond to an existing
     *                           {@code TransportAlt}
     */
    @ApiMethod(
            name = "remove",
            path = "transportAlt/{id}",
            httpMethod = ApiMethod.HttpMethod.DELETE)
    public void remove(@Named("id") Long id) throws NotFoundException {
        checkExists(id);
        ofy().delete().type(TransportAlt.class).id(id).now();
        logger.info("Deleted TransportAlt with ID: " + id);
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
            path = "transportAlt",
            httpMethod = ApiMethod.HttpMethod.GET)
    public CollectionResponse<TransportAlt> list(@Nullable @Named("cursor") String cursor, @Nullable @Named("limit") Integer limit) {
        limit = limit == null ? DEFAULT_LIST_LIMIT : limit;
        Query<TransportAlt> query = ofy().load().type(TransportAlt.class).limit(limit);
        if (cursor != null) {
            query = query.startAt(Cursor.fromWebSafeString(cursor));
        }
        QueryResultIterator<TransportAlt> queryIterator = query.iterator();
        List<TransportAlt> transportAltList = new ArrayList<TransportAlt>(limit);
        while (queryIterator.hasNext()) {
            transportAltList.add(queryIterator.next());
        }
        return CollectionResponse.<TransportAlt>builder().setItems(transportAltList).setNextPageToken(queryIterator.getCursor().toWebSafeString()).build();
    }

    private void checkExists(Long id) throws NotFoundException {
        try {
            ofy().load().type(TransportAlt.class).id(id).safe();
        } catch (com.googlecode.objectify.NotFoundException e) {
            throw new NotFoundException("Could not find TransportAlt with ID: " + id);
        }
    }
}
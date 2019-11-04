package com.ihorpolataiko.dropwizarddemo.resource;

import com.ihorpolataiko.dropwizarddemo.dao.ItemDao;
import com.ihorpolataiko.dropwizarddemo.domain.Item;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.bson.types.ObjectId;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Api("Item resource")
@Path("/items")
@Produces(MediaType.APPLICATION_JSON)
public class ItemResource {

    private final ItemDao itemDao;

    @Inject
    public ItemResource(ItemDao itemDao) {
        this.itemDao = itemDao;
    }

    @GET
    public Response findAll() {
        return Response.ok(itemDao.findAll()).build();
    }

    @Path("/{id}")
    @GET
    public Response findById(@ApiParam(value = "id") @PathParam("id") @NotNull ObjectId id) {
        return Response.ok(itemDao.findById(id)).build();
    }

    @Path("/{id}")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(@ApiParam(value = "id") @PathParam("id") @NotNull ObjectId id,
                           @ApiParam("item") @NotNull Item item) {
        item.setId(id);
        return Response.ok(itemDao.update(item)).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(@ApiParam("item") @NotNull Item item) {
        return Response.ok(itemDao.create(item)).status(Response.Status.CREATED).build();
    }

    @Path("/{id}")
    @DELETE
    public Response deleteById(@ApiParam(value = "id") @PathParam("id") @NotNull ObjectId id) {
        itemDao.deleteById(id);
        return Response.noContent().build();
    }
}

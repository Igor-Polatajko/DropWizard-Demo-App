package com.ihorpolataiko.dropwizarddemo.resource;

import com.ihorpolataiko.dropwizarddemo.dao.ItemDao;
import com.ihorpolataiko.dropwizarddemo.transfer.ItemDto;
import io.dropwizard.jersey.PATCH;
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
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.ihorpolataiko.dropwizarddemo.transfer.ItemDto.fromDto;
import static com.ihorpolataiko.dropwizarddemo.transfer.ItemDto.toDto;

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
        List<ItemDto> transferList = itemDao.findAll().stream()
                .map(ItemDto::toDto)
                .collect(Collectors.toList());
        return Response.ok(transferList).build();
    }

    @Path("/{id}")
    @GET
    public Response findById(@ApiParam(value = "id") @PathParam("id") @NotNull String id) {
        return Response.ok(toDto(itemDao.findById(new ObjectId(id)))).build();
    }

    @Path("/{id}")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(@ApiParam(value = "id") @PathParam("id") @NotNull String id,
                           @ApiParam("item") @NotNull ItemDto item) {
        item.setId(id);
        itemDao.update(fromDto(item));
        return Response.ok().build();
    }

    @Path("/{id}")
    @PATCH
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateFields(@ApiParam(value = "id") @PathParam("id") @NotNull String id,
                                 @ApiParam("item") @NotNull Map<String, Object> fieldsToUpdate) {
        itemDao.updateField(new ObjectId(id), fieldsToUpdate);
        return Response.ok().build();
    }

    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(@ApiParam("item") @NotNull ItemDto item) {
        return Response.ok(itemDao.create(fromDto(item))).status(Response.Status.CREATED).build();
    }


    @Path("/{id}")
    @DELETE
    public Response deleteById(@ApiParam(value = "id") @PathParam("id") @NotNull String id) {
        itemDao.deleteById(new ObjectId(id));
        return Response.noContent().build();
    }
}

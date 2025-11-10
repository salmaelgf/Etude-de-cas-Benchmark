package com.example.resources;

import com.example.dao.ItemDAO;
import com.example.dto.ItemDTO;
import com.example.entities.Item;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.stream.Collectors;

@Path("/items")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ItemResource {

    private final ItemDAO itemDAO = new ItemDAO();

    // GET all items (paginated)
    @GET
    public List<ItemDTO> getItems(@QueryParam("page") @DefaultValue("1") int page,
                                  @QueryParam("size") @DefaultValue("50") int size) {
        List<Item> items = itemDAO.findAll(page, size);
        return items.stream().map(this::toDTO).collect(Collectors.toList());
    }

    // GET item by ID
    @GET
    @Path("{id}")
    public ItemDTO getItemById(@PathParam("id") Long id) {
        Item item = itemDAO.findById(id);
        return toDTO(item);
    }

    // GET items by category
    @GET
    @Path("/byCategory/{categoryId}")
    public List<ItemDTO> getItemsByCategory(@PathParam("categoryId") Long categoryId,
                                            @QueryParam("page") @DefaultValue("1") int page,
                                            @QueryParam("size") @DefaultValue("50") int size) {
        List<Item> items = itemDAO.findByCategoryId(categoryId, page, size);
        return items.stream().map(this::toDTO).collect(Collectors.toList());
    }

    // CREATE item
    @POST
    public Response createItem(Item item) {
        itemDAO.save(item);
        return Response.status(Response.Status.CREATED).entity(toDTO(item)).build();
    }

    // UPDATE item
    @PUT
    @Path("{id}")
    public Response updateItem(@PathParam("id") Long id, Item item) {
        item.setId(id);
        itemDAO.update(item);
        return Response.ok(toDTO(item)).build();
    }

    // DELETE item
    @DELETE
    @Path("{id}")
    public Response deleteItem(@PathParam("id") Long id) {
        itemDAO.delete(id);
        return Response.noContent().build();
    }

    // Convert Item entity to ItemDTO
    private ItemDTO toDTO(Item item) {
        ItemDTO dto = new ItemDTO();
        dto.setId(item.getId());
        dto.setSku(item.getSku());
        dto.setName(item.getName());
        dto.setPrice(item.getPrice());
        dto.setStock(item.getStock());
        if (item.getCategory() != null) {
            dto.setCategoryId(item.getCategory().getId());
        }
        return dto;
    }
}

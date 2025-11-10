package com.example.resources;

import com.example.dao.CategoryDAO;
import com.example.dto.CategoryDTO;
import com.example.dto.ItemDTO;
import com.example.entities.Category;
import com.example.entities.Item;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.stream.Collectors;

@Path("/categories")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CategoryResource {

    private final CategoryDAO dao = new CategoryDAO();

    @GET
    public List<CategoryDTO> list(@QueryParam("page") @DefaultValue("1") int page,
                                  @QueryParam("size") @DefaultValue("50") int size) {
        List<Category> categories = dao.findAll(page, size);
        return categories.stream().map(this::toDTO).collect(Collectors.toList());
    }

    @GET
    @Path("{id}")
    public CategoryDTO get(@PathParam("id") Long id) {
        Category category = dao.findByIdWithItems(id);
        return toDTO(category);
    }

    @POST
    public Response create(Category category) {
        dao.save(category);
        return Response.status(Response.Status.CREATED).entity(toDTO(category)).build();
    }

    @PUT
    @Path("{id}")
    public Response update(@PathParam("id") Long id, Category category) {
        category.setId(id);
        dao.update(category);
        return Response.ok(toDTO(category)).build();
    }

    @DELETE
    @Path("{id}")
    public Response delete(@PathParam("id") Long id) {
        dao.delete(id);
        return Response.noContent().build();
    }

    private CategoryDTO toDTO(Category c) {
        CategoryDTO dto = new CategoryDTO();
        dto.setId(c.getId());
        dto.setCode(c.getCode());
        dto.setName(c.getName());
        if (c.getItems() != null) {
            List<ItemDTO> items = c.getItems().stream().map(this::itemToDTO).collect(Collectors.toList());
            dto.setItems(items);
        }
        return dto;
    }

    private ItemDTO itemToDTO(Item i) {
        ItemDTO dto = new ItemDTO();
        dto.setId(i.getId());
        dto.setSku(i.getSku());
        dto.setName(i.getName());
        dto.setPrice(i.getPrice());
        dto.setStock(i.getStock());
        dto.setCategoryId(i.getCategory().getId());
        return dto;
    }
}

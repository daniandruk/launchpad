package org.daniandruk.launchpad.rest;

import java.awt.Color;
import org.daniandruk.launchpad.model.Item;
import java.util.List;
import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import org.daniandruk.launchpad.image.ImageUtils;
import org.daniandruk.launchpad.service.RedditService;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Component;

@Component
@Path("/media")
public class Endpoint {

    private final org.slf4j.Logger logger = LoggerFactory.getLogger(RedditService.class);

    RedditService redditService;
    ImageUtils imageUtils;

    @Inject
    public Endpoint(RedditService redditService, ImageUtils imageUtils) {
        this.redditService = redditService;
        this.imageUtils = imageUtils;
    }

    @GET
    @Path("/search")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Item> search(@NotNull @QueryParam("subject") String name) {
        logger.trace("Search: " + name);
        List<Item> items = redditService.getItems(name, 3);
        return items;
    }

    @GET
    @Path("/rgb")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Item> rgb(@NotNull @QueryParam("subject") String name,
            @NotNull @QueryParam("red") int red, @NotNull @QueryParam("green") int green, @NotNull @QueryParam("blue") int blue) {
        logger.trace("RGB: " + name + " - (" + red + ", " + green + ", " + blue + ")");
        Color color = new Color(red, green, blue);
        List<Item> items = redditService.getItems(name, 100);
        List<Item> result = imageUtils.sortByDistance(items, color, 3);
        return result;
    }

}

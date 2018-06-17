package org.daniandruk.launchpad;

import java.util.List;
import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.springframework.stereotype.Component;

@Component
@Path("/media")
public class Endpoint {

    @Inject
    RedditService redditService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Item> message(@NotNull @QueryParam("subject") String name) {
        System.out.println(name);
        List<Item> items = redditService.getItems(name, 3);
        return items;
    }

}

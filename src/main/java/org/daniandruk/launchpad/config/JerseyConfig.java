package org.daniandruk.launchpad.config;

import javax.ws.rs.ApplicationPath;
import org.daniandruk.launchpad.rest.Endpoint;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

@Component
@ApplicationPath("/rest")
public class JerseyConfig extends ResourceConfig {

    public JerseyConfig() {
        register(Endpoint.class);
    }

}

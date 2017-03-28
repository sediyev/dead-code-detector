package com.aurea;

import com.aurea.controller.DeadCodeDetector;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;

/**
 * Created by sediy on 3/28/2017.
 */
@Configuration
public class JerseyConfig extends ResourceConfig{
    public JerseyConfig(){
        register(DeadCodeDetector.class);
    }
}

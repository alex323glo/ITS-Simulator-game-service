package org.alex323glo.its_simulator.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

/**
 * Main REST Controller.
 *
 * @author Alexey_O
 * @version 0.1
 */
@RestController
public class MainController {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(MainController.class);

    private final Environment environment;

    @Autowired
    public MainController(Environment environment) {
        this.environment = environment;
    }

    @GetMapping("/")
    public ResponseEntity<Void> indexPage() {
        LOGGER.info("Serving '/' (index page) endpoint...");
        String indexPageURLString = environment.getProperty("front-part.index-page.url");
        LOGGER.info("'/' (index page) request will be served with '" + indexPageURLString + "' static resource");

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(indexPageURLString));
        return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
    }

}

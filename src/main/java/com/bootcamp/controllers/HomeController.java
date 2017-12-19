/**
 * Created by darextossa on 5/4/17.
 */
package com.bootcamp.controllers;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Bello
 */
@RestController
@CrossOrigin(origins = "*")
public class HomeController {

    /**
     * The web service home page
     *
     * @return
     */
    @RequestMapping("/")
    public String index() {
        return "index";
    }
}

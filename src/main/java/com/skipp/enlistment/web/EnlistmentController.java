package com.skipp.enlistment.web;

import com.skipp.enlistment.domain.Enlistment;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

// TODO What stereotype annotation should be put here?
@RequestMapping("/enlist")
public class EnlistmentController {

    // TODO What bean should be wired here?

    // TODO What @XXXMapping annotation should be put here?
    // TODO What's the annotation to use to define the status code returned when a handler is successful?
    // TODO What's the appropriate status code for this handler?
    // Hint: What does the test expect?
    // TODO This should only be accessed by students. Apply the appropriate annotation.
    public Enlistment enlist(@RequestBody Enlistment enlistment, Authentication auth) {
        // TODO implement this handler
        // Hint: 'auth' is where you can get the username of the user accessing the API
        return null;
    }

    // TODO What @XXXMapping annotation should be put here?
    public void cancel(@RequestBody Enlistment enlistment) {
        // TODO implement this handler
    }

}

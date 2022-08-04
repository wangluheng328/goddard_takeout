package com.wes.goddard.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * Global exception handling
 */

// Use with @ExceptionHandler, so that @ExceptionHandler can manage exception globally instead of in this controller only
// Specifies which controllers to intercept
@ControllerAdvice(annotations = {RestController.class, Controller.class})
// When using @RequestMapping, its return value usually defines the redirect url
// However, when using @ResponseBody, the returned value is directly written into the HTTP response as JSON
@ResponseBody
//The above two annotations can be combined to a single annotation @RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * Exception Handling Method
     *
     * To give user a message stating the reason, when they enter a duplicated username when adding employees
     *
     * @return
     */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> exceptionHandler(SQLIntegrityConstraintViolationException ex){
        log.error(ex.getMessage());

        if(ex.getMessage().contains("Duplicate entry")){
            String[] split = ex.getMessage().split(" ");
            String msg = split[2] + "already exists";
            return R.error(msg);
        }

        return R.error("Unknown error");
    }

    // Specific for the CustomException class, so that frontend will show the msgs such as:
    // "Cannot Remove: There are dishes in this category"
    @ExceptionHandler(CustomException.class)
    public R<String> exceptionHandler(CustomException ex){
        log.error(ex.getMessage());
        return R.error(ex.getMessage());
    }
}

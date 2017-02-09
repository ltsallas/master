package wifi4eu.wifi4eu.web.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(description = "TestResource")
@RequestMapping("/")
@ResponseStatus(HttpStatus.OK)
public class TestResource3 {

    @ApiOperation(value = "Test resource for Swagger implementation")
    @RequestMapping(value = "/test3",
            method = RequestMethod.GET)
    public String getSomething() {
        return "hello eUI";
    }

}
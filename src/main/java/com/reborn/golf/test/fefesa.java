package com.reborn.golf.test;

import com.reborn.golf.service.IamportManager;
import com.siot.IamportRestClient.exception.IamportResponseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@Log4j2
@RequiredArgsConstructor
public class fefesa {
    private final IamportManager iamportManager;

    @PostMapping(value = "/payment/success",produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    Test suc(@RequestBody Test test){
        try {
            iamportManager.verification(test.getImpUid());
        } catch (IamportResponseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.info(test);
        return test;
    }
}

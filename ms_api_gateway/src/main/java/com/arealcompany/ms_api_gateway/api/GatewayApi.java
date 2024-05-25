package com.arealcompany.ms_api_gateway.api;

import com.arealcompany.ms_api_gateway.business.GatewayController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class GatewayApi {


    private final com.arealcompany.ms_api_gateway.business.GatewayController controller;

    public GatewayApi(GatewayController GatewayController) {
        this.controller = GatewayController;
    }

    @GetMapping("/{service}")
    public String forwardRequest(@PathVariable String service,
                                 @RequestParam Map<String,String> params){
        return controller.forwardRequest(service, "", params);
    }

    @GetMapping("/{service}/{endpoint}")
    public String forwardRequest(@PathVariable String service,
                                 @PathVariable String endpoint,
                                 @RequestParam Map<String,String> params){
        return controller.forwardRequest(service, endpoint, params);
    }
}

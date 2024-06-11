package com.arealcompany.ms_api_gateway.api;

import com.arealcompany.ms_api_gateway.business.GatewayController;
import com.arealcompany.ms_common.utils.Response;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class GatewayApi {

    private final com.arealcompany.ms_api_gateway.business.GatewayController controller;

    public GatewayApi(GatewayController GatewayController) {
        this.controller = GatewayController;
    }

    @GetMapping("/{service}/get/{endpoint}")
    public String forwardRequest(@PathVariable String service,
                                 @PathVariable String endpoint,
                                 @RequestParam Map<String,String> params){
        return controller.forwardRequest(service,"get", endpoint, params);
    }

    @PostMapping("/{service}/update/{endpoint}")
    public String forwardRequestPost(@PathVariable String service,
                                     @PathVariable String endpoint,
                                     @RequestParam Map<String,String> params,
                                     @RequestBody String body){
        return controller.forwardRequest(service, "update", endpoint, params,body);
    }

    @PostMapping("/{service}/update")
    public String forwardRequestPost(@PathVariable String service,
                                     @RequestParam Map<String,String> params,
                                     @RequestBody String body){
        return controller.forwardRequest(service, "update", "", params, body);
    }

    @GetMapping("/auth")
    public String auth() {
        return Response.get(true);
    }
}

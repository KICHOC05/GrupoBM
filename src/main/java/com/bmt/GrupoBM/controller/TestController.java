package com.bmt.GrupoBM.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
public class TestController {

    @GetMapping("/test-redirect")
    @ResponseBody
    public Map<String, String> testRedirect() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "El controlador funciona");
        response.put("status", "ok");
        return response;
    }
}
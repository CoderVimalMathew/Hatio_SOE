package com.example.demo.controller;

import com.example.demo.model.ConversionRequest;
import com.example.demo.service.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class CurrencyController {

    @Autowired
    private CurrencyService currencyService;

    @GetMapping("/rates")
    public Map<String, Double> getRates(@RequestParam(defaultValue = "USD") String base) {
        return currencyService.fetchRates(base);
    }

    @PostMapping("/convert")
    public Map<String, Object> convertCurrency(@RequestBody ConversionRequest request) {
        Map<String, Double> rates = currencyService.fetchRates(request.getFrom());
        double convertedAmount = currencyService.convertCurrency(request, rates);

        // Use LinkedHashMap to maintain order
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("from", request.getFrom());
        response.put("to", request.getTo());
        response.put("amount", request.getAmount());
        response.put("convertedAmount", convertedAmount);

        return response;
    }
}


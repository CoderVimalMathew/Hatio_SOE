package com.example.demo.controller;

import com.example.demo.model.ConversionRequest;
import com.example.demo.service.CurrencyService;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
This module is used to set up the api endpoint paths.
*/
@RestController
@RequestMapping("/api")
public class CurrencyController {

  @Autowired
    private CurrencyService currencyService;
  
  /**
  This class is used to get the rates, base and then fetch the appropriate records.
  */
  @GetMapping("/rates")
    public Map<String, Double> getRates(@RequestParam(defaultValue = "USD") String base) {
    return currencyService.fetchRates(base);
  }
  
  /**
  This class is used to initialize convertion of the currencies and display the output.
  */
  @PostMapping("/convert")
    public Map<String, Object> convertCurrency(@RequestBody ConversionRequest request) {
    Map<String, Double> rates = currencyService.fetchRates(request.getFrom());
    double convertedAmount = currencyService.convertCurrency(request, rates);

    Map<String, Object> response = new LinkedHashMap<>();
    response.put("from", request.getFrom());
    response.put("to", request.getTo());
    response.put("amount", request.getAmount());
    response.put("convertedAmount", convertedAmount);

    return response;
  }
}

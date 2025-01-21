package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

import com.example.demo.model.ConversionRequest;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

/**
 * Test class for CurrencyService functionality.
 */
@ExtendWith(MockitoExtension.class)
class CurrencyServiceTest {

  @Mock
  private RestTemplate restTemplate;

  @InjectMocks
  private CurrencyService currencyService;
    
  private static final String API_URL = "https://api.exchangeratesapi.io/v1/latest";
  private static final String API_KEY = "271c1e75ddfa6ef5328806ada803f394";
    
  /**
   * Sets up test environment before each test.
   * Configures API URL and key using reflection.
   */
  @BeforeEach
  void setUp() {
    ReflectionTestUtils.setField(currencyService, "apiUrl", API_URL);
    ReflectionTestUtils.setField(currencyService, "apiKey", API_KEY);
  }

  /**
   * Tests the fetchRates method.
   * Verifies correct rate retrieval for EUR base currency.
   */
  @Test
  void testFetchRates() {
    Map<String, Object> rates = new HashMap<>();
    rates.put("USD", 1.035985);  
    rates.put("EUR", 1.0);     

    Map<String, Object> apiResponse = new HashMap<>();
    apiResponse.put("success", true);
    apiResponse.put("base", "EUR");
    apiResponse.put("rates", rates);

    lenient().when(restTemplate.exchange(
      eq(API_URL + "?access_key=" + API_KEY),
      eq(HttpMethod.GET),
      eq(null),
      any(ParameterizedTypeReference.class)
    )).thenReturn(ResponseEntity.ok(apiResponse));

    Map<String, Double> result = currencyService.fetchRates("EUR");
        
    assertNotNull(result);
    assertEquals(1.035985, result.get("USD"), 0.0001); 
  }
  
  /**
   * Tests the convertCurrency method.
   * Verifies USD to EUR conversion with 100.0 amount.
   */
  @Test
  void testConvertCurrency() {
    ConversionRequest request = new ConversionRequest();
    request.setFrom("USD");
    request.setTo("EUR");
    request.setAmount(100.0);

    Map<String, Double> rates = new HashMap<>();
    rates.put("USD", 1.035985); 
    rates.put("EUR", 1.0);    

    double result = currencyService.convertCurrency(request, rates);
    assertEquals(96.53, result, 0.01);  
  }
}

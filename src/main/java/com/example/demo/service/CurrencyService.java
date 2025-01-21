package com.example.demo.service;

import com.example.demo.model.ConversionRequest;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * Service class responsible for managing currency-related operations.
 * Provides methods to convert, validate, and manipulate currency data.
 */
@Service
public class CurrencyService {

  @Value("${currency.api.url}")
  private String apiUrl;

  @Value("${currency.api.key}")
  private String apiKey;

  /**
   * Fetches exchange rates from an external API.
   * Handles API unavailability and invalid responses.
   */
  public Map<String, Double> fetchRates(String baseCurrency) {
    RestTemplate restTemplate = new RestTemplate();
    String url = apiUrl + "?access_key=" + apiKey;

    try {
      ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
          url,
          HttpMethod.GET,
          null,
          new ParameterizedTypeReference<Map<String, Object>>() {}
      );

      Map<String, Object> responseBody = response.getBody();
      if (responseBody == null || !responseBody.containsKey("rates")) {
        throw new RuntimeException("Failed to fetch exchange rates: Response body is invalid.");
      }

      @SuppressWarnings("unchecked")
      Map<String, Object> ratesObj = (Map<String, Object>) responseBody.get("rates");

      TreeMap<String, Double> rates = new TreeMap<>(ratesObj.entrySet().stream()
          .collect(Collectors.toMap(
              Map.Entry::getKey,
              e -> formatDouble(convertToDouble(e.getValue()), 6)
          )));

      if (!baseCurrency.equals("EUR")) {
        Double baseRate = rates.get(baseCurrency);
        if (baseRate == null) {
          throw new RuntimeException("Invalid base currency: " + baseCurrency);
        }

        return rates.entrySet().stream()
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                e -> formatDouble(e.getValue() / baseRate, 6),
                (v1, v2) -> v1,
                TreeMap::new
            ));
      }

      return rates;

    } catch (HttpClientErrorException e) {
      throw new RuntimeException("External API returned an error: " + e.getMessage(), e);
    } catch (RestClientException e) {
      throw new RuntimeException("Failed to connect to the external API. ", e);
    }
  }

  /**
   * Converts a currency value from one currency to another.
   * Handles invalid currency codes gracefully.
   */
  public double convertCurrency(ConversionRequest request, Map<String, Double> rates) {
    if (!rates.containsKey(request.getTo()) || !rates.containsKey(request.getFrom())) {
      throw new RuntimeException("Invalid currency code(s) provided!");
    }

    double amountInEur = request.getAmount() / rates.get(request.getFrom());
    double result = amountInEur * rates.get(request.getTo());
    return formatDouble(result, 2);
  }

  /**
   * Converts an Object to a Double.
   */
  private double convertToDouble(Object value) {
    if (value instanceof Integer) {
      return ((Integer) value).doubleValue();
    } else if (value instanceof Double) {
      return (Double) value;
    } else {
      throw new IllegalArgumentException("Unsupported rate value type: " + value.getClass());
    }
  }

  /**
   * Formats a double value to a specified number of decimal places.
   */
  public double formatDouble(double value, int decimalPlaces) {
    BigDecimal bd = BigDecimal.valueOf(value);
    bd = bd.setScale(decimalPlaces, RoundingMode.HALF_UP);
    return bd.doubleValue();
  }
}


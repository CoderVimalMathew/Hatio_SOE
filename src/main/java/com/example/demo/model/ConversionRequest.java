package com.example.demo.model;

/**
This module is used to get and set 
the required attributes for a conversion.
*/
public class ConversionRequest {
  private String from;
  private String to;
  private double amount;

  public String getFrom() {
    return from;
  }

  public void setFrom(String from) {
    this.from = from;
  }

  public String getTo() {
    return to;
  }

  public void setTo(String to) {
    this.to = to;
  }

  public double getAmount() {
    return amount;
  }

  public void setAmount(double amount) {
    this.amount = amount;
  }
}

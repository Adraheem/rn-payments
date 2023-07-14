package com.rnpayments.gpay.dtos;

import java.util.ArrayList;

public class InitGpayDto {
  private ArrayList<Object> allowedCardAuthMethods;
  private ArrayList<Object> allowedCardNetworks;
  private boolean requireBillingAddress;
  private int walletEnvironment;
  private String gateway;
  private String gatewayMerchantId;
  private String merchantName;

  public ArrayList<Object> getAllowedCardAuthMethods() {
    return allowedCardAuthMethods;
  }

  public void setAllowedCardAuthMethods(ArrayList<Object> allowedCardAuthMethods) {
    this.allowedCardAuthMethods = allowedCardAuthMethods;
  }

  public ArrayList<Object> getAllowedCardNetworks() {
    return allowedCardNetworks;
  }

  public void setAllowedCardNetworks(ArrayList<Object> allowedCardNetworks) {
    this.allowedCardNetworks = allowedCardNetworks;
  }

  public boolean isRequireBillingAddress() {
    return requireBillingAddress;
  }

  public void setRequireBillingAddress(boolean requireBillingAddress) {
    this.requireBillingAddress = requireBillingAddress;
  }

  public int getWalletEnvironment() {
    return walletEnvironment;
  }

  public void setWalletEnvironment(int walletEnvironment) {
    this.walletEnvironment = walletEnvironment;
  }

  public String getGateway() {
    return gateway;
  }

  public void setGateway(String gateway) {
    this.gateway = gateway;
  }

  public String getGatewayMerchantId() {
    return gatewayMerchantId;
  }

  public void setGatewayMerchantId(String gatewayMerchantId) {
    this.gatewayMerchantId = gatewayMerchantId;
  }

  public String getMerchantName() {
    return merchantName;
  }

  public void setMerchantName(String merchantName) {
    this.merchantName = merchantName;
  }
}

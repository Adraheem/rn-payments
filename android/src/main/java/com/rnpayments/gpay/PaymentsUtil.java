package com.rnpayments.gpay;

import android.content.Context;

import com.google.android.gms.wallet.PaymentsClient;
import com.google.android.gms.wallet.Wallet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

public class PaymentsUtil {

  private static JSONArray allowedCardNetworks;
  private static JSONArray allowedCardAuthMethods;
  private static boolean requireBillingAddress;
  private static String gateway;
  private static String gatewayMerchantId;
  private static String merchantName;

  public static void setGateway(String gateway) {
    PaymentsUtil.gateway = gateway;
  }

  public static void setGatewayMerchantId(String gatewayMerchantId) {
    PaymentsUtil.gatewayMerchantId = gatewayMerchantId;
  }

  public static void setMerchantName(String merchantName) {
    PaymentsUtil.merchantName = merchantName;
  }

  public static void setAllowedCardNetworks(ArrayList<Object> allowedCardNetworks) {
    PaymentsUtil.allowedCardNetworks = new JSONArray(allowedCardNetworks);
  }

  public static void setAllowedCardAuthMethods(ArrayList<Object> allowedCardAuthMethods) {
    PaymentsUtil.allowedCardAuthMethods = new JSONArray(allowedCardAuthMethods);
  }

  public static void setRequireBillingAddress(boolean requireBillingAddress) {
    PaymentsUtil.requireBillingAddress = requireBillingAddress;
  }

  private static JSONObject getGatewayTokenizationSpecification() throws JSONException {
    return new JSONObject() {{
      put("type", "PAYMENT_GATEWAY");
      put("parameters", new JSONObject() {{
        put("gateway", PaymentsUtil.gateway);
        put("gatewayMerchantId", PaymentsUtil.gatewayMerchantId);
      }});
    }};
  }

  private static JSONObject getMerchantInfo() throws JSONException {
    return new JSONObject().put("merchantName", PaymentsUtil.merchantName);
  }

  private static JSONObject getCardPaymentMethod() throws JSONException {
    JSONObject cardPaymentMethod = getBaseCardPaymentMethod();
    cardPaymentMethod.put("tokenizationSpecification", getGatewayTokenizationSpecification());

    return cardPaymentMethod;
  }

  private static JSONObject getBaseCardPaymentMethod() throws JSONException {
    JSONObject cardPaymentMethod = new JSONObject();
    cardPaymentMethod.put("type", "CARD");

    JSONObject parameters = new JSONObject();
    parameters.put("allowedAuthMethods", allowedCardAuthMethods);
    parameters.put("allowedCardNetworks", allowedCardNetworks);
    // Optionally, you can add billing address/phone number associated with a CARD payment method.
    parameters.put("billingAddressRequired", requireBillingAddress);

    if (requireBillingAddress) {
      JSONObject billingAddressParameters = new JSONObject();
      billingAddressParameters.put("format", "FULL");

      parameters.put("billingAddressParameters", billingAddressParameters);
    }

    cardPaymentMethod.put("parameters", parameters);

    return cardPaymentMethod;
  }

  public static PaymentsClient createPaymentsClient(Context context, int environment) {
    Wallet.WalletOptions walletOptions =
      new Wallet.WalletOptions.Builder().setEnvironment(environment).build();
    return Wallet.getPaymentsClient(context, walletOptions);
  }

  private static JSONObject getBaseRequest() throws JSONException {
    return new JSONObject().put("apiVersion", 2).put("apiVersionMinor", 0);
  }

  public static JSONObject getIsReadyToPayRequest() {
    try {
      JSONObject isReadyToPayRequest = getBaseRequest();
      isReadyToPayRequest.put(
        "allowedPaymentMethods", new JSONArray().put(getBaseCardPaymentMethod()));

      return isReadyToPayRequest;

    } catch (JSONException e) {
      return null;
    }
  }

  private static JSONObject getTransactionInfo(String price, String countryCode, String currencyCode) throws JSONException {
    JSONObject transactionInfo = new JSONObject();
    transactionInfo.put("totalPrice", price);
    transactionInfo.put("totalPriceStatus", "FINAL");
    transactionInfo.put("countryCode", countryCode);
    transactionInfo.put("currencyCode", currencyCode);
    transactionInfo.put("checkoutOption", "COMPLETE_IMMEDIATE_PURCHASE");

    return transactionInfo;
  }

  public static JSONObject getPaymentDataRequest(JSONObject transactionInfo, JSONObject shippingInfo) {

    try {
      JSONObject paymentDataRequest = PaymentsUtil.getBaseRequest();
      paymentDataRequest.put(
        "allowedPaymentMethods", new JSONArray().put(PaymentsUtil.getCardPaymentMethod()));
      paymentDataRequest.put("transactionInfo", transactionInfo);
      paymentDataRequest.put("merchantInfo", PaymentsUtil.getMerchantInfo());

      /* An optional shipping address requirement is a top-level property of the PaymentDataRequest
      JSON object. */
      if (shippingInfo != null) {
        paymentDataRequest.put("shippingAddressRequired", true);
        paymentDataRequest.put("shippingAddressParameters", shippingInfo);
      }
      return paymentDataRequest;

    } catch (JSONException e) {
      return null;
    }
  }

  public static String amountToString(long amount) {
    return new BigDecimal(amount)
      .divide(new BigDecimal(100), RoundingMode.HALF_EVEN)
      .setScale(2, RoundingMode.HALF_EVEN)
      .toString();
  }

}

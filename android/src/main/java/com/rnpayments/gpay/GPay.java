package com.rnpayments.gpay;

import android.app.Activity;
import android.util.Log;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReadableMap;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.wallet.IsReadyToPayRequest;
import com.google.android.gms.wallet.PaymentData;
import com.google.android.gms.wallet.PaymentDataRequest;
import com.google.android.gms.wallet.PaymentsClient;
import com.rnpayments.gpay.dtos.InitGpayDto;

import org.json.JSONException;
import org.json.JSONObject;

public class GPay {

  private static final String TAG = "ReactNative";

  private final Activity currentActivity;
  private final PaymentsClient paymentsClient;
  private boolean canUseGooglePay;

  public GPay(Activity currentActivity, InitGpayDto gpayDto) {
    /*
     *
     * */
    this.currentActivity = currentActivity;
    PaymentsUtil.setAllowedCardAuthMethods(gpayDto.getAllowedCardAuthMethods());
    PaymentsUtil.setAllowedCardNetworks(gpayDto.getAllowedCardNetworks());
    PaymentsUtil.setRequireBillingAddress(gpayDto.isRequireBillingAddress());
    PaymentsUtil.setGateway(gpayDto.getGateway());
    PaymentsUtil.setGatewayMerchantId(gpayDto.getGatewayMerchantId());
    PaymentsUtil.setMerchantName(gpayDto.getMerchantName());
    this.paymentsClient = PaymentsUtil.createPaymentsClient(currentActivity, gpayDto.getWalletEnvironment());

    this.fetchCanUseGooglePay();
  }

  public Activity getCurrentActivity() {
    return currentActivity;
  }

  public boolean isGPayAvailable() {
    return this.canUseGooglePay;
  }

  public void requestPayment(ReadableMap paymentRequest, final Promise promise) throws JSONException {
    final Task<PaymentData> task = this.getLoadPaymentDataTask(paymentRequest);

    assert task != null;
    task.addOnCompleteListener(completedTask -> {
      if (completedTask.isSuccessful()) {
        promise.resolve(completedTask.getResult());
      } else {
        promise.reject(TAG, "Failed to complete payment");
      }
    });
  }

  private void fetchCanUseGooglePay() {
    final JSONObject isReadyToPayJson = PaymentsUtil.getIsReadyToPayRequest();
    if (isReadyToPayJson == null) {
      this.canUseGooglePay = false;
      return;
    }

    IsReadyToPayRequest request = IsReadyToPayRequest.fromJson(isReadyToPayJson.toString());
    Task<Boolean> task = paymentsClient.isReadyToPay(request);
    task.addOnCompleteListener(
      completedTask -> {
        if (completedTask.isSuccessful()) {
          this.canUseGooglePay = completedTask.getResult();
        } else {
          Log.w("isReadyToPay failed", completedTask.getException());
          this.canUseGooglePay = false;
        }
      });
  }

  private Task<PaymentData> getLoadPaymentDataTask(ReadableMap map) throws JSONException {
    JSONObject transactionInfo = new JSONObject();
    transactionInfo.put("totalPrice", map.getString("totalPrice"));
    transactionInfo.put("totalPriceStatus", map.getString("totalPriceStatus"));
    transactionInfo.put("countryCode", map.getString("countryCode"));
    transactionInfo.put("currencyCode", map.getString("currencyCode"));
    transactionInfo.put("checkoutOption", "COMPLETE_IMMEDIATE_PURCHASE");

    JSONObject paymentDataRequestJson = PaymentsUtil.getPaymentDataRequest(transactionInfo, null);
    if (paymentDataRequestJson == null) {
      return null;
    }

    PaymentDataRequest request =
      PaymentDataRequest.fromJson(paymentDataRequestJson.toString());
    return paymentsClient.loadPaymentData(request);
  }

}

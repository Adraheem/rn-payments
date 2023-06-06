package com.rnpayments.gpay;

import android.app.Activity;
import android.util.Log;

import com.facebook.react.bridge.Promise;
import com.google.android.gms.wallet.AutoResolveHelper;
import com.google.android.gms.wallet.PaymentDataRequest;
import com.google.android.gms.wallet.PaymentsClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Optional;

public class GPay {

  // Arbitrarily-picked constant integer you define to track a request for payment data activity.
  private static final int LOAD_PAYMENT_DATA_REQUEST_CODE = 991;

  // A client for interacting with the Google Pay API.
  private PaymentsClient paymentsClient;

  public void requestPayment(Promise promise, Activity activity) {

    // The price provided to the API should include taxes and shipping.
    // This price is not displayed to the user.
    try {
//      final Activity activity = getCurrentActivity();
      if (activity == null) {
        Log.w("ReactNative", "[GooglePay] activity is null");
        promise.reject("NO_ACTIVITY", "activity is null");
        return;
      }
      // replace 10000 with price
      Optional<JSONObject> paymentDataRequestJson = PaymentsUtil.getPaymentDataRequest(10000);
      if (!paymentDataRequestJson.isPresent()) {
        return;
      }

      Log.d("ReactNative", paymentDataRequestJson.get().toString());

      PaymentDataRequest request =
        PaymentDataRequest.fromJson(paymentDataRequestJson.get().toString());

      // Since loadPaymentData may show the UI asking the user to select a payment method, we use
      // AutoResolveHelper to wait for the user interacting with it. Once completed,
      // onActivityResult will be called with the result.
      if (request != null) {
        AutoResolveHelper.resolveTask(
          paymentsClient.loadPaymentData(request),
          activity, LOAD_PAYMENT_DATA_REQUEST_CODE);
      }

    } catch (Exception e) {
      throw new RuntimeException("The price cannot be deserialized from the JSON object.");
    }
  }
}

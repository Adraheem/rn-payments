package com.rnpayments.webpay;

import static com.rnpayments.RnPaymentsModule.NAME;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReadableMap;
import com.interswitchng.iswmobilesdk.IswMobileSdk;
import com.interswitchng.iswmobilesdk.shared.models.core.Environment;
import com.interswitchng.iswmobilesdk.shared.models.core.IswPaymentInfo;
import com.interswitchng.iswmobilesdk.shared.models.core.IswPaymentResult;
import com.interswitchng.iswmobilesdk.shared.models.core.IswSdkConfig;

import java.util.Objects;

public class InterSwitchWebpay implements IswMobileSdk.IswPaymentCallback {
  @Override
  public void onPaymentCompleted(@NonNull IswPaymentResult iswPaymentResult) {
    Log.d(NAME, "Successful: "+iswPaymentResult);
  }

  @Override
  public void onUserCancel() {
    Log.e(NAME, "Error occurred");
  }

  public void configureSDK(ReadableMap configMap, Application application) {
    Log.d(NAME, "Attempt to configure Webpay SDK");

    String clientId = configMap.getString("clientId");
    String merchantCode = configMap.getString("merchantCode");
    String clientSecret = configMap.getString("clientSecret");
    String currencyCode = configMap.getString("currencyCode");
    int environment = configMap.getInt("environment");

    // create sdk configuration
    IswSdkConfig config = new IswSdkConfig(clientId,
      clientSecret, merchantCode, currencyCode);

    // uncomment to set environment, default is Environment.TEST
    Environment env;
    switch (environment){
      case 1: env= Environment.SANDBOX;
      case 2: env= Environment.PRODUCTION;
      default: env = Environment.TEST;
    }
    config.setEnv(env);

    // initialize sdk at boot of application
    IswMobileSdk.initialize(application, config);
    Log.d(NAME, "Webpay initialization completed");
  }

  public void initiatePayment(Promise promise) {
    Log.d(NAME, "Webpay: Initiating payment");
    // set customer info
    String customerId = "1234",
      customerName = "John Doe",
      customerEmail = "adraheemzy@gmail.com",
      customerMobile = "2348185692069",
      // generate a unique random
      // reference for each transaction
      reference = "jshwoeurueioewhduhsksdjsdi";

    // amount in kobo e.g. "N500.00" -> 50000
    int amount = 50000; // e.g. 50000

    // create payment info
    IswPaymentInfo iswPaymentInfo = new IswPaymentInfo(
      customerId,
      customerName,
      customerEmail,
      customerMobile,
      reference,
      amount
    );

    // trigger payment
    // parameters
    // -- paymentInfo: the payment information to be processed
    // -- activityCallback: the IswPaymentCallback that receives the result
    IswMobileSdk.getInstance().pay(
      iswPaymentInfo,
      this
    );
    Log.d(NAME, "Webpay: Payment process completed");
    promise.resolve(true);
  }
}

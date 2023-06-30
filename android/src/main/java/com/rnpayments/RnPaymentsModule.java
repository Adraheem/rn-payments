package com.rnpayments;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.module.annotations.ReactModule;
import com.google.android.gms.wallet.WalletConstants;
import com.rnpayments.gpay.GPay;

import java.util.HashMap;
import java.util.Map;

@ReactModule(name = RnPaymentsModule.NAME)
public class RnPaymentsModule extends ReactContextBaseJavaModule {
  public static final String NAME = "RnPayments";
  private static final String ENVIRONMENT_PRODUCTION_KEY = "ENVIRONMENT_PRODUCTION";
  private static final String ENVIRONMENT_TEST_KEY = "ENVIRONMENT_TEST";

  private final GPay gpay;

  public RnPaymentsModule(ReactApplicationContext reactContext) {
    super(reactContext);
    this.gpay = new GPay(this.getCurrentActivity());
  }

  @Override
  @NonNull
  public String getName() {
    return NAME;
  }

  @Override
  public Map<String, Object> getConstants() {
    final Map<String, Object> constants = new HashMap<>();
    constants.put(ENVIRONMENT_PRODUCTION_KEY, WalletConstants.ENVIRONMENT_PRODUCTION);
    constants.put(ENVIRONMENT_TEST_KEY, WalletConstants.ENVIRONMENT_TEST);
    return constants;
  }


  // Example method
  // See https://reactnative.dev/docs/native-modules-android
  @ReactMethod
  public void multiply(double a, double b, Promise promise) {
    promise.resolve(a * b * b);
  }

  @ReactMethod
  public void makePayment(Promise promise) {

  }

  @ReactMethod
  public void setEnvironment(int environment) {
    gpay.setEnvironment(environment);
  }

  @ReactMethod
  public void isReadyToPay(ReadableArray allowedCardNetworks, ReadableArray allowedCardAuthMethods, final Promise promise) {
    gpay.isReadyToPay(allowedCardNetworks, allowedCardAuthMethods, promise);
  }

  @ReactMethod
  public void requestPayment(ReadableMap requestData, final Promise promise) {
    gpay.requestPayment(requestData, promise);
  }
}

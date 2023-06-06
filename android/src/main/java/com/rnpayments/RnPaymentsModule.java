package com.rnpayments;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.module.annotations.ReactModule;
import com.rnpayments.gpay.GPay;

@ReactModule(name = RnPaymentsModule.NAME)
public class RnPaymentsModule extends ReactContextBaseJavaModule {
  public static final String NAME = "RnPayments";

  public RnPaymentsModule(ReactApplicationContext reactContext) {
    super(reactContext);
  }

  @Override
  @NonNull
  public String getName() {
    return NAME;
  }


  // Example method
  // See https://reactnative.dev/docs/native-modules-android
  @ReactMethod
  public void multiply(double a, double b, Promise promise) {
    promise.resolve(a * b * b);
  }

  @ReactMethod
  public void makePayment(Promise promise){
    GPay gpay = new GPay();
    gpay.requestPayment(promise, this.getCurrentActivity());
  }
}

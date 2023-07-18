package com.rnpayments;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.module.annotations.ReactModule;
import com.google.android.gms.wallet.WalletConstants;
import com.rnpayments.gpay.GPay;
import com.rnpayments.gpay.dtos.InitGpayDto;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@ReactModule(name = RnPaymentsModule.NAME)
public class RnPaymentsModule extends ReactContextBaseJavaModule {
  public static final String NAME = "RnPayments";
  private static final String ENVIRONMENT_PRODUCTION_KEY = "ENVIRONMENT_PRODUCTION";
  private static final String ENVIRONMENT_TEST_KEY = "ENVIRONMENT_TEST";

  private GPay gpay;

  public RnPaymentsModule(ReactApplicationContext reactContext) {
    super(reactContext);
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

  @ReactMethod
  public void init(ReadableMap initData) {
    InitGpayDto gpayDto = new InitGpayDto();
    gpayDto.setAllowedCardAuthMethods(Objects.requireNonNull(initData.getArray("allowedCardAuthMethods")).toArrayList());
    gpayDto.setAllowedCardNetworks(Objects.requireNonNull(initData.getArray("allowedCardNetworks")).toArrayList());
    gpayDto.setRequireBillingAddress(initData.getBoolean("requireBillingAddress"));
    gpayDto.setWalletEnvironment(initData.getInt("environment")); // Prod -> 1, Test -> 3
    gpayDto.setGateway(initData.getString("gateway"));
    gpayDto.setGatewayMerchantId(initData.getString("gatewayMerchantId"));
    gpayDto.setMerchantName(initData.getString("merchantName"));

    this.gpay = new GPay(this.getReactApplicationContext(), gpayDto);
  }

  @ReactMethod
  public boolean isGPayAvailable() {
    return gpay.isGPayAvailable();
  }

  @ReactMethod
  public void pay(ReadableMap paymentRequest, final Promise promise) throws JSONException {
    gpay.requestPayment(paymentRequest, promise);
  }

}

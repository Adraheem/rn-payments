import { NativeModules, Platform } from 'react-native';

export type AllowedCardNetworkType =
  | 'AMEX'
  | 'DISCOVER'
  | 'JCB'
  | 'MASTERCARD'
  | 'VISA';

export type AllowedCardAuthMethodsType = 'PAN_ONLY' | 'CRYPTOGRAM_3DS';

export type tokenizationSpecificationType = 'PAYMENT_GATEWAY' | 'DIRECT';

export enum Environment {
  "PRODUCTION" = 1,
  "TEST" = 3
}

export interface InitRNPayments {
  allowedCardAuthMethods: AllowedCardAuthMethodsType[],
  allowedCardNetworks: AllowedCardNetworkType[],
  requireBillingAddress: boolean,
  environment: Environment,
  gateway: string,
  gatewayMerchantId: string,
  merchantName: string,
}

export interface PaymentRequest {
  totalPrice: string,
  totalPriceStatus: "FINAL",
  countryCode: string,
  currencyCode: string,
}

export interface RequestDataType {
  cardPaymentMethod: {
    tokenizationSpecification: {
      type: tokenizationSpecificationType;
      gateway?: string;
      gatewayMerchantId?: string;
    };
    allowedCardNetworks: AllowedCardNetworkType[];
    allowedCardAuthMethods: AllowedCardAuthMethodsType[];
  };
  transaction: {
    totalPrice: string;
    totalPriceStatus: string;
    currencyCode: string;
  };
  merchantName: string;
}

const LINKING_ERROR =
  `The package 'rn-payments' doesn't seem to be linked. Make sure: \n\n` +
  Platform.select({ ios: "- You have run 'pod install'\n", default: '' }) +
  '- You rebuilt the app after installing the package\n' +
  '- You are not using Expo Go\n';

const RNPayments = NativeModules.RnPayments
  ? NativeModules.RnPayments
  : new Proxy(
      {},
      {
        get() {
          throw new Error(LINKING_ERROR);
        },
      }
    );

export function init(data: InitRNPayments){
  RNPayments.init(data);
}

export function isAvailable(): boolean {
  return RNPayments.isGPayAvailable();
}

export function pay(paymentRequest: PaymentRequest): Promise<any>{
  return RNPayments.pay(paymentRequest);
}

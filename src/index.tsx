import { NativeModules, Platform } from 'react-native';

export type AllowedCardNetworkType =
  | 'AMEX'
  | 'DISCOVER'
  | 'JCB'
  | 'MASTERCARD'
  | 'VISA';

export type AllowedCardAuthMethodsType = 'PAN_ONLY' | 'CRYPTOGRAM_3DS';

export type tokenizationSpecificationType = 'PAYMENT_GATEWAY' | 'DIRECT';

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

export function multiply(a: number, b: number): Promise<number> {
  return RNPayments.multiply(a, b);
}

export function makePayment(): Promise<any> {
  return RNPayments.makePayment();
}

export function setEnvironment(environment: number) {
  RNPayments.setEnvironment(environment);
}

export function isReadyToPay(
  allowedCardNetworks: AllowedCardNetworkType[],
  allowedCardAuthMethods: AllowedCardAuthMethodsType[]
): Promise<boolean> {
  return RNPayments.isReadyToPay(allowedCardNetworks, allowedCardAuthMethods);
}

export function requestPayment(requestData: RequestDataType): Promise<string> {
  return RNPayments.requestPayment(requestData);
}

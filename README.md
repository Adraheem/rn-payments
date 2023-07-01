# rn-payments

React native payments

## Installation

```sh
npm install rn-payments
```

## Usage

```js
import {
  AllowedCardNetworkType,
  AllowedCardAuthMethodsType,
  RequestDataType,
  setEnvironment,
  isReadyToPay,
  requestPayment,
} from 'rn-payments';

```

Put this in your App.tsx to set environment for fgoogle pay

```js
if (Platform.OS === 'android') {
  setEnvironment(0); // 0 - Test; 1 - Production
}
```

Set google pay payment data
```js
const allowedCardNetworks: AllowedCardNetworkType[] = ['VISA', 'MASTERCARD'];
const allowedCardAuthMethods: AllowedCardAuthMethodsType[] = [
  'PAN_ONLY',
  'CRYPTOGRAM_3DS',
];

const gatewayRequestData: RequestDataType = {
  cardPaymentMethod: {
    tokenizationSpecification: {
      type: 'PAYMENT_GATEWAY',
      gateway: 'example',
      gatewayMerchantId: 'exampleGatewayMerchantId',
    },
    allowedCardNetworks,
    allowedCardAuthMethods,
  },
  transaction: {
    totalPrice: '123',
    totalPriceStatus: 'FINAL',
    currencyCode: 'RUB',
  },
  merchantName: 'Example Merchant',
};

const payWithGooglePay = (requestData: RequestDataType) => {
  // Check if Google Pay is available
  isReadyToPay(allowedCardNetworks, allowedCardAuthMethods).then((ready) => {
    if (ready) {
      // Request payment token
      requestPayment(requestData).then(handleSuccess).catch(handleError);
    } else {
      Alert.alert('GPay not ready!');
    }
  });
};

const handleSuccess = (token: string) => {
  // Send a token to your payment gateway
  Alert.alert('Success', `token: ${token}`);
};

const handleError = (error: any) =>
  Alert.alert('Error', `${error.code}\n${error.message}`);

```

## Contributing

See the [contributing guide](CONTRIBUTING.md) to learn how to contribute to the repository and the development workflow.

## License

MIT

---

Made with [create-react-native-library](https://github.com/callstack/react-native-builder-bob)

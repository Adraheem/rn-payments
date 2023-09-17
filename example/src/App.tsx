import env from '../environment';
import * as React from 'react';

import { StyleSheet, View, Text, Button, Platform, Alert } from 'react-native';
import {
  AllowedCardAuthMethodsType,
  AllowedCardNetworkType,
  Environment,
  PaymentRequest,
  WebpayEnvironment,
  configureWebpay,
  init,
  initiateWebpayPayment,
  isAvailable,
  pay,
} from 'rn-payments';

const allowedCardNetworks: AllowedCardNetworkType[] = ['VISA', 'MASTERCARD'];
const allowedCardAuthMethods: AllowedCardAuthMethodsType[] = [
  'PAN_ONLY',
  'CRYPTOGRAM_3DS',
];

const paymentRequest: PaymentRequest = {
  totalPrice: '10000',
  totalPriceStatus: 'FINAL',
  countryCode: 'US',
  currencyCode: 'USD',
};

// const gatewayRequestData: RequestDataType = {
//   cardPaymentMethod: {
//     tokenizationSpecification: {
//       type: 'PAYMENT_GATEWAY',
//       gateway: 'example',
//       gatewayMerchantId: 'exampleGatewayMerchantId',
//     },
//     allowedCardNetworks,
//     allowedCardAuthMethods,
//   },
//   transaction: {
//     totalPrice: '123',
//     totalPriceStatus: 'FINAL',
//     currencyCode: 'NGN',
//     countryCode: "NG"
//   },
//   shippingAddressParameters: {
//     phoneNumberRequired: true,
//     allowedCountryCodes: ["NG"]
//   },
//   merchantName: 'Example Merchant',
// };

export default function App() {
  const [result, setResult] = React.useState(false);

  React.useEffect(() => {
    configureWebpay({
      clientId: env.clientId,
      merchantCode: env.merchantCode,
      clientSecret: env.clientSecret,
      currencyCode: '556',
      environment: WebpayEnvironment.SANDBOX,
    });
    if (Platform.OS === 'android') {
      init({
        allowedCardAuthMethods,
        allowedCardNetworks,
        environment: Environment.TEST,
        gateway: '',
        gatewayMerchantId: '',
        merchantName: '',
        requireBillingAddress: false,
      });

      setTimeout(() => {
        setResult(isAvailable());
      }, 10000);
    }
  }, []);

  const payWithGooglePay = (requestData: PaymentRequest) => {
    pay(requestData)
      .then((res) => {
        handleSuccess(res);
      })
      .catch((err) => {
        handleError(err);
      });
  };

  const handleSuccess = (token: string) => {
    // Send a token to your payment gateway
    console.log(token);
    Alert.alert('Success', `token: {token}`);
  };

  const handleError = (error: any) => {
    Alert.alert('Error', 'error');
    console.log(error);
  };

  const payWithWebpay = () => {
    initiateWebpayPayment({
      amount: 5000000,
      customerId: '00000001',
      customerName: 'Demo customer',
      customerEmail: 'hello@gmail.com',
      customerMobile: '08066734672',
      reference: 'dwmbfw3y87riudsgyf84793fuhi',
    });
  };

  return (
    <View style={styles.container}>
      <Text>Result: {result ? 'Available' : 'NOT AVAILABLE'}</Text>
      <Button
        onPress={() => payWithGooglePay(paymentRequest)}
        title="Make payment"
      />
      <Button onPress={payWithWebpay} title="Make payment using webpay" />
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
  box: {
    width: 60,
    height: 60,
    marginVertical: 20,
  },
});

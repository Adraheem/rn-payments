import * as React from 'react';

import { StyleSheet, View, Text, Button, Platform, Alert } from 'react-native';
import {
  multiply,
  // makePayment,
  AllowedCardNetworkType,
  AllowedCardAuthMethodsType,
  RequestDataType,
  setEnvironment,
  isReadyToPay,
  requestPayment,
} from 'rn-payments';

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

export default function App() {
  const [result, setResult] = React.useState<number | undefined>();

  React.useEffect(() => {
    multiply(10, 7).then(setResult);
    if (Platform.OS === 'android') {
      setEnvironment(0); // Test
    }
  }, []);

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

  return (
    <View style={styles.container}>
      <Text>Result: {result}</Text>
      <Button
        onPress={() => payWithGooglePay(gatewayRequestData)}
        title="Make payment"
      />
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

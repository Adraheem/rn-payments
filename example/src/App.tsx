import * as React from 'react';

import { StyleSheet, View, Text, Button } from 'react-native';
import { multiply, makePayment } from 'rn-payments';

export default function App() {
  const [result, setResult] = React.useState<number | undefined>();

  React.useEffect(() => {
    multiply(10, 7).then(setResult);
  }, []);

  return (
    <View style={styles.container}>
      <Text>Result: {result}</Text>
      <Button
        onPress={async () => {
          makePayment()
            .then(() => console.log('success'))
            .catch((e) => console.log(e));
        }}
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

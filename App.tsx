import React, {useState} from 'react';
import {
  SafeAreaView,
  View,
  Button,
  NativeModules,
  findNodeHandle,
  UIManager,
  Platform,
  Dimensions,
} from 'react-native';

import BitmovinPlayer from './src/RNBitmovinPlayer';
import RNBitmovinPlayer from './src/RNFCBitmovinPlayer';

const App = () => {
  const [isFullscreen, setIsFullscreen] = useState(false);
  const BitmovinPlayerModule = NativeModules.RNBitmovinPlayer;
  console.log(
    'screen: ',
    Dimensions.get('screen'),
    'window: ',
    Dimensions.get('window'),
  );
  const config = {
    source: {url: 'https://bitdash-a.akamaihd.net/content/sintel/sintel.mpd'},
  };

  return (
    <SafeAreaView
      style={{
        flex: 0.5,
        width: '100%',
      }}>
      <View style={{flex: 1, height: '100%', maxHeight: 600}}>
        <RNBitmovinPlayer
          style={{flex: 1, height: 500, maxHeight: 600}}
          configuration={config}
        />
      </View>
    </SafeAreaView>
  );
};

export default App;

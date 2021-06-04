import React, {useState} from 'react';
import {SafeAreaView, View, Button, NativeModules} from 'react-native';

import BitmovinPlayer from './src/RNBitmovinPlayer';

const App = () => {
  const [isFullscreen, setIsFullscreen] = useState(false);
  const BitmovinPlayerModule = NativeModules.RNBitmovinPlayer;
  console.log(NativeModules);
  const config = {
    source: {url: 'https://bitdash-a.akamaihd.net/content/sintel/sintel.mpd'},
    style: {fullscreenIcon: true},
  };
  return (
    <SafeAreaView style={{flex: 0.5, width: '100%'}}>
      <View style={{flex: 1}}>
        <BitmovinPlayer style={{flex: 1}} configuration={config} />
      </View>
    </SafeAreaView>
  );
};

export default App;

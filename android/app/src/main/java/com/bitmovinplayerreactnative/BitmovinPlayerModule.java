package com.bitmovinplayerreactnative;

import android.content.Intent;

import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactMethod;

public class BitmovinPlayerModule extends ReactContextBaseJavaModule {
    public BitmovinPlayerModule(ReactApplicationContext reactContext) {
      super(reactContext);
    }

    @Override
    public String getName() {
      return "BitmovinPlayerModule";
    }

    @ReactMethod
    public void startPlayer() {
        Intent i = new Intent(getReactApplicationContext(), BitmovinPlayerActivity.class);

        getReactApplicationContext().startActivity(i);
    }
}



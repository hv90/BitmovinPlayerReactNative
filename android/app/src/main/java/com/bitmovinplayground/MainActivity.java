package com.bitmovinplayground;

import android.app.Activity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;

import com.bitmovin.player.PlayerView;

import com.facebook.react.ReactActivity;

public class MainActivity extends ReactActivity {
  public Activity _activity;
  public View _decorView;
  public PlayerView _playerView;
  public Toolbar _toolbar;

  /**
   * Returns the name of the main component registered from JavaScript. This is
   * used to schedule rendering of the component.
   */
  @Override
  protected String getMainComponentName() {
    return "bitmovinPlayground";
  }

  /*
   * public MainActivity(Activity activity, PlayerView playerView, Toolbar
   * toolbar) { this._activity = activity; this._playerView = playerView;
   * this._toolbar = toolbar; this._decorView =
   * activity.getWindow().getDecorView();
   * RNBitmovinPlayerModule.getThePlayerView(this._playerView); }
   * 
   * public Activity getTheActivity() { return this._activity; }
   */

}

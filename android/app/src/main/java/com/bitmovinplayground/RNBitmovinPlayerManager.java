package com.bitmovinplayground;

import android.view.View;

import com.bitmovin.player.api.event.Event;
import com.bitmovin.player.api.event.PlayerEvent;
import com.bitmovin.player.api.event.PlayerEvent.Error;
import com.bitmovin.player.api.event.PlayerEvent.FullscreenEnter;
import com.bitmovin.player.api.event.PlayerEvent.FullscreenExit;
import com.bitmovin.player.api.event.PlayerEvent.Muted;
import com.bitmovin.player.api.event.PlayerEvent.Paused;
import com.bitmovin.player.api.event.PlayerEvent.Play;
import com.bitmovin.player.api.event.PlayerEvent.PlaybackFinished;
import com.bitmovin.player.api.event.PlayerEvent.Ready;
import com.bitmovin.player.api.event.PlayerEvent.RenderFirstFrame;
import com.bitmovin.player.api.event.PlayerEvent.Seek;
import com.bitmovin.player.api.event.PlayerEvent.Seeked;
import com.bitmovin.player.api.event.PlayerEvent.StallEnded;
import com.bitmovin.player.api.event.PlayerEvent.StallStarted;
import com.bitmovin.player.api.event.PlayerEvent.TimeChanged;
import com.bitmovin.player.api.event.PlayerEvent.Unmuted;
import com.bitmovin.player.api.event.EventListener;
import com.bitmovin.player.api.source.SourceConfig;
import com.bitmovin.player.api.Player;
import com.bitmovin.player.api.PlayerConfig;
import com.bitmovin.player.api.ui.FullscreenHandler;
import com.bitmovin.player.ui.FullscreenUtil;
import com.bitmovin.player.api.ui.StyleConfig;
import com.bitmovin.player.PlayerView;

import com.bitmovinplayground.R;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.facebook.react.uimanager.events.RCTEventEmitter;

import java.util.Map;

import org.json.JSONObject;
import org.json.JSONException;

public class RNBitmovinPlayerManager extends SimpleViewManager<PlayerView>
        implements FullscreenHandler, LifecycleEventListener {

    public static final String REACT_CLASS = "RNBitmovinPlayer";

    private PlayerView _playerView;
    private Player _player;
    private View _decorView;
    private boolean isFullscreen;
    private ThemedReactContext _reactContext;
    private ReactApplicationContext mCallerContext;

    @Override
    public String getName() {
        return REACT_CLASS;
    }

    public RNBitmovinPlayerManager(ReactApplicationContext reactContext) {
        mCallerContext = reactContext;
        // _decorView = reactContext.getCurrentActivity().getWindow().getDecorView();
    }

    public Map getExportedCustomBubblingEventTypeConstants() {
        return MapBuilder.builder()
                .put("onReady", MapBuilder.of("phasedRegistrationNames", MapBuilder.of("bubbled", "onReady")))
                .put("onPlay", MapBuilder.of("phasedRegistrationNames", MapBuilder.of("bubbled", "onPlay")))
                .put("onPaused", MapBuilder.of("phasedRegistrationNames", MapBuilder.of("bubbled", "onPaused")))
                .put("onTimeChanged",
                        MapBuilder.of("phasedRegistrationNames", MapBuilder.of("bubbled", "onTimeChanged")))
                .put("onStallStarted",
                        MapBuilder.of("phasedRegistrationNames", MapBuilder.of("bubbled", "onStallStarted")))
                .put("onStallEnded", MapBuilder.of("phasedRegistrationNames", MapBuilder.of("bubbled", "onStallEnded")))
                .put("onPlaybackFinished",
                        MapBuilder.of("phasedRegistrationNames", MapBuilder.of("bubbled", "onPlaybackFinished")))
                .put("onRenderFirstFrame",
                        MapBuilder.of("phasedRegistrationNames", MapBuilder.of("bubbled", "onRenderFirstFrame")))
                .put("onError", MapBuilder.of("phasedRegistrationNames", MapBuilder.of("bubbled", "_onPlayerError")))
                .put("onMuted", MapBuilder.of("phasedRegistrationNames", MapBuilder.of("bubbled", "onMuted")))
                .put("onUnmuted", MapBuilder.of("phasedRegistrationNames", MapBuilder.of("bubbled", "onUnmuted")))
                .put("onSeek", MapBuilder.of("phasedRegistrationNames", MapBuilder.of("bubbled", "onSeek")))
                .put("onSeeked", MapBuilder.of("phasedRegistrationNames", MapBuilder.of("bubbled", "onSeeked")))
                .put("onFullscreenEnter",
                        MapBuilder.of("phasedRegistrationNames", MapBuilder.of("bubbled", "onFullscreenEnter")))
                .put("_onFullscreenExit",
                        MapBuilder.of("phasedRegistrationNames", MapBuilder.of("bubbled", "_onFullscreenExit")))
                .build();
    }

    @Override
    public PlayerView createViewInstance(ThemedReactContext context) {
        _reactContext = context;
        _playerView = new PlayerView(context);
        isFullscreen = false;
        _decorView = _reactContext.getCurrentActivity().getWindow().getDecorView();
        // .findViewById(R.id.bitmovinPlayerView);

        _player = _playerView.getPlayer();
        setListeners();

        return _playerView;
    }

    @Override
    public void onDropViewInstance(PlayerView view) {
        _playerView.onDestroy();

        super.onDropViewInstance(view);

        _player = null;
        _playerView = null;
    }

    @ReactProp(name = "configuration")
    public void setConfiguration(PlayerView view, ReadableMap config) {
        SourceConfig _srcConfig;
        PlayerConfig _playerConfig = new PlayerConfig();
        StyleConfig _styleConfig = new StyleConfig();
        JSONObject _json = new JSONObject();

        ReadableMap sourceMap = null;
        ReadableMap posterMap = null;
        ReadableMap styleMap = null;

        if (config.hasKey("source")) {
            sourceMap = config.getMap("source");
        }

        if (sourceMap != null && sourceMap.getString("url") != null) {
            _srcConfig = SourceConfig.fromUrl(sourceMap.getString("url"));
            // configuration.setSourceItem(sourceMap.getString("url"));

            /*
             * if (sourceMap.getString("title") != null) {
             * configuration.getSourceItem().setTitle(sourceMap.getString("title")); }
             * 
             * if (config.hasKey("poster")) { posterMap = config.getMap("poster"); }
             * 
             * if (posterMap != null && posterMap.getString("url") != null) { boolean
             * persistent = false;
             * 
             * if (posterMap.hasKey("persistent")) { persistent =
             * posterMap.getBoolean("persistent"); }
             * 
             * configuration.getSourceItem().setPosterImage(posterMap.getString("url"),
             * persistent); }
             */
            if (config.hasKey("style")) {
                styleMap = config.getMap("style");
            }

            if (styleMap != null) {
                if (styleMap.hasKey("uiEnabled") && !styleMap.getBoolean("uiEnabled")) {
                    // configuration.getStyleConfiguration().setUiEnabled(false);
                    _styleConfig.setUiEnabled(false);
                }

                if (styleMap.hasKey("uiCss") && styleMap.getString("uiCss") != null) {
                    // configuration.getStyleConfiguration().setPlayerUiCss(styleMap.getString("uiCss"));
                    _styleConfig.setPlayerUiCss(styleMap.getString("uiCss"));
                }

                if (styleMap.hasKey("supplementalUiCss") && styleMap.getString("supplementalUiCss") != null) {
                    // configuration.getStyleConfiguration().setSupplementalPlayerUiCss(styleMap.getString("supplementalUiCss"));
                    _styleConfig.setSupplementalPlayerUiCss(styleMap.getString("supplementalUiCss"));
                }

                if (styleMap.hasKey("uiJs") && styleMap.getString("uiJs") != null) {
                    // configuration.getStyleConfiguration().setPlayerUiJs(styleMap.getString("uiJs"));
                    _styleConfig.setPlayerUiJs(styleMap.getString("uiJs"));
                }

                if (styleMap.hasKey("fullscreenIcon") && styleMap.getBoolean("fullscreenIcon")) {
                    _playerView.setFullscreenHandler(this);
                }

                /*
                 * _playerConfig.setStyleConfig(_styleConfig); _player =
                 * Player.create(_reactContext, _playerConfig); _player.load(_srcConfig);
                 */
            }

            _player.load(_srcConfig);
            // setListeners();
        }
    }

    @Override
    public boolean isFullscreen() {
        return isFullscreen;
    }

    @Override
    public void onResume() {
    }

    @Override
    public void onPause() {
    }

    @Override
    public void onDestroy() {
    }

    @Override
    public void onFullscreenRequested() {
        isFullscreen = true;

        // _decorView = _reactContext.getCurrentActivity().getWindow().getDecorView();

        _decorView.post(() -> {
            int uiParams = FullscreenUtil.getSystemUiVisibilityFlags(isFullscreen, true);
            _decorView.setSystemUiVisibility(uiParams);

        });

        // ((PlayerView) _decorView).enterFullscreen();
    }

    @Override
    public void onFullscreenExitRequested() {
        isFullscreen = false;

        _decorView.post(() -> {
            int uiParams = FullscreenUtil.getSystemUiVisibilityFlags(isFullscreen, true);
            _decorView.setSystemUiVisibility(uiParams);

        });

        // ((PlayerView) _decorView).exitFullscreen();
    }

    @Override
    public void onHostResume() {
        _playerView.onResume();
    }

    @Override
    public void onHostPause() {
        _playerView.onPause();
    }

    @Override
    public void onHostDestroy() {
        _playerView.onDestroy();
    }

    private void setListeners() {
        _player.<Ready>on(Ready.class, new EventListener<Ready>() {
            @Override
            public void onEvent(Ready event) {
                WritableMap map = Arguments.createMap();

                _reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(_playerView.getId(), "onReady", map);
            }
        });

        _player.<Play>on(Play.class, new EventListener<Play>() {
            @Override
            public void onEvent(Play event) {
                WritableMap map = Arguments.createMap();

                map.putDouble("time", event.getTime());

                _reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(_playerView.getId(), "onPlay", map);
            }
        });

        _player.<Paused>on(Paused.class, new EventListener<Paused>() {

            @Override
            public void onEvent(Paused event) {
                WritableMap map = Arguments.createMap();

                map.putDouble("time", event.getTime());

                _reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(_playerView.getId(), "onPaused", map);
            }
        });

        _player.<TimeChanged>on(TimeChanged.class, new EventListener<TimeChanged>() {

            @Override
            public void onEvent(TimeChanged event) {
                WritableMap map = Arguments.createMap();

                map.putDouble("time", event.getTime());

                _reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(_playerView.getId(), "onTimeChanged",
                        map);
            }
        });

        _player.<StallStarted>on(StallStarted.class, new EventListener<StallStarted>() {

            @Override
            public void onEvent(StallStarted event) {
                WritableMap map = Arguments.createMap();

                _reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(_playerView.getId(), "onStallStarted",
                        map);
            }
        });

        _player.<StallEnded>on(StallEnded.class, new EventListener<StallEnded>() {

            @Override
            public void onEvent(StallEnded event) {
                WritableMap map = Arguments.createMap();

                _reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(_playerView.getId(), "onStallEnded", map);
            }
        });

        _player.<PlaybackFinished>on(PlaybackFinished.class, new EventListener<PlaybackFinished>() {
            @Override
            public void onEvent(PlaybackFinished event) {
                WritableMap map = Arguments.createMap();

                _reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(_playerView.getId(), "onPlaybackFinished",
                        map);
            }
        });

        _player.<RenderFirstFrame>on(RenderFirstFrame.class, new EventListener<RenderFirstFrame>() {

            @Override
            public void onEvent(RenderFirstFrame event) {
                WritableMap map = Arguments.createMap();

                _reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(_playerView.getId(), "onRenderFirstFrame",
                        map);
            }
        });

        _player.<Error>on(Error.class, new EventListener<Error>() {

            @Override
            public void onEvent(Error event) {
                WritableMap map = Arguments.createMap();
                WritableMap errorMap = Arguments.createMap();

                errorMap.putInt("code", event.getCode().getValue());
                errorMap.putString("message", event.getMessage());

                map.putMap("error", errorMap);

                _reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(_playerView.getId(), "onError", map);
            }
        });

        _player.<Muted>on(Muted.class, new EventListener<Muted>() {

            @Override
            public void onEvent(Muted event) {
                WritableMap map = Arguments.createMap();

                _reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(_playerView.getId(), "onMuted", map);
            }
        });

        _player.<Unmuted>on(Unmuted.class, new EventListener<Unmuted>() {

            @Override
            public void onEvent(Unmuted event) {
                WritableMap map = Arguments.createMap();

                _reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(_playerView.getId(), "onUnmuted", map);
            }
        });

        _player.<Seek>on(Seek.class, new EventListener<Seek>() {

            @Override
            public void onEvent(Seek event) {
                WritableMap map = Arguments.createMap();

                map.putDouble("seekTarget", event.getTo().getTime());
                map.putDouble("position", event.getFrom().getTime());

                _reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(_playerView.getId(), "onSeek", map);
            }
        });

        _player.<Seeked>on(Seeked.class, new EventListener<Seeked>() {

            @Override
            public void onEvent(Seeked event) {
                WritableMap map = Arguments.createMap();

                _reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(_playerView.getId(), "onSeeked", map);
            }
        });

        _player.<FullscreenEnter>on(FullscreenEnter.class, new EventListener<FullscreenEnter>() {

            @Override
            public void onEvent(FullscreenEnter event) {
                WritableMap map = Arguments.createMap();

                _reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(_playerView.getId(), "onFullscreenEnter",
                        map);
            }
        });

        _player.<FullscreenExit>on(FullscreenExit.class, new EventListener<FullscreenExit>() {

            @Override
            public void onEvent(FullscreenExit event) {
                WritableMap map = Arguments.createMap();

                _reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(_playerView.getId(), "onFullscreenExit",
                        map);
            }
        });
    }
}
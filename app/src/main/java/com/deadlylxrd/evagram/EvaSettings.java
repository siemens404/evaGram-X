package com.deadlylxrd.evagram;

import android.content.SharedPreferences;
import android.os.SystemClock;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.drinkmore.Tracer;
import org.thunderdog.challegram.Log;
import org.thunderdog.challegram.tool.UI;
import org.thunderdog.challegram.unsorted.Settings;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.concurrent.atomic.AtomicBoolean;

import me.vkryl.core.reference.ReferenceList;
import me.vkryl.leveldb.LevelDB;

public class EvaSettings {

  // Version
  private static final int VERSION_1 = 1;
  private static final int VERSION = VERSION_1;

  private static final AtomicBoolean hasInstance = new AtomicBoolean(false);

  private static final String KEY_VERSION = "version";

  // eva features
  //
  // General
  public static final String KEY_SHOW_CHATID = "show_chatid"; // chat id
  //
  // Appearance
  public static final String KEY_HIDE_NUMBER = "hide_number";
  public static final String KEY_ENABLE_COMMENTS = "enable_comments";
  //
  // Chats
  public static final String KEY_DISABLE_CAMERA_BUTTON = "disable_camera_button";
  public static final String KEY_DISABLE_RECORD_BUTTON = "disable_record_button";
  public static final String KEY_REMEMBER_SEND_OPTIONS = "remember_send_options";
  public static final String KEY_SEND_AS_COPY = "send_as_copy";
  public static final String KEY_SEND_REMOVE_CAPTIONS = "send_remove_captions";
  public static final String KEY_SEND_WITHOUT_SOUND = "send_without_sound";
  //
  // Drawer
  public static final String KEY_DRAWER_CONTACTS = "drawer_contacts";
  public static final String KEY_DRAWER_CALLS = "drawer_calls";
  public static final String KEY_DRAWER_FAVOURITE = "drawer_favourite";
  public static final String KEY_DRAWER_INVITE_FRIENDS = "drawer_invite_friends";
  public static final String KEY_DRAWER_HELP = "drawer_help";
  public static final String KEY_DRAWER_NIGHTMODE = "drawer_nightmode";

  // evaSettings vars
  private static volatile EvaSettings instance;
  private final LevelDB config;

  private EvaSettings () {
    File configDir = new File(UI.getAppContext().getFilesDir(), "evaconfig");
    if (!configDir.exists() && !configDir.mkdir()) {
      throw new IllegalStateException("Unable to create working directory");
    }
    long ms = SystemClock.uptimeMillis();
    config = new LevelDB(new File(configDir, "db").getPath(), true, new LevelDB.ErrorHandler() {
      @Override public boolean onFatalError (LevelDB levelDB, Throwable error) {
        Tracer.onDatabaseError(error);
        return true;
      }

      @Override public void onError (LevelDB levelDB, String message, @Nullable Throwable error) {
        // Cannot use custom Log, since settings are not yet loaded
        android.util.Log.e(Log.LOG_TAG, message, error);
      }
    });
    int configVersion = 0;
    try {
      configVersion = Math.max(0, config.tryGetInt(KEY_VERSION));
    } catch (FileNotFoundException ignored) {
    }
    if (configVersion > VERSION) {
      Log.e("Downgrading database version: %d -> %d", configVersion, VERSION);
      config.putInt(KEY_VERSION, VERSION);
    }
    for (int version = configVersion + 1; version <= VERSION; version++) {
      SharedPreferences.Editor editor = config.edit();
      upgradeConfig(config, editor, version);
      editor.putInt(KEY_VERSION, version);
      editor.apply();
    }
    Log.i("Opened database in %dms", SystemClock.uptimeMillis() - ms);
  }

  public static EvaSettings instance () {
    if (instance == null) {
      synchronized (EvaSettings.class) {
        if (instance == null) {
          if (hasInstance.getAndSet(true)) throw new AssertionError();
          instance = new EvaSettings();
        }
      }
    }
    return instance;
  }

  public LevelDB edit () {
    return config.edit();
  }

  public void remove (String key) {
    config.remove(key);
  }

  public void putLong (String key, long value) {
    config.putLong(key, value);
  }

  public long getLong (String key, long defValue) {
    return config.getLong(key, defValue);
  }

  public long[] getLongArray (String key) {
    return config.getLongArray(key);
  }

  public void putLongArray (String key, long[] value) {
    config.putLongArray(key, value);
  }

  public void putInt (String key, int value) {
    config.putInt(key, value);
  }

  public int getInt (String key, int defValue) {
    return config.getInt(key, defValue);
  }

  public void putFloat (String key, float value) {
    config.putFloat(key, value);
  }

  public void putBoolean (String key, boolean value) {
    config.putBoolean(key, value);
  }

  public boolean getBoolean (String key, boolean defValue) {
    return config.getBoolean(key, defValue);
  }

  public void putVoid (String key) {
    config.putVoid(key);
  }

  public boolean containsKey (String key) {
    return config.contains(key);
  }

  public void putString (String key, @NonNull String value) {
    config.putString(key, value);
  }

  public String getString (String key, String defValue) {
    return config.getString(key, defValue);
  }

  public void removeByPrefix (String prefix, @Nullable SharedPreferences.Editor editor) {
    config.removeByPrefix(prefix); // editor
  }

  public void removeByAnyPrefix (String[] prefixes, @Nullable SharedPreferences.Editor editor) {
    config.removeByAnyPrefix(prefixes); // , editor
  }

  public LevelDB config () {
    return config;
  }

  private void upgradeConfig (LevelDB config, SharedPreferences.Editor editor, int version) {
    // DO NOTHING
  }

  public interface SettingsChangeListener {
    void onSettingsChanged (String key, Object newSettings, Object oldSettings);
  }

  private ReferenceList<SettingsChangeListener> newSettingsListeners;

  public void addNewSettingsListener (SettingsChangeListener listener) {
    if (newSettingsListeners == null)
      newSettingsListeners = new ReferenceList<>();
    newSettingsListeners.add(listener);
  }

  public void removeNewSettingsListener (SettingsChangeListener listener) {
    if (newSettingsListeners != null) {
      newSettingsListeners.remove(listener);
    }
  }

  private void notifyNewSettingsListeners (String key, Object newSettings, Object oldSettings) {
    if (newSettingsListeners != null) {
      for (SettingsChangeListener listener : newSettingsListeners) {
        listener.onSettingsChanged(key, newSettings, oldSettings);
      }
    }
  }

  // General
  public boolean isChatIdShows () {
    return getBoolean(KEY_SHOW_CHATID, false);
  }

  public void toggleShowChatID () {
    notifyNewSettingsListeners(KEY_SHOW_CHATID, !isChatIdShows(), isChatIdShows());
    putBoolean(KEY_SHOW_CHATID, !isChatIdShows());
  }

  // Appearance
  public boolean isCommentsEnabled () {
    return getBoolean(KEY_ENABLE_COMMENTS, false);
  }

  public void toggleEnableComments () {
    notifyNewSettingsListeners(KEY_ENABLE_COMMENTS, !isCommentsEnabled(), isCommentsEnabled());
    putBoolean(KEY_ENABLE_COMMENTS, !isCommentsEnabled());
  }

  public boolean isNumberHidden () {
    return getBoolean(KEY_HIDE_NUMBER, false);
  }

  public void toggleHideNumber () {
    notifyNewSettingsListeners(KEY_HIDE_NUMBER, !isNumberHidden(), isNumberHidden());
    putBoolean(KEY_HIDE_NUMBER, !isNumberHidden());
  }

  // Chats
  public boolean isDisableCameraButton () {
    return getBoolean(KEY_DISABLE_CAMERA_BUTTON, false);
  }

  public void toggleDisableCameraButton () {
    putBoolean(KEY_DISABLE_CAMERA_BUTTON, !isDisableCameraButton());
  }

  public boolean isDisableRecordButton () {
    return getBoolean(KEY_DISABLE_RECORD_BUTTON, false);
  }

  public void toggleDisableRecordButton () {
    putBoolean(KEY_DISABLE_RECORD_BUTTON, !isDisableRecordButton());
  }

  public boolean isRememberSendOptions () {
    return getBoolean(KEY_REMEMBER_SEND_OPTIONS, false);
  }

  public void toggleRememberSendOptions () {
    putBoolean(KEY_REMEMBER_SEND_OPTIONS, !isRememberSendOptions());
  }

  public boolean isSendAsCopy () {
    return getBoolean(KEY_SEND_AS_COPY, false);
  }

  public void putSendAsCopy (boolean enable) {
    putBoolean(KEY_SEND_AS_COPY, enable);
  }

  public boolean isSendRemoveCaptions () {
    return getBoolean(KEY_SEND_REMOVE_CAPTIONS, false);
  }

  public void putSendRemoveCaptions (boolean enable) {
    putBoolean(KEY_SEND_REMOVE_CAPTIONS, enable);
  }

  public boolean isSendWithoutSound () {
    return getBoolean(KEY_SEND_WITHOUT_SOUND, false);
  }

  public void putSendWithoutSound (boolean enable) {
    putBoolean(KEY_SEND_WITHOUT_SOUND, enable);
  }

  // Drawer
  public boolean isDrawerContactsShow () {
    return getBoolean(KEY_DRAWER_CONTACTS, true);
  }

  public void toggleDrawerContacts () {
    putBoolean(KEY_DRAWER_CONTACTS, !isDrawerContactsShow());
  }

  public boolean isDrawerCallsShow () {
    return getBoolean(KEY_DRAWER_CALLS, true);
  }

  public void toggleDrawerCalls () {
    putBoolean(KEY_DRAWER_CALLS, !isDrawerCallsShow());
  }

  public boolean isDrawerFavouriteShow () {
    return getBoolean(KEY_DRAWER_FAVOURITE, true);
  }

  public void toggleDrawerFavourite () {
    putBoolean(KEY_DRAWER_FAVOURITE, !isDrawerFavouriteShow());
  }

  public boolean isDrawerFriendsShow () {
    return getBoolean(KEY_DRAWER_INVITE_FRIENDS, false);
  }

  public void toggleDrawerInviteFriends () {
    putBoolean(KEY_DRAWER_INVITE_FRIENDS, !isDrawerFriendsShow());
  }

  public boolean isDrawerHelpShow () {
    return getBoolean(KEY_DRAWER_HELP, false);
  }

  public void toggleDrawerHelp () {
    putBoolean(KEY_DRAWER_HELP, !isDrawerHelpShow());
  }

  public boolean isDrawerNightmodeShow () {
    return getBoolean(KEY_DRAWER_NIGHTMODE, true);
  }

  public void toggleDrawerNightMode () {
    putBoolean(KEY_DRAWER_NIGHTMODE, !isDrawerNightmodeShow());
  }
}

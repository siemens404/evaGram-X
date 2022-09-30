package com.deadlylxrd.evagram.utils;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.deadlylxrd.evagram.utils.LifecycleUtils;
import com.deadlylxrd.evagram.utils.ReflectionUtils;

import java.util.Locale;
import java.util.Objects;

public class AndroidUtils {

  // Getting the global context through reflection to use context on application initialization
  @NonNull
  public static Context getGlobalContext() {
    try {
      return ReflectionUtils.invokeStaticMethod("android.app.AppGlobals", "getInitialApplication");
    } catch (Exception e) {
      Log.d("GlobalContext", "Error while fetching context via refl");
    }
    return Objects.requireNonNull(LifecycleUtils.getCurrentActivity());
  }

  // Toast
  public static void sendToast(String text) {
    Toast.makeText(getGlobalContext(), text, Toast.LENGTH_SHORT).show();
  }
}

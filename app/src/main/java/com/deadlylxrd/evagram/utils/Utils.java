package com.deadlylxrd.evagram.utils;

import org.thunderdog.challegram.BuildConfig;
import org.thunderdog.challegram.R;
import org.thunderdog.challegram.core.Lang;

import com.deadlylxrd.evagram.EvaConfig;

public class Utils {

  // Get app version
  public static String getEvaVersion() {
    String msg = Lang.getString(R.string.evaVersionName, EvaConfig.EVA_VERSION);
    return msg;
  }

}

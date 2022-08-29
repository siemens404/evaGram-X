package com.deadlylxrd.evagram.ui;

import android.content.Context;
import android.util.SparseIntArray;
import android.view.View;

import com.deadlylxrd.evagram.EvaSettings;
import com.deadlylxrd.evagram.ui.SettingsGeneralController;
import com.deadlylxrd.evagram.ui.SettingsAppearanceController;
import com.deadlylxrd.evagram.ui.SettingsChatsController;
import com.deadlylxrd.evagram.ui.SettingsExperimentalController;

import org.thunderdog.challegram.R;
import org.thunderdog.challegram.component.base.SettingView;
import org.thunderdog.challegram.core.Lang;
import org.thunderdog.challegram.navigation.ViewController;
import org.thunderdog.challegram.telegram.Tdlib;
import org.thunderdog.challegram.telegram.TdlibUi;
import org.thunderdog.challegram.ui.ListItem;
import org.thunderdog.challegram.ui.RecyclerViewController;
import org.thunderdog.challegram.ui.SettingsAdapter;
import org.thunderdog.challegram.v.CustomRecyclerView;

import java.util.ArrayList;

public class SettingsEvaController extends RecyclerViewController<Void> implements View.OnClickListener, ViewController.SettingsIntDelegate {
  private SettingsAdapter adapter;

  public SettingsEvaController (Context context, Tdlib tdlib) {
    super(context, tdlib);
  }

  @Override public CharSequence getName () {
    return Lang.getString(R.string.EvaSettings);
  }

  @Override public void onClick (View v) {
    int id = v.getId();
    switch (id) {
      case R.id.btn_generalSettings:
        navigateTo(new SettingsGeneralController(context, tdlib));
        break;
      case R.id.btn_appearanceSettings:
        navigateTo(new SettingsAppearanceController(context, tdlib));
        break;
      case R.id.btn_chatsSettings:
        navigateTo(new SettingsChatsController(context, tdlib));
        break;
      case R.id.btn_experimentalSettings:
        navigateTo(new SettingsExperimentalController(context, tdlib));
        break;
      case R.id.btn_tgChannel: {
        tdlib.ui().openUrl(this, "https://t.me/evaGramOfficial", new TdlibUi.UrlOpenParameters().forceInstantView());
        break;
      }
      case R.id.btn_sourceCode: {
        tdlib.ui().openUrl(this, "https://github.com/evaGram/evaGram", new TdlibUi.UrlOpenParameters().forceInstantView());
        break;
      }
      case R.id.btn_supportProject: {
        tdlib.ui().openUrl(this, "https://deadlylxrd.me/donate", new TdlibUi.UrlOpenParameters().forceInstantView());
        break;
      }
    }
  }

  @Override public void onApplySettings (int id, SparseIntArray result) {

  }

  @Override public int getId () {
    return R.id.controller_evaSettings;
  }

  @Override protected void onCreateView (Context context, CustomRecyclerView recyclerView) {
    adapter = new SettingsAdapter(this) {
      @Override protected void setValuedSetting (ListItem item, SettingView view, boolean isUpdate) {
        view.setDrawModifier(item.getDrawModifier());
      }
    };

    ArrayList<ListItem> items = new ArrayList<>();

    items.add(new ListItem(ListItem.TYPE_SETTING, R.id.btn_generalSettings, R.drawable.baseline_widgets_24, R.string.GeneralSettings));
    items.add(new ListItem(ListItem.TYPE_SEPARATOR));
    items.add(new ListItem(ListItem.TYPE_SETTING, R.id.btn_appearanceSettings, R.drawable.baseline_palette_24, R.string.AppearanceSettings));
    items.add(new ListItem(ListItem.TYPE_SEPARATOR));
    items.add(new ListItem(ListItem.TYPE_SETTING, R.id.btn_chatsSettings, R.drawable.baseline_chat_bubble_24, R.string.ChatsSettings));
    items.add(new ListItem(ListItem.TYPE_SEPARATOR));
    items.add(new ListItem(ListItem.TYPE_SETTING, R.id.btn_experimentalSettings, R.drawable.baseline_bug_report_24, R.string.ExperimentalSettings));
    items.add(new ListItem(ListItem.TYPE_SHADOW_BOTTOM));

    items.add(new ListItem(ListItem.TYPE_SHADOW_TOP));
    items.add(new ListItem(ListItem.TYPE_SETTING, R.id.btn_tgChannel, R.drawable.baseline_help_24, R.string.TgChannel));
    items.add(new ListItem(ListItem.TYPE_SEPARATOR));
    items.add(new ListItem(ListItem.TYPE_SETTING, R.id.btn_sourceCode, R.drawable.baseline_github_24, R.string.EvaSourceCode));
    items.add(new ListItem(ListItem.TYPE_SHADOW_BOTTOM));

    items.add(new ListItem(ListItem.TYPE_SHADOW_TOP));
    items.add(new ListItem(ListItem.TYPE_SETTING, R.id.btn_supportProject, R.drawable.baseline_paid_24, R.string.SupportProject));
    items.add(new ListItem(ListItem.TYPE_SHADOW_BOTTOM));

    adapter.setItems(items, true);
    recyclerView.setAdapter(adapter);
  }
}

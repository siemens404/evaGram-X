package com.deadlylxrd.evagram.ui;

import android.content.Context;
import android.content.Intent;
import android.util.SparseIntArray;
import android.view.View;

import com.deadlylxrd.evagram.EvaSettings;
import com.deadlylxrd.evagram.utils.LifecycleUtils;

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

public class CustomDrawerController extends RecyclerViewController<Void> implements View.OnClickListener, ViewController.SettingsIntDelegate {
  private SettingsAdapter adapter;

  public CustomDrawerController (Context context, Tdlib tdlib) {
    super(context, tdlib);
  }

  @Override public CharSequence getName () {
    return Lang.getString(R.string.DrawerSettings);
  }

  @Override public void onBackPressed() {
    LifecycleUtils.restartApp();
  }

  @Override public void onClick (View v) {
    int id = v.getId();
    switch (id) {
      case R.id.btn_drawerContacts:
        EvaSettings.instance().toggleDrawerContacts();
        adapter.updateValuedSettingById(R.id.btn_drawerContacts);
        break;
      case R.id.btn_drawerCalls:
        EvaSettings.instance().toggleDrawerCalls();
        adapter.updateValuedSettingById(R.id.btn_drawerCalls);
        break;
      case R.id.btn_drawerFavourite:
        EvaSettings.instance().toggleDrawerFavourite();
        adapter.updateValuedSettingById(R.id.btn_drawerFavourite);
        break;
      case R.id.btn_drawerInviteFriends:
        EvaSettings.instance().toggleDrawerInviteFriends();
        adapter.updateValuedSettingById(R.id.btn_drawerInviteFriends);
        break;
      case R.id.btn_drawerHelp:
        EvaSettings.instance().toggleDrawerHelp();
        adapter.updateValuedSettingById(R.id.btn_drawerHelp);
        break;
      case R.id.btn_drawerNightMode:
        EvaSettings.instance().toggleDrawerNightMode();
        adapter.updateValuedSettingById(R.id.btn_drawerNightMode);
        break;
    }
  }

  @Override public void onApplySettings (int id, SparseIntArray result) {

  }

  @Override public int getId () {
    return R.id.controller_drawerSettings;
  }

  @Override protected void onCreateView (Context context, CustomRecyclerView recyclerView) {
    adapter = new SettingsAdapter(this) {
      @Override protected void setValuedSetting (ListItem item, SettingView view, boolean isUpdate) {
        view.setDrawModifier(item.getDrawModifier());
        switch (item.getId()) {
          case R.id.btn_drawerContacts:
            view.getToggler().setRadioEnabled(EvaSettings.instance().isDrawerContactsShow(), isUpdate);
            break;
          case R.id.btn_drawerCalls:
            view.getToggler().setRadioEnabled(EvaSettings.instance().isDrawerCallsShow(), isUpdate);
            break;
          case R.id.btn_drawerFavourite:
            view.getToggler().setRadioEnabled(EvaSettings.instance().isDrawerFavouriteShow(), isUpdate);
            break;
          case R.id.btn_drawerInviteFriends:
            view.getToggler().setRadioEnabled(EvaSettings.instance().isDrawerFriendsShow(), isUpdate);
            break;
          case R.id.btn_drawerHelp:
            view.getToggler().setRadioEnabled(EvaSettings.instance().isDrawerHelpShow(), isUpdate);
            break;
          case R.id.btn_drawerNightMode:
            view.getToggler().setRadioEnabled(EvaSettings.instance().isDrawerNightmodeShow(), isUpdate);
            break;
        }
      }
    };

    ArrayList<ListItem> items = new ArrayList<>();

    // Custom drawer sections
    items.add(new ListItem(ListItem.TYPE_EMPTY_OFFSET_SMALL));
    items.add(new ListItem(ListItem.TYPE_DESCRIPTION, 0, 0, R.string.DrawerSettingsDesc));

    items.add(new ListItem(ListItem.TYPE_SHADOW_TOP));
    items.add(new ListItem(ListItem.TYPE_RADIO_SETTING, R.id.btn_drawerContacts, 0, R.string.Contacts));
    items.add(new ListItem(ListItem.TYPE_RADIO_SETTING, R.id.btn_drawerCalls, 0, R.string.Calls));
    items.add(new ListItem(ListItem.TYPE_RADIO_SETTING, R.id.btn_drawerFavourite, 0, R.string.SavedMessages));
    items.add(new ListItem(ListItem.TYPE_RADIO_SETTING, R.id.btn_drawerInviteFriends, 0, R.string.InviteFriends));
    items.add(new ListItem(ListItem.TYPE_RADIO_SETTING, R.id.btn_drawerHelp, 0, R.string.Help));
    items.add(new ListItem(ListItem.TYPE_RADIO_SETTING, R.id.btn_drawerNightMode, 0, R.string.NightMode));
    items.add(new ListItem(ListItem.TYPE_SHADOW_BOTTOM));

    adapter.setItems(items, true);
    recyclerView.setAdapter(adapter);
  }
}

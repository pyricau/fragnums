package com.example.fragnums;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import com.squareup.enumsbatter.R;
import java.util.ArrayList;

import static android.view.animation.AnimationUtils.loadAnimation;

public final class MainActivity extends AppCompatActivity {

  ArrayList<BackstackFrame> backstack;
  Screen currentScreen;

  FrameLayout container;
  View currentView;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    container = (FrameLayout) findViewById(R.id.main_container);
    if (savedInstanceState == null) {
      currentScreen = Screen.values()[0];
      backstack = new ArrayList<>();
    } else {
      currentScreen = Screen.values()[savedInstanceState.getInt("currentScreen")];
      backstack = savedInstanceState.getParcelableArrayList("backstack");
    }
    currentView = currentScreen.inflate(container);
    container.addView(currentView);
    currentScreen.bind(currentView);
    updateActionBar();
  }

  @Override public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putInt("currentScreen", currentScreen.ordinal());
    outState.putParcelableArrayList("backstack", backstack);
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    currentScreen.unbind();
  }

  @Override public void onBackPressed() {
    if (backstack.size() > 0) {
      goBack();
      return;
    }
    super.onBackPressed();
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home:
        goBack();
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  public void goTo(Screen screen) {
    currentScreen.unbind();
    currentView.startAnimation(loadAnimation(this, R.anim.exit_forward));
    container.removeView(currentView);
    BackstackFrame backstackFrame = new BackstackFrame(currentScreen, currentView);
    backstack.add(backstackFrame);

    currentScreen = screen;
    currentView = currentScreen.inflate(container);
    currentView.startAnimation(loadAnimation(this, R.anim.enter_forward));
    container.addView(currentView);
    currentScreen.bind(currentView);

    updateActionBar();
  }

  public void goBack() {
    currentScreen.unbind();
    currentView.startAnimation(loadAnimation(this, R.anim.exit_backward));
    container.removeView(currentView);

    BackstackFrame latest = backstack.remove(backstack.size() - 1);
    currentScreen = latest.screen;
    currentView = currentScreen.inflate(container);
    currentView.startAnimation(loadAnimation(this, R.anim.enter_backward));
    container.addView(currentView, 0);
    latest.restore(currentView);
    currentScreen.bind(currentView);

    updateActionBar();
  }

  private void updateActionBar() {
    ActionBar actionBar = getSupportActionBar();
    actionBar.setDisplayHomeAsUpEnabled(backstack.size() != 0);
    CharSequence title = currentScreen.getTitle();
    if (title == null) {
      title = getTitle();
    }
    actionBar.setTitle(title);
  }
}

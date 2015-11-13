package com.example.fragnums;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.LayoutRes;
import android.support.annotation.StringRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.squareup.enumsbatter.R;

import static android.content.Intent.ACTION_VIEW;

public enum Screen {

  MAIN(R.layout.main) {
    @Override protected void onBind() {
      findViewById(R.id.enumsmatter).setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          goTo(ENUMSMATTER);
        }
      });
      findViewById(R.id.tshirt).setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          goTo(T_SHIRT);
        }
      });
    }
  },

  ENUMSMATTER(R.layout.enumsmatter, R.string.enumsmatter_title),

  T_SHIRT(R.layout.tshirt, R.string.tshirt_title) {
    @Override protected void onBind() {
      findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View view) {
          Intent intent = new Intent(ACTION_VIEW, Uri.parse("https://teespring.com/enumsmatter"));
          view.getContext().startActivity(intent);
        }
      });
    }
  };

  private final int layoutResId;
  private final int titleResId;
  protected View view;

  Screen(@LayoutRes int layoutResId) {
    this(layoutResId, -1);
  }

  Screen(@LayoutRes int layoutResId, @StringRes int titleResId) {
    this.layoutResId = layoutResId;
    this.titleResId = titleResId;
  }

  public View inflate(ViewGroup container) {
    Context context = container.getContext();
    return LayoutInflater.from(context).inflate(layoutResId, container, false);
  }

  public void bind(View view) {
    this.view = view;
    onBind();
  }

  public void unbind() {
    onUnbind();
    view = null;
  }

  protected void onBind() {
  }

  protected void onUnbind() {
  }

  public CharSequence getTitle() {
    if (titleResId == -1) {
      return null;
    }
    return view.getContext().getString(titleResId);
  }

  protected void goBack() {
    MainActivity activity = (MainActivity) view.getContext();
    activity.goBack();
  }

  protected void goTo(Screen screen) {
    MainActivity activity = (MainActivity) view.getContext();
    activity.goTo(screen);
  }

  @SuppressWarnings({ "unchecked", "UnusedDeclaration" })
  protected <T extends View> T findViewById(int id) {
    return (T) view.findViewById(id);
  }

}

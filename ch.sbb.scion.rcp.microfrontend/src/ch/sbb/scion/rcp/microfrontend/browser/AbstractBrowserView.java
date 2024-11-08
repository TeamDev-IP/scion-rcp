/*
 * Project: RCS - Rail Control System
 *
 * © Copyright by SBB AG, Alle Rechte vorbehalten
 */
package ch.sbb.scion.rcp.microfrontend.browser;

import java.util.ArrayList;
import java.util.List;

/**
 * The base implementation of {@link BrowserView}.
 */
public abstract class AbstractBrowserView implements BrowserView {

  private final List<NavigationListener> listeners = new ArrayList<>();

  @Override
  public void addNavigationListener(final NavigationListener listener) {
    if (!listeners.contains(listener)) {
      listeners.add(listener);
    }
  }

  @Override
  public void removeNavigationListener(final NavigationListener listener) {
    listeners.remove(listener);
  }

  /**
   * Notifies that a main frame has finished loading.
   */
  protected void notifyFrameLoadFinished() {
    for (NavigationListener listener : listeners) {
      listener.onFrameLoadFinished();
    }
  }
}

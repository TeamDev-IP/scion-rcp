/*
 * Project: RCS - Rail Control System
 *
 * © Copyright by SBB AG, Alle Rechte vorbehalten
 */
package ch.sbb.scion.rcp.microfrontend.browser;

/**
 * A listener for receiving the navigation notifications from the browser view.
 */
public interface NavigationListener {

  /**
   * Invoked when the main frame on a web page has completed loading.
   */
  public void onFrameLoadFinished();
}

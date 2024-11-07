/*
 * Project: RCS - Rail Control System
 *
 * © Copyright by SBB AG, Alle Rechte vorbehalten
 */
package ch.sbb.scion.rcp.microfrontend.browser;

/**
 * A listener for receiving browser view navigation notifications.
 */
public interface NavigationListener {

  /**
   * Invoked when a main frame on a web page has completed loading.
   */
  public void onFrameLoadFinished();
}

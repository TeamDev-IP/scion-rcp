/*
 * Project: RCS - Rail Control System
 *
 * © Copyright by SBB AG, Alle Rechte vorbehalten
 */
package ch.sbb.scion.rcp.microfrontend.browser;

/**
 * The list of supported {@link BrowserView} types.
 */
public enum BrowserViewType {
  /**
   * JxBrowser's SWT browser view control.
   */
  JXBROWSER,
  /**
   * Standard Eclipse SWT browser view control.
   */
  SWT
}

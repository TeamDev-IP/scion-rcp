/*
 * Project: RCS - Rail Control System
 *
 * © Copyright by SBB AG, Alle Rechte vorbehalten
 */
package ch.sbb.scion.rcp.microfrontend.browser;

/**
 * An object which should be disposed of to free up resources when no longer needed.
 */
public interface Disposable {

  /**
   * Disposes the object and releases all allocated resources.
   */
  void dispose();
}

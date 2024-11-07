/*
 * Project: RCS - Rail Control System
 *
 * © Copyright by SBB AG, Alle Rechte vorbehalten
 */
package ch.sbb.scion.rcp.microfrontend.browser;

/**
 * A JavaScript function which should be disposed of to free up resources when no longer needed.
 */
public interface DisposableJsFunction {

  /**
   * Disposes the function and releases all allocated resources. This method should be called when the function is no longer needed to
   * ensure efficient resource management.
   */
  void dispose();
}

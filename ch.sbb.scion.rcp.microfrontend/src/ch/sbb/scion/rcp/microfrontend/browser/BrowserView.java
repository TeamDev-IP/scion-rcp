/*
 * Project: RCS - Rail Control System
 *
 * © Copyright by SBB AG, Alle Rechte vorbehalten
 */
package ch.sbb.scion.rcp.microfrontend.browser;

import java.util.function.Consumer;

import org.eclipse.swt.widgets.Display;

/**
 * A browser view control, which allows working with web content.
 */
public interface BrowserView {

  /**
   * Adds a listener to be notified about browser navigation events.
   *
   * @param listener
   *          The listener to be added. Must not be {@code null}.
   */
  void addNavigationListener(NavigationListener listener);

  /**
   * Removes a previously added navigation listener.
   *
   * @param listener
   *          The listener to be removed. Must not be {@code null}.
   */
  void removeNavigationListener(NavigationListener listener);

  /**
   * Loads the specified URL in the browser.
   *
   * @param url
   *          The URL to load. Must not be {@code null}.
   */
  void loadUrl(String url);

  /**
   * Executes the given JavaScript code in the context of the currently loaded page.
   *
   * @param javaScript
   *          The JavaScript code to execute. Must not be {@code null}.
   * @return The result of the JavaScript execution, or {@code null} if no return value.
   */
  Object executeJavaScript(String javaScript);

  /**
   * Checks if the browser view has focus.
   *
   * @return {@code true} if the browser view has focus.
   */
  boolean isFocused();

  /**
   * Registers a JavaScript function that can be called from JavaScript code.
   *
   * @param name
   *          The name of the JavaScript function. Must not be {@code null} or empty.
   * @param once
   *          If true, the function will be removed after its first invocation.
   * @param callback
   *          The callback to execute when the function is called. Must not be {@code null}.
   */
  DisposableJsFunction registerJsFunction(String name, boolean once, Consumer<Object[]> callback);

  /**
   * Returns the {@link Display} instance of this browser view.
   */
  Display getDisplay();
}

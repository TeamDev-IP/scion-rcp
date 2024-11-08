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
   *          the listener to be added. Must not be {@code null}.
   */
  void addNavigationListener(NavigationListener listener);

  /**
   * Removes a previously added navigation listener.
   *
   * @param listener
   *          the listener to be removed. Must not be {@code null}.
   */
  void removeNavigationListener(NavigationListener listener);

  /**
   * Loads the specified URL in the browser.
   *
   * @param url
   *          the URL to load. Must not be {@code null}.
   */
  void loadUrl(String url);

  /**
   * Executes the given JavaScript code in the context of the currently loaded page.
   *
   * @param javaScript
   *          the JavaScript code to execute. Must not be {@code null}.
   * @return the result of the JavaScript execution, or {@code null} if no return value
   */
  Object executeJavaScript(String javaScript);

  /**
   * Checks if the browser view has focus.
   *
   * @return {@code true} if the browser view has focus
   */
  boolean isFocused();

  /**
   * Registers a JavaScript function that can be called from JavaScript code.
   *
   * @param name
   *          the name of the JavaScript function. Must not be {@code null} or empty.
   * @param once
   *          if true, the function will be removed after its first invocation
   * @param callback
   *          the callback to execute when the function is called. Must not be {@code null}.
   */
  Disposable registerJsFunction(String name, boolean once, Consumer<Object[]> callback);

  /**
   * Returns the {@link Display} instance of this browser view.
   */
  Display getDisplay();
}

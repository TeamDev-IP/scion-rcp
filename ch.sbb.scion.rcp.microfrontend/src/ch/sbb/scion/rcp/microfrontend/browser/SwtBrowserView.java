/*
 * Project: RCS - Rail Control System
 *
 * © Copyright by SBB AG, Alle Rechte vorbehalten
 */
package ch.sbb.scion.rcp.microfrontend.browser;

import java.util.function.Consumer;

import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.BrowserFunction;
import org.eclipse.swt.browser.ProgressAdapter;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.widgets.Display;

/**
 * Standard SWT implementation of the {@link BrowserView} interface.
 */
public class SwtBrowserView extends AbstractBrowserView {

  private final Browser browser;

  public SwtBrowserView(final Browser browser) {
    this.browser = browser;
    browser.addProgressListener(new ProgressAdapter() {

      @Override
      public void completed(final ProgressEvent event) {
        notifyFrameLoadFinished();
      }
    });
  }

  @Override
  public void loadUrl(final String url) {
    browser.setUrl(url);
  }

  @Override
  public Object executeJavaScript(final String javaScript) {
    return browser.execute(javaScript);
  }

  @Override
  public boolean isFocused() {
    return browser.isFocusControl();
  }

  @Override
  public Disposable registerJsFunction(final String name, final boolean once, final Consumer<Object[]> callback) {
    var browserFunction = new BrowserFunction(browser, name) {

      @Override
      public Boolean function(final Object[] arguments) {
        if (once) {
          dispose();
        }
        // Invoke the callback asynchronously to first complete the invocation of this browser function.
        // Otherwise, creating a new {@link Browser} instance in the callback would lead to a deadlock.
        browser.getDisplay().asyncExec(() -> callback.accept(arguments));
        return Boolean.TRUE;
      }
    };

    return new Disposable() {

      @Override
      public void dispose() {
        if (!browserFunction.isDisposed()) {
          browserFunction.dispose();
        }
      }
    };
  }

  @Override
  public Display getDisplay() {
    return browser.getDisplay();
  }
}

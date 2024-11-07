/*
 * Project: RCS - Rail Control System
 *
 * © Copyright by SBB AG, Alle Rechte vorbehalten
 */
package ch.sbb.scion.rcp.microfrontend.browser;

import java.util.function.Consumer;

import org.eclipse.swt.widgets.Display;

import com.teamdev.jxbrowser.browser.Browser;
import com.teamdev.jxbrowser.js.JsFunctionCallback;
import com.teamdev.jxbrowser.js.JsObject;
import com.teamdev.jxbrowser.navigation.event.FrameLoadFinished;
import com.teamdev.jxbrowser.view.swt.BrowserView;

/**
 * JxBrowser implementation of the {@link BrowserView} interface.
 */
public class JxBrowserView extends AbstractBrowserView {

  private final Browser browser;
  private final BrowserView browserView;

  public JxBrowserView(final BrowserView browserView) {
    this.browser = browserView.getBrowser();
    this.browserView = browserView;

    browser.navigation().on(FrameLoadFinished.class, e -> {
      if (e.frame().isMain()) {
        notifyFrameLoadFinished();
      }
    });
  }

  @Override
  public void loadUrl(final String url) {
    browser.navigation().loadUrlAndWait(url);
  }

  @Override
  public Object executeJavaScript(final String javaScript) {
    return browser.mainFrame().orElseThrow().executeJavaScript(javaScript);
  }

  @Override
  public boolean isFocused() {
    return browserView.isFocusControl();
  }

  @Override
  public Disposable registerJsFunction(final String name, final boolean once, final Consumer<Object[]> callback) {
    JsObject window = browserView.getBrowser().mainFrame().orElseThrow().executeJavaScript("window");
    // Define the JsFunctionCallback to handle invocations from JavaScript
    JsFunctionCallback jsFunctionCallback = new JsFunctionCallback() {

      @Override
      public Object invoke(final Object... args) {
        if (once) {
          // Remove the callback from the JavaScript context after one use
          window.removeProperty(name);
        }

        // Execute the callback in SWT's display thread
        // Invoke the callback asynchronously to first complete the invocation of this browser function.
        // Otherwise, creating a new {@link Browser} instance in the callback would lead to a deadlock.
        browserView.getDisplay().asyncExec(() -> callback.accept(args));

        return Boolean.TRUE;
      }
    };

    // Bind the JavaScript function to the `window` object
    window.putProperty(name, jsFunctionCallback);

    return new Disposable() {

      @Override
      public void dispose() {
        window.removeProperty(name);
      }
    };
  }

  @Override
  public Display getDisplay() {
    return browserView.getDisplay();
  }
}

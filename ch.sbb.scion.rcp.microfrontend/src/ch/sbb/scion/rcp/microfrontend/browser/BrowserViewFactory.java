/*
 * Project: RCS - Rail Control System
 *
 * © Copyright by SBB AG, Alle Rechte vorbehalten
 */
package ch.sbb.scion.rcp.microfrontend.browser;

import static com.teamdev.jxbrowser.engine.RenderingMode.HARDWARE_ACCELERATED;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.widgets.Composite;

import com.teamdev.jxbrowser.engine.Engine;
import com.teamdev.jxbrowser.engine.EngineOptions;

/**
 * Creates different types of {@link BrowserView} instances.
 * <p>Supports the following browser implementations:
 * <ul>
 *    <li>{@link JxBrowserView},</li>
 *    <li>{@link SwtBrowserView}.</li>
 * </ul>
 */
public final class BrowserViewFactory {

  /**
   * Creates a new {@link JxBrowserView} instance within the provided composite.
   *
   * @param composite
   *          the parent composite in which the browser view will be embedded. Must not be {@code null}.
   * @return a new {@link JxBrowserView} instance embedded in the given composite
   */
  public static BrowserView createJxBrowserView(final Composite composite) {
    var engine = Engine.newInstance(EngineOptions.newBuilder(HARDWARE_ACCELERATED).licenseKey("your_key").build());
    var browser = engine.newBrowser();
    return new JxBrowserView(com.teamdev.jxbrowser.view.swt.BrowserView.newInstance(composite, browser));
  }

  /**
   * Creates a new {@link SwtBrowserView} instance within the provided composite.
   *
   * @param composite
   *          the parent composite in which the browser view will be embedded. Must not be {@code null}.
   * @return a new {@link SwtBrowserView} instance embedded in the given composite
   */
  public static BrowserView createSwtBrowserView(final Composite composite) {
    var browser = new Browser(composite, SWT.EDGE);
    return new SwtBrowserView(browser);
  }

  private BrowserViewFactory() {
  }
}

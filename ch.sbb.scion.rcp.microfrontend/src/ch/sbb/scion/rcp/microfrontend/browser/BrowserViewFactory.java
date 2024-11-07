/*
 * Project: RCS - Rail Control System
 *
 * © Copyright by SBB AG, Alle Rechte vorbehalten
 */
package ch.sbb.scion.rcp.microfrontend.browser;

import static com.teamdev.jxbrowser.engine.RenderingMode.HARDWARE_ACCELERATED;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import com.teamdev.jxbrowser.engine.Engine;
import com.teamdev.jxbrowser.engine.EngineOptions;
import com.teamdev.jxbrowser.view.swt.BrowserView;

/**
 * Creates different types of {@link BrowserView} instances.
 * 
 * <p>Supports the following browser implementations:
 * <ul> 
 *    <li>{@link JxBrowserView},</li>
 *    <li>{@link SwtBrowserView}.</li>
 * </ul>
 * 
 */
public final class BrowserViewFactory {

  /**
   * Creates a new {@link BrowserView} instance of the specified type within the provided composite.
   *
   * @param type
   *          The type of the browser view to create.
   * @param composite
   *          The parent composite in which the browser view will be embedded. Must not be {@code null}.
   * @return A new {@link BrowserView} instance created with the specified type and embedded in the given composite.
   */
  public static ch.sbb.scion.rcp.microfrontend.browser.BrowserView createBrowserView(final BrowserViewType type, final Composite composite) {
    if (type == BrowserViewType.JXBROWSER) {
      var engine = Engine.newInstance(EngineOptions.newBuilder(HARDWARE_ACCELERATED).licenseKey("your_key").build());
      var browser = engine.newBrowser();
      return new JxBrowserView(BrowserView.newInstance(composite, browser));
    }

    if (type == BrowserViewType.SWT) {
      var browser = new org.eclipse.swt.browser.Browser(composite, SWT.EDGE);
      return new SwtBrowserView(browser);
    }

    throw new IllegalArgumentException("Unsupported type " + type + ". ");
  }

  private BrowserViewFactory() {
  }
}

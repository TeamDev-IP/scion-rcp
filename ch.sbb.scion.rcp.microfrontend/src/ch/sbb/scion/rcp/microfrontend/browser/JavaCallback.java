package ch.sbb.scion.rcp.microfrontend.browser;

import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.regex.Pattern;

import org.eclipse.swt.browser.BrowserFunction;

import ch.sbb.scion.rcp.microfrontend.IDisposable;

/**
 * Allows interaction from JavaScript with Java code. Injects a function to the {Window} of the currently loaded document that can be
 * invoked from JavaScript. Invoking that function calls the passed callback. The function's name can be obtained via
 * {@link JavaCallback#name}. Wraps a SWT {@link BrowserFunction}.
 */
public class JavaCallback implements IDisposable {

  public final String name;

  private CompletableFuture<BrowserView> whenBrowser;
  private Consumer<Object[]> callback;
  private DisposableJsFunction browserFunction;

  public JavaCallback(BrowserView browser, Consumer<Object[]> callback) {
    this(CompletableFuture.completedFuture(browser), callback);
  }

  public JavaCallback(CompletableFuture<BrowserView> whenBrowser, Consumer<Object[]> callback) {
    this.whenBrowser = whenBrowser;
    this.name = toValidJavaScriptIdentifier("__scion_rcp_browserfunction_" + UUID.randomUUID());
    this.callback = callback;
  }

  /**
   * Installs this callback on the {Window} of the currently loaded document in the browser. Applications must dispose this function if not
   * used anymore. This method resolves to the callback when installed the callback.
   */
  public CompletableFuture<JavaCallback> install() {
    return install(false);
  }

  /**
   * Installs this callback on the {Window} of the currently loaded document in the browser. This callback is automatically uninstalled
   * after first invocvation. This method resolves to the callback when installed the callback.
   */
  public CompletableFuture<JavaCallback> installOnce() {
    return install(true);
  }

  public CompletableFuture<JavaCallback> install(boolean once) {
    return whenBrowser.thenAccept(browserView -> {
      browserFunction = browserView.registerJsFunction(name, once, callback);
    }).thenApply(browserView -> this);
  }

  /**
   * Adds this {@link JavaCallback} to the passed collection.
   */
  public JavaCallback addTo(final Collection<IDisposable> disposables) {
    disposables.add(this);
    return this;
  }

  /**
   * Disposes of the resources associated with this BrowserFunction. Applications must dispose of all BrowserFunctions that they create.
   * <p>
   * Note that disposing a Browser automatically disposes all BrowserFunctions associated with it.
   * </p>
   */
  @Override
  public void dispose() {
    if (browserFunction != null) {
      browserFunction.dispose();
      browserFunction = null;
    }
  }
 
  private String toValidJavaScriptIdentifier(String name) {
    if (Pattern.matches("^\\d.+", name)) {
      throw new IllegalArgumentException(String.format("JavaScript identifier must not start with a digit. [name=%s]", name));
    }
    return name.replaceAll("[^\\w\\d\\$]", "_");
  }
}


package ch.sbb.scion.rcp.microfrontend.browser;

import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import java.lang.reflect.Type;

import org.eclipse.core.runtime.Platform;

import ch.sbb.scion.rcp.microfrontend.IDisposable;
import ch.sbb.scion.rcp.microfrontend.MessageClient;
import ch.sbb.scion.rcp.microfrontend.internal.ParameterizedType;
import ch.sbb.scion.rcp.microfrontend.internal.Resources;
import ch.sbb.scion.rcp.microfrontend.internal.gson.GsonFactory;
import ch.sbb.scion.rcp.microfrontend.script.Scripts;
import ch.sbb.scion.rcp.microfrontend.script.Scripts.Helpers;
import ch.sbb.scion.rcp.microfrontend.subscriber.ISubscriber;
import ch.sbb.scion.rcp.microfrontend.subscriber.ISubscription;

/**
 * Subscribes to an RxJS Observable in the browser.
 */
public class RxJsObservable<T> {

  private CompletableFuture<BrowserView> whenBrowser;
  private Type clazz;
  private String rxjsObservableIIFE;

  /**
   * Initializes a new {@code RxJsObservable} instance.
   *
   * @param browser 
   *          a {@code CompletableFuture} containing the {@link BrowserView} instance,
   *          used to interact with the browser environment where the RxJS observable will run
   * @param rxjsObservableIIFE
   *          a {@code String} containing a JavaScript IIFE (Immediately Invoked Function Expression),
   *          which defines and starts an RxJS observable immediately upon execution
   * @param clazz 
   *          the {@code Type} of emitted items, allowing for generic types.
   *          Unlike {@code Class<?>}, {@code Type} preserves specific type parameters, making it suitable for 
   *          returning complex data structures with parameterized types.
   */
  public RxJsObservable(final CompletableFuture<BrowserView> browser, final String rxjsObservableIIFE, final Type clazz) {
    this.whenBrowser = browser;
    this.rxjsObservableIIFE = rxjsObservableIIFE;
    this.clazz = clazz;
  }

  public ISubscription subscribe(final ISubscriber<T> observer) {
    var disposables = new ArrayList<IDisposable>();

    new JavaCallback(whenBrowser, args -> {
      try {
        var emission = GsonFactory.create().<Emission<T>> fromJson((String) args[0], new ParameterizedType(Emission.class, clazz));
        switch (emission.type) {
        case Next: {
          observer.onNext(emission.next);
          break;
        }
        case Error: {
          observer.onError(new RuntimeException(emission.error));
          disposables.forEach(IDisposable::dispose);
          break;
        }
        case Complete: {
          observer.onComplete();
          disposables.forEach(IDisposable::dispose);
          break;
        }
        }
      }
      catch (RuntimeException e) {
        Platform.getLog(MessageClient.class).error("Unhandled error in callback", e);
      }
    }).addTo(disposables).install().thenAccept(callback -> {
      var uuid = UUID.randomUUID();
      new JavaScriptExecutor(whenBrowser, Resources.readString("js/rxjs-observable/subscribe.js"))
          .replacePlaceholder("callback", callback.name).replacePlaceholder("subscriptionStorageKey", uuid)
          .replacePlaceholder("helpers.toJson", Helpers.toJson).replacePlaceholder("storage", Scripts.Storage)
          .replacePlaceholder("rxjsObservableIIFE", rxjsObservableIIFE).execute();

      disposables.add(() -> new JavaScriptExecutor(whenBrowser, Resources.readString("js/rxjs-observable/unsubscribe.js"))
          .replacePlaceholder("subscriptionStorageKey", uuid).replacePlaceholder("storage", Scripts.Storage).execute());
    });

    return () -> disposables.forEach(IDisposable::dispose);
  }

  private static class Emission<T> {

    public static enum Type {
      Next,
      Error,
      Complete
    };

    public Type type;
    public T next;
    public String error;
  }
}

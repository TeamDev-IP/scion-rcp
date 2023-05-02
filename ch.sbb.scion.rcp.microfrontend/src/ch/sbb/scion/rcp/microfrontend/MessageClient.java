package ch.sbb.scion.rcp.microfrontend;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.google.gson.JsonElement;

import ch.sbb.scion.rcp.microfrontend.browser.JavaCallback;
import ch.sbb.scion.rcp.microfrontend.browser.JavaScriptExecutor;
import ch.sbb.scion.rcp.microfrontend.browser.RxJsObservable;
import ch.sbb.scion.rcp.microfrontend.host.MicrofrontendPlatformRcpHost;
import ch.sbb.scion.rcp.microfrontend.internal.ParameterizedType;
import ch.sbb.scion.rcp.microfrontend.internal.Resources;
import ch.sbb.scion.rcp.microfrontend.internal.gson.GsonFactory;
import ch.sbb.scion.rcp.microfrontend.model.PublishOptions;
import ch.sbb.scion.rcp.microfrontend.model.RequestOptions;
import ch.sbb.scion.rcp.microfrontend.model.TopicMessage;
import ch.sbb.scion.rcp.microfrontend.script.Script;
import ch.sbb.scion.rcp.microfrontend.script.Script.Flags;
import ch.sbb.scion.rcp.microfrontend.script.Scripts.Helpers;
import ch.sbb.scion.rcp.microfrontend.script.Scripts.Refs;
import ch.sbb.scion.rcp.microfrontend.subscriber.ISubscriber;
import ch.sbb.scion.rcp.microfrontend.subscriber.ISubscription;

/**
 * @see <a href="https://scion-microfrontend-platform-api.vercel.app/classes/MessageClient.html">MessageClient</a>
 */
@Component(service = MessageClient.class)
public class MessageClient {

  @Reference
  private MicrofrontendPlatformRcpHost microfrontendPlatformRcpHost;

  /**
   * @see "https://scion-microfrontend-platform-api.vercel.app/classes/MessageClient.html#publish"
   */
  public CompletableFuture<Void> publish(final String topic) {
    return publishJson(topic, null, null);
  }

  /**
   * @see "https://scion-microfrontend-platform-api.vercel.app/classes/MessageClient.html#publish"
   */
  public CompletableFuture<Void> publish(final String topic, final Object message) {
    return publishJson(topic, GsonFactory.create().toJson(message), null);
  }

  /**
   * @see "https://scion-microfrontend-platform-api.vercel.app/classes/MessageClient.html#publish"
   */
  public CompletableFuture<Void> publish(final String topic, final PublishOptions options) {
    return publishJson(topic, null, options);
  }

  /**
   * @see "https://scion-microfrontend-platform-api.vercel.app/classes/MessageClient.html#publish"
   */
  public CompletableFuture<Void> publish(final String topic, final Object message, final PublishOptions options) {
    return publishJson(topic, GsonFactory.create().toJson(message), options);
  }

  /**
   * @see "https://scion-microfrontend-platform-api.vercel.app/classes/MessageClient.html#publish"
   */
  public CompletableFuture<Void> publish(final String topic, final JsonElement jsonElement, final PublishOptions options) {
    return publishJson(topic, GsonFactory.create().toJson(jsonElement), options);
  }

  /**
   * @see "https://scion-microfrontend-platform-api.vercel.app/classes/MessageClient.html#request_"
   */
  public <T> ISubscription request(final String topic, final Class<T> clazz, final ISubscriber<TopicMessage<T>> subscriber) {
    return requestJson(topic, null, null, clazz, subscriber);
  }

  /**
   * @see "https://scion-microfrontend-platform-api.vercel.app/classes/MessageClient.html#request_"
   */
  public <T> ISubscription request(final String topic, final Object body, final Class<T> clazz,
      final ISubscriber<TopicMessage<T>> subscriber) {
    return requestJson(topic, GsonFactory.create().toJson(body), null, clazz, subscriber);
  }

  /**
   * @see "https://scion-microfrontend-platform-api.vercel.app/classes/MessageClient.html#request_"
   */
  public <T> ISubscription request(final String topic, final RequestOptions options, final Class<T> clazz,
      final ISubscriber<TopicMessage<T>> subscriber) {
    return requestJson(topic, null, options, clazz, subscriber);
  }

  /**
   * @see "https://scion-microfrontend-platform-api.vercel.app/classes/MessageClient.html#request_"
   */
  public <T> ISubscription request(final String topic, final Object body, final RequestOptions options, final Class<T> clazz,
      final ISubscriber<TopicMessage<T>> subscriber) {
    return requestJson(topic, GsonFactory.create().toJson(body), options, clazz, subscriber);
  }

  /**
   * @see "https://scion-microfrontend-platform-api.vercel.app/classes/MessageClient.html#request_"
   */
  public <T> ISubscription request(final String topic, final JsonElement jsonElement, final RequestOptions options, final Class<T> clazz,
      final ISubscriber<TopicMessage<T>> subscriber) {
    return requestJson(topic, GsonFactory.create().toJson(jsonElement), options, clazz, subscriber);
  }

  /**
   * @see "https://scion-microfrontend-platform-api.vercel.app/classes/MessageClient.html#request_"
   */
  private <T> ISubscription requestJson(final String topic, final String json, RequestOptions options, final Class<T> clazz,
      final ISubscriber<TopicMessage<T>> subscriber) {
    Objects.requireNonNull(topic);
    Objects.requireNonNull(clazz);
    Objects.requireNonNull(subscriber);
    options = Optional.ofNullable(options).orElse(new RequestOptions());
    var requestIIFE = new Script(Resources.readString("js/sci-message-client/request.iife.js"))
        .replacePlaceholder("refs.MessageClient", Refs.MessageClient).replacePlaceholder("topic", topic).replacePlaceholder("request", json)
        .replacePlaceholder("options.headers", options.headers(), Flags.ToJson).replacePlaceholder("options.retain", options.isRetain())
        .replacePlaceholder("helpers.fromJson", Helpers.fromJson).substitute();

    var observable = new RxJsObservable<TopicMessage<T>>(microfrontendPlatformRcpHost.whenHostBrowser, requestIIFE,
        new ParameterizedType(TopicMessage.class, clazz));
    return observable.subscribe(subscriber);
  }

  /**
   * @see "https://scion-microfrontend-platform-api.vercel.app/classes/MessageClient.html#observe_"
   */
  public ISubscription subscribe(final String topic, final ISubscriber<TopicMessage<String>> subscriber) {
    return subscribe(topic, String.class, subscriber);
  }

  /**
   * @see "https://scion-microfrontend-platform-api.vercel.app/classes/MessageClient.html#observe_"
   */
  public <T> ISubscription subscribe(final String topic, final Class<T> clazz, final ISubscriber<TopicMessage<T>> subscriber) {
    Objects.requireNonNull(topic);
    Objects.requireNonNull(clazz);
    Objects.requireNonNull(subscriber);
    var observeIIFE = new Script(Resources.readString("js/sci-message-client/observe.iife.js"))
        .replacePlaceholder("refs.MessageClient", Refs.MessageClient).replacePlaceholder("topic", topic, Flags.ToJson)
        .replacePlaceholder("helpers.fromJson", Helpers.fromJson).substitute();

    var observable = new RxJsObservable<TopicMessage<T>>(microfrontendPlatformRcpHost.whenHostBrowser, observeIIFE,
        new ParameterizedType(TopicMessage.class, clazz));
    return observable.subscribe(subscriber);
  }

  /**
   * @see "https://scion-microfrontend-platform-api.vercel.app/classes/MessageClient.html#publish"
   */
  private CompletableFuture<Void> publishJson(final String topic, final String json, final PublishOptions publishOptions) {
    Objects.requireNonNull(topic);
    var options = Optional.ofNullable(publishOptions).orElse(new PublishOptions());
    var published = new CompletableFuture<Void>();
    new JavaCallback(microfrontendPlatformRcpHost.whenHostBrowser, args -> {
      var error = args[0];
      if (error == null) {
        published.complete(null);
      }
      else {
        published.completeExceptionally(new RuntimeException((String) error));
      }
    }).installOnce().thenAccept(callback -> {
      new JavaScriptExecutor(microfrontendPlatformRcpHost.hostBrowser, Resources.readString("js/sci-message-client/publish.js"))
          .replacePlaceholder("callback", callback.name).replacePlaceholder("topic", topic, Flags.ToJson)
          .replacePlaceholder("message", json).replacePlaceholder("options.headers", options.headers(), Flags.ToJson)
          .replacePlaceholder("options.retain", options.isRetain()).replacePlaceholder("refs.MessageClient", Refs.MessageClient)
          .replacePlaceholder("helpers.fromJson", Helpers.fromJson).runInsideAsyncFunction().execute();
    });

    return published;
  }
}
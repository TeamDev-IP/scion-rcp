package ch.sbb.scion.rcp.microfrontend.script;

public interface Scripts {

  public static final String Storage = "window['__SCION_RCP'].storage";

  public static class Refs {
    public static final String MicrofrontendPlatform = "window['__SCION_RCP'].refs.MicrofrontendPlatform";
    public static final String MessageClient = "window['__SCION_RCP'].refs.MessageClient";
    public static final String IntentClient = "window['__SCION_RCP'].refs.IntentClient";
    public static final String OutletRouter = "window['__SCION_RCP'].refs.OutletRouter";
    public static final String ManifestService = "window['__SCION_RCP'].refs.ManifestService";
  }

  public static class Helpers {
    public static final String toJson = "window['__SCION_RCP'].helpers.toJson";
    public static final String fromJson = "window['__SCION_RCP'].helpers.fromJson";
  }
}

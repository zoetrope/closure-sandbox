import com.google.template.soy.SoyFileSet;
import com.google.template.soy.data.SoyMapData;
import com.google.template.soy.tofu.SoyTofu;
import org.vertx.java.core.http.HttpServer;
import org.vertx.java.core.http.RouteMatcher;
import org.vertx.java.platform.Verticle;

import java.io.File;

public class TypedSoyVerticle extends Verticle {

  public void start() {
    HttpServer server = vertx.createHttpServer();
    RouteMatcher matcher = new RouteMatcher();

    SoyTofu tofu = SoyFileSet.builder()
        .add(new File("hello.soy"))
        .add(new File("operate.soy"))
        .build()
        .compileToTofu();

    matcher.get("/string", req -> {
      SoyMapData data = new SoyMapData("name", "string");
      String output = tofu.newRenderer("example.templates.hello.render")
          .setData(data)
          .render();
      req.response().end(output);
    });

    matcher.get("/int", req -> {
      SoyMapData data = new SoyMapData("name", 1234);
      String output = tofu.newRenderer("example.templates.hello.render")
          .setData(data)
          .render();
      req.response().end(output);
    });

    matcher.get("/operate", req -> {
      SoyMapData data = new SoyMapData("x", 12, "y", 34);
      String output = tofu.newRenderer("example.templates.operate.render")
          .setData(data)
          .render();
      req.response().end(output);
    });

    server.requestHandler(matcher).listen(8181);
  }
}

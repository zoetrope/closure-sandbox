import com.google.template.soy.SoyFileSet;
import com.google.template.soy.tofu.SoyTofu;
import org.vertx.java.core.http.HttpServer;
import org.vertx.java.core.http.RouteMatcher;
import org.vertx.java.core.json.impl.Json;
import org.vertx.java.platform.Verticle;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class HelloJsSoyVerticle extends Verticle {

  public void start() {
    HttpServer server = vertx.createHttpServer();
    RouteMatcher matcher = new RouteMatcher();

    SoyFileSet sfs = SoyFileSet.builder()
        .add(new File("main.soy"))
        .build();
    SoyTofu tofu = sfs.compileToTofu();

    matcher.get("/", req -> {
      String output = tofu.newRenderer("example.templates.main.render")
          .render();
      req.response().end(output);
    });

    matcher.get("/data.json", req -> {
      Map<String, Object> map = new HashMap<>();
      map.put("name", "Mr. Soy");
      String json = Json.encode(map);
      req.response().end(json);
    });

    server.requestHandler(matcher).listen(8181);
  }
}

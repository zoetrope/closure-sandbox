import com.google.template.soy.SoyFileSet;
import com.google.template.soy.data.SoyMapData;
import com.google.template.soy.tofu.SoyTofu;
import com.google.template.soy.types.SoyTypeRegistry;
import org.vertx.java.core.http.HttpServer;
import org.vertx.java.core.http.RouteMatcher;
import org.vertx.java.platform.Verticle;

import java.io.File;

public class TypedSoyVerticle extends Verticle {

    public void start() {
        HttpServer server = vertx.createHttpServer();
        RouteMatcher matcher = new RouteMatcher();

        SoyFileSet sfs = SoyFileSet.builder()
                .add(new File("hello.soy"))
                .build();
        SoyTofu tofu = sfs.compileToTofu();

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

        server.requestHandler(matcher).listen(8181);
    }
}

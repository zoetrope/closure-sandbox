import com.google.template.soy.SoyFileSet;
import com.google.template.soy.data.SoyMapData;
import com.google.template.soy.tofu.SoyTofu;
import org.vertx.java.core.http.HttpServer;
import org.vertx.java.core.http.RouteMatcher;
import org.vertx.java.platform.Verticle;

import java.io.File;

public class AutoEscapingVerticle extends Verticle {

    public void start() {
        HttpServer server = vertx.createHttpServer();
        RouteMatcher matcher = new RouteMatcher();

        SoyFileSet sfs = SoyFileSet.builder()
                .add(new File("main.soy"))
                .add(new File("strict.soy"))
                .add(new File("contextual.soy"))
                .build();
        SoyTofu tofu = sfs.compileToTofu();


        matcher.get("/:mode", req -> {
            String mode = req.params().get("mode");
            String message =  "<script>alert('danger!');</script>";
            SoyMapData data = new SoyMapData("message", message, "mode", mode);

            String output = tofu.newRenderer("example.templates.main.render")
                    .setData(data)
                    .render();
            req.response().end(output);
        });

        server.requestHandler(matcher).listen(8181);
    }
}

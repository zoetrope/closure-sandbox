import com.google.template.soy.SoyFileSet;
import com.google.template.soy.data.SoyGlobalsValue;
import com.google.template.soy.data.SoyMapData;
import com.google.template.soy.tofu.SoyTofu;
import org.vertx.java.core.http.HttpServer;
import org.vertx.java.core.http.RouteMatcher;
import org.vertx.java.platform.Verticle;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

public class FutureVerticle extends Verticle {

    public void start() {
        container.logger().info("MainVerticle start.");

        HttpServer server = vertx.createHttpServer();

        RouteMatcher matcher = new RouteMatcher();

        SoyFileSet sfs = SoyFileSet.builder().add(new File("hello.soy")).build();
        SoyTofu tofu = sfs.compileToTofu();

        Future<String> future = new SimpleFuture<String>(()->{

            System.out.println("future???");
            return "my name is hoge";
        });

        Map<String, Object> map = new HashMap();
        map.put("name", future);
        SoyTofu.Renderer renderer = tofu.newRenderer("voy.helloWorld");
        renderer.setData(map);

/*        SoyTofu.Renderer renderer = tofu.newRenderer("voy.helloWorld")
                .setData(new SoyMapData("fuga", future))
                .setData(new SoyMapData("name", "hogehoge"));
                */

        matcher.get("/", req -> {
            String output = renderer.render();
            req.response().end(output);
        });

        server.requestHandler(matcher).listen(8181);
    }
}



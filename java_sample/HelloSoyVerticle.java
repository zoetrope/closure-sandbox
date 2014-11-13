import com.google.template.soy.SoyFileSet;
import com.google.template.soy.data.SoyMapData;
import com.google.template.soy.tofu.SoyTofu;
import org.vertx.java.core.http.HttpServer;
import org.vertx.java.core.http.RouteMatcher;
import org.vertx.java.platform.Verticle;

import java.io.File;

public class HelloSoyVerticle extends Verticle {

  public void start() {
    HttpServer server = vertx.createHttpServer();
    RouteMatcher matcher = new RouteMatcher();

    // soyファイルの読み込み
    SoyFileSet sfs = SoyFileSet.builder()
        .add(new File("hello.soy"))
        .build();
    // soyファイルをコンパイル
    SoyTofu tofu = sfs.compileToTofu();

    // GETリクエストがきたらsoyのレンダリングをして返す
    matcher.get("/:name", req -> {
      // URLに含まれるパラメータを使ってデータを生成
      SoyMapData data = new SoyMapData("name", req.params().get("name"));

      // soyのレンダリング
      String output = tofu.newRenderer("example.helloWorld")
          .setData(data)
          .render();
      req.response().end(output);
    });

    server.requestHandler(matcher).listen(8080);
  }
}

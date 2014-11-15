import com.google.template.soy.SoyFileSet;
import com.google.template.soy.data.SoyMapData;
import com.google.template.soy.tofu.SoyTofu;
import org.vertx.java.platform.Verticle;

import java.io.File;

public class HelloSoyVerticle extends Verticle {

  public void start() {
    // テンプレートファイルを読み込んでコンパイル
    SoyTofu tofu = SoyFileSet.builder()
        .add(new File("hello.soy"))
        .build()
        .compileToTofu();

    // GETリクエストがきたらレンダリングをして返す
    vertx.createHttpServer().requestHandler(req -> {
      SoyMapData data = new SoyMapData("name", "World");
      String output = tofu.newRenderer("example.templates.hello")
          .setData(data)
          .render();
      req.response().end(output);
    }).listen(8080);
  }
}

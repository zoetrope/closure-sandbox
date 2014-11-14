import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.inject.*;
import com.google.template.soy.SoyFileSet;
import com.google.template.soy.data.SoyMapData;
import com.google.template.soy.data.SoyValue;
import com.google.template.soy.data.restricted.StringData;
import com.google.template.soy.tofu.SoyTofu;
import com.google.template.soy.types.SoyType;
import com.google.template.soy.types.SoyTypeProvider;
import com.google.template.soy.types.SoyTypeRegistry;
import com.google.template.soy.types.primitive.ErrorType;
import com.google.template.soy.types.primitive.PrimitiveType;
import com.google.template.soy.types.primitive.UnknownType;
import org.vertx.java.core.http.HttpServer;
import org.vertx.java.core.http.RouteMatcher;
import org.vertx.java.platform.Verticle;

import java.io.File;
import java.util.List;
import java.util.Set;

public class MyTypeProvider extends Verticle {

    class MyType extends PrimitiveType {
        /*
        private static final MyType INSTANCE = new MyType();
        public static MyType getInstance() {
            return INSTANCE;
        }
        */

        @Override
        public Kind getKind() {
            return Kind.ANY;
        }

        @Override
        public boolean isInstance(SoyValue value) {
            return value instanceof StringData;
        }
    }

    private SoyTypeRegistry createDummyTypeRegistry() {
        return new SoyTypeRegistry(ImmutableSet.<SoyTypeProvider>of(
                (typeName, typeRegistry) -> {
                    System.out.println(typeName);
                    if(typeName.equals("hoge")) {
                        return new MyType();
                    }
                    return new ErrorType(typeName);
                }));
    }

    public void start() {
        HttpServer server = vertx.createHttpServer();
        RouteMatcher matcher = new RouteMatcher();


        SoyFileSet.Builder sfsBuilder = SoyFileSet.builder();
        sfsBuilder.setLocalTypeRegistry(createDummyTypeRegistry());
        SoyFileSet sfs = sfsBuilder
                .add(new File("named.soy"))
                .build();

        SoyTofu tofu = sfs.compileToTofu();

        matcher.get("/", req -> {
            SoyMapData data = new SoyMapData("name", 123);
            String output = tofu.newRenderer("example.templates.hello.render")
                    .setData(data)
                    .render();
            req.response().end(output);
        });

        server.requestHandler(matcher).listen(8181);
    }
}

import com.google.common.collect.ImmutableList;
import com.google.inject.*;
import com.google.template.soy.SoyFileSet;
import com.google.template.soy.data.SoyCustomValueConverter;
import com.google.template.soy.tofu.SoyTofu;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class MyCustomValueConverter {
  void main(){
    Module converters =
        new Module() {
          @Override public void configure(Binder binder) {
          }
          @Provides
          List<SoyCustomValueConverter> provideSoyCustomValueConverters() {
            return ImmutableList.<SoyCustomValueConverter>of(
                (valueConverter, obj) -> {
                  if (obj instanceof Object[]) {
                    return valueConverter.convert(Arrays.asList((Object[]) obj));
                  } else {
                    return null;
                  }
                });
          }
        };
    Injector injector = Guice.createInjector(converters);

    SoyFileSet.Builder sfsBuilder = injector.getInstance(SoyFileSet.Builder.class);
    SoyFileSet sfs = sfsBuilder
        .add(new File("hello.soy"))
        .build();

    SoyTofu tofu = sfs.compileToTofu();
  }
}

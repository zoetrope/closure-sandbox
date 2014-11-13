import com.google.common.collect.ImmutableSet;
import com.google.common.io.Files;
import com.google.common.io.InputSupplier;
import com.google.template.soy.base.internal.SoyFileKind;
import com.google.template.soy.base.internal.SoyFileSupplier;
import com.google.template.soy.basetree.SyntaxVersion;
import com.google.template.soy.shared.SoyAstCache;
import com.google.template.soy.soyparse.SoyFileSetParser;
import com.google.template.soy.soytree.*;
import com.google.template.soy.types.SoyTypeProvider;
import com.google.template.soy.types.SoyTypeRegistry;
import com.google.template.soy.types.primitive.UnknownType;

import java.io.File;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class MySoyParser {

  private SoyTypeRegistry createDummyTypeRegistry() {
    return new SoyTypeRegistry(ImmutableSet.<SoyTypeProvider>of(
        (typeName, typeRegistry) -> UnknownType.getInstance()));
  }

  public void build() {
    File inputFile = new File("dummy.soy");
    SoyTypeRegistry typeRegistry = createDummyTypeRegistry();
    SoyAstCache cache = null;
    SyntaxVersion declaredSyntaxVersion = SyntaxVersion.V2_0;

    List<SoyFileSupplier> soyFileSuppliers = new ArrayList<>();
    InputSupplier<InputStreamReader> sup = Files.newReaderSupplier(inputFile, StandardCharsets.UTF_8);
    soyFileSuppliers.add(SoyFileSupplier.Factory.create(sup, SoyFileKind.SRC, inputFile.getPath()));

    SoyFileSetParser parser = new SoyFileSetParser(typeRegistry, cache, declaredSyntaxVersion, soyFileSuppliers);
    SoyFileSetNode soyTree = parser.parse();
  }

  public static void main(String[] args) {

    MySoyParser parser = new MySoyParser();
    parser.build();
  }
}

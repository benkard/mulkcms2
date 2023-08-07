package eu.mulk.mulkcms2.common.hibernate;

import static org.hibernate.query.sqm.produce.function.FunctionParameterType.ANY;
import static org.hibernate.query.sqm.produce.function.FunctionParameterType.STRING;
import static org.hibernate.sql.ast.SqlAstNodeRenderingMode.DEFAULT;
import static org.hibernate.type.StandardBasicTypes.BOOLEAN;

import org.hibernate.boot.model.FunctionContributions;
import org.hibernate.boot.model.FunctionContributor;

public final class HibernateFunctionContributor implements FunctionContributor {

  @Override
  public void contributeFunctions(FunctionContributions functionContributions) {
    var typeConfiguration = functionContributions.getTypeConfiguration();
    var typeRegistry = typeConfiguration.getBasicTypeRegistry();
    var functionRegistry = functionContributions.getFunctionRegistry();

    functionRegistry
        .namedDescriptorBuilder("post_matches_websearch")
        .setInvariantType(typeRegistry.resolve(BOOLEAN))
        .setExactArgumentCount(3)
        .setArgumentListSignature("(STRING searchTerms, STRING language, STRING queryText)")
        .setArgumentRenderingMode(DEFAULT)
        .setParameterTypes(ANY, STRING, STRING)
        .register();
  }
}

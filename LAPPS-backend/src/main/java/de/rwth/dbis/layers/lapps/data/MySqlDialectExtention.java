package de.rwth.dbis.layers.lapps.data;

import org.hibernate.dialect.MySQL5Dialect;
import org.hibernate.dialect.function.SQLFunctionTemplate;
import org.hibernate.type.IntegerType;

/**
 * Extending of the default {@link MySQL5Dialect} has been needed to introduce bitwise operations.
 */
public class MySqlDialectExtention extends MySQL5Dialect {
  /**
   * If falling back to the default dialect implementation is needed, change the hibernate.dialect
   * property in the persistence file!
   */
  public MySqlDialectExtention() {
    super();
    registerFunction("bitwiseAnd", new SQLFunctionTemplate(IntegerType.INSTANCE, "(?1 & ?2)"));
  }
}

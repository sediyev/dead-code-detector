package com.aurea.service.finder;

import com.scitools.understand.Entity;
import java.util.function.Predicate;
import org.apache.commons.lang3.StringUtils;

class FinderFilters {

  static Predicate<Entity> notInAbstractMethod() {
    return entity -> entity.refs("definein", "method abstract", false).length == 0;
  }

  static Predicate<Entity> notUsed() {
    return entity -> entity.refs("useby", null, false).length == 0;
  }

  // In enum type Understand api shows a parameter having EnumType.valueOf.s definition
  // leading to erroneous detection. This filter is implemented to remove such cases
  static Predicate<Entity> hasNotImplicitDefinition() {
    return entity -> entity.refs("definein implicit", null, false).length == 0;
  }

  static Predicate<Entity> notCalled() {
    return entity -> entity.refs("callby", null, false).length == 0;
  }

  // In Understand api lambda function name starts with .(lambda
  static Predicate<Entity> notLambda() {
    return entity -> !StringUtils.contains(entity.name(), "(lambda");
  }

  // Method does not have @Override annotation
  static Predicate<Entity> notOverridden() {
    return entity -> entity.refs("override", null, false).length == 0;
  }
}

/*
 *    Copyright 2009-2022 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.apache.ibatis.mapping;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.builder.BuilderException;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.reflection.ParamNameUtil;
import org.apache.ibatis.session.Configuration;

/**
 * @author Clinton Begin
 */
public class ResultMap {
  private Configuration configuration;

  private String id;
  private Class<?> type;
  private List<ResultMapping> resultMappings;
  private List<ResultMapping> idResultMappings;
  private List<ResultMapping> constructorResultMappings;
  private List<ResultMapping> propertyResultMappings;
  private Set<String> mappedColumns;
  private Set<String> mappedProperties;
  private Discriminator discriminator;
  private boolean hasNestedResultMaps;
  private boolean hasNestedQueries;
  private Boolean autoMapping;

  private ResultMap() {
  }

  public static class Builder {
    private static final Log log = LogFactory.getLog(Builder.class);

    private ResultMap resultMap = new ResultMap();

    public Builder(Configuration configuration, String id, Class<?> type, List<ResultMapping> resultMappings) {
      this(configuration, id, type, resultMappings, null);
    }

    public Builder(Configuration configuration, String id, Class<?> type, List<ResultMapping> resultMappings, Boolean autoMapping) {
      resultMap.configuration = configuration;
      resultMap.id = id;
      resultMap.type = type;
      resultMap.resultMappings = resultMappings;
      resultMap.autoMapping = autoMapping;
    }

    public Builder discriminator(Discriminator discriminator) {
      resultMap.discriminator = discriminator;
      return this;
    }

    public Class<?> type() {
      return resultMap.type;
    }

    public ResultMap build() {
      return resultMap.build(this);
    }

    public List<String> argNamesOfMatchingConstructor(List<String> constructorArgNames) {
      Constructor<?>[] constructors = resultMap.type.getDeclaredConstructors();
      for (Constructor<?> constructor : constructors) {
        Class<?>[] paramTypes = constructor.getParameterTypes();
        if (constructorArgNames.size() == paramTypes.length) {
          List<String> paramNames = getArgNames(constructor);
          if (constructorArgNames.containsAll(paramNames)
              && argTypesMatch(constructorArgNames, paramTypes, paramNames)) {
            return paramNames;
          }
        }
      }
      return null;
    }

    private boolean argTypesMatch(final List<String> constructorArgNames,
        Class<?>[] paramTypes, List<String> paramNames) {
      for (int i = 0; i < constructorArgNames.size(); i++) {
        Class<?> actualType = paramTypes[paramNames.indexOf(constructorArgNames.get(i))];
        Class<?> specifiedType = resultMap.constructorResultMappings.get(i).getJavaType();
        if (!actualType.equals(specifiedType)) {
          log(constructorArgNames, i, actualType, specifiedType);
		return false;
        }
      }
      return true;
    }

	private void log(final List<String> constructorArgNames, int i, Class<?> actualType, Class<?> specifiedType) {
		if (log.isDebugEnabled()) {
			log.debug("While building result map '" + resultMap.id + "', found a constructor with arg names "
					+ constructorArgNames + ", but the type of '" + constructorArgNames.get(i)
					+ "' did not match. Specified: [" + specifiedType.getName() + "] Declared: [" + actualType.getName()
					+ "]");
		}
	}

    private List<String> getArgNames(Constructor<?> constructor) {
      List<String> paramNames = new ArrayList<>();
      List<String> actualParamNames = null;
      final Annotation[][] paramAnnotations = constructor.getParameterAnnotations();
      int paramCount = paramAnnotations.length;
      for (int paramIndex = 0; paramIndex < paramCount; paramIndex++) {
        String name = name(constructor, actualParamNames, paramAnnotations, paramIndex);
		paramNames.add(name != null ? name : "arg" + paramIndex);
      }
      return paramNames;
    }

	private String name(Constructor<?> constructor, List<String> actualParamNames,
			final Annotation[][] paramAnnotations, int paramIndex) {
		String name = null;
		for (Annotation annotation : paramAnnotations[paramIndex]) {
			if (annotation instanceof Param) {
				name = ((Param) annotation).value();
				break;
			}
		}
		if (name == null && resultMap.configuration.isUseActualParamName()) {
			if (actualParamNames == null) {
				actualParamNames = ParamNameUtil.getParamNames(constructor);
			}
			if (actualParamNames.size() > paramIndex) {
				name = actualParamNames.get(paramIndex);
			}
		}
		return name;
	}
  }

  public String getId() {
    return id;
  }

  public boolean hasNestedResultMaps() {
    return hasNestedResultMaps;
  }

  public boolean hasNestedQueries() {
    return hasNestedQueries;
  }

  public Class<?> getType() {
    return type;
  }

  public List<ResultMapping> getResultMappings() {
    return resultMappings;
  }

  public List<ResultMapping> getConstructorResultMappings() {
    return constructorResultMappings;
  }

  public List<ResultMapping> getPropertyResultMappings() {
    return propertyResultMappings;
  }

  public List<ResultMapping> getIdResultMappings() {
    return idResultMappings;
  }

  public Set<String> getMappedColumns() {
    return mappedColumns;
  }

  public Set<String> getMappedProperties() {
    return mappedProperties;
  }

  public Discriminator getDiscriminator() {
    return discriminator;
  }

  public void forceNestedResultMaps() {
    hasNestedResultMaps = true;
  }

  public Boolean getAutoMapping() {
    return autoMapping;
  }

public ResultMap build(Builder builder) {
	if (this.id == null) {
		throw new IllegalArgumentException("ResultMaps must have an id");
	}
	this.mappedColumns = new HashSet<>();
	this.mappedProperties = new HashSet<>();
	this.idResultMappings = new ArrayList<>();
	this.constructorResultMappings = new ArrayList<>();
	this.propertyResultMappings = new ArrayList<>();
	final List<String> constructorArgNames = new ArrayList<>();
	for (ResultMapping resultMapping : this.resultMappings) {
		this.hasNestedQueries = this.hasNestedQueries || resultMapping.getNestedQueryId() != null;
		this.hasNestedResultMaps = this.hasNestedResultMaps
				|| (resultMapping.getNestedResultMapId() != null && resultMapping.getResultSet() == null);
		final String column = resultMapping.getColumn();
		if (column != null) {
			this.mappedColumns.add(column.toUpperCase(Locale.ENGLISH));
		} else if (resultMapping.isCompositeResult()) {
			for (ResultMapping compositeResultMapping : resultMapping.getComposites()) {
				final String compositeColumn = compositeResultMapping.getColumn();
				if (compositeColumn != null) {
					this.mappedColumns.add(compositeColumn.toUpperCase(Locale.ENGLISH));
				}
			}
		}
		final String property = resultMapping.getProperty();
		if (property != null) {
			this.mappedProperties.add(property);
		}
		if (resultMapping.getFlags().contains(ResultFlag.CONSTRUCTOR)) {
			this.constructorResultMappings.add(resultMapping);
			if (resultMapping.getProperty() != null) {
				constructorArgNames.add(resultMapping.getProperty());
			}
		} else {
			this.propertyResultMappings.add(resultMapping);
		}
		if (resultMapping.getFlags().contains(ResultFlag.ID)) {
			this.idResultMappings.add(resultMapping);
		}
	}
	if (this.idResultMappings.isEmpty()) {
		this.idResultMappings.addAll(this.resultMappings);
	}
	if (!constructorArgNames.isEmpty()) {
		final List<String> actualArgNames = builder.argNamesOfMatchingConstructor(constructorArgNames);
		if (actualArgNames == null) {
			throw new BuilderException(
					"Error in result map '" + this.id + "'. Failed to find a constructor in '" + getType().getName()
							+ "' by arg names " + constructorArgNames + ". There might be more info in debug log.");
		}
		this.constructorResultMappings.sort((o1, o2) -> {
			int paramIdx1 = actualArgNames.indexOf(o1.getProperty());
			int paramIdx2 = actualArgNames.indexOf(o2.getProperty());
			return paramIdx1 - paramIdx2;
		});
	}
	this.resultMappings = Collections.unmodifiableList(this.resultMappings);
	this.idResultMappings = Collections.unmodifiableList(this.idResultMappings);
	this.constructorResultMappings = Collections.unmodifiableList(this.constructorResultMappings);
	this.propertyResultMappings = Collections.unmodifiableList(this.propertyResultMappings);
	this.mappedColumns = Collections.unmodifiableSet(this.mappedColumns);
	return this;
}

}

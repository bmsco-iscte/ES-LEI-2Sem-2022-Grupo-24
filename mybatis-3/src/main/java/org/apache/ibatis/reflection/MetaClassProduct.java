package org.apache.ibatis.reflection;


import org.apache.ibatis.reflection.property.PropertyTokenizer;

public class MetaClassProduct {
	private final ReflectorFactory reflectorFactory;

	public MetaClassProduct(ReflectorFactory reflectorFactory) {
		this.reflectorFactory = reflectorFactory;
	}

	public MetaClass metaClassForProperty(PropertyTokenizer prop, MetaClass metaClass) {
		Class<?> propType = metaClass.getGetterType(prop);
		return MetaClass.forClass(propType, reflectorFactory);
	}

	public MetaClass metaClassForProperty(String name, Reflector thisReflector) {
		Class<?> propType = thisReflector.getGetterType(name);
		return MetaClass.forClass(propType, reflectorFactory);
	}

	public Class<?> getGetterType(String name, MetaClass metaClass) {
		PropertyTokenizer prop = new PropertyTokenizer(name);
		if (prop.hasNext()) {
			MetaClass metaProp = metaClassForProperty(prop, metaClass);
			return metaProp.getGetterType(prop.getChildren());
		}
		return metaClass.getGetterType(prop);
	}

	public Class<?> getSetterType(String name, Reflector thisReflector) {
		PropertyTokenizer prop = new PropertyTokenizer(name);
		if (prop.hasNext()) {
			MetaClass metaProp = metaClassForProperty(prop.getName(), thisReflector);
			return metaProp.getSetterType(prop.getChildren());
		} else {
			return thisReflector.getSetterType(prop.getName());
		}
	}

	public boolean hasSetter(String name, Reflector thisReflector) {
		PropertyTokenizer prop = new PropertyTokenizer(name);
		if (prop.hasNext()) {
			if (thisReflector.hasSetter(prop.getName())) {
				MetaClass metaProp = metaClassForProperty(prop.getName(), thisReflector);
				return metaProp.hasSetter(prop.getChildren());
			} else {
				return false;
			}
		} else {
			return thisReflector.hasSetter(prop.getName());
		}
	}

	public StringBuilder buildProperty(String name, StringBuilder builder, Reflector thisReflector) {
		PropertyTokenizer prop = new PropertyTokenizer(name);
		if (prop.hasNext()) {
			String propertyName = thisReflector.findPropertyName(prop.getName());
			if (propertyName != null) {
				builder.append(propertyName);
				builder.append(".");
				MetaClass metaProp = metaClassForProperty(propertyName, thisReflector);
				metaProp.buildProperty(prop.getChildren(), builder);
			}
		} else {
			String propertyName = thisReflector.findPropertyName(name);
			if (propertyName != null) {
				builder.append(propertyName);
			}
		}
		return builder;
	}

	public boolean hasGetter(String name, Reflector thisReflector, MetaClass metaClass) {
		PropertyTokenizer prop = new PropertyTokenizer(name);
		if (prop.hasNext()) {
			if (thisReflector.hasGetter(prop.getName())) {
				MetaClass metaProp = metaClassForProperty(prop, metaClass);
				return metaProp.hasGetter(prop.getChildren());
			} else {
				return false;
			}
		} else {
			return thisReflector.hasGetter(prop.getName());
		}
	}
}
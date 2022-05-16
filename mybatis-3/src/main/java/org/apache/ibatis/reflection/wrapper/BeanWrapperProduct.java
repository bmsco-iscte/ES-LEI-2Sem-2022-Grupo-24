package org.apache.ibatis.reflection.wrapper;


import org.apache.ibatis.reflection.MetaClass;
import org.apache.ibatis.reflection.property.PropertyTokenizer;
import org.apache.ibatis.reflection.invoker.Invoker;
import org.apache.ibatis.reflection.ExceptionUtil;
import org.apache.ibatis.reflection.ReflectionException;

public class BeanWrapperProduct {
	private final MetaClass metaClass;

	public BeanWrapperProduct(MetaClass forClass) {
		this.metaClass = forClass;
	}

	public MetaClass getMetaClass() {
		return metaClass;
	}

	public Object getBeanProperty(PropertyTokenizer prop, Object object) {
		try {
			Invoker method = metaClass.getGetInvoker(prop.getName());
			try {
				return method.invoke(object, BeanWrapper.NO_ARGUMENTS);
			} catch (Throwable t) {
				throw ExceptionUtil.unwrapThrowable(t);
			}
		} catch (RuntimeException e) {
			throw e;
		} catch (Throwable t) {
			throw new ReflectionException("Could not get property '" + prop.getName() + "' from " + object.getClass()
					+ ".  Cause: " + t.toString(), t);
		}
	}

	public void setBeanProperty(PropertyTokenizer prop, Object object, Object value) {
		try {
			Invoker method = metaClass.getSetInvoker(prop.getName());
			Object[] params = { value };
			try {
				method.invoke(object, params);
			} catch (Throwable t) {
				throw ExceptionUtil.unwrapThrowable(t);
			}
		} catch (Throwable t) {
			throw new ReflectionException("Could not set property '" + prop.getName() + "' of '" + object.getClass()
					+ "' with value '" + value + "' Cause: " + t.toString(), t);
		}
	}
}
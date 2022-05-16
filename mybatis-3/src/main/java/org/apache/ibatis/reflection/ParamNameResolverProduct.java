package org.apache.ibatis.reflection;


import java.util.Map;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.binding.MapperMethod.ParamMap;

import java.util.Map;
import java.util.SortedMap;

public class ParamNameResolverProduct {
	private final boolean useActualParamName;
	private boolean hasParamAnnotation;

	public ParamNameResolverProduct(boolean isUseActualParamName) {
		this.useActualParamName = isUseActualParamName;
	}

	public boolean getUseActualParamName() {
		return useActualParamName;
	}

	public void setHasParamAnnotation(boolean hasParamAnnotation) {
		this.hasParamAnnotation = hasParamAnnotation;
	}

	/**
	* <p> A single non-special parameter is returned without a name. Multiple parameters are named using the naming rule. In addition to the default names, this method also adds the generic names (param1, param2, ...). </p>
	* @param args the args
	* @return  the named params
	*/
	public Object getNamedParams(Object[] args, SortedMap<Integer, String> thisNames) {
		final int paramCount = thisNames.size();
		if (args == null || paramCount == 0) {
			return null;
		} else if (!hasParamAnnotation && paramCount == 1) {
			Object value = args[thisNames.firstKey()];
			return ParamNameResolver.wrapToMapIfCollection(value, useActualParamName ? thisNames.get(0) : null);
		} else {
			final Map<String, Object> param = new ParamMap<>();
			int i = 0;
			for (Map.Entry<Integer, String> entry : thisNames.entrySet()) {
				param.put(entry.getValue(), args[entry.getKey()]);
				final String genericParamName = ParamNameResolver.GENERIC_NAME_PREFIX + (i + 1);
				if (!thisNames.containsValue(genericParamName)) {
					param.put(genericParamName, args[entry.getKey()]);
				}
				i++;
			}
			return param;
		}
	}
}
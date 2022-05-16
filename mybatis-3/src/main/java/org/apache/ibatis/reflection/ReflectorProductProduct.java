package org.apache.ibatis.reflection;


import java.lang.reflect.Method;
import java.util.Map;
import java.util.Map.Entry;
import java.util.List;
import java.util.HashMap;
import java.util.Arrays;
import org.apache.ibatis.reflection.property.PropertyNamer;
import org.apache.ibatis.util.MapUtil;
import java.util.ArrayList;

public class ReflectorProductProduct {
	public void addGetMethods(Method[] methods, ReflectorProduct reflectorProduct) {
		Map<String, List<Method>> conflictingGetters = new HashMap<>();
		Arrays.stream(methods).filter(m -> m.getParameterTypes().length == 0 && PropertyNamer.isGetter(m.getName()))
				.forEach(m -> addMethodConflict(conflictingGetters, PropertyNamer.methodToProperty(m.getName()), m));
		resolveGetterConflicts(conflictingGetters, reflectorProduct);
	}

	public void resolveGetterConflicts(Map<String, List<Method>> conflictingGetters,
			ReflectorProduct reflectorProduct) {
		for (Entry<String, List<Method>> entry : conflictingGetters.entrySet()) {
			Method winner = winner(reflectorProduct, entry);
		}
	}

	private Method winner(ReflectorProduct reflectorProduct, Map.Entry<String, List<Method>> entry) {
		boolean isAmbiguous = isAmbiguous(reflectorProduct, entry);
		Method winner = null;
		for (Method candidate : entry.getValue()) {
			if (winner == null) {
				winner = candidate;
				continue;
			}
			Class<?> winnerType = winner.getReturnType();
			Class<?> candidateType = candidate.getReturnType();
			if (candidateType.equals(winnerType)) {
				if (!boolean.class.equals(candidateType)) {
					break;
				} else if (candidate.getName().startsWith("is")) {
					winner = candidate;
				}
			} else if (candidateType.isAssignableFrom(winnerType)) {
			} else if (winnerType.isAssignableFrom(candidateType)) {
				winner = candidate;
			} else {
				break;
			}
		}
		return winner;
	}

	private boolean isAmbiguous(ReflectorProduct reflectorProduct, Map.Entry<String, List<Method>> entry) {
		Method winner = null;
		String propName = entry.getKey();
		boolean isAmbiguous = false;
		for (Method candidate : entry.getValue()) {
			if (winner == null) {
				winner = candidate;
				continue;
			}
			Class<?> winnerType = winner.getReturnType();
			Class<?> candidateType = candidate.getReturnType();
			if (candidateType.equals(winnerType)) {
				if (!boolean.class.equals(candidateType)) {
					isAmbiguous = true;
					break;
				} else if (candidate.getName().startsWith("is")) {
					winner = candidate;
				}
			} else if (candidateType.isAssignableFrom(winnerType)) {
			} else if (winnerType.isAssignableFrom(candidateType)) {
				winner = candidate;
			} else {
				isAmbiguous = true;
				break;
			}
		}
		reflectorProduct.addGetMethod(propName, winner, isAmbiguous);
		return isAmbiguous;
	}

	public void addMethodConflict(Map<String, List<Method>> conflictingMethods, String name, Method method) {
		if (isValidPropertyName(name)) {
			List<Method> list = MapUtil.computeIfAbsent(conflictingMethods, name, k -> new ArrayList<>());
			list.add(method);
		}
	}

	public boolean isValidPropertyName(String name) {
		return !(name.startsWith("$") || "serialVersionUID".equals(name) || "class".equals(name));
	}

	public void addSetMethods(Method[] methods, ReflectorProduct reflectorProduct) {
		Map<String, List<Method>> conflictingSetters = new HashMap<>();
		Arrays.stream(methods).filter(m -> m.getParameterTypes().length == 1 && PropertyNamer.isSetter(m.getName()))
				.forEach(m -> addMethodConflict(conflictingSetters, PropertyNamer.methodToProperty(m.getName()), m));
		reflectorProduct.resolveSetterConflicts(conflictingSetters);
	}
}
package org.apache.ibatis.session;


import java.util.Map;
import org.apache.ibatis.mapping.ParameterMap;
import org.apache.ibatis.session.Configuration.StrictMap;
import java.util.Collection;

public class ConfigurationProduct4 {
	private final Map<String, ParameterMap> parameterMaps = new StrictMap<>("Parameter Maps collection");

	public Collection<String> getParameterMapNames() {
		return parameterMaps.keySet();
	}

	public Collection<ParameterMap> getParameterMaps() {
		return parameterMaps.values();
	}

	public ParameterMap getParameterMap(String id) {
		return parameterMaps.get(id);
	}

	public boolean hasParameterMap(String id) {
		return parameterMaps.containsKey(id);
	}

	public void addParameterMap(ParameterMap pm) {
		parameterMaps.put(pm.getId(), pm);
	}
}
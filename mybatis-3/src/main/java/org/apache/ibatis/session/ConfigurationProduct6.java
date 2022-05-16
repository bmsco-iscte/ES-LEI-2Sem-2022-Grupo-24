package org.apache.ibatis.session;


import java.util.Map;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.Configuration.StrictMap;
import java.util.Collection;
import org.apache.ibatis.builder.xml.XMLStatementBuilder;
import java.util.LinkedList;
import org.apache.ibatis.builder.CacheRefResolver;
import org.apache.ibatis.builder.annotation.MethodResolver;

public class ConfigurationProduct6 {
	private ConfigurationProduct6Product configurationProduct6Product = new ConfigurationProduct6Product();
	private final Map<String, MappedStatement> mappedStatements = new StrictMap<MappedStatement>(
			"Mapped Statements collection")
			.conflictMessageProducer((savedValue, targetValue) -> ". please check " + savedValue.getResource() + " and "
					+ targetValue.getResource());
	public Collection<XMLStatementBuilder> getIncompleteStatements() {
		return configurationProduct6Product.getIncompleteStatements();
	}

	public Collection<CacheRefResolver> getIncompleteCacheRefs() {
		return configurationProduct6Product.getIncompleteCacheRefs();
	}

	public Collection<MethodResolver> getIncompleteMethods() {
		return configurationProduct6Product.getIncompleteMethods();
	}

	public void addMappedStatement(MappedStatement ms) {
		mappedStatements.put(ms.getId(), ms);
	}

	public Collection<String> getMappedStatementNames(Configuration configuration) {
		configurationProduct6Product.buildAllStatements(configuration);
		return mappedStatements.keySet();
	}

	public Collection<MappedStatement> getMappedStatements(Configuration configuration) {
		configurationProduct6Product.buildAllStatements(configuration);
		return mappedStatements.values();
	}

	public MappedStatement getMappedStatement(String id, boolean validateIncompleteStatements,
			Configuration configuration) {
		if (validateIncompleteStatements) {
			configurationProduct6Product.buildAllStatements(configuration);
		}
		return mappedStatements.get(id);
	}

	public boolean hasStatement(String statementName, boolean validateIncompleteStatements,
			Configuration configuration) {
		if (validateIncompleteStatements) {
			configurationProduct6Product.buildAllStatements(configuration);
		}
		return mappedStatements.containsKey(statementName);
	}

	public void buildAllStatements(Configuration configuration) {
		configurationProduct6Product.buildAllStatements(configuration);
	}
}
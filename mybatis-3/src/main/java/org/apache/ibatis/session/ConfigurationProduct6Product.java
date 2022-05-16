package org.apache.ibatis.session;


import java.util.Collection;
import org.apache.ibatis.builder.xml.XMLStatementBuilder;
import java.util.LinkedList;
import org.apache.ibatis.builder.CacheRefResolver;
import org.apache.ibatis.builder.annotation.MethodResolver;

public class ConfigurationProduct6Product {
	private final Collection<XMLStatementBuilder> incompleteStatements = new LinkedList<>();
	private final Collection<CacheRefResolver> incompleteCacheRefs = new LinkedList<>();
	private final Collection<MethodResolver> incompleteMethods = new LinkedList<>();

	public Collection<XMLStatementBuilder> getIncompleteStatements() {
		return incompleteStatements;
	}

	public Collection<CacheRefResolver> getIncompleteCacheRefs() {
		return incompleteCacheRefs;
	}

	public Collection<MethodResolver> getIncompleteMethods() {
		return incompleteMethods;
	}

	public void buildAllStatements(Configuration configuration) {
		configuration.parsePendingResultMaps();
		if (!incompleteCacheRefs.isEmpty()) {
			synchronized (incompleteCacheRefs) {
				incompleteCacheRefs.removeIf(x -> x.resolveCacheRef() != null);
			}
		}
		if (!incompleteStatements.isEmpty()) {
			synchronized (incompleteStatements) {
				incompleteStatements.removeIf(x -> {
					x.parseStatementNode();
					return true;
				});
			}
		}
		if (!incompleteMethods.isEmpty()) {
			synchronized (incompleteMethods) {
				incompleteMethods.removeIf(x -> {
					x.resolve();
					return true;
				});
			}
		}
	}
}
package org.apache.ibatis.session;


import org.apache.ibatis.scripting.LanguageDriverRegistry;
import org.apache.ibatis.scripting.LanguageDriver;
import org.apache.ibatis.scripting.xmltags.XMLLanguageDriver;

public class ConfigurationProduct5 {
	private final LanguageDriverRegistry languageRegistry = new LanguageDriverRegistry();

	public LanguageDriverRegistry getLanguageRegistry() {
		return languageRegistry;
	}

	public void setDefaultScriptingLanguage(Class<? extends LanguageDriver> driver) {
		if (driver == null) {
			driver = XMLLanguageDriver.class;
		}
		languageRegistry.setDefaultDriverClass(driver);
	}

	/**
	* Gets the language driver.
	* @param langClass the lang class
	* @return  the language driver
	* @since  3.5.1
	*/
	public LanguageDriver getLanguageDriver(Class<? extends LanguageDriver> langClass) {
		if (langClass == null) {
			return languageRegistry.getDefaultDriver();
		}
		languageRegistry.register(langClass);
		return languageRegistry.getDriver(langClass);
	}
}
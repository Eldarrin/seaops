package io.eldarrin.seaops.common.config;

import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

public class ConfigRetrieverHelper {
	
	private static final String NAME = "name";
	private static final String NAMESPACE = "namespace";
	private static final String HOCON = "hocon";
	private static final String CONFIGMAP = "configmap";
	private static final String OPTIONAL = "optional";

	private static final Logger logger = LoggerFactory.getLogger(ConfigRetrieverHelper.class);
	
	public ConfigRetrieverOptions getOptions(String namespaceName, String configMapName) {
		logger.info("start cr");
		ConfigRetrieverOptions configRetrieverOptions = new ConfigRetrieverOptions();
		logger.info("new options");
		if (System.getenv().containsKey("OPENSHIFT_BUILD_NAMESPACE")) {
			logger.info("in if statemtn");
			ConfigStoreOptions kubeConfig = new ConfigStoreOptions()
					.setType(CONFIGMAP)
					.setFormat(HOCON)
					.setConfig(new JsonObject()
							.put(OPTIONAL, true)
							.put(NAMESPACE, namespaceName)
							.put(NAME, configMapName));
			logger.info("near end");
			configRetrieverOptions.addStore(kubeConfig); // Values here will override identical keys from above
			logger.info(configRetrieverOptions.toJson());
		}
		logger.info("at end");
		return configRetrieverOptions;
	}

}

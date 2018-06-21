package me.yevgeny.q2jwxmigrator.utilities;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.logging.Logger;

public class ConfigurationManager {
    private final static String propertiesFileName = "/configuration.properties";
    private static final Logger logger = Logger.getLogger(ConfigurationManager.class.getSimpleName());
    private static ConfigurationManager ourInstance;
    private Properties properties;

    public static ConfigurationManager getInstance() throws FileNotFoundException {
        if (null == ourInstance) {
            ourInstance = new ConfigurationManager();
            ourInstance.properties = new Properties();
            ourInstance.loadConfiguration();
        }

        return ourInstance;
    }

    public String getConfigurationValue(String key) throws NoSuchElementException {
        String value = ourInstance.properties.getProperty(key);
        if (null == value) {
            throw new NoSuchElementException(String.format("Couldn't find \"%s\" in \"%s\"", key,propertiesFileName));
        }

        logger.info(String.format("Current value for \"%s\" is: \"%s\"", key, value));
        return value;
    }

    private void loadConfiguration() throws FileNotFoundException {
        try (InputStream input = ConfigurationManager.class.getResourceAsStream(propertiesFileName)) {
            ourInstance.properties.load(input);
        } catch (IOException e) {
            throw new FileNotFoundException(String.format("Couldn't find configuration file named:  \"%s\"\n%s",
                    propertiesFileName, e.getMessage()));
        }
    }

    private ConfigurationManager() {
    }
}

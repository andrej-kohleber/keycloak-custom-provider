package com.example.provider;

import org.keycloak.component.ComponentModel;
import org.keycloak.component.ComponentValidationException;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.provider.ProviderConfigProperty;
import org.keycloak.provider.ProviderConfigurationBuilder;
import org.keycloak.storage.UserStorageProviderFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;


public class CustomUserStorageProviderFactory implements UserStorageProviderFactory<CustomUserStorageProvider> {

    private static final Logger log = LoggerFactory.getLogger(CustomUserStorageProviderFactory.class);
    protected final List<ProviderConfigProperty> configMetadata;
    
    public CustomUserStorageProviderFactory() {
        this.configMetadata = ProviderConfigurationBuilder.create()
                .property()
                .name(Constants.JDBC_DRIVER)
                .label("JDBC Driver Class")
                .type(ProviderConfigProperty.STRING_TYPE)
                .defaultValue(Constants.JDBC_DRIVER)
                .helpText("Fully qualified class name of the JDBC driver")
                .add()
                .property()
                .name(Constants.JDBC_URL)
                .label("JDBC URL")
                .type(ProviderConfigProperty.STRING_TYPE)
                .defaultValue(Constants.JDBC_URL)
                .helpText("JDBC URL used to connect to the user database")
                .add()
                .property()
                .name(Constants.DB_USERNAME)
                .label("Database User")
                .defaultValue(Constants.DB_USERNAME)
                .type(ProviderConfigProperty.STRING_TYPE)
                .helpText("Username used to connect to the database")
                .add()
                .property()
                .name(Constants.DB_PASSWORD)
                .label("Database Password")
                .type(ProviderConfigProperty.STRING_TYPE)
                .defaultValue(Constants.DB_PASSWORD)
                .helpText("Password used to connect to the database")
                .secret(true)
                .add()
                .property()
                .name(Constants.VALIDATION_QUERY)
                .label("SQL Validation Query")
                .type(ProviderConfigProperty.STRING_TYPE)
                .helpText("SQL query used to validate a connection")
                .defaultValue("select 1")
                .add()
                .build();
    }

    @Override
    public CustomUserStorageProvider create(KeycloakSession keycloakSession, ComponentModel componentModel) {
        log.info("[I63] creating new CustomUserStorageProvider");
        return new CustomUserStorageProvider(keycloakSession, componentModel);
    }

    @Override
    public String getId() {
        log.info("[I69] getId()");
        return "custom-user-provider";
    }

    @Override
    public List<ProviderConfigProperty> getConfigProperties() {
        return configMetadata;
    }

    @Override
    public void validateConfiguration(KeycloakSession session, RealmModel realm, ComponentModel config) throws ComponentValidationException {
        try (Connection connection = DbUtil.getConnection(config)) {
            log.info("[I84] Testing connection...");
            connection.createStatement().execute(config.get(Constants.VALIDATION_QUERY));
            log.info("[I92] Connection OK !");
        } catch (SQLException ex) {
            log.warn("[W94] Unable to validate connection: ex={}", ex.getMessage());
            throw new ComponentValidationException("Unable to validate database connection", ex);
        }
    }

    @Override
    public void onUpdate(KeycloakSession session, RealmModel realm, ComponentModel oldModel, ComponentModel newModel) {
        log.info("[I94] onUpdate()" );
    }

    @Override
    public void onCreate(KeycloakSession session, RealmModel realm, ComponentModel model) {
        log.info("[I99] onCreate()" );
    }
}

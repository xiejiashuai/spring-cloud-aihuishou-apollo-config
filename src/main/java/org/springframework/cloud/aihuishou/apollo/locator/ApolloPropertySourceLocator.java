package org.springframework.cloud.aihuishou.apollo.locator;

import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.ConfigService;
import com.ctrip.framework.apollo.spring.config.ConfigPropertySourceFactory;
import com.ctrip.framework.apollo.spring.config.PropertySourcesConstants;
import com.ctrip.framework.apollo.spring.util.SpringInjector;
import org.springframework.cloud.aihuishou.apollo.ApolloProperties;
import org.springframework.cloud.bootstrap.config.PropertySourceLocator;
import org.springframework.core.Ordered;
import org.springframework.core.env.CompositePropertySource;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertySource;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * Apollo Implement {@link PropertySourceLocator}
 *
 * @author jiashuai.xie
 * @since 2019/1/6 13:05 1.0.0.RELEASE
 */
public class ApolloPropertySourceLocator implements PropertySourceLocator,Ordered {

    private int order = Ordered.HIGHEST_PRECEDENCE + 10;

    private final ApolloProperties apolloProperties;

    private final ConfigPropertySourceFactory configPropertySourceFactory = SpringInjector.getInstance(ConfigPropertySourceFactory.class);

    public ApolloPropertySourceLocator(ApolloProperties apolloProperties) {
        this.apolloProperties = apolloProperties;
    }

    @Override
    public PropertySource<?> locate(Environment environment) {

        ConfigurableEnvironment configurableEnvironment = (ConfigurableEnvironment) environment;

        initializeSystemProperty(environment);

        if (configurableEnvironment.getPropertySources().contains(PropertySourcesConstants.APOLLO_BOOTSTRAP_PROPERTY_SOURCE_NAME)) {
            //already initialized
            return null;
        }

        List<String> namespaces = apolloProperties.getNamespaces();

        CompositePropertySource apolloPropertySource = new CompositePropertySource(PropertySourcesConstants.APOLLO_BOOTSTRAP_PROPERTY_SOURCE_NAME);

        for (String namespace : namespaces) {
            Config config = ConfigService.getConfig(namespace);
            apolloPropertySource.addPropertySource(configPropertySourceFactory.getConfigPropertySource(namespace, config));
        }
        return apolloPropertySource;
    }

    private void initializeSystemProperty(Environment environment) {

        String appId = apolloProperties.getAppId();

        if (!StringUtils.hasText(appId)) {
            appId = environment.getProperty("spring.application.name", "");
        }

        if (!StringUtils.hasText(appId)) {
            throw new IllegalArgumentException("app-id must have value,you can configure it by spring.application.name or spring.cloud.apollo.config.app-id");
        }

        System.setProperty("app.id", appId);

    }

    @Override
    public int getOrder() {
        return order;
    }
}

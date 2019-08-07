/*
 * Copyright (C) 2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.framework.cloud.apollo.locator;

import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.ConfigService;
import com.ctrip.framework.apollo.spring.config.ConfigPropertySourceFactory;
import com.ctrip.framework.apollo.spring.config.PropertySourcesConstants;
import com.ctrip.framework.apollo.spring.util.SpringInjector;
import org.framework.cloud.apollo.ApolloProperties;
import org.springframework.cloud.bootstrap.config.PropertySourceLocator;
import org.springframework.core.Ordered;
import org.springframework.core.env.*;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.framework.cloud.apollo.ApolloProperties.APOLLO_PROPERTIES_PREFIX;

/**
 * Apollo Implement {@link PropertySourceLocator}
 *
 * @author <a href="mailto:jiashuai.xie01@gmail.com">xiejiashuai</a>
 * @since 2019/1/6 13:05 1.0.0.RELEASE
 */
public class ApolloPropertySourceLocator implements PropertySourceLocator, Ordered {

    private int order = Ordered.HIGHEST_PRECEDENCE + 10;

    private final ApolloProperties apolloProperties;

    private final ConfigPropertySourceFactory configPropertySourceFactory = SpringInjector.getInstance(ConfigPropertySourceFactory.class);

    public ApolloPropertySourceLocator(ApolloProperties apolloProperties) {
        this.apolloProperties = apolloProperties;
    }

    @Override
    public PropertySource<?> locate(Environment environment) {

        ConfigurableEnvironment configurableEnvironment = (ConfigurableEnvironment) environment;

        if (configurableEnvironment.getPropertySources().contains(PropertySourcesConstants.APOLLO_BOOTSTRAP_PROPERTY_SOURCE_NAME)) {
            //already initialized
            return null;
        }

        initializeSystemProperty(environment);

        List<String> namespaces = apolloProperties.getNamespaces();

        CompositePropertySource apolloPropertySource = new CompositePropertySource(PropertySourcesConstants.APOLLO_BOOTSTRAP_PROPERTY_SOURCE_NAME);

        for (String namespace : namespaces) {
            Config config = ConfigService.getConfig(namespace);
            apolloPropertySource.addPropertySource(configPropertySourceFactory.getConfigPropertySource(namespace, config));
        }

        Map<String,Object> enableBootstrapConfigMap =new HashMap<>();
        enableBootstrapConfigMap.put(PropertySourcesConstants.APOLLO_BOOTSTRAP_ENABLED,true);
        MapPropertySource enableBootstrapPropertySource = new MapPropertySource("apollo-bootstrap",enableBootstrapConfigMap);
        apolloPropertySource.addFirstPropertySource(enableBootstrapPropertySource);

        return apolloPropertySource;
    }

    private void initializeSystemProperty(Environment environment) {

        String appId = apolloProperties.getAppId();

        if (!StringUtils.hasText(appId)) {
            appId = environment.getProperty("spring.application.name", "");
        }

        if (!StringUtils.hasText(appId)) {
            throw new IllegalArgumentException(
                    "app-id must have value,you can configure it by spring.application.name or " + APOLLO_PROPERTIES_PREFIX + "app-id"
            );
        }

        System.setProperty("app.id", appId);

    }

    @Override
    public int getOrder() {
        return order;
    }
}

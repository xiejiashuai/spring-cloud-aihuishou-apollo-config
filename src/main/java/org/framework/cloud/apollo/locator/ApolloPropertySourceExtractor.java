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

import com.ctrip.framework.apollo.spring.config.ConfigPropertySource;
import org.springframework.beans.BeansException;
import org.springframework.cloud.bootstrap.BootstrapApplicationListener;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.env.CompositePropertySource;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertySource;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static com.ctrip.framework.apollo.spring.config.PropertySourcesConstants.APOLLO_BOOTSTRAP_PROPERTY_SOURCE_NAME;

/**
 * Apollo Property Source
 *
 * @author <a href="mailto:jiashuai.xie01@gmail.com">xiejiashuai</a>
 * @since 2019/1/8 15:30 1.0.0.RELEASE
 */
public class ApolloPropertySourceExtractor implements ApplicationContextAware {


    public static final String BOOTSTRAP_PROPERTY_SOURCE_NAME = BootstrapApplicationListener.BOOTSTRAP_PROPERTY_SOURCE_NAME
            + "Properties";

    private ConfigurableEnvironment environment;

    public Map<String, Map<String, Object>> getApolloPropertySource() {

        Map<String, Map<String, Object>> propsMap = new HashMap<>(16);

        CompositePropertySource bootstrapPropertySource = (CompositePropertySource) environment.getPropertySources().get(BOOTSTRAP_PROPERTY_SOURCE_NAME);

        if (null != bootstrapPropertySource) {

            Collection<PropertySource<?>> propertySources = bootstrapPropertySource.getPropertySources();

            propertySources.stream().filter(propertySource -> APOLLO_BOOTSTRAP_PROPERTY_SOURCE_NAME.contains(propertySource.getName())).findFirst().ifPresent(propertySource -> {

                CompositePropertySource apolloPropertySource = (CompositePropertySource) propertySource;

                apolloPropertySource.getPropertySources().forEach(source -> {

                    if (source instanceof ConfigPropertySource) {
                        Map<String, Object> map = new HashMap<>(16);
                        ConfigPropertySource configPropertySource = (ConfigPropertySource) source;
                        Stream.of(configPropertySource.getPropertyNames()).forEach(name -> {
                            Object value = configPropertySource.getProperty(name);
                            map.put(name, value);
                        });
                        propsMap.put(configPropertySource.getName(), map);
                    }
                });
            });
        }
        return propsMap;
    }

    public Map<String, Object> getByPropertyName(String propertyName) {

        Map<String, Object> propsMap = new HashMap<>(16);

        CompositePropertySource bootstrapPropertySource = (CompositePropertySource) environment.getPropertySources().get(BOOTSTRAP_PROPERTY_SOURCE_NAME);

        if (null != bootstrapPropertySource) {

            Collection<PropertySource<?>> propertySources = bootstrapPropertySource.getPropertySources();

            propertySources.stream().filter(propertySource -> APOLLO_BOOTSTRAP_PROPERTY_SOURCE_NAME.contains(propertySource.getName())).findFirst().ifPresent(propertySource -> {

                CompositePropertySource apolloPropertySource = (CompositePropertySource) propertySource;

                apolloPropertySource.getPropertySources().forEach(source -> {

                    if (source instanceof ConfigPropertySource) {

                        ConfigPropertySource configPropertySource = (ConfigPropertySource) source;

                        if (configPropertySource.containsProperty(propertyName)) {
                            propsMap.put(configPropertySource.getName(), configPropertySource.getProperty(propertyName));
                        }
                    }
                });
            });
        }
        return propsMap;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.environment = (ConfigurableEnvironment) applicationContext.getEnvironment();
    }
}

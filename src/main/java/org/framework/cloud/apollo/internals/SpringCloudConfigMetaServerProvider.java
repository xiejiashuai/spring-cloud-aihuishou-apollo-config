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
package org.framework.cloud.apollo.internals;

import com.ctrip.framework.apollo.core.enums.Env;
import com.ctrip.framework.apollo.core.spi.MetaServerProvider;
import com.google.common.collect.Lists;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.env.PropertySourceLoader;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.SpringFactoriesLoader;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ctrip.framework.apollo.core.internals.LegacyMetaServerProvider.ORDER;
import static org.framework.cloud.apollo.ApolloProperties.APOLLO_PROPERTIES_PREFIX;
import static org.framework.cloud.apollo.ApolloProperties.META_SERVER_ADDR;

/**
 * Spring Cloud Implement {@link MetaServerProvider}
 *
 * @author <a href="mailto:jiashuai.xie01@gmail.com">xiejiashuai</a>
 * @since 2019/1/6 12:59 1.0.0.RELEASE
 */
public class SpringCloudConfigMetaServerProvider implements MetaServerProvider {

    private static final int DEFAULT_ORDER = ORDER + 1;

    private static final String DEFAULT_NAMES = "bootstrap";

    private static final String DOT = ".";

    private static final Map<String, String> DOMAIN = new HashMap<>();

    public SpringCloudConfigMetaServerProvider() {

        String configFileName = getConfigFileName();

        List<PropertySourceLoader> propertySourceLoaders = SpringFactoriesLoader.loadFactories(PropertySourceLoader.class, getClass().getClassLoader());

        AnnotationAwareOrderComparator.sort(propertySourceLoaders);

        StandardEnvironment environment = new StandardEnvironment();

        MutablePropertySources mutablePropertySources = environment.getPropertySources();

        for (PropertySourceLoader propertySourceLoader : propertySourceLoaders) {

            String[] fileExtensions = propertySourceLoader.getFileExtensions();

            ArrayList<String> fileExtensionList = Lists.newArrayList(fileExtensions);

            for (String fileExtension : fileExtensionList) {

                String fullConfigFileName = configFileName + DOT +fileExtension;

                ClassPathResource bootstrapResource = new ClassPathResource(fullConfigFileName);

                List<PropertySource<?>> propertySources;
                try {

                    propertySources = propertySourceLoader.load(fullConfigFileName, bootstrapResource);

                } catch (Exception e) {
                    // ignore
                    continue;
                }

                propertySources.forEach(mutablePropertySources::addFirst);

            }
        }
        Binder.get(environment)
                .bind(APOLLO_PROPERTIES_PREFIX + DOT + META_SERVER_ADDR, Map.class)
                .ifBound(DOMAIN::putAll);
    }

    @Override
    public String getMetaServerAddress(Env targetEnv) {
        String metaServerAddress = DOMAIN.get(targetEnv.toString().toLowerCase());
        return metaServerAddress == null ? null : metaServerAddress.trim();
    }

    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }

    private String getConfigFileName() {

        // first get from system properties
        String configFileName = System.getProperty("apollo.config-file.name");

        if (!StringUtils.hasText(configFileName)) {
            // first get from system env
            configFileName = System.getenv("apollo.config-file.name");
        }

        if (!StringUtils.hasText(configFileName)) {
            // first get from system env
            configFileName = DEFAULT_NAMES;
        }

        return configFileName;
    }
}

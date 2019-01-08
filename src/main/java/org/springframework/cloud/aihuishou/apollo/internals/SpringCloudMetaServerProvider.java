package org.springframework.cloud.aihuishou.apollo.internals;

import com.ctrip.framework.apollo.core.enums.Env;
import com.ctrip.framework.apollo.core.spi.MetaServerProvider;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import static org.springframework.cloud.aihuishou.apollo.ApolloProperties.APOLLO_PROPERTIES_PREFIX;
import static org.springframework.cloud.aihuishou.apollo.ApolloProperties.META_SERVER_ADDR;

/**
 * Spring Cloud Implement {@link MetaServerProvider}
 *
 * @author jiashuai.xie
 * @since 2019/1/6 12:59 1.0.0.RELEASE
 */
public class SpringCloudMetaServerProvider implements MetaServerProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpringCloudMetaServerProvider.class);

    private static final int ORDER = MetaServerProvider.HIGHEST_PRECEDENCE + 5;

    private static final String DEFAULT_NAMES = "bootstrap.";

    private static final Map<String, String> DOMAIN = new HashMap<>();

    public SpringCloudMetaServerProvider() {


        List<PropertySourceLoader> propertySourceLoaders = SpringFactoriesLoader.loadFactories(PropertySourceLoader.class, getClass().getClassLoader());

        AnnotationAwareOrderComparator.sort(propertySourceLoaders);

        StandardEnvironment environment = new StandardEnvironment();
        MutablePropertySources mutablePropertySources = environment.getPropertySources();

        for (PropertySourceLoader propertySourceLoader : propertySourceLoaders) {

            String[] fileExtensions = propertySourceLoader.getFileExtensions();

            ArrayList<String> fileExtensionList = Lists.newArrayList(fileExtensions);

            for (String fileExtension : fileExtensionList) {

                ClassPathResource bootstrapResource = new ClassPathResource(DEFAULT_NAMES + fileExtension);

                List<PropertySource<?>> propertySources;
                try {

                    propertySources = propertySourceLoader.load(DEFAULT_NAMES + fileExtension, bootstrapResource);

                } catch (Exception e) {
                    // ignore
                    continue;
                }
                propertySources.forEach(mutablePropertySources::addFirst);
            }
        }

        Map<String, String> propMap = Binder.get(environment).bind(APOLLO_PROPERTIES_PREFIX + "." + META_SERVER_ADDR, Map.class).get();
        DOMAIN.putAll(propMap);
    }

    @Override
    public String getMetaServerAddress(Env targetEnv) {
        String metaServerAddress = DOMAIN.get(targetEnv.toString().toLowerCase());
        return metaServerAddress == null ? null : metaServerAddress.trim();
    }

    @Override
    public int getOrder() {
        return ORDER;
    }

}

package org.springframework.cloud.aihuishou.apollo.autoconfigure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.endpoint.condition.ConditionalOnEnabledEndpoint;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.cloud.aihuishou.apollo.ApolloProperties;
import org.springframework.cloud.aihuishou.apollo.endpoint.ApolloConfigEndpoint;
import org.springframework.cloud.aihuishou.apollo.locator.ApolloPropertySourceExtractor;
import org.springframework.cloud.aihuishou.apollo.refresh.ApolloConfigRefreshHistory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ApolloConfigEndpoint auto configuration
 *
 * @author jiashuai.xie
 * @since 2019/1/8 16:03 1.0.0.RELEASE
 */
@Configuration
@ConditionalOnWebApplication
@ConditionalOnClass(value = Endpoint.class)
public class ApolloConfigEndpointAutoConfiguration {

    @Autowired
    private ApolloProperties apolloProperties;

    @Autowired
    private ApolloConfigRefreshHistory refreshHistory;

    @ConditionalOnMissingBean
    @ConditionalOnEnabledEndpoint
    @Bean
    public ApolloConfigEndpoint apolloConfigEndpoint(ApolloPropertySourceExtractor apolloPropertySourceExtractor) {
        return new ApolloConfigEndpoint(apolloProperties, refreshHistory, apolloPropertySourceExtractor);
    }


}

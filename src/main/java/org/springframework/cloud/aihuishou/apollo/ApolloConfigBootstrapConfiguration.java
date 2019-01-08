package org.springframework.cloud.aihuishou.apollo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.aihuishou.apollo.locator.ApolloPropertySourceExtractor;
import org.springframework.cloud.aihuishou.apollo.locator.ApolloPropertySourceLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author jiashuai.xie
 * @since 2019/1/6 12:56 1.0.0.RELEASE
 */
@Configuration
@EnableConfigurationProperties(ApolloProperties.class)
public class ApolloConfigBootstrapConfiguration {

    @Autowired
    private ApolloProperties apolloProperties;

    @Bean
    public ApolloPropertySourceLocator apolloPropertySourceLocator() {
        return new ApolloPropertySourceLocator(apolloProperties);
    }



}

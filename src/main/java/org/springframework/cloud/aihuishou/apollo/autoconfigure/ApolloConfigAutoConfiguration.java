package org.springframework.cloud.aihuishou.apollo.autoconfigure;

import org.springframework.cloud.aihuishou.apollo.ApolloProperties;
import org.springframework.cloud.aihuishou.apollo.locator.ApolloPropertySourceExtractor;
import org.springframework.cloud.aihuishou.apollo.refresh.ApolloConfigRefreshHistory;
import org.springframework.cloud.aihuishou.apollo.refresh.ApolloContextRefresher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Apollo Config
 *
 * @author jiashuai.xie
 * @since 2019/1/7 22:47 1.0.0.RELEASE
 */
@Configuration
public class ApolloConfigAutoConfiguration {

    @Bean
    public ApolloContextRefresher apolloContextRefresher(ApolloProperties apolloProperties, ApolloConfigRefreshHistory apolloConfigRefreshHistory) {
        return new ApolloContextRefresher(apolloProperties, apolloConfigRefreshHistory);
    }

    @Bean
    public ApolloConfigRefreshHistory apolloConfigRefreshHistory() {
        return new ApolloConfigRefreshHistory();
    }


    @Bean
    public ApolloPropertySourceExtractor apolloPropertySourceExtractor() {
        return new ApolloPropertySourceExtractor();
    }

}

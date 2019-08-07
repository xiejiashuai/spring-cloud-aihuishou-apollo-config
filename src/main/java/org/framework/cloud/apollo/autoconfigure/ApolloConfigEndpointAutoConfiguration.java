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
package org.framework.cloud.apollo.autoconfigure;

import org.framework.cloud.apollo.ApolloProperties;
import org.framework.cloud.apollo.endpoint.ApolloConfigEndpoint;
import org.framework.cloud.apollo.locator.ApolloPropertySourceExtractor;
import org.framework.cloud.apollo.refresh.ApolloConfigRefreshHistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.endpoint.condition.ConditionalOnEnabledEndpoint;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ApolloConfigEndpoint auto configuration
 *
 * @author <a href="mailto:jiashuai.xie01@gmail.com">xiejiashuai</a>
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

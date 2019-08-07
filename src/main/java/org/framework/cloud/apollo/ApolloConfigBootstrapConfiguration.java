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
package org.framework.cloud.apollo;

import org.framework.cloud.apollo.locator.ApolloPropertySourceLocator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author <a href="mailto:jiashuai.xie01@gmail.com">xiejiashuai</a>
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

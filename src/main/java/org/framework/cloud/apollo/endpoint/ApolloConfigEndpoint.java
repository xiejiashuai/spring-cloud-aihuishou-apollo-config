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
package org.framework.cloud.apollo.endpoint;

import org.framework.cloud.apollo.locator.ApolloPropertySourceExtractor;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.annotation.Selector;
import org.framework.cloud.apollo.ApolloProperties;
import org.framework.cloud.apollo.refresh.ApolloConfigRefreshHistory;

import java.util.HashMap;
import java.util.Map;

/**
 * Apollo Config Endpoint
 *
 * @author <a href="mailto:jiashuai.xie01@gmail.com">xiejiashuai</a>
 * @since 2019/1/8 15:57 1.0.0.RELEASE
 */
@Endpoint(id = "apollo-config")
public class ApolloConfigEndpoint {

    private final ApolloProperties properties;

    private final ApolloConfigRefreshHistory refreshHistory;

    private final ApolloPropertySourceExtractor propertySourceExtractor;

    public ApolloConfigEndpoint(ApolloProperties properties,
                                ApolloConfigRefreshHistory refreshHistory,
                                ApolloPropertySourceExtractor propertySourceExtractor) {
        this.properties = properties;
        this.refreshHistory = refreshHistory;
        this.propertySourceExtractor = propertySourceExtractor;
    }

    @ReadOperation
    public Map<String, Object> readAll() {
        Map<String, Object> result = new HashMap<>(16);
        result.put("Apollo-Config-Properties", properties);
        Map<String, Map<String, Object>> apolloPropertySources = propertySourceExtractor.getApolloPropertySource();
        result.put("Property-Source", apolloPropertySources);
        result.put("RefreshHistory", refreshHistory.getRecords());
        return result;
    }

    @ReadOperation
    public Map<String, Object> readToMatch(@Selector String propertyName) {
        return propertySourceExtractor.getByPropertyName(propertyName);
    }

}

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

import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

import static org.framework.cloud.apollo.ApolloProperties.APOLLO_PROPERTIES_PREFIX;
import static org.framework.cloud.apollo.ApolloProperties.META_SERVER_ADDR;

/**
 * apollo properties
 *
 * @author <a href="mailto:jiashuai.xie01@gmail.com">xiejiashuai</a>
 * @since 2019/1/6 12:51 1.0.0.RELEASE
 */
@ConfigurationProperties(prefix = APOLLO_PROPERTIES_PREFIX)
@ConditionalOnProperty(prefix = APOLLO_PROPERTIES_PREFIX, name = META_SERVER_ADDR)
@Data
public class ApolloProperties {

    public static final String APOLLO_PROPERTIES_PREFIX = "spring.cloud.config.apollo";

    public static final String META_SERVER_ADDR = "meta-server-addr";

    private String appId = "";

    private List<String> namespaces = new ArrayList<String>();

}

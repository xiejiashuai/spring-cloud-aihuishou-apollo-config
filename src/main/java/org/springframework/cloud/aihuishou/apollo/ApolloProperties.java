package org.springframework.cloud.aihuishou.apollo;

import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.cloud.aihuishou.apollo.ApolloProperties.APOLLO_PROPERTIES_PREFIX;
import static org.springframework.cloud.aihuishou.apollo.ApolloProperties.META_SERVER_ADDR;

/**
 * apollo properties
 *
 * @author jiashuai.xie
 * @since 2019/1/6 12:51 1.0.0.RELEASE
 */
@ConfigurationProperties(prefix = APOLLO_PROPERTIES_PREFIX)
@ConditionalOnProperty(prefix = APOLLO_PROPERTIES_PREFIX, name = META_SERVER_ADDR)
@Data
public class ApolloProperties {

    public static final String APOLLO_PROPERTIES_PREFIX = "spring.cloud.apollo.config";

    public static final String META_SERVER_ADDR = "meta-server-addr";

    private String appId = "";

    private List<String> namespaces = new ArrayList<String>();

}

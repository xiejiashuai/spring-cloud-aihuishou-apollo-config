package org.springframework.cloud.aihuishou.apollo.endpoint;

import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.annotation.Selector;
import org.springframework.cloud.aihuishou.apollo.ApolloProperties;
import org.springframework.cloud.aihuishou.apollo.locator.ApolloPropertySourceExtractor;
import org.springframework.cloud.aihuishou.apollo.refresh.ApolloConfigRefreshHistory;

import java.util.HashMap;
import java.util.Map;

/**
 * Apollo Config Endpoint
 *
 * @author jiashuai.xie
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

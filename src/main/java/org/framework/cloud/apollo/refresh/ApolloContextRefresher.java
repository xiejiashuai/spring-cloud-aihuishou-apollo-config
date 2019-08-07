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
package org.framework.cloud.apollo.refresh;

import com.ctrip.framework.apollo.ConfigService;
import com.ctrip.framework.apollo.model.ConfigChange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.properties.bind.Binder;
import org.framework.cloud.apollo.ApolloProperties;
import org.springframework.cloud.endpoint.event.RefreshEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.util.CollectionUtils;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.framework.cloud.apollo.ApolloProperties.APOLLO_PROPERTIES_PREFIX;

/**
 * On application start up, ApolloContextRefresher add apollo listeners to all namespace,
 * when there is a change in the data, listeners will refresh configurations.
 *
 * @author <a href="mailto:jiashuai.xie01@gmail.com">xiejiashuai</a>
 * @since 2019/1/7 19:24 1.0.0.RELEASE
 */
public class ApolloContextRefresher implements ApplicationContextAware, EnvironmentAware {

    private final static Logger LOGGER = LoggerFactory.getLogger(ApolloContextRefresher.class);

    private ConfigurableApplicationContext applicationContext;

    private final ApolloProperties apolloProperties;

    private final ApolloConfigRefreshHistory apolloConfigRefreshHistory;

    private ConfigurableEnvironment configurableEnvironment;

    private AtomicBoolean hasInitialized = new AtomicBoolean(false);

    public ApolloContextRefresher(ApolloProperties apolloProperties,
                                  ApolloConfigRefreshHistory apolloConfigRefreshHistory) {
        this.apolloProperties = apolloProperties;
        this.apolloConfigRefreshHistory = apolloConfigRefreshHistory;
    }


    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReadyEvent(ApplicationReadyEvent event) {

        // many Spring context
        ConfigurableApplicationContext configurableApplicationContext = event.getApplicationContext();

        // current application context
        if (null != configurableApplicationContext.getParent()
                && configurableApplicationContext.equals(applicationContext)
                && hasInitialized.compareAndSet(false, true)) {
            registerApolloListenersForApplications();
        }
    }

    private void registerApolloListenersForApplications() {

        List<String> namespaces = apolloProperties.getNamespaces();

        namespaces.forEach(namespace -> {

            Boolean refreshable = Binder.get(configurableEnvironment).bind(APOLLO_PROPERTIES_PREFIX + "." + "refreshable" + "." + namespace, Boolean.class).orElse(Boolean.FALSE);

            if (refreshable != null && refreshable) {
                ConfigService.getConfig(namespace).addChangeListener(changeEvent -> {

                    List<ConfigChange> configChanges = new ArrayList<>();

                    Set<String> changedKeys = changeEvent.changedKeys();
                    changedKeys.forEach(changedKey -> configChanges.add(changeEvent.getChange(changedKey)));

                    String md5 = "";
                    if (!CollectionUtils.isEmpty(configChanges)) {
                        try {
                            MessageDigest md = MessageDigest.getInstance("MD5");
                            md5 = new BigInteger(1, md.digest(configChanges.toString().getBytes("UTF-8")))
                                    .toString(16);
                        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
                            LOGGER.warn("[Apollo] unable to get md5 for namespace: " + namespace, e);
                        }
                    }

                    apolloConfigRefreshHistory.add(namespace, md5);

                    LOGGER.info("apollo config changed keys:{}", changedKeys);
                    applicationContext.publishEvent(new RefreshEvent(this, null, "Refresh Apollo config"));
                });
            }

        });


    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = (ConfigurableApplicationContext) applicationContext;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.configurableEnvironment = (ConfigurableEnvironment) environment;
    }
}

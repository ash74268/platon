/*
 * Copyright 2016-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.vorb.platon.web;

import de.vorb.platon.web.api.ApiConfig;
import de.vorb.platon.web.mvc.WebMvcConfig;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

@Configuration
public class WebConfig {

    @Bean
    public ServletRegistrationBean api() {
        return createServletRegistrationBean("API", ApiConfig.class, "/api/*");
    }

    @Bean
    public ServletRegistrationBean webMvc() {
        return createServletRegistrationBean("WebMVC", WebMvcConfig.class, "/*");
    }

    private ServletRegistrationBean createServletRegistrationBean(String beanName, Class<?> annotatedContextClass,
            String... urlMappings) {

        final DispatcherServlet dispatcherServlet = new DispatcherServlet();
        final AnnotationConfigWebApplicationContext applicationContext = new AnnotationConfigWebApplicationContext();

        applicationContext.register(annotatedContextClass);
        dispatcherServlet.setApplicationContext(applicationContext);

        final ServletRegistrationBean servletRegistrationBean =
                new ServletRegistrationBean(dispatcherServlet, urlMappings);
        servletRegistrationBean.setName(beanName);
        return servletRegistrationBean;
    }
}

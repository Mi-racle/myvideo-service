/*
 * Copyright 2017 the original author or authors.
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

package com.java.example.myvideo;

import java.util.Collections;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.hateoas.server.core.EvoInflectorLinkRelationProvider;

/**
 * @author Greg Turnquist
 */
@SpringBootApplication
public class MyVideoServiceApplication {

	public static void main(String... args) {
		SpringApplication app = new SpringApplication(MyVideoServiceApplication.class);
		app.setDefaultProperties(Collections.singletonMap("server.port", "8083"));
		app.run(args);
	}

	/**
	 * Format embedded collections by pluralizing the resource's type.
	 * 
	 * @return
	 */
	@Bean
	EvoInflectorLinkRelationProvider relProvider() {
		return new EvoInflectorLinkRelationProvider();
	}
}

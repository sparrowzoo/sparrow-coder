/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sparrowzoo.coder.domain.service.template;

import com.sparrowzoo.coder.domain.service.TemplateEngineer;

import java.util.Iterator;
import java.util.ServiceLoader;

public class TemplateEngineerProvider {
    private static final String DEFAULT_PROVIDER = "com.sparrowzoo.coder.domain.service.template.DefaultTemplateEngineer";
    private volatile static TemplateEngineer engineer;

    public static TemplateEngineer getEngineer() {
        if (engineer != null) {
            return engineer;
        }
        synchronized (TemplateEngineer.class) {
            if (engineer != null) {
                return engineer;
            }

            ServiceLoader<TemplateEngineer> loader = ServiceLoader.load(TemplateEngineer.class);
            Iterator<TemplateEngineer> it = loader.iterator();
            if (it.hasNext()) {
                engineer = it.next();
                return engineer;
            }

            try {
                Class<?> jsonClazz = Class.forName(DEFAULT_PROVIDER);
                engineer = (TemplateEngineer) jsonClazz.newInstance();
                return engineer;
            } catch (Exception x) {
                throw new RuntimeException(
                    "Provider " + DEFAULT_PROVIDER + " could not be instantiated: " + x,
                    x);
            }
        }
    }
}

/*
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.github.sakserv.minicluster.impl;

import com.github.sakserv.minicluster.config.ConfigVars;
import com.github.sakserv.minicluster.config.PropertyParser;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class ZookeeperLocalClusterIntegrationTest {

    // Logger
    private static final Logger LOG = LoggerFactory.getLogger(ZookeeperLocalClusterIntegrationTest.class);

    // Setup the property parser
    private static PropertyParser propertyParser;
    static {
        try {
            propertyParser = new PropertyParser(ConfigVars.DEFAULT_PROPS_FILE);
            propertyParser.parsePropsFile();
        } catch(IOException e) {
            LOG.error("Unable to load property file: " + propertyParser.getProperty(ConfigVars.DEFAULT_PROPS_FILE));
        }
    }

    private static ZookeeperLocalCluster zookeeperLocalCluster;
    @BeforeClass
    public static void setUp() throws Exception {
        zookeeperLocalCluster = new ZookeeperLocalCluster.Builder()
                .setPort(Integer.parseInt(propertyParser.getProperty(ConfigVars.ZOOKEEPER_PORT_KEY)))
                .setTempDir(propertyParser.getProperty(ConfigVars.ZOOKEEPER_TEMP_DIR_KEY))
                .setZookeeperConnectionString(propertyParser.getProperty(ConfigVars.ZOOKEEPER_CONNECTION_STRING_KEY))
                .build();
        zookeeperLocalCluster.start();
    }

    @AfterClass
    public static void tearDown() throws Exception {
        zookeeperLocalCluster.stop();
    }

    @Test
    public void testZookeeperCluster() {
        assertEquals(propertyParser.getProperty(ConfigVars.ZOOKEEPER_CONNECTION_STRING_KEY), 
                zookeeperLocalCluster.getZookeeperConnectionString());
    }
}

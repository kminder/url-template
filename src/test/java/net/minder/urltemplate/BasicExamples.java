/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.minder.urltemplate;

import org.apache.hadoop.gateway.util.urltemplate.Parser;
import org.apache.hadoop.gateway.util.urltemplate.Rewriter;
import org.apache.hadoop.gateway.util.urltemplate.Template;
import org.junit.Test;

import java.net.URI;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class BasicExamples {

  Template pattern, template;
  URI input, output, expect;

  @Test
  public void testCopyEverything() throws Exception {
    input = new URI( "test-scheme://test-username:test-password@tests-host:42/test-root/test-parent/test-child/test-file#test-fragment?test-name=test-value" );
    pattern = Parser.parse( "{scheme}://{username}:{password}@{host}:{port}/{path}#{fragment}?{**}" );
    template = Parser.parse( "{scheme}://{username}:{password}@{host}:{port}/{path}#{fragment}?{**}" );
    expect = new URI( "test-scheme://test-username:test-password@tests-host:42/test-root/test-parent/test-child/test-file#test-fragment?test-name=test-value" );
    output = Rewriter.rewrite( input, pattern, template, null, null );
    assertThat( output, is( equalTo( expect ) ) );
  }

}

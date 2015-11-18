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
import org.apache.hadoop.gateway.util.urltemplate.Resolver;
import org.apache.hadoop.gateway.util.urltemplate.Rewriter;
import org.apache.hadoop.gateway.util.urltemplate.Template;
import org.junit.Test;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

import static net.minder.urltemplate.UriMatcher.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ResolverExamples {

  Template pattern, template;
  URI input, output, expect;

  @Test
  public void testCustomResolver() throws Exception {
    Resolver resolver = new Resolver() {
      public List<String> resolve( String name ) {
        return name.equalsIgnoreCase( "prefix" ) ? Arrays.asList("test-prefix") : null;
      }
    };
    input = new URI( "http://input-host:777/path-one/path-two/test-file" );
    pattern = Parser.parse( "*://*:*/{path=**}" );
    template = Parser.parse( "https://test-host:42/{prefix}/{path=**}" );
    expect = new URI( "https://test-host:42/test-prefix/path-one/path-two/test-file" );
    output = Rewriter.rewrite( input, pattern, template, resolver, null );
    assertThat( output, is( equalTo( expect ) ) );
  }

  @Test
  public void testSysPropResolver() throws Exception {
    Resolver resolver = new Resolver() {
      public List<String> resolve( String name ) {
        String value = System.getProperty( name );
        return value != null ? Arrays.asList( value ) : null;
      }
    };
    input = new URI( "http://input-host:777/path-one/path-two/test-file" );
    pattern = Parser.parse( "*://*:*/{path=**}" );
    template = Parser.parse( "https://test-host:42/{user.language}/{path=**}" );
    expect = new URI( "https://test-host:42/en/path-one/path-two/test-file" );
    output = Rewriter.rewrite( input, pattern, template, resolver, null );
    assertThat( output, is( equalTo( expect ) ) );
  }

}

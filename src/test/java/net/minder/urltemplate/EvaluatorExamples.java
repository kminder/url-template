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

import org.apache.hadoop.gateway.util.urltemplate.Evaluator;
import org.apache.hadoop.gateway.util.urltemplate.Parser;
import org.apache.hadoop.gateway.util.urltemplate.Rewriter;
import org.apache.hadoop.gateway.util.urltemplate.Template;
import org.junit.Test;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

import static net.minder.urltemplate.UriMatcher.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class EvaluatorExamples {

  private static class SysPropEvaluator implements Evaluator {

    public List<String> evaluate( String function, List<String> parameters ) {
      List<String> result = null;
      if( function.equalsIgnoreCase( "sysprop" ) && parameters.size() > 0 ) {
        String value = System.getProperty( parameters.get( 0 ) );
        result = value != null ? Arrays.asList( value ) : null;
      }
      return result;
    }

  }

  Template pattern, template;
  URI input, output, expect;

  Evaluator evaluator = new SysPropEvaluator();

  @Test
  public void testSysPropEvaluatorDirect() throws Exception {
    input = new URI( "http://input-host:777/path-one/path-two/test-file" );
    pattern = Parser.parse( "*://*:*/{path=**}" );
    template = Parser.parse( "https://test-host:42/{$sysprop[user.language]}/{path=**}" );
    expect = new URI( "https://test-host:42/en/path-one/path-two/test-file" );
    output = Rewriter.rewrite( input, pattern, template, null, evaluator );
    assertThat( output, is( equalTo( expect ) ) );
  }

  @Test
  public void testSysPropEvaluatorIndirect() throws Exception {
    input = new URI( "http://input-host:777/path-one/path-two/user.language" );
    pattern = Parser.parse( "*://*:*/{path=**}/{prop-name=*}" );
    template = Parser.parse( "https://test-host:42/{$sysprop(prop-name)}/{path=**}" );
    expect = new URI( "https://test-host:42/en/path-one/path-two" );
    output = Rewriter.rewrite( input, pattern, template, null, evaluator );
    assertThat( output, is( equalTo( expect ) ) );
  }

}

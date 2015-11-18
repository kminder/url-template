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

import static net.minder.urltemplate.UriMatcher.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public class PathCardinalityExamples {

  Template pattern, template;
  URI input, output, expect;

  @Test
  public void testGlobToGlob() throws Exception {
    input = new URI( "http://input-host:777/path-one/path-two/test-file" );
    pattern = Parser.parse( "*://*:*/{path=**}" );
    template = Parser.parse( "https://test-host:42/test-prefix/{path=**}" );
    expect = new URI( "https://test-host:42/test-prefix/path-one/path-two/test-file" );
    output = Rewriter.rewrite( input, pattern, template, null, null );
    assertThat( output, is( equalTo( expect ) ) );
  }

  @Test
  public void testGlobToDefault() throws Exception {
    input = new URI( "http://input-host:777/path-one/path-two/test-file" );
    pattern = Parser.parse( "*://*:*/{path=**}" );
    template = Parser.parse( "https://test-host:42/test-prefix/{path}" );
    expect = new URI( "https://test-host:42/test-prefix/path-one/path-two/test-file" );
    output = Rewriter.rewrite( input, pattern, template, null, null );
    assertThat( output, is( equalTo( expect ) ) );
  }

  @Test
  public void testDefaultToDefault() throws Exception {
    input = new URI( "http://input-host:777/path-one/path-two/test-file" );
    pattern = Parser.parse( "*://*:*/{path}" );
    template = Parser.parse( "https://test-host:42/test-prefix/{path}" );
    expect = new URI( "https://test-host:42/test-prefix/path-one/path-two/test-file" );
    output = Rewriter.rewrite( input, pattern, template, null, null );
    assertThat( output, is( equalTo( expect ) ) );
  }

  @Test
  public void testDefaultToStar() throws Exception {
    input = new URI( "http://input-host:777/path-one/path-two/test-file" );
    pattern = Parser.parse( "*://*:*/{path}" );
    template = Parser.parse( "https://test-host:42/test-prefix/{path=*}" );
    expect = new URI( "https://test-host:42/test-prefix/path-one" );
    output = Rewriter.rewrite( input, pattern, template, null, null );
    assertThat( output, is( equalTo( expect ) ) );
  }

  @Test
  public void testStarToDefault() throws Exception {
    input = new URI( "http://input-host:777/path-one/path-two/test-file" );
    pattern = Parser.parse( "*://*:*/{path=*}" );
    template = Parser.parse( "https://test-host:42/test-prefix/{path}" );
    expect = new URI( "https://test-host:42/test-prefix/path-one/path-two/test-file" );
    output = Rewriter.rewrite( input, pattern, template, null, null );
    assertThat( output, is( nullValue() ) );
  }

  @Test
  public void testPartialMatch() throws Exception {
    // Positive.
    input = new URI( "http://input-host:777/test-some-root/path-one/path-two/test-file" );
    pattern = Parser.parse( "*://*:*/{root=test*root}/{path}" );
    template = Parser.parse( "https://test-host:42/{root}/{path}" );
    expect = new URI( "https://test-host:42/test-some-root/path-one/path-two/test-file" );
    output = Rewriter.rewrite( input, pattern, template, null, null );
    assertThat( output, is( expect ) );

    // Negative.
    input = new URI( "http://input-host:777/mismatch-root/path-one/path-two/test-file" );
    pattern = Parser.parse( "*://*:*/{root=test*root}/{path=*}" );
    template = Parser.parse( "https://test-host:42/{root}/{path}" );
    output = Rewriter.rewrite( input, pattern, template, null, null );
    assertThat( output, is( nullValue() ) );
  }

}

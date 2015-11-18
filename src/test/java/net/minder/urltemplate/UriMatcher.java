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

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.net.URI;
import java.util.LinkedHashMap;
import java.util.Map;

public class UriMatcher extends TypeSafeMatcher<URI> {

  private static final String[] EMPTY_PAIRS = {};

  private final URI expect;

  public UriMatcher( URI expect ) {
    this.expect = expect;
  }

  @Override
  public boolean matchesSafely( URI actual ) {
    return equals( expect.getScheme(), actual.getScheme() ) &&
        equals( expect.getAuthority(), actual.getAuthority() ) &&
        equals( expect.getPath(), actual.getPath() ) &&
        equals( expect.getFragment(), actual.getFragment() ) &&
        getQuery( expect ).equals( getQuery( actual ) );
  }

  public void describeTo( Description description ) {
    description.appendText( expect.toString() );
  }

  @Factory
  public static Matcher<URI> equalTo( URI expect ) {
    return new UriMatcher( expect );
  }

  private static boolean equals( String left, String right ) {
    if ( left == null && right == null ) {
      return true;
    } else if ( left == null || right == null ) {
      return false;
    } else {
      return left.equals( right );
    }
  }

  private static Map<String, String> getQuery( URI uri ) {
    final Map<String, String> map = new LinkedHashMap<String, String>();
    final String query = uri.getQuery();
    final String[] pairs = query == null ? EMPTY_PAIRS : uri.getQuery().split("&");
    for ( String pair : pairs ) {
      final int i = pair.indexOf( "=" );
      final String name = i > 0 ? pair.substring( 0, i ) : pair;
      final String value = i > 0 && pair.length() > i + 1 ? pair.substring( i + 1 ) : "";
      map.put( name, value );
    }
    return map;
  }

}

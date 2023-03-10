/*
 * Copyright [ 2020 - 2023 ] [Matthew Buckton]
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.mapsmessaging.security.identity.parsers;

import java.util.ServiceLoader;

public class PasswordParserFactory {

  private static final PasswordParserFactory instance = new PasswordParserFactory();
  private final ServiceLoader<PasswordParser> passwordParsers;

  private PasswordParserFactory() {
    passwordParsers = ServiceLoader.load(PasswordParser.class);
  }

  public static PasswordParserFactory getInstance() {
    return instance;
  }

  public PasswordParser parse(String password) {
    for (PasswordParser passwordParser : passwordParsers) {

      if (password.length() >= passwordParser.getKey().length() && password.startsWith(passwordParser.getKey())) {
        return passwordParser.create(password);
      }
    }
    return new PlainPasswordParser(password);
  }

}

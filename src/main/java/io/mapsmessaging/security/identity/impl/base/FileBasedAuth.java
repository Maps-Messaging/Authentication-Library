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

package io.mapsmessaging.security.identity.impl.base;

import static io.mapsmessaging.security.logging.AuthLogMessages.CHECKING_PASSWORD_STORE;
import static io.mapsmessaging.security.logging.AuthLogMessages.NO_SUCH_USER_FOUND;
import static io.mapsmessaging.security.logging.AuthLogMessages.PASSWORD_FILE_CHANGE_DETECTED;
import static io.mapsmessaging.security.logging.AuthLogMessages.PASSWORD_FILE_LOADED;
import static io.mapsmessaging.security.logging.AuthLogMessages.PASSWORD_FILE_LOAD_EXCEPTION;

import io.mapsmessaging.logging.Logger;
import io.mapsmessaging.logging.LoggerFactory;
import io.mapsmessaging.security.identity.IdentityEntry;
import io.mapsmessaging.security.identity.IdentityLookup;
import io.mapsmessaging.security.identity.NoSuchUserFoundException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public abstract class FileBasedAuth implements IdentityLookup {

  private final Logger logger = LoggerFactory.getLogger(FileBasedAuth.class);
  private final String filePath;
  private final Map<String, IdentityEntry> usernamePasswordMap;
  private long lastModified;

  protected FileBasedAuth() {
    filePath = "";
    usernamePasswordMap = new LinkedHashMap<>();
  }

  protected FileBasedAuth(String filepath) {
    filePath = filepath;
    lastModified = 0;
    usernamePasswordMap = new LinkedHashMap<>();
  }

  @Override
  public IdentityEntry findEntry(String username) {
    load();
    return usernamePasswordMap.get(username);
  }

  @Override
  public char[] getPasswordHash(String username) throws NoSuchUserFoundException {
    load();
    IdentityEntry identityEntry = usernamePasswordMap.get(username);
    if (identityEntry == null) {
      logger.log(NO_SUCH_USER_FOUND, username);
      throw new NoSuchUserFoundException("User: " + username + " not found");
    }
    return identityEntry.getPassword().toCharArray();
  }

  protected abstract IdentityEntry load(String line);

  private void load() {
    logger.log(CHECKING_PASSWORD_STORE, filePath);
    File file = new File(filePath);
    if (file.exists() && lastModified != file.lastModified()) {
      logger.log(PASSWORD_FILE_CHANGE_DETECTED, filePath);
      lastModified = file.lastModified();
      usernamePasswordMap.clear();
      try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
        String line = reader.readLine();
        while (line != null) {
          IdentityEntry identityEntry = load(line);
          usernamePasswordMap.put(identityEntry.getUsername(), identityEntry);
          line = reader.readLine();
        }
        logger.log(PASSWORD_FILE_LOADED, usernamePasswordMap.size(), filePath);
      } catch (IOException e) {
        logger.log(PASSWORD_FILE_LOAD_EXCEPTION, filePath, e);
      }
    }
  }
}

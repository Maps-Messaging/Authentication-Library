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

package io.mapsmessaging.security.logging;

import io.mapsmessaging.logging.Category;
import io.mapsmessaging.logging.LEVEL;
import io.mapsmessaging.logging.LogMessage;
import lombok.Getter;

public enum AuthLogMessages implements LogMessage {

  //-------------------------------------------------------------------------------------------------------------

  // <editor-fold desc="Generic messages">
  SCRAM_SERVER_INITIAL_PHASE(LEVEL.INFO, Auth_Category.AUTHENTICATION, "SCRAM server initialising using algorithm {}"),
  SCRAM_SERVER_STATE_CHANGE(LEVEL.INFO, Auth_Category.AUTHENTICATION, "SCRAM server state changed to {}"),


  DO_NOT_USE_IN_PRODUCTION(LEVEL.AUTH, Auth_Category.AUTHENTICATION, "Warning !!!, Not to be used in a production environment"),
  USER_LOGGED_IN(LEVEL.DEBUG, Auth_Category.AUTHENTICATION, "User {} logged in"),
  NO_SUCH_USER_FOUND(LEVEL.INFO, Auth_Category.AUTHENTICATION, "User {} not found"),
  USER_LOGGED_OUT(LEVEL.DEBUG, Auth_Category.AUTHENTICATION, "User {} logged out"),

  PASSWORD_FILE_LOADED(LEVEL.INFO, Auth_Category.AUTHENTICATION, "Successfully loaded {} entries for {}"),
  PASSWORD_FILE_LOAD_EXCEPTION(LEVEL.INFO, Auth_Category.AUTHENTICATION, "Password load failed for {}"),
  PASSWORD_FILE_CHANGE_DETECTED(LEVEL.DEBUG, Auth_Category.AUTHENTICATION, "Password file change detected on {}"),
  CHECKING_PASSWORD_STORE(LEVEL.DEBUG, Auth_Category.AUTHENTICATION, "Scanning for password file changes on file {}");
  // </editor-fold>

  @Getter
  private final String message;
  @Getter
  private final LEVEL level;
  @Getter
  private final Category category;
  @Getter
  private final int parameterCount;

  AuthLogMessages(LEVEL level, Auth_Category category, String message) {
    this.message = message;
    this.level = level;
    this.category = category;
    int location = message.indexOf("{}");
    int count = 0;
    while (location != -1) {
      count++;
      location = message.indexOf("{}", location + 2);
    }
    this.parameterCount = count;
  }

  public enum Auth_Category implements Category {
    AUTHORISATION("Authorisation"),
    AUTHENTICATION("Authentication"),
    SASL("Sasl"),
    JAAS("Jaas");


    @Getter
    private final String description;

    Auth_Category(String description) {
      this.description = description;
    }

    public String getDivision() {
      return "Security";
    }
  }
}

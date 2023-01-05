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

package io.mapsmessaging.security.identity.impl.htpasswd;

import io.mapsmessaging.security.identity.IdentityEntry;
import io.mapsmessaging.security.identity.IdentityLookup;
import io.mapsmessaging.security.identity.impl.base.FileBasedAuth;
import java.util.Map;

public class HtPasswd extends FileBasedAuth {


  public HtPasswd(){
    super();
  }

  public HtPasswd(String filepath) {
    super(filepath);
  }

  @Override
  protected IdentityEntry load(String line) {
    return new HtPasswdEntry(line);
  }

  @Override
  public String getName() {
    return "htpassword";
  }

  @Override
  public IdentityLookup create(Map<String, ?> config) {
    if(config.containsKey("htPasswordFile")){
      String filePath = config.get("htPasswordFile").toString();
      return new HtPasswd(filePath);
    }
    return null;
  }

}

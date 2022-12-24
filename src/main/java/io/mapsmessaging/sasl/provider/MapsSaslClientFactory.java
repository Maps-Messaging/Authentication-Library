/*
 * Copyright [ 2020 - 2022 ] [Matthew Buckton]
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

package io.mapsmessaging.sasl.provider;

import io.mapsmessaging.sasl.provider.scram.client.ScramSaslClient;
import java.util.Map;
import javax.security.auth.callback.CallbackHandler;
import javax.security.sasl.SaslClient;
import javax.security.sasl.SaslClientFactory;
import javax.security.sasl.SaslException;

public class MapsSaslClientFactory implements SaslClientFactory {

  @Override
  public SaslClient createSaslClient(String[] mechanisms, String authorizationId, String protocol, String serverName, Map<String, ?> props, CallbackHandler cbh)
      throws SaslException {
    for(String mechanism:mechanisms){
      if(mechanism.toLowerCase().startsWith("scram")){
        return new ScramSaslClient(authorizationId, protocol, serverName, props, cbh);
      }
    }
    throw new SaslException("Unknown mechanism "+mechanisms);
  }

  @Override
  public String[] getMechanismNames(Map<String, ?> props) {
    return new String[]{"SCRAM"};
  }
}

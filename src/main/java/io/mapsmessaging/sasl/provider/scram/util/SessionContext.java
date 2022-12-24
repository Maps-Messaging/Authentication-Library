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

package io.mapsmessaging.sasl.provider.scram.util;

import io.mapsmessaging.sasl.provider.scram.State;
import lombok.Getter;
import lombok.Setter;

public class SessionContext {

  @Getter
  @Setter
  private boolean receivedClientMessage = false;

  @Getter
  @Setter
  private String clientNonce;

  @Getter
  @Setter
  private String serverNonce;

  @Getter
  @Setter
  private String passwordSalt;

  @Getter
  @Setter
  private String username;

  @Getter
  @Setter
  private State state;

  @Getter
  @Setter
  private int interations;

  public SessionContext(){}
}

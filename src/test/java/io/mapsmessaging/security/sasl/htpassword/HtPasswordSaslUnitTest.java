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

package io.mapsmessaging.security.sasl.htpassword;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.mapsmessaging.security.identity.impl.htpasswd.HtPasswd;
import io.mapsmessaging.security.identity.parsers.sha.Sha1PasswordParser;
import io.mapsmessaging.security.sasl.BaseSasl;
import java.util.HashMap;
import java.util.Map;
import javax.security.sasl.Sasl;
import javax.security.sasl.SaslException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class HtPasswordSaslUnitTest extends BaseSasl {

  private static final String SERVER_NAME = "myServer";
  private static final String PROTOCOL = "amqp";
  private static final String AUTHORIZATION_ID = null;
  private static final String QOP_LEVEL = "auth";

  @ParameterizedTest
  @ValueSource(strings = {"DIGEST-MD5", "CRAM-MD5"})
  void simpleDigestNonSaltValidTest(String mechanism) throws SaslException {
    Sha1PasswordParser passwordParser = new Sha1PasswordParser();
    byte[] password = passwordParser.computeHash("This is a random password".getBytes(), null, 0);
    testMechanism(mechanism, "fred2@google.com", new String(password));
  }

  @ParameterizedTest
  @ValueSource(strings = {"SCRAM-BCRYPT-SHA1"})
  void simpleScramValidTest(String mechanism) throws SaslException {
    testMechanism(mechanism, "test3", "This is an bcrypt password");
  }

  @ParameterizedTest
  @ValueSource(strings = {"DIGEST-MD5", "CRAM-MD5"})
  void simpleWrongPasswordTest(String mechanism) {
    Sha1PasswordParser passwordParser = new Sha1PasswordParser();
    byte[] password = passwordParser.computeHash("This is a wrong password".getBytes(), null, 0);
    Assertions.assertThrowsExactly(SaslException.class, () -> testMechanism(mechanism, "fred2@google.com", new String(password)));
  }

  void testMechanism(String mechanism, String user, String password) throws SaslException {
    Map<String, String> props = new HashMap<>();
    props.put(Sasl.QOP, QOP_LEVEL);
    createServer(new HtPasswd("./src/test/resources/.htpassword"), mechanism, PROTOCOL, SERVER_NAME, props);
    createClient(
        user,
        password,
        new String[]{mechanism},
        PROTOCOL,
        AUTHORIZATION_ID,
        SERVER_NAME,
        props
    );
    simpleValidation();
  }

  void simpleValidation() throws SaslException {
    assertNotNull(saslServer, "This should not be null");
    assertNotNull(saslClient, "This should not be null");
    runAuth();
    assertTrue(saslServer.isComplete());
    assertTrue(saslClient.isComplete());

    String qop = (String) saslClient.getNegotiatedProperty(Sasl.QOP);
    Assertions.assertTrue(qop.startsWith("auth"), "We should have an authorised SASL session");
  }
}
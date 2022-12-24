package io.mapsmessaging.sasl.provider.scram.client.state;

import io.mapsmessaging.sasl.provider.scram.State;
import io.mapsmessaging.sasl.provider.scram.msgs.ChallengeResponse;
import io.mapsmessaging.sasl.provider.scram.msgs.FirstClientMessage;
import io.mapsmessaging.sasl.provider.scram.util.SessionContext;
import java.io.IOException;
import java.util.Map;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.UnsupportedCallbackException;

public class InitialState extends State {

  public InitialState(String authorizationId, String protocol, String serverName, Map<String, ?> props, CallbackHandler cbh){
    super(authorizationId, protocol, serverName, props, cbh);
  }

  @Override
  public boolean isComplete() {
    return false;
  }

  @Override
  public ChallengeResponse produceChallenge(SessionContext context) throws IOException, UnsupportedCallbackException {
    context.setClientNonce(nonceGenerator.generateNonce(48));
    ChallengeResponse firstClientChallenge = new FirstClientMessage();
    NameCallback[] callbacks = new NameCallback[1];
    callbacks[0] = new NameCallback("SCRAM Username Prompt");
    cbh.handle(callbacks);
    firstClientChallenge.put(ChallengeResponse.USERNAME, callbacks[0].getName());
    firstClientChallenge.put(ChallengeResponse.NONCE, context.getClientNonce());
    return firstClientChallenge;
  }

  @Override
  public void handeResponse(ChallengeResponse response, SessionContext context) throws IOException, UnsupportedCallbackException {
    // This is the first state, there is no challenge or response
  }
}
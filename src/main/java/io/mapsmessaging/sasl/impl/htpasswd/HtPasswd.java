package io.mapsmessaging.sasl.impl.htpasswd;

import io.mapsmessaging.sasl.IdentityLookup;
import io.mapsmessaging.sasl.NoSuchUserFoundException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class HtPasswd implements IdentityLookup {

  private final String filePath;
  private final Map<String, IdentityEntry> usernamePasswordMap;
  private long lastModified;

  public HtPasswd(String filepath) {
    filePath = filepath;
    lastModified = 0;
    usernamePasswordMap = new LinkedHashMap<>();
  }

  @Override
  public char[] getPasswordHash(String username) throws NoSuchUserFoundException {
    load();
    IdentityEntry identityEntry = usernamePasswordMap.get(username);
    if (identityEntry == null) {
      throw new NoSuchUserFoundException("User: " + username + " not found");
    }
    return identityEntry.getPasswordHash();
  }

  private void load() {
    File file = new File(filePath);
    if (file.exists() && lastModified != file.lastModified()) {
      lastModified = file.lastModified();
      usernamePasswordMap.clear();
      try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
        String line = reader.readLine();
        while (line != null) {
          IdentityEntry identityEntry = new IdentityEntry(line);
          usernamePasswordMap.put(identityEntry.getUsername(), identityEntry);
          line = reader.readLine();
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}
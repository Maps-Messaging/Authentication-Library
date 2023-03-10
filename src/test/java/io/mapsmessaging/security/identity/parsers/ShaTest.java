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

import org.junit.jupiter.api.Test;

class ShaTest extends BashHashFunctions {


  @Test
  void checkSha512Hash() {
    testHashing("$6$DVW4laGf$QwTuOOtd.1G3u2fs8d5/OtcQ73qTbwA.oAC1XWTmkkjrvDLEJ2WweTcBdxRkzfjQVfZCw3OVVBAMsIGMkH3On/", "onewordpassword");
  }

  @Test
  void checkSha512HashWithSpaces() {
    testHashing("$6$fiizFR2o$IQNwJXIXyQEL1ikJqvFrYGMBRiTBLnjY0OFfty9O472tWdJOY6czvUpuSDJQpzojQkLqNlP6devotoSBQCp//1", "this has spaces");
  }

  @Test
  void checkSha512HashBadPassword() {
    testHashing("$6$fiizFR2o$IQNwJXIXyQEL1ikJqvFrYGMBRiTBLnjY0OFfty9O472tWdJOY6czvUpuSDJQpzojQkLqNlP6devotoSBQCp//1", "just wrong", false);
  }
}

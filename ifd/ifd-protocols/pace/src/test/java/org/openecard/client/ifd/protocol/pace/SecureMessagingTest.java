<<<<<<< HEAD
/* Copyright 2012, Hochschule fuer angewandte Wissenschaften Coburg
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.openecard.client.ifd.protocol.pace;

import org.junit.Assert;
import org.junit.Test;
import org.openecard.client.common.util.StringUtils;

/**
 *
 * @author Dirk Petrautzki <petrautzki@hs-coburg.de>
 *
 */
public class SecureMessagingTest {

    @Test
    public void testEncryptionAndDecryption() throws Exception {

        /*
         * setup Secure Messaging, values are taken from "Worked Example for
         * Extended Access Control (EAC), v1.02"
         */
        byte[] keyEnc = StringUtils.toByteArray("68 40 6B 41 62 10 05 63 D9 C9 01 A6 15 4D 29 01", true);
        byte[] keyMac = StringUtils.toByteArray("73 FF 26 87 84 F7 2A F8 33 FD C9 46 40 49 AF C9", true);
        SecureMessaging sm = new SecureMessaging(keyMac, keyEnc);

        /*
         * test encryption
         */
        byte[] plainAPDU = StringUtils.toByteArray("00 22 81 B6 0F 83 0D 44 45 43 56 43 41 41 54 30 30 30 30 31", true);
        byte[] encryptedAPDU = sm.encrypt(plainAPDU);
        byte[] expectedEncryptedAPDU = StringUtils.toByteArray("0C2281B61D871101BE90237EEB4BA0FF253EA246AE31C8B88E0892D21C73A1DFE99900");
        Assert.assertArrayEquals(expectedEncryptedAPDU, encryptedAPDU);

        /*
         * test decryption
         */
        byte[] apduToDecrypt = StringUtils.toByteArray("99 02 90 00 8E 08 A8 95 70 A6 86 64 A7 D6 90 00", true);
        byte[] decryptedAPDU = sm.decrypt(apduToDecrypt);
        byte[] expectedDecryptedAPDU = new byte[] { (byte) 0x90, 0x00 };
        Assert.assertArrayEquals(expectedDecryptedAPDU, decryptedAPDU);

        /*
         * test encryption with ext. apdu
         */
        byte[] plainAPDU2 = StringUtils
                .toByteArray("002A00BE0001B67F4E82016E5F290100420E44455445535465494430303030317F4982011D060A04007F000702020202038120A9FB57DBA1EEA9BC3E660A909D838D726E3BF623D52620282013481D1F6E537782207D5A0975FC2C3057EEF67530417AFFE7FB8055C126DC5C6CE94A4B44F330B5D9832026DC5C6CE94A4B44F330B5D9BBD77CBF958416295CF7E1CE6BCCDC18FF8C07B68441048BD2AEB9CB7E57CB2C4B482FFC81B7AFB9DE27E1E3BD23C23A4453BD9ACE3262547EF835C3DAC4FD97F8461A14611DC9C27745132DED8E545C1D54C72F0469978520A9FB57DBA1EEA9BC3E660A909D838D718C397AA3B561A6F7901E0E82974856A7864104096EB58BFD86252238EC2652185C43C3A56C320681A21E37A8E69DDC387C0C5F5513856EFE2FDC656E604893212E29449B365E304605AC5413E75BE31E641F128701015F200E44455445535465494430303030327F4C12060904007F0007030102025305FE0F01FFFF5F25060100000902015F24060103000902015F3740141120A0FDFC011A52F3F72B387A3DC7ACA88B4868D5AE9741780B6FF8A0B49E5F55169A2D298EF5CF95935DCA0C3DF3E9D42DC45F74F2066317154961E6C746");
        expectedEncryptedAPDU = StringUtils
                .toByteArray("0C2A00BE0001CF878201C101ABAF1B4D8BC4B3A39B076EA8C36A4EFA2FA709C85FBD1047F6DB2F2B069A737E6F08DA822E6256D70A71A52C6224AB20FE055BD3B0F3546A2ECF7C758DBF1AF1092D05080B7B770DFE4EE6D58D084C7B771560DBB0016DE7E7BDD3200AB38DB8C2574C3C4533289EB3C356C3F934B226C01AF3D8D3B20CB79DB2659BF024B5AABC37A1A4459CE9D5436E54CB5D817071663C7B18EBD6D2C66EB09575E9D7AD2D72FE95F91BD6B224DE55621BF6BA6514E3F82BD0E75C21F45807062C611BFFAE726FF6E0F47D84B8357A767F411D4E33FF79C4063302CDCFBCA347D1203791529904CD3B5F985C581F640E7E4E6D2FA6F0C44F8BEF9CD4EB1C7934B2631BDC17726DFB8DC42B4CCC05A69E8E12BD4A3799BF0C1FCEC77F346A770E78894622423B472F26A014C5B745DFBB26C5FE68620A085FF8DDD9FDEDAD6A1DFCE82C4873D6EB4277F0CBFB20DC9335A50A6FAC6F1622F18F481FFE1D7A32CA778702447772024231D1D1654DFDD04A3AE9CC3AEFCB789A927162B2A6A16856B0DF40E8CC4D2AA8384AF5FD5F52892E71D5891B58E1E91CE282734F6BD835E89EDDC618D29B75752888D54A7CE24F16A680B16385D6CA74B70704CB83A5C75D54F2F10FC58E0815FB2BBD0828E8860000");
        encryptedAPDU = sm.encrypt(plainAPDU2);
        Assert.assertArrayEquals(expectedEncryptedAPDU, encryptedAPDU);
    }
}
=======
/* Copyright 2012, Hochschule fuer angewandte Wissenschaften Coburg 
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.openecard.client.ifd.protocol.pace;

import org.junit.Assert;
import org.junit.Test;
import org.openecard.client.common.util.ByteUtils;
import org.openecard.client.common.util.CardCommands;
import org.openecard.client.common.util.StringUtils;

/**
 * 
 * @author Dirk Petrautzki <petrautzki@hs-coburg.de>
 * 
 */
public class SecureMessagingTest {

    @Test
    public void testEncryptionAndDecryption() throws Exception {

        /*
         * setup Secure Messaging, values are taken from "Worked Example for
         * Extended Access Control (EAC), v1.02"
         */
        byte[] keyEnc = StringUtils.toByteArray("68 40 6B 41 62 10 05 63 D9 C9 01 A6 15 4D 29 01", true);
        byte[] keyMac = StringUtils.toByteArray("73 FF 26 87 84 F7 2A F8 33 FD C9 46 40 49 AF C9", true);
        SecureMessaging sm = new SecureMessaging(keyMac, keyEnc);

        /*
         * test encryption
         */
        byte[] plainAPDU = StringUtils.toByteArray("00 22 81 B6 0F 83 0D 44 45 43 56 43 41 41 54 30 30 30 30 31", true);
        byte[] encryptedAPDU = sm.encrypt(plainAPDU);
        byte[] expectedEncryptedAPDU = StringUtils.toByteArray("0C2281B61D871101BE90237EEB4BA0FF253EA246AE31C8B88E0892D21C73A1DFE99900");
        Assert.assertArrayEquals(expectedEncryptedAPDU, encryptedAPDU);

        /*
         * test decryption
         */
        byte[] apduToDecrypt = StringUtils.toByteArray("99 02 90 00 8E 08 A8 95 70 A6 86 64 A7 D6 90 00", true);
        byte[] decryptedAPDU = sm.decrypt(apduToDecrypt);
        byte[] expectedDecryptedAPDU = new byte[] { (byte) 0x90, 0x00 };
        Assert.assertArrayEquals(expectedDecryptedAPDU, decryptedAPDU);

        /*
         * test encryption with ext. apdu
         */
        byte[] plainAPDU2 = StringUtils
                .toByteArray("002A00BE0001B67F4E82016E5F290100420E44455445535465494430303030317F4982011D060A04007F000702020202038120A9FB57DBA1EEA9BC3E660A909D838D726E3BF623D52620282013481D1F6E537782207D5A0975FC2C3057EEF67530417AFFE7FB8055C126DC5C6CE94A4B44F330B5D9832026DC5C6CE94A4B44F330B5D9BBD77CBF958416295CF7E1CE6BCCDC18FF8C07B68441048BD2AEB9CB7E57CB2C4B482FFC81B7AFB9DE27E1E3BD23C23A4453BD9ACE3262547EF835C3DAC4FD97F8461A14611DC9C27745132DED8E545C1D54C72F0469978520A9FB57DBA1EEA9BC3E660A909D838D718C397AA3B561A6F7901E0E82974856A7864104096EB58BFD86252238EC2652185C43C3A56C320681A21E37A8E69DDC387C0C5F5513856EFE2FDC656E604893212E29449B365E304605AC5413E75BE31E641F128701015F200E44455445535465494430303030327F4C12060904007F0007030102025305FE0F01FFFF5F25060100000902015F24060103000902015F3740141120A0FDFC011A52F3F72B387A3DC7ACA88B4868D5AE9741780B6FF8A0B49E5F55169A2D298EF5CF95935DCA0C3DF3E9D42DC45F74F2066317154961E6C746");
        expectedEncryptedAPDU = StringUtils
                .toByteArray("0C2A00BE0001CF878201C101ABAF1B4D8BC4B3A39B076EA8C36A4EFA2FA709C85FBD1047F6DB2F2B069A737E6F08DA822E6256D70A71A52C6224AB20FE055BD3B0F3546A2ECF7C758DBF1AF1092D05080B7B770DFE4EE6D58D084C7B771560DBB0016DE7E7BDD3200AB38DB8C2574C3C4533289EB3C356C3F934B226C01AF3D8D3B20CB79DB2659BF024B5AABC37A1A4459CE9D5436E54CB5D817071663C7B18EBD6D2C66EB09575E9D7AD2D72FE95F91BD6B224DE55621BF6BA6514E3F82BD0E75C21F45807062C611BFFAE726FF6E0F47D84B8357A767F411D4E33FF79C4063302CDCFBCA347D1203791529904CD3B5F985C581F640E7E4E6D2FA6F0C44F8BEF9CD4EB1C7934B2631BDC17726DFB8DC42B4CCC05A69E8E12BD4A3799BF0C1FCEC77F346A770E78894622423B472F26A014C5B745DFBB26C5FE68620A085FF8DDD9FDEDAD6A1DFCE82C4873D6EB4277F0CBFB20DC9335A50A6FAC6F1622F18F481FFE1D7A32CA778702447772024231D1D1654DFDD04A3AE9CC3AEFCB789A927162B2A6A16856B0DF40E8CC4D2AA8384AF5FD5F52892E71D5891B58E1E91CE282734F6BD835E89EDDC618D29B75752888D54A7CE24F16A680B16385D6CA74B70704CB83A5C75D54F2F10FC58E0815FB2BBD0828E8860000");
        encryptedAPDU = sm.encrypt(plainAPDU2);
        Assert.assertArrayEquals(expectedEncryptedAPDU, encryptedAPDU);

        /*
         * test apdu without data
         */
        byte[] plainAPDU3 = new byte[]{0x00, (byte) 0x84, 0x00, 0x00, 0x08};
        encryptedAPDU = sm.encrypt(plainAPDU3);
        expectedEncryptedAPDU = StringUtils
                .toByteArray("0C8400000D9701088E08BB839625FFDCBB9C00");
        
        Assert.assertArrayEquals(expectedEncryptedAPDU, encryptedAPDU);
        
        /*
         * test already encrypted apdu
         */
        try {
            sm.encrypt(expectedEncryptedAPDU);
            Assert.fail("Encrypting an already encrypted APDU should give an error.");
        } catch (Exception e) {
            /* expected */
        }

        /*
         * test already decrypted apdu
         */
        try {
            sm.encrypt(expectedEncryptedAPDU);
            Assert.fail("Decrypting an already decrypted APDU should give an error.");
        } catch (Exception e) {
            /* expected */
        }
    }
}
>>>>>>> 29bf8dc... fix nullpointer in secure messaging + updated test

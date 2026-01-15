package com.company.tradius_backend.utils;


import org.apache.commons.codec.binary.Hex;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

@Component
public class RazorpayWebhookVerifier {

    public boolean verify(String payload, String signature, String secret) {
        try {
            String generatedSignature = HmacSHA256(payload, secret);
            return generatedSignature.equals(signature);
        } catch (Exception e) {
            return false;
        }
    }

    private String HmacSHA256(String data, String secret) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");
        SecretKeySpec keySpec =
                new SecretKeySpec(secret.getBytes(), "HmacSHA256");
        mac.init(keySpec);
        byte[] rawHmac = mac.doFinal(data.getBytes());
        return Hex.encodeHexString(rawHmac);
    }
}


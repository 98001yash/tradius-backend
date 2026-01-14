package com.company.tradius_backend.utils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class RazorpaySignatureVerifier {

    private static final String HMAC_SHA256 = "HmacSHA256";

    public static boolean verify(
            String orderId,
            String paymentId,
            String razorpaySignature,
            String secret
    ) {
        try {
            String payload = orderId + "|" + paymentId;

            Mac mac = Mac.getInstance(HMAC_SHA256);
            mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), HMAC_SHA256));

            byte[] hash = mac.doFinal(payload.getBytes(StandardCharsets.UTF_8));
            String generatedSignature = Base64.getEncoder().encodeToString(hash);

            return generatedSignature.equals(razorpaySignature);
        } catch (Exception e) {
            return false;
        }
    }
}
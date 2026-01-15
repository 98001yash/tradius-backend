package com.company.tradius_backend.controller;


import com.company.tradius_backend.service.RazorpayWebhookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/webhooks")
@RequiredArgsConstructor
@Slf4j
public class RazorpayWebhookController {

    private final RazorpayWebhookService webhookService;

    @PostMapping("/razorpay")
    public ResponseEntity<Void> handleRazorpayWebhook(
            @RequestBody String payload,
            @RequestHeader("X-Razorpay-Signature") String signature
    ) {
        log.info("Razorpay webhook received");
        webhookService.processWebhook(payload, signature);
        return ResponseEntity.ok().build(); // ALWAYS 200
    }
}

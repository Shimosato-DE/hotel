package com.example.samuraitravel.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.example.samuraitravel.service.StripeService;
import com.stripe.Stripe;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.net.Webhook;

@Controller
public class StripeWebhookController {

	private final StripeService stripeService;

	@Value("${stripe.api-key}")
	private String stripeApiKey;

	@Value("${stripe.webhook-secret}")
	private String webhookSecret;

	public StripeWebhookController(StripeService stripeService) {
		this.stripeService = stripeService;
	}

	@PostMapping("/stripe/webhook")
	public ResponseEntity<String> webhook(@RequestBody String payload,
			@RequestHeader("Stripe-Signature") String sigHeader) {
		Stripe.apiKey = stripeApiKey;
		Event event = null;

		try {

			event = Webhook.constructEvent(payload, sigHeader, webhookSecret);

		} catch (SignatureVerificationException e) {

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);

		}

		//checkout.session.completed(決済成功イベント)の場合、
		if ("checkout.session.completed".equals(event.getType())) {
		
			//stripeServicesのprocessSessionCompletedメソッドを呼び出す
			//引数：StripeのWebhookから送られてきたEventオブジェクト
			stripeService.processSessionCompleted(event);
		}
		
		//すべての処理が成功した場合、Stripeに対してHTTP 200 OKを返す
		return new ResponseEntity<>("Success", HttpStatus.OK);

	}

}

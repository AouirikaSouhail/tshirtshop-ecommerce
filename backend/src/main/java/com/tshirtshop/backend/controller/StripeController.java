package com.tshirtshop.backend.controller;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import com.tshirtshop.backend.dto.CheckoutItemDTO;
import com.tshirtshop.backend.dto.CheckoutRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.PostConstruct;
import java.util.*;

@RestController
@RequestMapping("/api/stripe")
@CrossOrigin(origins = "http://localhost:4200")
public class StripeController {

    @Value("${stripe.api.key}")
    private String stripeApiKey;

    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeApiKey;
    }

    @PostMapping("/create-checkout-session")
    public ResponseEntity<Map<String, String>> createCheckoutSession(@RequestBody CheckoutRequest request)

    throws StripeException {

        List<SessionCreateParams.LineItem> lineItems = new ArrayList<>();

        for (CheckoutItemDTO item : request.getItems()) {
            lineItems.add(
                    SessionCreateParams.LineItem.builder()
                            .setQuantity((long) item.getQuantity())
                            .setPriceData(
                                    SessionCreateParams.LineItem.PriceData.builder()
                                            .setCurrency("eur")
                                            .setUnitAmount((long) (item.getPrice() * 100))
                                            .setProductData(
                                                    SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                            .setName(item.getProductName())
                                                            .build())
                                            .build())
                            .build());
        }

        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setCustomerEmail(request.getEmail()) // ✅ Très important pour lier l’email à la commande
                .setSuccessUrl("http://localhost:4200/confirmation")
                .setCancelUrl("http://localhost:4200/panier")
                .addAllLineItem(lineItems)
                .build();

        Session session = Session.create(params);

        Map<String, String> responseData = new HashMap<>();
        responseData.put("id", session.getId());

        return ResponseEntity.ok(responseData);
    }
}

package com.babichev.stripedemo.controller;

import com.babichev.stripedemo.model.ChargeRequest;
import com.babichev.stripedemo.service.StripeService;
import com.stripe.exception.*;
import com.stripe.model.Charge;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ChargeController {

    private StripeService paymentsService;

    public ChargeController(StripeService paymentsService) {
        this.paymentsService = paymentsService;
    }

    @PostMapping("/charge")
    public String charge(ChargeRequest chargeRequest, Model model) throws APIConnectionException, APIException, AuthenticationException, InvalidRequestException, CardException {
        chargeRequest.setDescription("Example charge");
        chargeRequest.setCurrency(ChargeRequest.Currency.EUR);
        Charge charge = paymentsService.charge(chargeRequest);

        model.addAttribute("id", charge.getId());
        model.addAttribute("status", charge.getStatus());
        model.addAttribute("chargeId", charge.getId());
        model.addAttribute("balance_transaction", charge.getBalanceTransaction());
        return "successful";
    }

    @ExceptionHandler(StripeException.class)
    public String handleError(Model model, StripeException ex) {
        model.addAttribute("error", ex.getMessage());
        return "successful";
    }
}

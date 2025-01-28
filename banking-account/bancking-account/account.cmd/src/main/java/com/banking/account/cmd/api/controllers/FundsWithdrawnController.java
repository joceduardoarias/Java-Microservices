package com.banking.account.cmd.api.controllers;

import com.banking.account.cmd.api.command.WithdrawFundsCommand;
import com.banking.account.common.dto.BaseResponse;
import com.banking.cqrs.core.Infrastructure.CommandDispatcher;
import exceptions.AggregateNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;

@RestController
@RequestMapping("/api/v1/withdraw-funds")
public class FundsWithdrawnController {
    private final Logger logger = Logger.getLogger(FundsWithdrawnController.class.getName());

    @Autowired
    private CommandDispatcher commandDispatcher;

    @PostMapping
    public ResponseEntity<?> withdrawFunds(@PathVariable String id, @RequestBody WithdrawFundsCommand command) {
        try {
            logger.info("Received withdraw funds command request");
            command.setId(id);
            logger.info("Withdrawing funds");
            commandDispatcher.send(command);
            return  new ResponseEntity<>(new BaseResponse("El retiro de dinero fue exitoso"), HttpStatus.OK);
        } catch (IllegalStateException | AggregateNotFoundException e) {
            logger.severe("Failed to withdraw funds: " + e.getMessage());
            return new ResponseEntity<>(new BaseResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
        }catch (Exception e) {
            logger.severe("Failed to withdraw funds: " + e.getMessage());
            return new ResponseEntity<>(new BaseResponse("Failed to withdraw funds"), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

}

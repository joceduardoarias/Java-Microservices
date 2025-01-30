package com.banking.account.cmd.api.controllers;

import com.banking.account.cmd.api.command.DepositFundsCommand;
import com.banking.account.common.dto.BaseResponse;
import com.banking.cqrs.core.Infrastructure.CommandDispatcher;
import exceptions.AggregateNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;

@RestController
@RequestMapping("/api/v1/deposit-funds")
public class DepositFundsController {
    private final Logger logger = Logger.getLogger(DepositFundsController.class.getName());

    @Autowired
    private CommandDispatcher commandDispatcher;

    @PutMapping("/{id}")
    public ResponseEntity<?> depositFunds(@PathVariable String id, @RequestBody DepositFundsCommand command) {
        try {
            logger.info("Received deposit funds command request");
            command.setId(id);
            logger.info("Depositing funds");
            commandDispatcher.send(command);
            return  new ResponseEntity<>(new BaseResponse("El deposito de dinero fue exitoso"), HttpStatus.OK);
        } catch (IllegalStateException | AggregateNotFoundException e) {
            logger.severe("Failed to deposit funds: " + e.getMessage());
            return new ResponseEntity<>(new BaseResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
        }catch (Exception e) {
            logger.severe("Failed to deposit funds: " + e.getMessage());
            return new ResponseEntity<>(new BaseResponse("Failed to deposit funds"), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

}

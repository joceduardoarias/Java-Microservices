package com.banking.account.cmd.api.controllers;

import com.banking.account.cmd.api.command.OpenAccountCommand;
import com.banking.account.cmd.api.dto.OpenAccountResponse;
import com.banking.account.common.dto.BaseResponse;
import com.banking.cqrs.core.Infrastructure.CommandDispatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.MessageFormat;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/v1/open-account")
public class OpenAccountController {
    private final Logger logger = Logger.getLogger(OpenAccountController.class.getName());

    @Autowired
    private CommandDispatcher commandDispatcher;

    @PostMapping
    public ResponseEntity<?> openAccount(@RequestBody OpenAccountCommand command) {
        logger.info("Received open account command request");
        var id = UUID.randomUUID().toString();
        command.setId(id);
        logger.info(MessageFormat.format("Opening account with id: {0}", command.getId()));
        try {
            commandDispatcher.send(command);
            return new ResponseEntity<>(new OpenAccountResponse("Account opened successfully", id), HttpStatus.CREATED);
        } catch (IllegalStateException e) {
            logger.log(Level.WARNING, MessageFormat.format("Failed to open account: {0}", e.getMessage()));
            return new ResponseEntity<>(new BaseResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
        }catch (Exception e) {
            logger.log(Level.SEVERE, MessageFormat.format("Failed to open account: {0}", e.getMessage()));
            return new ResponseEntity<>(new OpenAccountResponse("Failed to open account"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

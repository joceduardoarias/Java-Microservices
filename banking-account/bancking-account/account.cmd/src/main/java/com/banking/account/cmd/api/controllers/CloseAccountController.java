package com.banking.account.cmd.api.controllers;

import com.banking.account.cmd.api.command.CloseAccountCommand;
import com.banking.account.common.dto.BaseResponse;
import com.banking.cqrs.core.Infrastructure.CommandDispatcher;
import exceptions.AggregateNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;

@RestController
@RequestMapping(value = "/api/v1/closeAccount")
public class CloseAccountController {
    private final Logger logger = Logger.getLogger(CloseAccountController.class.getName());

    @Autowired
    private CommandDispatcher commandDispatcher;

    @DeleteMapping
    public ResponseEntity<?> closeAccount(@PathVariable("id") String id) {
        logger.info("Received close account command request");
        try {
            commandDispatcher.send(new CloseAccountCommand(id));
            return new ResponseEntity<>(new BaseResponse("La cuenta fue cerrada exitosamente"), HttpStatus.OK);
        } catch (IllegalStateException | AggregateNotFoundException e) {
            logger.severe("Failed to close account: " + e.getMessage());
            return new ResponseEntity<>(new BaseResponse(e.getMessage()),HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            logger.severe("Failed to close account: " + e.getMessage());
            return new ResponseEntity<>(new BaseResponse("Failed to close account"),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

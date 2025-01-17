package com.banking.account.cmd;

import com.banking.account.cmd.api.command.*;
import com.banking.cqrs.core.Infrastructure.CommandDispatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class CommandApplication {
	@Autowired
	private CommandDispatcher commandDispatcher;
	@Autowired
	private CommandHanlder commandHanlder;

	public static void main(String[] args) {
		SpringApplication.run(CommandApplication.class, args);
	}

	@PostConstruct
	public void registerCommandHandlers() {
		System.out.println("Starting to register command handlers...");
		try {
			System.out.println("Registering OpenAccountCommand handler...");
			commandDispatcher.registerHandler(OpenAccountCommand.class, commandHanlder::handle);
			System.out.println("OpenAccountCommand handler registered successfully.");

			System.out.println("Registering DepositFundsCommand handler...");
			commandDispatcher.registerHandler(DepositFundsCommand.class, commandHanlder::handle);
			System.out.println("DepositFundsCommand handler registered successfully.");

			System.out.println("Registering WithdrawFundsCommand handler...");
			commandDispatcher.registerHandler(WithdrawFundsCommand.class, commandHanlder::handle);
			System.out.println("WithdrawFundsCommand handler registered successfully.");

			System.out.println("Registering CloseAccountCommand handler...");
			commandDispatcher.registerHandler(CloseAccountCommand.class, commandHanlder::handle);
			System.out.println("CloseAccountCommand handler registered successfully.");
		} catch (Exception e) {
			System.err.println("Error while registering command handlers: " + e.getMessage());
			e.printStackTrace();
		}
	}

}

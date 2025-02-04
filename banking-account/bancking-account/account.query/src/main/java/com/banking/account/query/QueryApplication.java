package com.banking.account.query;

import com.banking.account.query.api.queries.*;
import com.banking.cqrs.core.Infrastructure.QueryDispatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class QueryApplication {
	@Autowired
	private QueryDispatcher queryDispatcher;
	@Autowired
	private QueryHandler queryHandler;

	public static void main(String[] args) {

		SpringApplication.run(QueryApplication.class, args);
	}

	// This method is called after the application context has been loaded
	// and the dispatcher is ready to handle queries
	@PostConstruct
	public void registerQueryHandler() {
		System.out.println("Starting to register query handler...");
		try {
			System.out.println("Registering FindAccountByHolderQuery handler...");
			queryDispatcher.registerHandler(FindAccountByHolderQuery.class, queryHandler::handle);
			System.out.println("FindAccountByHolderQuery handler registered successfully.");

			System.out.println("Registering FindAccountByIdQuery handler...");
			queryDispatcher.registerHandler(FindAccountByIdQuery.class, queryHandler::handle);
			System.out.println("FindAccountByIdQuery handler registered successfully.");

			System.out.println("Query handler registered successfully.");
			queryDispatcher.registerHandler(FindAccountWithBalanceQuery.class, queryHandler::handle);
			System.out.println("FindAccountWithBalanceQuery handler registered successfully.");

			System.out.println("Registering FindAllAccountsQuery handler...");
			queryDispatcher.registerHandler(FindAllAccountsQuery.class, queryHandler::handle);
			System.out.println("FindAllAccountsQuery handler registered successfully.");

		} catch (Exception e) {
			System.err.println("Error while registering query handler: " + e.getMessage());
			e.printStackTrace();
		}
	}

}

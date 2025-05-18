package com.danielang.common.health;

import com.danielang.common.RightTimeService;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.HealthCheckResponseBuilder;
import org.eclipse.microprofile.health.Readiness;
import org.jboss.logging.Logger;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.ListTablesRequest;
import software.amazon.awssdk.services.dynamodb.model.ListTablesResponse;

/**
 * @program: awsurance-common-service
 * @author: Daniel
 * @create: 2025-05-18 21:31
 **/
@Readiness
@ApplicationScoped
public class DynamodbTableHealthCheck implements HealthCheck {
	private static final Logger LOG = Logger.getLogger(DynamodbTableHealthCheck.class);
	private final DynamoDbClient dynamoDbClient;

	public DynamodbTableHealthCheck(DynamoDbClient dynamoDbClient) {
		this.dynamoDbClient = dynamoDbClient;
	}

	@Override
	public HealthCheckResponse call() {
		HealthCheckResponseBuilder tableCheck = HealthCheckResponse.named("Dynamodb table check");
		try {
			// very cheap call: no table name required
			ListTablesResponse listTablesResponse = dynamoDbClient.listTables(ListTablesRequest.builder().build());
			if (listTablesResponse.tableNames().contains(RightTimeService.COMMON_INFO_TABLE)) {
				tableCheck.up();
			} else {
				tableCheck.down();
			}
		} catch (Exception e) {
			LOG.error("DynamoDB table failed", e);
			tableCheck.down();
		}

		return tableCheck.build();
	}
}

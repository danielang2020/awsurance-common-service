package com.danielang.common.health;

import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.HealthCheckResponseBuilder;
import org.eclipse.microprofile.health.Readiness;
import org.jboss.logging.Logger;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.ListTablesRequest;

/**
 * @program: awsurance-common-service
 * @author: Daniel
 * @create: 2025-05-18 21:31
 **/
@Readiness
@ApplicationScoped
public class DynamodbConnectionHealthCheck implements HealthCheck {
	private static final Logger LOG = Logger.getLogger(DynamodbConnectionHealthCheck.class);
	private final DynamoDbClient dynamoDbClient;

	public DynamodbConnectionHealthCheck(DynamoDbClient dynamoDbClient) {
		this.dynamoDbClient = dynamoDbClient;
	}

	@Override
	public HealthCheckResponse call() {
		HealthCheckResponseBuilder responseBuilder = HealthCheckResponse.named("Dynamodb connection health check");
		try {
			// very cheap call: no table name required
			dynamoDbClient.listTables(ListTablesRequest.builder().limit(1).build());
			responseBuilder.up();
		} catch (Exception e) {
			LOG.error("DynamoDB connection failed", e);
			responseBuilder.down();
		}

		return responseBuilder.build();
	}
}

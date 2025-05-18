package com.danielang.common;

import com.danielang.common.db.entity.RightTimeInfo;
import io.quarkus.grpc.GrpcService;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

@GrpcService
public class RightTimeService implements RightTimeGrpc {
	private final DynamoDbTable<RightTimeInfo> rightTimeTable;

	@Inject
	public RightTimeService(DynamoDbEnhancedClient dynamoEnhancedClient) {
		this.rightTimeTable = dynamoEnhancedClient.table("RightTimeInfo",
				TableSchema.fromClass(RightTimeInfo.class));
	}

	@Override
	public Uni<RightTimeGetReply> getRightTime(RightTimeGetRequest request) {
		String insuranceTenant = request.getInsuranceTenant();
		String user = request.getUser();
		Key key = Key.builder().partitionValue(insuranceTenant).sortValue(user).build();
		RightTimeInfo item = rightTimeTable.getItem(key);

		if (item == null) {
			return Uni.createFrom().item(
					RightTimeGetReply.newBuilder()
							.setResponseCode("fail")
							.build());
		}else{
			return Uni.createFrom().item(
					RightTimeGetReply.newBuilder()
							.setResponseCode("ok")
							.setRightTime(item.getRightTime())
							.setRightTimeZone(item.getRightTimeZone())
							.build());
		}
	}

	@Override
	public Uni<RightTimeUpdateReply> updateRightTime(RightTimeUpdateRequest request) {
		var rightTime = new RightTimeInfo(request.getInsuranceTenant(), request.getUser(), request.getRightTime(),
				request.getRightTimeZone());
		rightTimeTable.updateItem(rightTime);

		return Uni.createFrom().item(
				RightTimeUpdateReply.newBuilder()
						.setResponseCode("ok")
						.build());
	}
}

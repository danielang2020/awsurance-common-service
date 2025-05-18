package com.danielang.common;

import com.danielang.common.db.entity.RightTimeInfo;
import com.danielang.common.utils.ResponseInfo;
import io.quarkus.grpc.GrpcService;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

@GrpcService
public class RightTimeService implements RightTimeGrpc {
	private static final String COMMON_INFO_TABLE = "CommonInfo";
	private final DynamoDbTable<RightTimeInfo> rightTimeTable;

	@Inject
	public RightTimeService(DynamoDbEnhancedClient dynamoEnhancedClient) {
		this.rightTimeTable = dynamoEnhancedClient.table(COMMON_INFO_TABLE, TableSchema.fromClass(RightTimeInfo.class));
	}

	@Override
	public Uni<RightTimeGetReply> getRightTime(RightTimeGetRequest request) {
		String insuranceTenant = request.getInsuranceTenant();
		String user = request.getUser();
		Key key = Key.builder().partitionValue(insuranceTenant).sortValue(user).build();
		RightTimeInfo item = rightTimeTable.getItem(key);

		if (item == null) {
			return Uni.createFrom()
					.item(RightTimeGetReply.newBuilder().setResponseCode(ResponseInfo.NOT_FOUND).build());
		} else {
			return Uni.createFrom().item(RightTimeGetReply.newBuilder().setResponseCode(ResponseInfo.OK)
					.setRightTime(item.getRightTime()).setRightTimeZone(item.getRightTimeZone()).build());
		}
	}

	@Override
	public Uni<RightTimeUpdateReply> updateRightTime(RightTimeUpdateRequest request) {
		var rightTime = new RightTimeInfo(request.getInsuranceTenant(), request.getUser(), request.getRightTime(),
				request.getRightTimeZone());
		rightTimeTable.updateItem(rightTime);

		return Uni.createFrom().item(RightTimeUpdateReply.newBuilder().setResponseCode(ResponseInfo.OK).build());
	}
}

package com.danielang.common;

import com.danielang.common.db.entity.RightTimeInfoEntity;
import com.danielang.common.utils.ResponseInfo;
import io.quarkus.grpc.GrpcService;
import io.smallrye.mutiny.Uni;
import org.jboss.logging.Logger;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.utils.StringUtils;

@GrpcService
public class RightTimeService implements RightTimeGrpc {
	public static final String COMMON_INFO_TABLE = "CommonInfo";
	public static final String ALL_USER = "ALL";
	private static final Logger LOG = Logger.getLogger(RightTimeService.class);
	private final DynamoDbTable<RightTimeInfoEntity> rightTimeTable;

	public RightTimeService(DynamoDbEnhancedClient dynamoEnhancedClient) {
		this.rightTimeTable = dynamoEnhancedClient.table(COMMON_INFO_TABLE,
				TableSchema.fromClass(RightTimeInfoEntity.class));
	}

	@Override
	public Uni<RightTimeGetReply> getRightTime(RightTimeGetRequest request) {
		String insuranceTenant = request.getInsuranceTenant();
		String user = request.getUser();
		Key key = Key.builder().partitionValue(insuranceTenant).sortValue(user).build();
		RightTimeInfoEntity item = rightTimeTable.getItem(key);

		if (item == null) {
			LOG.warn("RightTime not found for insuranceTenant: " + insuranceTenant + ", user: " + user);
			key = Key.builder().partitionValue(insuranceTenant).sortValue(ALL_USER).build();
			item = rightTimeTable.getItem(key);
			if (item == null) {
				LOG.warn("RightTime not found for insuranceTenant: " + insuranceTenant);
				return Uni.createFrom()
						.item(RightTimeGetReply.newBuilder().setResponseCode(ResponseInfo.NOT_FOUND).build());
			}
		}
		return Uni.createFrom()
				.item(RightTimeGetReply.newBuilder().setResponseCode(ResponseInfo.OK).setRightTime(item.getRightTime())
						.setRightTimeZone(item.getRightTimeZone()).build());
	}

	@Override
	public Uni<RightTimeUpdateReply> updateRightTime(RightTimeUpdateRequest request) {
		RightTimeInfoEntity rightTime;
		if (StringUtils.isBlank(request.getUser())) {
			rightTime = new RightTimeInfoEntity(request.getInsuranceTenant(), ALL_USER, request.getRightTime(),
					request.getRightTimeZone());
		}else{
			rightTime = new RightTimeInfoEntity(request.getInsuranceTenant(), request.getUser(), request.getRightTime(),
					request.getRightTimeZone());
		}

		rightTimeTable.updateItem(rightTime);

		return Uni.createFrom().item(RightTimeUpdateReply.newBuilder().setResponseCode(ResponseInfo.OK).build());
	}
}

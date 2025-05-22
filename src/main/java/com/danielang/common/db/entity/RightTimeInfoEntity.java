package com.danielang.common.db.entity;

import io.quarkus.runtime.annotations.RegisterForReflection;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

/**
 * @program: awsurance-common-service
 * @author: Daniel
 * @create: 2025-05-18 11:34
 **/
@RegisterForReflection
@DynamoDbBean
public class RightTimeInfoEntity {
	private String pk;
	private String sk;
	private String rightTime;
	private int rightTimeZone;

	public RightTimeInfoEntity() {
	}

	public RightTimeInfoEntity(String pk, String sk, String rightTime, int rightTimeZone) {
		this.pk = pk;
		this.sk = sk;
		this.rightTime = rightTime;
		this.rightTimeZone = rightTimeZone;
	}

	@DynamoDbPartitionKey
	public String getPk() {
		return pk;
	}

	public void setPk(String pk) {
		this.pk = pk;
	}

	@DynamoDbSortKey
	public String getSk() {
		return sk;
	}

	public void setSk(String sk) {
		this.sk = sk;
	}

	public String getRightTime() {
		return rightTime;
	}

	public void setRightTime(String rightTime) {
		this.rightTime = rightTime;
	}

	public int getRightTimeZone() {
		return rightTimeZone;
	}

	public void setRightTimeZone(int rightTimeZone) {
		this.rightTimeZone = rightTimeZone;
	}
}

//package com.danielang.common;
//
//import io.quarkus.grpc.GrpcClient;
//import io.quarkus.test.junit.QuarkusTest;
//import org.junit.jupiter.api.Test;
//
//import java.time.Duration;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//@QuarkusTest
//class RightTimeServiceTest {
//	@GrpcClient
//	RightTimeGrpc rightTimeGrpc;
//
//	@Test
//	void testHello() {
//		RightTimeReply reply = rightTimeGrpc.getRightTime(RightTimeRequest.newBuilder().setName("Neo").build()).await()
//				.atMost(Duration.ofSeconds(5));
//		assertEquals("Hello Neo!", reply.getMessage());
//	}
//
//}

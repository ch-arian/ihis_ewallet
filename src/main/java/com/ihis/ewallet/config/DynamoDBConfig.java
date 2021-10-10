package com.ihis.ewallet.config;

import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.ResourceInUseException;
import com.ihis.ewallet.dtos.EWalletUser;
import com.ihis.ewallet.dtos.Transaction;

import lombok.extern.slf4j.Slf4j;

import com.amazonaws.client.builder.AwsClientBuilder;

@Configuration
@EnableDynamoDBRepositories(basePackages = "com.ihis.ewallet.repo")
@Slf4j
public class DynamoDBConfig {

	@Value("${amazon.dynamodb.endpoint}")
	private String amazonDynamoDBEndpoint;

	@Value("${amazon.aws.accesskey}")
	private String amazonAWSAccessKey;

	@Value("${amazon.aws.secretkey}")
	private String amazonAWSSecretKey;

	@Bean
	public AmazonDynamoDB amazonDynamoDB(AWSCredentials amazonAWSCredentials) {
		AmazonDynamoDB amazonDynamoDB = AmazonDynamoDBClientBuilder.standard()
				.withEndpointConfiguration(
						new AwsClientBuilder.EndpointConfiguration(amazonDynamoDBEndpoint, "ap-southeast-1"))
				.withCredentials(amazonAWSCredentialsProvider()).build();

		// create table
		createTable(EWalletUser.class, amazonDynamoDB);
		createTable(Transaction.class, amazonDynamoDB);
		return amazonDynamoDB;
	}

	public <T> void createTable(Class<T> klass, AmazonDynamoDB amazonDynamoDB) {
		DynamoDBMapper dynamoMapper = new DynamoDBMapper(amazonDynamoDB);
		CreateTableRequest tableRequest = dynamoMapper.generateCreateTableRequest(klass);
		tableRequest.setProvisionedThroughput(
				new ProvisionedThroughput().withReadCapacityUnits(1L).withWriteCapacityUnits(1L));
		try {
			amazonDynamoDB.createTable(tableRequest);
		} catch (ResourceInUseException e) {
			log.error("TestUser table existed");
		}
	}

	public AWSCredentialsProvider amazonAWSCredentialsProvider() {
		return new AWSStaticCredentialsProvider(amazonAWSCredentials());
	}

	@Bean
	public AWSCredentials amazonAWSCredentials() {
		return new BasicAWSCredentials(amazonAWSAccessKey, amazonAWSSecretKey);
	}

}

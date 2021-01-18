package com.nubefact.ose;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

@Configuration
public class AWSConfiguration {

	@Value("${ose.aws_s3user}")
	private String aws_s3user;

	@Value("${ose.aws_s3pwd}")
	private String aws_s3pwd;
	
	@Value("${ose.aws_s3url}")
	private String aws_s3url;
	
	@Bean 
	public AmazonS3 amazonS3()
	{
		AWSCredentials credentials = new BasicAWSCredentials(aws_s3user,aws_s3pwd);
		ClientConfiguration clientConfiguration = new ClientConfiguration();
		clientConfiguration.setSignerOverride("AWSS3V4SignerType");
		return AmazonS3ClientBuilder.standard()
		.withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(aws_s3url, "us-east-1"))
		.withPathStyleAccessEnabled(true).withClientConfiguration(clientConfiguration)
		.withCredentials(new AWSStaticCredentialsProvider(credentials)).build();			
	}		
}

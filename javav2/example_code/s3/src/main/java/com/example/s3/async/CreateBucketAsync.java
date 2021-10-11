// snippet-comment:[These are tags for the AWS doc team's sample catalog. Do not remove.]
// snippet-sourcedescription:[CreateBucketAsync.java demonstrates how to create an Amazon Simple Storage Service (Amazon S3) object using the Async client.]
//snippet-keyword:[AWS SDK for Java v2]
//snippet-keyword:[Code Sample]
//snippet-service:[Amazon S3]
//snippet-sourcetype:[full-example]
//snippet-sourcedate:[10/06/2021]
//snippet-sourceauthor:[scmacdon-aws]

/*
   Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
   SPDX-License-Identifier: Apache-2.0
*/

package com.example.s3.async;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.*;
import java.net.URISyntaxException;
import java.util.concurrent.CompletableFuture;

/**
 * To run this AWS code example, ensure that you have setup your development environment, including your AWS credentials.
 *
 * For information, see this documentation topic:
 *
 * https://docs.aws.amazon.com/sdk-for-java/latest/developer-guide/get-started.html
 */
public class CreateBucketAsync {

    public static void main(String[] args) throws URISyntaxException {
        final String USAGE = "\n" +
                "Usage:\n" +
                "    <bucketName> \n\n" +
                "Where:\n" +
                "    bucketName - the name of the bucket to create. The bucket name must be unique, or an error occurs.\n\n" ;

        if (args.length != 1) {
            System.out.println(USAGE);
            System.exit(1);
        }

        String bucketName = args[0];
        System.out.format("Creating a bucket named %s\n",
                bucketName);

        Region region = Region.US_EAST_1;
        S3AsyncClient s3AsyncClient  = S3AsyncClient.builder()
                .region(region)
                .build();

        createBucket (s3AsyncClient, bucketName);
    }

    public static void createBucket( S3AsyncClient s3AsyncClient, String bucketName) {
        try {

            CreateBucketRequest bucketRequest = CreateBucketRequest.builder()
                    .bucket(bucketName)
                    .build();

            CompletableFuture<CreateBucketResponse> futureGet  = s3AsyncClient.createBucket(bucketRequest);
            futureGet.whenComplete((resp, err) -> {
                try {
                    if (resp != null) {
                        System.out.println(bucketName +" is ready~");
                    } else {
                        err.printStackTrace();
                    }
                } finally {
                    // Only close the client when you are completely done with it.
                    s3AsyncClient.close();
                }
            });
            futureGet.join();

        } catch (S3Exception e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }
}

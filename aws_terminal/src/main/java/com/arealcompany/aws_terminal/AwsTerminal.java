package com.arealcompany.aws_terminal;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.*;

import java.util.Map;

public class AwsTerminal {

    private static final String INSTANCE_TYPE = "t3.micro";
    private static Ec2Client ec2;
    private static final String[] services = new String[]{"ms-api-gateway", "ms-news", "ms-population", "ms-nba", "vaadin-client"};
    private static final Map<String,Integer> ports = Map.of(
            "ms-api-gateway", 8080,
            "ms-news", null,
            "ms-population", null,
            "ms-nba", null,
            "vaadin-client", 80
    );
    private static String imageId;

    public static void main(String[] args) {
        ec2 = Ec2Client.builder()
                .region(Region.IL_CENTRAL_1)
                .build();

        DescribeImagesRequest describeImagesRequest = DescribeImagesRequest.builder()
                .filters(Filter.builder()
                        .name("tag:Name")
                        .values("Docker")
                        .build()).build();
        DescribeImagesResponse imagesResponse = ec2.describeImages(describeImagesRequest);
        imageId = imagesResponse.images().getFirst().imageId();

        while(true){
            try{
                System.out.println("AWS Terminal");
                System.out.println("1. Start services");
                System.out.println("2. Shutdown services");
                System.out.println("3. Exit");
                String answer = System.console().readLine();
                switch(answer){
                    case "2" -> startServices();
                    case "3" -> shutdownServices();
                    case "4" -> exit();
                }
            } catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    private static void startServices() {
        System.out.println("Booting up services");
        for(String service : services){
            runIfNotRunning(imageId, service);
        }
    }

    private static void exit() {
    }

    private static void shutdownServices() {

    }

    private static void listInstances() {
    }

    private static void runIfNotRunning(String imageId, String service) {
        DescribeInstancesRequest describeInstancesRequest = DescribeInstancesRequest.builder()
                .filters(Filter.builder()
                        .name("tag:Name")
                        .values(service).build()).build();
        DescribeInstancesResponse instancesResponse = ec2.describeInstances(describeInstancesRequest);
        if(instancesResponse.hasReservations()){
            return;
        }

        System.out.printf("Starting %s%n", service);

        RunInstancesRequest runRequest = RunInstancesRequest.builder()
                .imageId(imageId)
                .instanceType(INSTANCE_TYPE)
                .maxCount(1)
                .minCount(1)
                .userData(getUserScript(service))
                .tagSpecifications(TagSpecification.builder()
                        .resourceType(ResourceType.INSTANCE)
                        .tags(Tag.builder()
                                .key("Name")
                                .value(service)
                                .build())
                        .build())
                .build();

        ec2.runInstances(runRequest);
    }

    private static String getUserScript(String imageName){
        return getUserScript(imageName, null);
    }

    private static String getUserScript(String imageName, Integer port){
        return """
                #!/bin/bash
                wget """
    }
}

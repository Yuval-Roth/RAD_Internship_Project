package com.arealcompany.aws_terminal;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.*;

public class AwsTerminal {

    private static final String INSTANCE_TYPE = "t3.micro";
    private static Ec2Client ec2;
    private static final String[] services = new String[]{"ms-api-gateway", "ms-news", "ms-population", "ms-nba", "vaadin-client"};

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
        String imageId = imagesResponse.images().getFirst().imageId();

        while(true){
            try{
                System.out.println("Booting up services");
                for(String service : services){
                    runIfNotRunning(imageId, service);
                }

                System.out.println("AWS Terminal");
                System.out.println("1. List instances");
                System.out.println("2. Start instances");
                System.out.println("3. Shutdown instances");
                System.out.println("4. Exit");
                String answer = System.console().readLine();
                switch(answer){
                    case "1" -> listInstances();
                    case "2" -> startInstances();
                    case "3" -> shutdownInstances();
                    case "4" -> exit();
                }
            } catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    private static void startInstances() {
    }

    private static void exit() {
    }

    private static void shutdownInstances() {

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
                .build();

        ec2.runInstances(runRequest);
    }

    private static String getUserScript(String imageName){
        return getUserScript(imageName, null);
    }

    private static String getUserScript(String imageName, Integer port){
        return """
                #!/bin/bash
                docker run --rm %s %s""".formatted(
                        port != null ? "-p %d:%d".formatted(port,port) : "",
                        imageName
        );
    }
}

package com.arealcompany.aws_terminal;

import com.arealcompany.ms_common.utils.EnvUtils;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.*;

import java.util.Arrays;
import java.util.Base64;
import java.util.List;

public class AwsTerminal {

    private static final String INSTANCE_TYPE = "t3.medium";
    private static final String INSTANCE_NAME = "RAD_microservices";
    private static Ec2Client ec2;
    private static String imageId;
    private static final String SECURITY_GROUP = "sg-0befc037eb4342677";

    public static void main(String[] args) {
        ec2 = Ec2Client.builder()
                .region(Region.IL_CENTRAL_1)
                .build();

        DescribeImagesRequest describeImagesRequest = DescribeImagesRequest.builder()
                .filters(Filter.builder()
                        .name("tag:Name")
                        .values("docker")
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
                    case "1" -> startServices();
                    case "2" -> shutdownServices();
                    case "3" -> exit();
                }
            } catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    private static void startServices() {
        System.out.println("Booting up services");
        runIfNotRunning(imageId, INSTANCE_NAME);
    }

    private static void exit() {
    }

    private static void shutdownServices() {

    }

    private static void runIfNotRunning(String imageId, String machineName) {
        if(isRunning()){
            return;
        }
        System.out.printf("Starting %s%n", machineName);

        RunInstancesRequest runRequest = RunInstancesRequest.builder()
                .imageId(imageId)
                .securityGroupIds(SECURITY_GROUP)
                .instanceType(INSTANCE_TYPE)
                .maxCount(1)
                .minCount(1)
                .keyName("default_key_pair")
                .userData(new String(Base64.getEncoder().encode(getUserScript().getBytes())))
                .tagSpecifications(TagSpecification.builder()
                        .resourceType(ResourceType.INSTANCE)
                        .tags(Tag.builder()
                                .key("Name")
                                .value(machineName)
                                .build())
                        .build())
                .build();
        ec2.runInstances(runRequest);
    }

    private static boolean isRunning() {
        return (int) ec2.describeInstances().reservations().stream()
                .flatMap(reservation -> reservation.instances().stream())
                .filter(instance -> instance.state().name() != InstanceStateName.TERMINATED)
                .filter(instance -> instance.tags().stream()
                        .anyMatch(tag -> tag.key().equals("Name") && tag.value().equals(INSTANCE_NAME)))
                .count() > 0;
    }

    private static String getUserScript(){
        return """
                #!/bin/bash
                cd ~/
                wget https://raw.githubusercontent.com/Yuval-Roth/RAD_Internship_Project/docker/aws_terminal/docker-compose.yml -O docker-compose.yml
                echo %s >> keys.env
                echo %s >> keys.env
                ./docker-compose up""".formatted(
                        "RAPIDAPI_KEY="+EnvUtils.getEnvField("RAPIDAPI_KEY"),
                        "GNEWS_KEY="+EnvUtils.getEnvField("GNEWS_KEY")
                );
    }
}

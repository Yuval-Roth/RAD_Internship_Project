package com.arealcompany.aws_terminal;

import com.arealcompany.ms_common.utils.EnvUtils;
import org.springframework.beans.factory.annotation.Value;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.*;

import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.NoSuchElementException;

public class AwsTerminal {

    @Value("${ec2.instance.type}")
    private static String INSTANCE_TYPE;
    @Value("${ec2.instance.name}")
    private static String INSTANCE_NAME;
    @Value("${ec2.instance.security_group}")
    private static String SECURITY_GROUP;

    private static Ec2Client ec2;
    private static String imageId;

    public static void main(String[] args) {
        ec2 = Ec2Client.builder()
                .region(Region.IL_CENTRAL_1)
                .build();

        DescribeImagesRequest describeImagesRequest = DescribeImagesRequest.builder()
                .filters(Filter.builder()
                        .name("tag:Name")
                        .values("docker")
                        .build())
                .build();
        DescribeImagesResponse imagesResponse = ec2.describeImages(describeImagesRequest);
        imageId = imagesResponse.images().getFirst().imageId();

        while (true) {
            try {
                System.out.println("AWS Terminal");
                System.out.println("1. Start services");
                System.out.println("2. Shutdown services");
                System.out.println("3. Exit");
                String answer = System.console().readLine();
                switch (answer) {
                    case "1" -> startServices();
                    case "2" -> shutdownServices();
                    case "3" -> exit();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void startServices() {
        System.out.println("Booting up services");
        runIfNotRunning(imageId, INSTANCE_NAME);
    }

    private static void exit() {
        System.out.println("Exiting AWS Terminal");
        ec2.close();
        System.exit(0);
    }

    private static void shutdownServices() {

        System.out.println("Shutting down services");
        DescribeInstancesRequest describeInstancesRequest = DescribeInstancesRequest.builder().build();
        DescribeInstancesResponse describeInstancesResponse = ec2.describeInstances(describeInstancesRequest);
        Instance instance;
        try {
            instance = describeInstancesResponse.reservations().stream()
                    .flatMap(reservation -> reservation.instances().stream())
                    .filter(i -> i.state().name() != InstanceStateName.TERMINATED)
                    .filter(i -> i.tags().stream()
                            .anyMatch(tag -> tag.key().equals("Name") && tag.value().equals(INSTANCE_NAME)))
                    .toList().get(0);
        } catch (NoSuchElementException exception) {
            System.out.println("No running instances found to shutdown");
            return;
        }

        TerminateInstancesRequest terminateRequest = TerminateInstancesRequest.builder()
                .instanceIds(instance.instanceId())
                .build();
        ec2.terminateInstances(terminateRequest);
        System.out.printf("Terminated instance with ID: %s%n", instance.instanceId());

    }

    private static void runIfNotRunning(String imageId, String machineName) {
        if (isRunning()) {
            System.out.printf("%s is already running%n", machineName);
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
                .filter(instance -> instance.state().name() != InstanceStateName.TERMINATED &&
                        instance.state().name() != InstanceStateName.SHUTTING_DOWN)
                .filter(instance -> instance.tags().stream()
                        .anyMatch(tag -> tag.key().equals("Name") && tag.value().equals(INSTANCE_NAME)))
                .count() > 0;
    }

    private static String getUserScript() {
        return """
                #!/bin/bash
                cd ~/
                wget https://raw.githubusercontent.com/Yuval-Roth/RAD_Internship_Project/main/aws_terminal/docker-compose.yml -O docker-compose.yml
                echo %s >> keys.env
                echo %s >> keys.env
                ./docker-compose up"""
                .formatted(
                        "RAPIDAPI_KEY=" + EnvUtils.getEnvField("RAPIDAPI_KEY"),
                        "GNEWS_KEY=" + EnvUtils.getEnvField("GNEWS_KEY"));
    }
}

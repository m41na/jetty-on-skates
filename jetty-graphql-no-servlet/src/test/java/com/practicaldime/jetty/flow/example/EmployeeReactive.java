package com.practicaldime.jetty.flow.example;

import java.util.List;
import java.util.concurrent.SubmissionPublisher;

public class EmployeeReactive {

	public static void main(String args[]) throws InterruptedException {

		// Create Publisher
		SubmissionPublisher<Employee> publisher = new SubmissionPublisher<>();

		// Register Subscriber
		EmployeeSubscriber subs = new EmployeeSubscriber();
		publisher.subscribe(subs);

		List<Employee> emps = EmployeeRepo.getEmps();

		// Publish items
		System.out.println("Publishing Items to Subscriber");
		emps.stream().forEach(i -> publisher.submit(i));

		// poll the counter to await processing of all messages
		while (emps.size() != subs.getCounter()) {
			Thread.sleep(10);
		}
		// close the Publisher
		publisher.close();
		
		System.out.println("Exiting the app");
	}
}

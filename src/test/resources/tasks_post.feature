#language en
Feature: Create a new task
  In order to remember something I have to do
  As a user
  I want to create a task

  Scenario: Create a task OK
    When I POST /tasks with
    """
    Content-Type: application/json

    {"description":"Write a Scala BDD example using JaxRS"}
    """
    Then the response status is 201
    And the response header Location matches https?://[^/]+/tasks/[0-9]+
    When I GET @Link
    Then the response status is 200
    And response["description"] is "Write a Scala BDD example using JaxRS"
    And response["done"] is false


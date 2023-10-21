# xxx
# @name=xyz
Feature: Guess the word
  123
  abc
  xyz

  # The background example
  Background:
    Given a global administrator named "Greg"
    # @tag=http://www.google.com/method?a=<b>
    And a blog named "Greg's anti-tax rants"
    And a customer named "Dr. Bill"
    And a blog named "Expensive Therapy" owned by "Dr. Bill"

  # The Scenario Outline example
  Scenario Outline: eating
    Given there are <start> cucumbers
    When I eat <eat> cucumbers
    Then I should have <left> cucumbers

    # Examples:
    Examples:
      | start | eat | left |
      |    12 |   5 |    7 |
      |    20 |   5 |   15 |

  # The first example has two steps
  Scenario: Maker starts a game
    # @tag=http://www.google.com/method?a=<b>
    When the Maker starts a game
    Then the Maker waits for a Breaker to join

  # The second example has three steps
  Scenario: Breaker joins a game
    Given the Maker has started a game with the word "silky"
    # this is and
    And given
    When the Breaker joins the Maker's game
    Then the Breaker must guess a word with 5 characters

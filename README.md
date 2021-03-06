#RegExpBuilder

RegExpBuilder integrates [Regular Expressions](http://wikipedia.org/wiki/Regular_expression) into the programming
language, thereby making them easy to read and maintain. Regular Expressions are created by using chained methods and variables such as arrays or strings.

##How to start
There are implementations available for Dart, Javascript, Java, and Python.

##Examples
Here are a couple of examples using Javascript:

###Money

```javascript
var regex = r
  .find("$")
  .min(1).digits()
  .then(".")
  .digit()
  .digit()
  .getRegExp();

regex.test("$10.00")); // true
```

###Nested patterns

```javascript
var pattern = r
  .min(1).of("p")
  .min(2).of("q");

var regex = r
  .exactly(2).like(pattern)
  .getRegExp();

regex.test("pqqpqq"); // true
```

##API documentation
RegExpBuilder can represent literally every possible regular expression using methods such as either(), or(), behind(), asGroup() and so on. You can find the API documentation for each language [here](https://github.com/thebinarysearchtree/RegExpBuilder/wiki).

##Dependencies
This projects depends on the [JUnit](http://junit.org) library for unit tests, so you have to have the _jar_ in your path.
An option is to switch to the [maven-support branch](https://github.com/IOAyman/regexpbuilderjava/tree/maven-support) (`git checkout maven-support`) if you prefer using [maven] (http://maven.org).

##License
The [MIT License](https://github.com/IOAyman/regexpbuilderjava/blob/master/LICENSE.md)

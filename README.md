#RegExpBuilder v1.0

RegExpBuilder integrates [Regular Expressions](http://wikipedia.org/wiki/Regular_expression) into the programming
language, thereby making them easy to read and maintain. Regular Expressions are created by using chained methods and variables such as arrays or strings.

##How to start
There are implementations available for Dart, Javascript, Java, and Python.

##Examples
Here are a couple of examples using Javascript:

###Money

```
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

```
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

##License
The [MIT License](https://github.com/IOAyman/regexpbuilderjava/blob/master/LICENSE.md)

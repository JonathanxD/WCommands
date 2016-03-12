# WCommands

Yet Another Command API - This Project is an API that provides a Interface to create and handle commands,
 using Reflection to call methods and change Field values (see **Reflection API**) or Handler implementation to Handle commands created using
 Specifications (see **Command API**)

### Command API

#### Creating a Manager

```java
WCommandCommon manager = new WCommandCommon();
```

#### Creating a simple command

This command will print "`[SAY] FOO`"

```java
CommandSpec spec = CommandBuilder.builder()
    // Command Name
    .withName(Text.of("say"))
    // Command Handler/Listener
    .withCommonHandler((c) -> {
        // DO A ACTION
        System.out.println("[SAY] FOO");
    })
    // Build command
    .build();
```

##### Register

```java
// Register command
manager.registerCommand(spec);
```

##### Test

```java
// Process & Invoke commands
// Call process() and invoke()
manager.processAndInvoke("say");
```

Output: `[SAY] FOO`

#### Creating a argument

Creating arguments is similar to commands

ID Enum:
```java
public enum ID {
  TEXT
}
```

```java
CommandSpec spec = CommandBuilder.builder()
    // Command Name
    .withName(Text.of("say"))
    // Command Handler/Listener
    .withCommonHandler((c) -> {
        // DO A ACTION
        System.out.println("[SAY] FOO");
    })
    // Define argument(s)
    .withArgument(
        ArgumentBuilder
            // Create a Build of ArgumentSpec ID type as ID and Value Type as String
            .<ID, String>builder()
            // Defines the ID
            .withId(ID.TEXT)
            // Defines the 'acceptor', in this case, accept all arguments
            .withPredicate(text->true)
            // Defines the converter, in this case, getPlainString of Text
            .withConverter(Text::getPlainString)
            // Set as Optional argument
            .setOptional(true)
            // Build argument
            .build()
        )
    // Build command
    .build();
```

Now, I'll retrieve the argument.

In `withCommonHandler`

```java
    //...
    .withCommonHandler((c) -> {
        // DO A ACTION
        CommandHolder ch = c.getCommand();
        // Try to find argument value
        Optional<String> str = ch.getArgValue(ID.TEXT);
        // Check if is present
        if(str.isPresent()) {
            // Say argument value
            System.out.println("[SAY] "+str.get());
        }else{
            // If is not present, say FOO
            System.out.println("[SAY] FOO");
        }
        // or
        // System.out.println("[SAY] "+str.orElse("FOO"));
    })
    //...
```


##### Test

```java
// Process & Invoke commands
// Call process() and invoke()
manager.processAndInvoke("say");
manager.processAndInvoke("say", "foo bar");
```
Output: `[SAY] FOO` and `[SAY] foo bar`


### Reflection API

Reflection API help developer to create Commands that call methods or change variables state.

#### Creating Manager

Reflection API Manager is different to Command API Manager

```java
MyClass myClass = new MyClass();
ReflectionCommandProcessor commandProcessor = ReflectionAPI.createWCommand(myClass);
```

#### Field

```java
public class MyClass {
  // Command annotation is required to declare command named 'say'
  @Command
  // Argument annotation is required to define it as argument
  // If argument annotation is not present, the command 'say' will do nothing
  @Argument(isOptional = true)
  private String say = "foo";
}
```

Using Reflection API is not needed to declare ID, the system will do that for you. However you can declare, but, it is optional.

##### Test

```java
// Call "say" command, will do nothing!
commandProcessor.processAndInvoke("say");
// Print variable
System.out.println("[SAY] "+myClass.say)
// Call "say" command with "foo bar" argument
commandProcessor.processAndInvoke("say", "foo bar");
// Print variable
System.out.println("[SAY] "+myClass.say)
```

Output: `[SAY] foo` and `[SAY] foo bar`

#### Method

```java
// Defines command named 'say2'
@Command
public void say2(/* Defines a optional argument*/ @Argument(isOptional = true) String text) {
  // If you define an argument as optional, it will be null if not present.
  System.out.println("[SAY2] "+(text == null ? "foo" : text));
}
```

Also you can use `Optional<String>`, but, you can only use `Optional` if you declare `isOptional = true`.

```java
// Defines command named 'say2'
@Command
public void say2(/* Defines a optional argument*/ @Argument(isOptional = true) Optional<String> text) {
  // If you define an argument as optional and argument type is Optional, it will be empty if not present.
  System.out.println("[SAY2] "+ text.orElse("foo"));
}
```

##### Test

```java
commandProcessor.processAndInvoke("say2");
commandProcessor.processAndInvoke("say2", "foo bar");
```

Output: `[SAY2] FOO` and `[SAY2] foo bar`

#### Sub Commands Since 1.5.3 @11/03/2016 

Since version 1.5.3 has a new way to add sub commands. See the example:

```java
//Defines command named greet
@Command
public void greet() {
  System.out.println("Hi!");
}
// Create a sub command named special for greet command
@SubCommand(value = "greet", commandSpec = @Command(isOptional = true))
public void special() {
  System.out.println("Hi <3");
}
// Also you can create a Sub command for a sub command.
@SubCommand(value = {"greet", "special"}, commandSpec = @Command(isOptional = true))
public void fan() {
  System.out.println(" ❤ ");
}
```

##### Test

```java
commandProcessor.processAndInvoke("greet");
commandProcessor.processAndInvoke("greet", "special");
commandProcessor.processAndInvoke("greet", "special", "fan");
```

Output: `Hi!`, `Hi <3` and ` ❤ `



##### How Works

SubCommandVisitor find command based on path (value) defined in annotation and call CommandVisitor to Parse @Command annotation, then adds the result as sub command.

**Attention**: ~~This System is under development, Sub Commands for Sub Commands may throw exceptions. If the SubCommand is parsed before the target SubCommand, the system will not find the command in register list and will throw exception. This problem don't occurs with simple @Command!~~

**Attention**: Fixed the problem with registration order. Now I need to develop a Cyclic dependency detection algorithm. cyclic dependencies will cause system to go to infinite loop like `parse cmd -> require cmd2 -> postpone cmd -> parse cmd2 -> require cmd -> postpone cmd2 -> parse cmd -> ...`.

### MIXING

You can Mix `Command API` with `Reflection API`, `ReflectionCommandProcessor` extends `WCommandCommon`, and
it have methods like `exportTo` and `importFrom` these methods Exports and Imports commands and interceptors.


### Intercept Handlers and change arguments

Intercepting handlers is simple

Using commands and arguments created in [Creating a argument](#creating-a-argument)

First, I will try to change the value of argument:

```java
manager.addInterceptor((commandData, handler) -> {
  // Find argument with ID.TEXT
  Optional<ArgumentHolder<ID, Object>> argumentHolder = commandData.getCommand().getArgument(ID.TEXT);
  // If present setValue
  argumentHolder.ifPresent(v -> v.setValue("ad"));
});
```

If you want to cancel command handling, you need to set handle as null like:

```java
handler.set(null);
```

Also you can 'replace' the handler

```java
handler.set(o -> System.out.println("hello from other handler!"));
```



##### Test

```java
commandProcessor.processAndInvoke("say", "foo bar");
```
Output: `[SAY] ad`


### More Examples

You can find more examples in Project Tests

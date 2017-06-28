# WCommands (**unmanteined**)

**Superseded by [KWCommands](https://github.com/JonathanxD/KWCommands)**

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
        return null;
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
        reutrn null;
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
    .withCommonHandler((c, data) -> {
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
        return null;
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



### Information

Information is a class that allow to you to register pre-defined and persistent "information". The "information" will be passed to Reflection and Command API in Handlers.

Examples:

[Command API](https://github.com/JonathanxD/WCommands/tree/master/src/test/java/com/github/jonathanxd/wcommands/commandapi/TestInformation.java)
[Reflection API](https://github.com/JonathanxD/WCommands/tree/master/src/test/java/com/github/jonathanxd/wcommands/reflection/TestInformation.java)


Information need to be passed with arguments.

##### Building Information

```java
InformationRegister
  // Create information builder with Immutable CommandList
  // Alternatives: 
  // .builder(WCommand) -> Create with WCommand
  // .blankBuilder()    -> Create without any default value.
  .builderWithList(processor)
  // Define the Sender as Sender "User". First parameter is ID to get the Information Value.
  .with(Sender.class, new Sender("User"))
  // Also you may want to add a description:
  //.with(Sender.class, new Sender("User"), "Description")
  // Build
  .build()
```

In your command handler you can get the information:

```java
// .... Handler
Optional<Person> sender = information.getOptional(Sender.class);
sender.ifPresent(sender -> System.out.printf("Sender: %s%n", sender.getName());
```

If you are using ReflectionAPI, has 2 ways to fetch the Information.
 
```java
@Command(...)
public void myCommand(@Info Information<Sender> senderInfo) {
  Sender sender = senderInfo.get();
  System.out.println("Sender: "+sender.getName());
}
```

Or

```java
@Command(...)
public void myCommand(Sender senderInfo) {
  Sender sender = senderInfo;
  System.out.println("Sender: "+sender.getName());
}
```

Information support object orientation, like:

```java
interface Entity {
//...
String getName();
//...
}
class Sender implements Entity {
...
}
@Command(...)
public void myCommand(@Info Information<Entity> entityInfo) {
  Entity entity = entityInfo.get();
  System.out.println("Entity: "+entity.getName());
}
```

If you add more than one information with same value type, the ReflectionAPI will not work correctly

##### Since 17/03/2016 - Version 1.8

InformationAPI only supports InfoId with tags and Type.

Reflection improvement:

```java
class Receiver{}
// ...
InformationRegister
  // Create information builder with Immutable CommandList
  .builder(processor)
  // Define the Receiver.
  .with(Receiver.class, (Entity) () -> "User2")
  // Define the Sender as Sender "User". First parameter is ID to get the Information Value.
  .with(Sender.class, new Sender("User"))
  // Build
  .build()
```

```java
@Command(...)
public void message(String message, @Info(type = Sender.class) Entity sender, @Info(type = Receiver.class) Entity receiver) {
//doSomething();
}
```

Reflection will pass `null` as parameter or an empty `Information` if information isn't provided.

Also you can use tags:

```java
.with("Receiver", (Entity) () -> "User2")
.with("Sender", (Entity) () -> "User")
```

```java
@Command(...)
public void message(String message, @Info(tags = "Sender") Entity sender, @Info(tags = "Receiver") Entity receiver) {
//doSomething();
}
//
// 'Or' operator tag:
@Command(...)
public void message(String message, @Info(tags = {"Receiver", "Sender"}) Entity receiverOrSender) {
//doSomething();
}
```

### Helper

HelperAPI is an command detail print system, with HelperAPI you can print command information if occurs an exception during command parsing.

How to use? You need only to construct a WCommand with HelperErrorHandler like that:

```java
// Command API
WCommandCommon commandCommon = new WCommandCommon(new HelperErrorHandler(CommonPrinter.TO_SYS_OUT));
// Reflection API
ReflectionCommandProcessor processor = ReflectionAPI.createWCommand(new HelperErrorHandler(CommonPrinter.TO_SYS_OUT), ...);
```

You can also call directly HelperAPI Methods.

```java
// Print Help for all commands and child commands.
HelperAPI.help(WCommand<?> command, Printer printer);
// Print help for commands in list and child commands.
HelperAPI.help(CommandList commandSpecs, Printer printer);
// Print help for a specific command and child commands.
HelperAPI.help(CommandSpec command, Printer printer);
```


### More Examples

You can find more examples in Project Tests

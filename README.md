#WCommands

Yet Another Command API - This Project is and API that provides a Interface to create and handle commands,
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
  // If you define an argument as optional, it will be null if not present.
  System.out.println("[SAY2] "+ text.orElse("foo");
}
```

##### Test

```java
commandProcessor.processAndInvoke("say2");
commandProcessor.processAndInvoke("say2", "foo bar");
```

Output: `[SAY2] FOO` and `[SAY2] foo bar`



### MIXING

You can Mix `Command API` with `Reflection API`, `ReflectionCommandProcessor` extends `WCommandCommon`, and
it have methods like `exportTo` and `importFrom` these methods Exports and Imports commands and interceptors.

### More Examples

You can find more examples in Project Tests

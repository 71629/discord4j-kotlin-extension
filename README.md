# Kommand - For [Discord4J](https://github.com/Discord4J/Discord4J)

## 🐿️ Features
- ☑️**Ease of Use**: Automates application commands installation, no more writing JSON yourself
- 📖**Readable**: Delegates options in slash commands and fields in modals so you can access the user's input like how you access regular objects
- 🧩**Kotlin Idiomatic**: DSL for building application command interactions and message components
- soon

## 🚌 Installation

### Requirements
- JDK 8+

You may install this library to your project using jitpack:

```kotlin
repositories {
    whateverRepositoryYouAreUsing()
    maven {
        name = "Jitpack"
        url = uri("https://jitpack.io")
    }
}

dependencies {
    implementation("com.discord4j:discord4j-core:3.3.3")
    implementation("com.hashtag071629:discord4j-kotlin-extension:1.0-SNAPSHOT")
    
    // You might also want to add those
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:1.10.2")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions:1.2.4")
    
    // And this
    implementation("ch.qos.logback:logback-classic:1.5.32")
}
```

## 🚀 Quick Example For Busy People

A basic command that replies to the user with the content sent to the bot:

```kotlin
// Import the classes and functions yourself.

fun main() {
    client("MTU.Your.Token", IntentSet.nonPrivileged()) {
        slashCommand {
            echoCommand()
        }
    }
    
    client.onDisconnect().block()
}

fun SlashCommand.echoCommand() = install {
    name = "echo"
    description = "3, 2, 1, repeat after me..."
    
    val content by require(stringOption("content", "the thing I should say..."))

    onInteraction { event ->
        event.reply(content).subscribe()
    }
}
```

## 💡 Examples on Notable Features

### Functional Message Components DSL

This library allows building message components using functional programming.

Button definition:

```kotlin
const val GREET_BUTTON_ID = "greet"

fun ButtonConfigurator.greetButton() = install(GREET_BUTTON_ID) {
    onClick { event ->
        event.reply("Hi!").subscribe()
        event.message.get().edit().withComponentsOrNull(helloContainer(true)).subscribe()
    }

    onException { _, e ->
        e.printStackTrace()
    }
}
```

Container definition

```kotlin
fun helloContainer(disable: Boolean) = components {
    container {
        +"Click The Button"
        separator()
        actionRow {
            button(GREET_BUTTON_ID) {
                style = if (disable) Button.Style.SECONDARY else Button.Style.PRIMARY
                label = if (disable) "Greeted" else "Click Me"
                disabled = disable
            }
        }
    }
}
```

The slash command

```kotlin
fun SlashCommand.greetCommand() = install {
    name = "greet"
    description = "Greets the user"

    onInteraction { event ->
        event.reply().withComponents(helloContainer(false)).subscribe()
    }
}
```

Initialization

```kotlin
client("MTU.Your.Token", IntentSet.nonPrivileged()) {
    slashCommand {
        greetCommand()
    }

    button {
        greetButton()
    }
}
```

## 🗣️ Yapping

⭐

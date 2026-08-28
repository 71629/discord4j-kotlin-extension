# Certainly! I'll explore your codebase and come up with an easy-to-remember name for this project!

I need a better project name.

## 🐿️ Features
- ☑️**Ease of Use**: Automates application commands installation, no more writing JSON yourself
- 📖**Readable**: Delegates options in slash commands and fields in modals so you can access the user's input like how you access regular objects
- 🧩**Kotlin Idiomatic**: Message Components DSL
- soon

## 🚌 Installation

### Requirements
- JDK 8+

This project is currently in a very early stage of development and is very unstable. Therefore, you'll need to build the artifacts yourself.
The master branch will always be targeting the latest version of Discord4J, this will be the same until I decide to release a stable version of this library.

Publish the artifacts to maven local:
```bash
./gradlew publishToMavenLocal
```

Install the library to your project:

```kotlin
repositories {
    whateverRepositoryYouAreUsing()
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

A basic command that replies the user with the content sent to the bot:

```kotlin
// Import the classes and functions yourself.

fun main() {
    runBlocking {
        client("MTU.Your.Token", IntentSet.all()) {
            slashCommand {
                install(Echo())
            }
        }
    }
    
    client.onDisconnect().block()
}

class Echo : SlashCommand() {
    override val name = "echo"
    override val description = "3, 2, 1, repeat after me..."
    
    private val content by require(stringOption("content", "the thing I should say..."))

    override suspend fun handle(event: ChatInputInteractionEvent) {
        event.reply(content).subscribe()
    }
}
```

## 💡 Examples on Notable Features

### Functional Message Components DSL

This library allows building message components using functional programming.
```kotlin
fun helloContainer(disable: Boolean) = components {
    container {
        +"-# Click the button!"
        separator()
        actions(disable)
    }
}

fun ContainerBuilder.actions(disable: Boolean) = actionRow {
    button(/* customId */ "reply") {
        style = Button.Style.SUCCESS
        label = "click me!"
        disabled = disable
        
        onClick { event ->
            event.reply("you clicked me!").subscribe()

            event.message.get().edit().withComponents(helloContainer(true)).subscribe()
        }
    }
}
```

## 🗣️ Yapping

⭐

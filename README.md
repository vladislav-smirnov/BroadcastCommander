# BroadcastCommander

A lightweight Android library for handling broadcast commands with ease through XML configuration.

---

## Features

- Simple XML-based command mapping configuration
- Support for different command types (Bundle, String(TODO), Boolean(TODO), Int(TODO))
- Reflection-based command execution
- Callback support for command handling
- Clean architecture implementation
- TODO: Thread-safe callback management

---

## Installation

Add the dependency to your app's `build.gradle`:

```gradle
dependencies {
    implementation 'io.github.airdaydreamers:broadcastcommander:x.x.x'
}
```

---

## Setup

1. Create an XML file in your `res/xml` directory to define command mappings. You can choose between two XML formats:

### Simple Format (`command_mappings_simple.xml`):
```xml
<?xml version="1.0" encoding="utf-8"?>
<commands>
    <command
        action="SIMPLE_ACTION_COMMAND_BUNDLE"
        handler="com.example.app.handlers.SimpleCommandUseCase"
        type="android.os.Bundle" />
</commands>
```

### Detailed Format (`command_mappings.xml`):
```xml
<?xml version="1.0" encoding="utf-8"?>
<commandItems xmlns:lib="http://schemas.android.com/apk/res-auto">
    <item
        lib:className="com.example.app.handlers.CommandUseCase"
        lib:command="ACTION_COMMAND_BUNDLE"
        lib:type="Bundle" />
</commandItems>
```

2. Create handler classes for your commands:

```kotlin
class SimpleCommandUseCase {
    operator fun invoke(bundle: Bundle) {
        // Handle the command
        bundle.getString("text")?.let {
            // Process the text
        }
    }
}
```

---

## Usage

### Sending Commands

You can send commands using ADB or programmatically:

#### Using ADB:
```bash
adb shell am broadcast -a io.github.airdaydreamers.commands.ACTION_COMMAND --es command "SIMPLE_ACTION_COMMAND_BUNDLE" --es text "Hello World" your.package.name
```

#### Programmatically:
```kotlin
val intent = Intent("io.github.airdaydreamers.commands.ACTION_COMMAND").apply {
    putExtra("command", "SIMPLE_ACTION_COMMAND_BUNDLE")
    putExtra("text", "Hello World")
}
context.sendBroadcast(intent)
```

### Handling Commands

The library automatically handles commands based on your XML configuration. When a command is received:

1. The library looks up the command in your XML configuration
2. Instantiates the corresponding handler class
3. Invokes the handler with the provided bundle

### Command Types

The library supports several command types:
- `Bundle`: For complex data structures
- `String`: For text-based commands -> TODO
- `Boolean`: For toggle-like commands -> TODO
- `Int`: For numeric commands -> TODO
- `Callback`: For commands requiring callbacks -> TODO

## Command Handler Implementation

Your command handlers should follow this pattern:

```kotlin
class YourCommandHandler {
    operator fun invoke(bundle: Bundle? = null) {
        // Handle the command
    }
    
    // Optional: Handle with context
    operator fun invoke(context: Context, bundle: Bundle? = null) {
        // Handle the command with context
    }
}
```

---

## Callback Support

Register callbacks to receive command notifications:

```kotlin
CallbackManager.registerCallback { bundle ->
    // Handle command callback
}
```

Don't forget to unregister when done:

```kotlin
CallbackManager.unregisterCallback(callback)
```

---

## Contributing

Contributions are welcome! Feel free to fork the repository, open issues, and submit pull requests if you'd like to contribute to the project!
Please make sure your code follows the project's guidelines and includes relevant documentation or tests.

---

Author
------
Vladislav Smirnov - @vladislav-smirnov on GitHub

---

## License

This project is licensed under the Apache License 2.0.  
See the [LICENSE](LICENSE) file for more details.

```
Copyright © 2024 Vladislav Smirnov

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

# SkyMythDE

SkyMythDE is a simple 1.8 Minecraft SkyPvP "Boilerplate" written in Java. This project provides a basic framework to kickstart the development of a SkyPvP game mode, featuring essential functionalities and customizable features. 

## Table of Contents

- [Features](#features)
- [Installation](#installation)
- [Usage](#usage)
- [Configuration](#configuration)
- [Contributing](#contributing)
- [License](#license)
- [Contact](#contact)

## Features

- **Basic SkyPvP Setup**: Provides a foundational setup for a SkyPvP game mode.
- **Customizable Configuration**: Easily modify game settings and configurations to suit your needs.
- **Player Management**: Includes basic player management functionalities.
- **Event Handling**: Supports essential Minecraft events for SkyPvP gameplay.
- **Extensible Framework**: Designed to be easily extended and customized.

## Installation

To set up SkyMythDE on your Minecraft server, follow these steps:

1. **Clone the repository**:
   ```bash
   git clone https://github.com/whynotmax/SkyMythDE.git
   cd SkyMythDE
   ```

2. **Build the project**:
   Ensure you have Maven installed on your system, then run:
   ```bash
   ./gradlew clean shadowJar
   ```

3. **Deploy the plugin**:
   Copy the generated jar file from the `target` directory to your Minecraft server's `plugins` directory.

4. **Start the server**:
   Start your Minecraft server to load the SkyMythDE plugin.

## Usage

Once the plugin is installed and the server is running, you can start configuring and using the SkyPvP functionalities. Refer to the [Configuration](#configuration) section for details on modifying the settings.

## Configuration

SkyMythDE comes with a default configuration file located in the `plugins/SkyMythDE` directory. You can customize various settings to tailor the gameplay experience. The configuration options include:

- **Spawn Locations**: Define spawn points for players.
- **Kits**: Configure default kits available to players.
- **Arena Settings**: Customize arena boundaries and other settings.

## Contributing

We welcome contributions to SkyMythDE! If you have ideas for improvements or have found a bug, feel free to open an issue or submit a pull request. Please follow the [contribution guidelines](CONTRIBUTING.md) when contributing to the project.

## License

SkyMythDE is licensed under the MIT License. See the [LICENSE](LICENSE) file for more details.

## Contact

If you have any questions or need further assistance, you can reach out to the project maintainer:

- GitHub: [whynotmax](https://github.com/whynotmax)

---

Thank you for using SkyMythDE! We hope you enjoy developing your SkyPvP game mode with our boilerplate.

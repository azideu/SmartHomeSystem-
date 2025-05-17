# SmartHomeSystem

**SmartHomeSystem** is a Java-based smart home simulation developed as a group project for CSC186. It demonstrates object-oriented programming principles and allows users to interact with various smart devices through both a command-line interface and a graphical user interface.

## Overview

This project simulates a smart home environment where users can control and monitor devices such as lights, thermostats, and security systems. The system supports user authentication, role management, and real-time device status tracking.

## Features

* Control and monitor smart home devices
* User authentication with role-based access
* Device status tracking and activity logging
* Swing-based GUI with device list, control buttons, and real-time status bar
* Command-line interface for systems without GUI support

## Project Structure

```
SmartHomeSystem-/
├── Main.java           # Entry point
├── devices/            # Smart device classes (Light, Thermostat, etc.)
├── user/               # User login, roles, and authentication
├── utils/              # Helper classes and utilities
└── gui/                # Swing-based GUI components
```

## Getting Started

### Prerequisites

* Java Development Kit (JDK) 8 or higher

### Running the Application

1. Clone the repository:

   ```bash
   git clone https://github.com/azideu/SmartHomeSystem-.git
   ```

2. Navigate to the project directory:

   ```bash
   cd SmartHomeSystem-
   ```

3. Compile the Java files:

   ```bash
   javac Main.java
   ```

4. Run the application:

   ```bash
   java Main
   ```

## Usage

**GUI Mode**

* Use the interface to add, remove, or toggle devices.
* Device statuses update in real time in the status bar.

**CLI Mode**

* Follow the on-screen prompts to log in and interact with devices using text commands.

## Future Enhancements

Planned improvements to the system include:

* Improved graphical user interface using JavaFX and CSS
* More robust and user-friendly input validation and error handling
* Enhanced scheduling features for device automation

## Contributing

Contributions are welcome. To contribute:

1. Fork the repository.
2. Create a feature branch.
3. Make your changes and commit them.
4. Open a pull request for review.

We appreciate your support. Thank you! ニャン〜

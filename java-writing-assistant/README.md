# Writing Assistant

A Spring Boot application that leverages OpenAI's GPT models to provide intelligent writing assistance. The application includes multiple specialized agents for different aspects of writing improvement.

## Features

- Context-aware text analysis
- Writing improvement suggestions
- Conversation history tracking
- Multiple specialized writing agents

## Prerequisites

- Java 17 or higher
- Maven 3.6 or higher
- OpenAI API access

## Setup

1. Clone the repository:
```bash
git clone <repository-url>
cd java-writing-assistant
```

2. Configure the application:
   - Copy `src/main/resources/application.properties.template` to `src/main/resources/application.properties`
   - Update the properties file with your OpenAI API credentials:
     ```properties
     openai.api.key=your-api-key-here
     openai.api.base-url=your-base-url-here
     ```

3. Build the project:
```bash
mvn clean install
```

4. Run the application:
```bash
mvn spring-boot:run
```

## Project Structure

```
src/main/java/com/example/writingassistant/
├── WritingAssistantApplication.java
├── agent/
│   ├── BasicWritingAgent.java
│   ├── ContextAwareAgent.java
│   └── ImprovementAgent.java
├── config/
│   └── OpenAIConfig.java
├── controller/
│   └── WritingAssistantController.java
├── model/
│   └── ConversationEntry.java
└── service/
    ├── OpenAIClientService.java
    ├── OpenAIClientServiceImpl.java
    └── WritingAssistantService.java
```

## Configuration

The application uses the following configuration properties:

- `openai.api.key`: Your OpenAI API key
- `openai.api.base-url`: The base URL for OpenAI API (can be customized for proxy setups)

## Security Note

Never commit your actual API credentials to version control. The `application.properties` file is included in `.gitignore` for this reason.

## Contributing

1. Fork the repository
2. Create your feature branch
3. Commit your changes
4. Push to the branch
5. Create a new Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details. 
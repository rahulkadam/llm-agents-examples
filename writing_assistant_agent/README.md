# Writing Assistant Agent

An advanced AI-powered writing assistant system that combines multiple specialized agents to help with various writing tasks. The system uses OpenAI's GPT models and maintains context awareness for improved assistance.

## Agents Overview

### 1. Basic Writing Assistant (WritingAssistantAgent)
- **Purpose**: Provides fundamental writing analysis and improvements
- **Capabilities**:
  - Text analysis (tone, style, readability)
  - Writing improvements (grammar, clarity, structure)
  - Topic research
- **Model**: GPT-3.5-turbo
- **Use Case**: Quick analysis and basic improvements

### 2. Enhanced Writing Agent (EnhancedWritingAgent)
- **Purpose**: Advanced writing analysis and style manipulation
- **Capabilities**:
  - Deep content analysis
  - Style transfer between different writing styles
  - Audience-specific content adaptation
  - Content expansion and summarization
- **Model**: GPT-4
- **Use Case**: Detailed analysis and style transformations

### 3. Context Storage Agent (ContextStorageAgent)
- **Purpose**: Manages conversation and analysis history
- **Capabilities**:
  - Stores context in JSON format
  - Organizes by context type
  - Manages context lifecycle
  - Automatic cleanup of old contexts
- **Use Case**: Maintaining conversation history and analysis results

### 4. Context Aware Agent (ContextAwareAgent)
- **Purpose**: Utilizes stored context for improved responses
- **Capabilities**:
  - Retrieves relevant historical context
  - Scores context relevance
  - Generates context-aware responses
- **Model**: GPT-4 for generation, GPT-3.5-turbo for scoring
- **Use Case**: Providing responses with historical awareness

## Integrated Workflows

### 1. Complete Writing Workflow
Chains multiple agents for comprehensive writing improvement:
1. Basic Analysis (WritingAssistant)
   - Initial assessment of tone, style, and readability
2. Enhanced Analysis (EnhancedWritingAgent)
   - Deep dive into content structure and effectiveness
3. Writing Improvements (WritingAssistant)
   - Specific suggestions for enhancement
4. Style Transfer (EnhancedWritingAgent)
   - Adaptation to desired writing style
- Results are stored for future reference

### 2. Research and Enhance Workflow
Combines research and context for in-depth topic exploration:
1. Initial Research (WritingAssistant)
   - Gathers key information and concepts
2. Context-Aware Analysis (ContextAwareAgent)
   - Adds historical context and related insights
3. Enhanced Analysis (EnhancedWritingAgent)
   - Analyzes combined information
- Research results are preserved in context

## Setup

1. Create a virtual environment:
```bash
python -m venv venv
source venv/bin/activate  # On Unix/macOS
# or
.\venv\Scripts\activate  # On Windows
```

2. Install dependencies:
```bash
pip install -r requirements.txt
```

3. Create a `.env` file with your API keys:
```
OPENAI_API_KEY=your_api_key_here
OPENAI_API_BASE_URL=your_base_url_here  # Optional
```

## Usage

Run the interactive assistant:
```bash
python main.py
```

Choose from the following options:
1. Basic Analysis
2. Enhanced Analysis
3. Style Transfer
4. Context-Aware Generation
5. Complete Writing Workflow (Chained)
6. Research and Enhance (Chained)
7. Quit

## Context Storage

Contexts are stored in the `context_storage` directory with the following structure:
- Filename format: `{context_type}_{timestamp}.json`
- Each file contains:
  - Metadata (timestamp, type)
  - Original content
  - Analysis results
  - Generated improvements
  - Style variations

## Features

- AI-powered text generation
- Writing style suggestions
- Grammar and spelling corrections
- Content improvement recommendations

## Requirements

- Python 3.x
- OpenAI API key
- Required packages:
  - openai>=1.0.0
  - python-dotenv>=0.19.0 
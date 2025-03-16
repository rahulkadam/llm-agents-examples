# Writing Assistant Agent

An AI-powered writing assistant system that helps improve your writing through iterative analysis and refinement. The system maintains context of your text throughout the session and allows for step-by-step improvements.

## Features

### 1. Contextual Analysis
- Instant analysis of tone, style, and readability
- Optional detailed analysis
- Maintains context throughout your session

### 2. Interactive Improvements
- Suggests specific improvements
- Preview changes before applying
- Keeps track of all modifications

### 3. Research Assistant
- Analyzes your text for related topics
- Provides relevant information
- Helps expand your content

### 4. Version Control
- Undo previous changes
- Track modification history
- See how your text evolves

## How It Works

The assistant maintains your current text throughout the session and offers different ways to improve it:

1. **Analysis**: Understand your text's current state
2. **Improve**: Get and apply specific improvements
3. **Research**: Find related information to enhance your content
4. **Undo**: Revert to previous versions if needed

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

3. Create a `.env` file with your API key:
```
OPENAI_API_KEY=your_api_key_here
```

## Usage

1. Run the assistant:
```bash
python main.py
```

2. Enter your text when prompted

3. Choose from available operations:
   - Analyze your text
   - Get improvement suggestions
   - Research related topics
   - Undo changes

4. The system will maintain your text context throughout the session, allowing you to:
   - See your current text at all times
   - Apply improvements iteratively
   - Undo changes if needed
   - Research related topics based on your content

## Requirements

- Python 3.x
- OpenAI API key
- Required packages:
  - openai>=1.0.0
  - python-dotenv>=0.19.0 
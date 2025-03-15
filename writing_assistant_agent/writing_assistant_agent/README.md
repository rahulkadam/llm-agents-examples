# Writing Assistant Agent

A Python-based AI writing assistant that leverages OpenAI's GPT models to help improve and analyze writing. This agent can help with text analysis, writing improvements, and topic research.

## Features

1. **Text Analysis**
   - Analyze tone (formal, informal, etc.)
   - Evaluate writing style
   - Assess readability
   - Get improvement suggestions

2. **Writing Improvement**
   - Grammar and clarity suggestions
   - Sentence structure optimization
   - Word choice recommendations
   - Overall flow improvements

3. **Topic Research**
   - Get key information about topics
   - Identify main points and concepts
   - Find useful examples
   - Discover potential writing angles

## Installation

1. Clone the repository:
```bash
git clone [your-repo-url]
cd writing_assistant_agent
```

2. Create and activate a virtual environment:
```bash
python -m venv venv
source venv/bin/activate  # On Windows: venv\Scripts\activate
```

3. Install dependencies:
```bash
pip install -r requirements.txt
```

4. Create a `.env` file in the project root:
```env
OPENAI_API_KEY=your_api_key_here
OPENAI_API_BASE_URL=your_custom_url_here  # Optional
```

## Usage

```python
from agent import WritingAssistant

# Initialize the assistant
assistant = WritingAssistant()

# Analyze text
text = "Your text here..."
analysis = assistant.analyze_text(text)
print(analysis['analysis'])

# Get writing improvements
improvements = assistant.improve_writing(text)
print(improvements['suggestions'])

# Research a topic
topic = "Artificial Intelligence"
research = assistant.research_topic(topic)
print(research['research'])
```

## Requirements

- Python 3.8+
- OpenAI API key
- Dependencies listed in requirements.txt:
  - openai>=1.0.0
  - python-dotenv

## Configuration

The agent can be configured using environment variables:

- `OPENAI_API_KEY` (Required): Your OpenAI API key
- `OPENAI_API_BASE_URL` (Optional): Custom base URL for API requests

## Error Handling

The agent includes basic error handling for:
- API authentication issues
- Rate limiting
- Invalid input validation

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## License

MIT License - See LICENSE file for details 
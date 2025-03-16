# WritingAssistantAgent.py
import os
from dotenv import load_dotenv
from openai import OpenAI

class WritingAssistant:
    def __init__(self):
        # Load environment variables
        load_dotenv()
        
        # Initialize OpenAI client with custom base URL if provided
        base_url = os.getenv("OPENAI_API_BASE_URL")
        self.client = OpenAI(
            api_key=os.getenv("OPENAI_API_KEY"),
            base_url=base_url if base_url else None
        )
        
        # Define agent capabilities
        self.capabilities = {
            "analyze_text": "Analyze text for tone, style, and readability",
            "improve_writing": "Suggest improvements for writing",
            "research_topic": "Find relevant information for a topic"
        }

    def analyze_text(self, text: str) -> dict:
        """Analyze the given text for tone, style, and readability."""
        prompt = f"""
        Please analyze the following text and provide insights about:
        1. Tone (formal, informal, etc.)
        2. Style (descriptive, technical, etc.)
        3. Readability (easy, moderate, difficult)
        4. Suggestions for improvement

        Text: {text}
        """
        
        response = self.client.chat.completions.create(
            model="gpt-3.5-turbo",
            messages=[
                {"role": "system", "content": "You are a writing analysis expert."},
                {"role": "user", "content": prompt}
            ]
        )
        
        return {
            "analysis": response.choices[0].message.content,
            "tokens_used": response.usage.total_tokens
        }

    def improve_writing(self, text: str) -> dict:
        """Improve the given text, considering any context provided."""
        response = self.client.chat.completions.create(
            model="gpt-3.5-turbo",
            messages=[
                {"role": "system", "content": "You are a writing assistant that helps improve sentences while maintaining context and flow. Provide clear, concise improvements."},
                {"role": "user", "content": text}
            ]
        )
        
        return {
            "suggestions": response.choices[0].message.content,
            "tokens_used": response.usage.total_tokens
        }

    def research_topic(self, text: str) -> dict:
        """Research and provide information about a topic."""
        prompt = f"""
        Please provide key information about the following topic:
        1. Main points
        2. Key concepts
        3. Useful examples
        4. Potential writing angles

        Topic: {text}
        """
        
        response = self.client.chat.completions.create(
            model="gpt-3.5-turbo",
            messages=[
                {"role": "system", "content": "You are a research assistant."},
                {"role": "user", "content": prompt}
            ]
        )
        
        return {
            "research": response.choices[0].message.content,
            "tokens_used": response.usage.total_tokens
        }

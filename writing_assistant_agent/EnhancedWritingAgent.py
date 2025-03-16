# EnhancedWritingAgent.py
import os
from dotenv import load_dotenv
from openai import OpenAI
from typing import Dict, List, Any

class EnhancedWritingAgent:
    def __init__(self):
        load_dotenv()
        self.client = OpenAI(api_key=os.getenv("OPENAI_API_KEY"))
        
        self.capabilities = {
            "deep_analysis": "Perform in-depth content analysis",
            "style_transfer": "Adapt text to different writing styles",
            "audience_targeting": "Optimize content for specific audiences",
            "content_expansion": "Expand brief content into detailed text",
            "content_summarization": "Create concise summaries of longer text"
        }

    def deep_analysis(self, text: str) -> dict:
        """Provide enhanced analysis and improvements."""
        prompt = f"""
        Based on the previous analysis and context, provide enhanced improvements focusing on:
        1. Clarity and impact
        2. Natural flow with context
        3. Specific word choices
        4. Overall effectiveness

        Input:
        {text}
        """

        response = self.client.chat.completions.create(
            model="gpt-3.5-turbo",
            messages=[
                {"role": "system", "content": "You are an advanced writing enhancement agent that provides deep, thoughtful improvements while maintaining the author's intent."},
                {"role": "user", "content": prompt}
            ]
        )

        return {
            "analysis": response.choices[0].message.content,
            "tokens_used": response.usage.total_tokens
        }

    def style_transfer(self, text: str, target_style: str) -> Dict[str, Any]:
        """Adapt text to a specific writing style."""
        prompt = f"""
        Please rewrite the following text in {target_style} style while maintaining the core message:
        
        Text: {text}
        
        Consider:
        1. Vocabulary and tone appropriate for {target_style}
        2. Typical sentence structures
        3. Common phrases and expressions
        4. Overall flow and organization
        """
        
        response = self.client.chat.completions.create(
            model="gpt-4",
            messages=[
                {"role": "system", "content": f"You are an expert in {target_style} writing style."},
                {"role": "user", "content": prompt}
            ]
        )
        
        return {
            "adapted_text": response.choices[0].message.content,
            "tokens_used": response.usage.total_tokens
        }

    def audience_targeting(self, text: str, target_audience: str) -> Dict[str, Any]:
        """Optimize content for a specific audience."""
        prompt = f"""
        Adapt the following text for {target_audience}:
        
        Text: {text}
        
        Consider:
        1. Language complexity and vocabulary
        2. Cultural references and context
        3. Examples and analogies
        4. Tone and formality
        5. Background knowledge assumptions
        """
        
        response = self.client.chat.completions.create(
            model="gpt-4",
            messages=[
                {"role": "system", "content": "You are an expert in audience-focused content adaptation."},
                {"role": "user", "content": prompt}
            ]
        )
        
        return {
            "adapted_text": response.choices[0].message.content,
            "tokens_used": response.usage.total_tokens
        } 
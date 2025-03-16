# ContextAwareAgent.py
import json
import os
from typing import Dict, List, Any
from openai import OpenAI
from dotenv import load_dotenv
from ContextStorageAgent import ContextStorageAgent

class ContextAwareAgent:
    def __init__(self, storage_dir: str = "context_storage"):
        load_dotenv()
        self.client = OpenAI(api_key=os.getenv("OPENAI_API_KEY"))
        self.context_storage = ContextStorageAgent(storage_dir)
        
    def _load_context(self, filename: str) -> Dict[str, Any]:
        """Load context from a file."""
        filepath = os.path.join(self.context_storage.storage_dir, filename)
        with open(filepath, 'r', encoding='utf-8') as f:
            return json.load(f)
            
    def get_relevant_contexts(self, query: str, max_contexts: int = 3) -> List[Dict[str, Any]]:
        """Find relevant contexts based on the query."""
        contexts = []
        for filename in self.context_storage.list_contexts():
            context = self._load_context(filename)
            # Calculate relevance score using GPT
            prompt = f"""
            Rate the relevance of this context to the query on a scale of 0-10:
            
            Query: {query}
            
            Context: {json.dumps(context['data'])}
            
            Return only the number (0-10).
            """
            
            response = self.client.chat.completions.create(
                model="gpt-3.5-turbo",
                messages=[
                    {"role": "system", "content": "You are a context relevance scorer. Respond only with a number 0-10."},
                    {"role": "user", "content": prompt}
                ]
            )
            
            score = float(response.choices[0].message.content.strip())
            contexts.append((score, context))
            
        # Sort by relevance score and return top contexts
        contexts.sort(reverse=True, key=lambda x: x[0])
        return [context for score, context in contexts[:max_contexts]]
    
    def generate_with_context(self, prompt: str, context_type: str = None) -> Dict[str, Any]:
        """Generate content using relevant historical context."""
        relevant_contexts = self.get_relevant_contexts(prompt)
        
        context_prompt = "Previous relevant contexts:\n"
        for ctx in relevant_contexts:
            context_prompt += f"- {json.dumps(ctx['data'])}\n"
            
        full_prompt = f"""
        Using the following historical contexts and the current prompt, generate an appropriate response.
        
        {context_prompt}
        
        Current prompt: {prompt}
        
        Please provide a response that incorporates relevant information from the historical contexts while addressing the current prompt.
        """
        
        response = self.client.chat.completions.create(
            model="gpt-4",
            messages=[
                {"role": "system", "content": "You are an AI assistant with access to historical context. Use this context to provide more informed and consistent responses."},
                {"role": "user", "content": full_prompt}
            ]
        )
        
        result = {
            "response": response.choices[0].message.content,
            "tokens_used": response.usage.total_tokens,
            "contexts_used": len(relevant_contexts)
        }
        
        # Store this interaction as new context
        self.context_storage.store_context({
            "prompt": prompt,
            "response": result["response"],
            "contexts_used": [ctx["metadata"] for ctx in relevant_contexts]
        })
        
        return result 

    def process_with_context(self, prompt: str, conversation_history: str) -> dict:
        """Process a prompt with conversation history."""
        analysis_prompt = f"""
        Previous conversation:
        {conversation_history}

        New prompt:
        {prompt}

        Please analyze:
        1. How this prompt relates to the previous conversation
        2. Key themes and patterns in the conversation
        3. Suggestions for maintaining consistency
        4. Potential improvements for better flow

        Provide a concise analysis that will help improve the response while maintaining context.
        """

        response = self.client.chat.completions.create(
            model="gpt-3.5-turbo",
            messages=[
                {"role": "system", "content": "You are a context-aware writing assistant that helps maintain conversation flow and coherence. Focus on the relationship between the current prompt and previous conversation."},
                {"role": "user", "content": analysis_prompt}
            ]
        )

        return {
            "context_analysis": response.choices[0].message.content,
            "tokens_used": response.usage.total_tokens
        } 
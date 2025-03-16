# main.py
from WritingAssistantAgent import WritingAssistant
from EnhancedWritingAgent import EnhancedWritingAgent
from ContextAwareAgent import ContextAwareAgent
import json
import os
from datetime import datetime

class WritingSession:
    def __init__(self):
        # Initialize all agents
        self.basic_agent = WritingAssistant()
        self.enhanced_agent = EnhancedWritingAgent()
        self.context_agent = ContextAwareAgent()
        self.history = []
        self.load_history()  # Load history when starting

    def load_history(self):
        """Load conversation history from JSON files"""
        if not os.path.exists("context_history"):
            return

        # Get all JSON files and sort by timestamp
        files = []
        for f in os.listdir("context_history"):
            if f.endswith(".json"):
                timestamp = f.split("_")[1].split(".")[0]  # Extract timestamp from filename
                files.append((timestamp, f))
        
        # Sort files by timestamp
        files.sort(key=lambda x: x[0])
        
        # Load each file into history
        for _, filename in files:
            try:
                with open(os.path.join("context_history", filename), 'r') as f:
                    entry = json.load(f)
                    self.history.append(entry)
            except Exception as e:
                print(f"Error loading {filename}: {e}")

        print(f"Loaded {len(self.history)} previous conversations from storage.")

    def add_to_history(self, entry):
        """Add an entry to history with both prompt and result"""
        self.history.append(entry)
        self.save_to_storage(entry)

    def save_to_storage(self, entry):
        """Save history entry to storage"""
        if not os.path.exists("context_history"):
            os.makedirs("context_history")
        
        timestamp = datetime.now().strftime("%Y%m%d_%H%M%S")
        filename = f"context_history/entry_{timestamp}.json"
        
        with open(filename, 'w') as f:
            json.dump(entry, f, indent=2)

    def get_conversation_history(self):
        """Get formatted conversation history for context"""
        if not self.history:
            return "No previous context available."
        
        history_text = "Previous conversation:\n"
        for entry in self.history[-3:]:  # Last 3 interactions
            history_text += f"\nPrompt: {entry['prompt']}\n"
            history_text += f"Response: {entry['response']}\n"
            if 'improvements' in entry:
                history_text += f"Improvements: {entry['improvements']}\n"
        return history_text

    def process_with_agents(self, prompt: str) -> dict:
        """Process prompt through all agents in sequence"""
        # Get conversation history
        conversation_history = self.get_conversation_history()

        # 1. Context agent processes with full history
        context_result = self.context_agent.process_with_context(
            prompt,
            conversation_history
        )

        # 2. Basic agent analyzes with context
        basic_result = self.basic_agent.improve_writing(
            f"""
Previous conversation:
{conversation_history}

Current prompt:
{prompt}

Context analysis:
{context_result['context_analysis']}

Please provide improvements while maintaining consistency with previous conversation.
"""
        )

        # 3. Enhanced agent provides final improvements
        enhanced_result = self.enhanced_agent.deep_analysis(
            f"""
Conversation history:
{conversation_history}

Current prompt:
{prompt}

Context analysis:
{context_result['context_analysis']}

Basic improvements:
{basic_result['suggestions']}

Please provide enhanced improvements while maintaining conversation flow.
"""
        )

        # Store the complete interaction
        self.add_to_history({
            "prompt": prompt,
            "context_analysis": context_result['context_analysis'],
            "improvements": basic_result['suggestions'],
            "response": enhanced_result['analysis'],
            "timestamp": datetime.now().isoformat()
        })

        return {
            "context_analysis": context_result['context_analysis'],
            "basic_improvements": basic_result['suggestions'],
            "enhanced_analysis": enhanced_result['analysis']
        }

def main():
    session = WritingSession()

    while True:
        print("\n" + "="*50)
        if session.history:
            print("\nPrevious conversation:")
            for entry in session.history[-3:]:
                print(f"\nYou: {entry['prompt']}")
                print(f"Assistant: {entry['response']}")
        
        print("\nOptions:")
        print("1. Enter prompt")
        print("2. Quit")
        
        choice = input("\nChoice: ")
        
        if choice == "2":
            print("Goodbye!")
            break
            
        if choice == "1":
            prompt = input("\nEnter your prompt: ")
            
            print("\nProcessing with conversation history...")
            results = session.process_with_agents(prompt)
            
            print("\nContext Analysis:")
            print("-" * 50)
            print(results['context_analysis'])
            
            print("\nSuggested Improvements:")
            print("-" * 50)
            print(results['basic_improvements'])
            
            print("\nEnhanced Response:")
            print("-" * 50)
            print(results['enhanced_analysis'])
            
        else:
            print("Invalid choice. Please try again.")

if __name__ == "__main__":
    main()

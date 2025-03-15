# main.py
from agent import WritingAssistant

def main():
    # Initialize the agent
    assistant = WritingAssistant()
    
    print("Welcome to the Writing Assistant!")
    print("\nAvailable commands:")
    print("1. analyze - Analyze text")
    print("2. improve - Get writing improvements")
    print("3. research - Research a topic")
    print("4. quit - Exit the program")
    
    while True:
        command = input("\nEnter command: ").lower()
        
        if command == "quit":
            break
            
        elif command == "analyze":
            text = input("Enter text to analyze: ")
            result = assistant.analyze_text(text)
            print("\nAnalysis:")
            print(result["analysis"])
            print(f"\nTokens used: {result['tokens_used']}")
            
        elif command == "improve":
            text = input("Enter text to improve: ")
            result = assistant.improve_writing(text)
            print("\nSuggestions:")
            print(result["suggestions"])
            print(f"\nTokens used: {result['tokens_used']}")
            
        elif command == "research":
            topic = input("Enter topic to research: ")
            result = assistant.research_topic(topic)
            print("\nResearch:")
            print(result["research"])
            print(f"\nTokens used: {result['tokens_used']}")
            
        else:
            print("Unknown command. Please try again.")

if __name__ == "__main__":
    main()

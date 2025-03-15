# main.py
from WritingAssistantAgent import WritingAssistant
from EnhancedWritingAgent import EnhancedWritingAgent
from ContextAwareAgent import ContextAwareAgent

def main():
    # Initialize all agents
    basic_agent = WritingAssistant()
    enhanced_agent = EnhancedWritingAgent()
    context_agent = ContextAwareAgent()

    while True:
        print("\nWriting Assistant Menu:")
        print("1. Basic Analysis")
        print("2. Enhanced Analysis")
        print("3. Style Transfer")
        print("4. Context-Aware Generation")
        print("5. Complete Writing Workflow (Chained)")
        print("6. Research and Enhance (Chained)")
        print("7. Quit")
        
        choice = input("\nEnter your choice (1-7): ")
        
        if choice == "7":
            print("Goodbye!")
            break
            
        if choice in ["1", "2", "3"]:
            text = input("\nEnter the text you want to analyze/modify: ")
            
            if choice == "1":
                print("\nBasic Analysis")
                print("-" * 50)
                result = basic_agent.analyze_text(text)
                print(result["analysis"])
                
            elif choice == "2":
                print("\nEnhanced Analysis")
                print("-" * 50)
                result = enhanced_agent.deep_analysis(text)
                print(result["analysis"])
                
            elif choice == "3":
                style = input("Enter target style (e.g., casual, formal, technical): ")
                print(f"\nStyle Transfer (to {style})")
                print("-" * 50)
                result = enhanced_agent.style_transfer(text, style)
                print(result["adapted_text"])
                
        elif choice == "4":
            query = input("\nEnter your question or topic: ")
            print("\nContext-Aware Generation")
            print("-" * 50)
            result = context_agent.generate_with_context(query)
            print(result["response"])
            print(f"Number of contexts used: {result['contexts_used']}")

        elif choice == "5":
            # Complete Writing Workflow: Analysis -> Improvement -> Style Transfer
            text = input("\nEnter your text: ")
            target_style = input("Enter desired style (e.g., casual, formal, technical): ")
            
            print("\n1. Basic Analysis")
            print("-" * 50)
            basic_result = basic_agent.analyze_text(text)
            print(basic_result["analysis"])
            
            print("\n2. Enhanced Analysis and Suggestions")
            print("-" * 50)
            enhanced_result = enhanced_agent.deep_analysis(text)
            print(enhanced_result["analysis"])
            
            print("\n3. Improving Writing")
            print("-" * 50)
            improved_result = basic_agent.improve_writing(text)
            print(improved_result["suggestions"])
            
            print("\n4. Style Transfer")
            print("-" * 50)
            style_result = enhanced_agent.style_transfer(text, target_style)
            print(style_result["adapted_text"])
            
            # Store the workflow results in context
            context_agent.context_storage.store_context({
                "original_text": text,
                "analysis": basic_result["analysis"],
                "enhanced_analysis": enhanced_result["analysis"],
                "improvements": improved_result["suggestions"],
                "style_transfer": style_result["adapted_text"]
            }, "complete_workflow")

        elif choice == "6":
            # Research and Enhance: Research -> Context -> Enhanced Writing
            topic = input("\nEnter the topic you want to research and write about: ")
            
            print("\n1. Initial Research")
            print("-" * 50)
            research_result = basic_agent.research_topic(topic)
            print(research_result["research"])
            
            print("\n2. Context-Aware Analysis")
            print("-" * 50)
            context_result = context_agent.generate_with_context(topic)
            print(context_result["response"])
            
            # Combine research and context for enhanced writing
            combined_text = f"""
            Research findings:
            {research_result['research']}
            
            Additional context:
            {context_result['response']}
            """
            
            print("\n3. Enhanced Analysis of Combined Information")
            print("-" * 50)
            enhanced_result = enhanced_agent.deep_analysis(combined_text)
            print(enhanced_result["analysis"])
            
            # Store the research workflow in context
            context_agent.context_storage.store_context({
                "topic": topic,
                "research": research_result["research"],
                "context_analysis": context_result["response"],
                "enhanced_analysis": enhanced_result["analysis"]
            }, "research_workflow")
            
        else:
            print("Invalid choice. Please try again.")
        
        input("\nPress Enter to continue...")

if __name__ == "__main__":
    main()

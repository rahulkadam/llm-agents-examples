# ContextStorageAgent.py
import json
import os
from datetime import datetime
from typing import Dict, List, Any

class ContextStorageAgent:
    def __init__(self, storage_dir: str = "context_storage"):
        self.storage_dir = storage_dir
        self._ensure_storage_directory()
        
    def _ensure_storage_directory(self):
        """Create storage directory if it doesn't exist."""
        if not os.path.exists(self.storage_dir):
            os.makedirs(self.storage_dir)
            
    def _generate_context_filename(self, context_type: str) -> str:
        """Generate a unique filename for storing context."""
        timestamp = datetime.now().strftime("%Y%m%d_%H%M%S")
        return f"{context_type}_{timestamp}.json"
    
    def store_context(self, context_data: Dict[str, Any], context_type: str = "general") -> str:
        """Store context data in a JSON file."""
        filename = self._generate_context_filename(context_type)
        filepath = os.path.join(self.storage_dir, filename)
        
        # Add metadata to context
        context_with_metadata = {
            "metadata": {
                "timestamp": datetime.now().isoformat(),
                "type": context_type
            },
            "data": context_data
        }
        
        with open(filepath, 'w', encoding='utf-8') as f:
            json.dump(context_with_metadata, f, indent=2)
            
        return filepath
    
    def list_contexts(self, context_type: str = None) -> List[str]:
        """List all available context files, optionally filtered by type."""
        all_files = os.listdir(self.storage_dir)
        if context_type:
            return [f for f in all_files if f.startswith(f"{context_type}_")]
        return all_files
    
    def delete_context(self, filename: str) -> bool:
        """Delete a specific context file."""
        filepath = os.path.join(self.storage_dir, filename)
        if os.path.exists(filepath):
            os.remove(filepath)
            return True
        return False
    
    def clear_old_contexts(self, days_old: int = 30) -> int:
        """Remove context files older than specified days."""
        current_time = datetime.now()
        deleted_count = 0
        
        for filename in os.listdir(self.storage_dir):
            filepath = os.path.join(self.storage_dir, filename)
            file_time = datetime.fromtimestamp(os.path.getctime(filepath))
            
            if (current_time - file_time).days > days_old:
                os.remove(filepath)
                deleted_count += 1
                
        return deleted_count 
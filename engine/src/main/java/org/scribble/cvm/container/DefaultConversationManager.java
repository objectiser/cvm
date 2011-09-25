/*
 * Copyright 2009-11 www.scribble.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package org.scribble.cvm.container;

import org.scribble.cvm.runtime.Conversation;
import org.scribble.cvm.runtime.ConversationId;
import org.scribble.cvm.runtime.DefaultConversation;
import org.scribble.cvm.runtime.Participant;

public class DefaultConversationManager implements ConversationManager {

	private java.util.Map<Key, Conversation> _conversations=new java.util.HashMap<Key, Conversation>();
	
	@Override
	public Conversation createConversation(ConversationId cid,
					Participant participant, java.util.Map<String,Object> vars) {
		Conversation ret=new DefaultConversation(participant,
								cid, vars);
		
		_conversations.put(new Key(cid, participant), ret);
		
		return(ret);
	}
	
	@Override
	public Conversation getConversation(ConversationId cid, Participant participant) {
		return(_conversations.get(new Key(cid, participant)));
	}

	@Override
	public void removeConversation(ConversationId cid, Participant participant) {
		_conversations.remove(new Key(cid, participant));
	}

	public class Key {
		private ConversationId _cid=null;
		private Participant _participant=null;
		
		public Key(ConversationId cid, Participant participant) {
			_cid = cid;
			_participant = participant;
		}
		
		public boolean equals(Object obj) {
			boolean ret=false;
			
			if (obj instanceof Key) {
				Key key=(Key)obj;
				
				ret = key._cid.equals(_cid) &&
						key._participant.equals(_participant);
			}
			
			return (ret);
		}
		
		public int hashCode() {
			return(_cid.hashCode());
		}
	}
}

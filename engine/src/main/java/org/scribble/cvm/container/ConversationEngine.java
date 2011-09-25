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

import org.scribble.cvm.runtime.ConversationId;
import org.scribble.cvm.runtime.ConversationType;
import org.scribble.cvm.runtime.Message;

public interface ConversationEngine {

	public void setPrincipal(java.security.Principal principal);
	
	public java.security.Principal getPrincipal();
	
	public void setConversationTypes(java.util.Set<ConversationType> conversationTypes);
	
	public java.util.Set<ConversationType> getConversationTypes();
	
	public void setMessagingLayer(MessagingLayer messaging);
	
	public void setConversationManager(ConversationManager conversationManager);
	
	public void setParticipantRegistry(ParticipantRegistry registry);
	
	public void setListener(ConversationEngineListener l);
	
	public void createConversation(ConversationId cid, ConversationType ctype,
						java.util.Map<String,Object> vars);
	
	public boolean process(Message mesg);
	
}

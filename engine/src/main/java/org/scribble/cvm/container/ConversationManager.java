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
import org.scribble.cvm.runtime.Conversation;
import org.scribble.cvm.runtime.Participant;

public interface ConversationManager {

	/**
	 * This method creates a session associated with the supplied
	 * conversation id, type and participant.
	 * 
	 * @param cid The conversation id
	 * @param participant The participant
	 * @param vars The optional initial variables
	 * @return The conversation
	 */
	public Conversation createConversation(ConversationId cid,
					Participant participant, java.util.Map<String,Object> vars);
	
	/**
	 * This method retrieves a session associated with the supplied
	 * conversation id and participant.
	 * 
	 * @param cid The conversation id
	 * @param participant The participant
	 * @return The conversation
	 */
	public Conversation getConversation(ConversationId cid,
								Participant participant);
	
	/**
	 * This method indicates that the session associated with the
	 * supplied conversation id and participant has completed.
	 * 
	 * @param cid The conversation id
	 * @param participant The participant
	 */
	public void removeConversation(ConversationId cid,
								Participant participant);
	
}

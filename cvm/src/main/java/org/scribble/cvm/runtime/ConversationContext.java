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
package org.scribble.cvm.runtime;

/**
 * This interface provides capabilities required during the execution of
 * a conversation instance.
 * 
 * @author gbrown
 *
 */
public interface ConversationContext {

	/**
	 * This method sends a message to a remote participant.
	 * 
	 * @param conversation The conversation
	 * @param mesg The message
	 */
	public void send(Conversation conversation, Message mesg);
	
	/**
	 * This method returns the local participant associated with the specified
	 * role. This variant of the method is intended to locate the participant defined
	 * within a single global conversation type as the calling participant.
	 * 
	 * @param role The role
	 * @return The participant
	 */
	public Participant findParticipant(String role);
	
	/**
	 * This method returns a participant implementing the specified abstract
	 * role. The properties can be used to help filter the specific participant
	 * from a set of potential candidates. If multiple participants still
	 * match, then the context will be responsible for determining which
	 * participant instance should be returned.
	 * 
	 * @param role The role
	 * @param props The properties used to filter the potential participant
	 * @return The participant, or null if a suitable candidate is not found
	 */
	public Participant findParticipant(String role, java.util.Properties props);
	
}

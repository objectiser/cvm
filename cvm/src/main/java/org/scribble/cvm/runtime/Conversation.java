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

public interface Conversation {
	
	public ConversationId getConversationId();
	
	public Participant getLocalParticipant();

	public Participant getRemoteParticipant(String role);

	public void addRemoteParticipant(String role, Participant p);

	public void removeRemoteParticipant(String role);

	public void register(EventHandler handler);
	
	//public void unregister(EventHandler handler);
	
	public java.util.List<EventHandler> getEventHandlers();
	
	public <T> T getVariable(Class<T> cls, String name);
	
}

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

public class DefaultConversation implements Conversation {
	
	private Participant _localParticipant=null;
	private ConversationId _conversationId=null;
	private java.util.Map<String,Participant> _remoteParticipants=
			new java.util.HashMap<String,Participant>();
	private java.util.Map<String,Object> _variables=
			new java.util.HashMap<String,Object>();
	private java.util.List<EventHandler> _handlers=
					new java.util.Vector<EventHandler>();

	public DefaultConversation(Participant participant, ConversationId cid,
						java.util.Map<String,Object> vars) {
		_localParticipant = participant;
		_conversationId = cid;
		if (vars != null) {
			_variables = vars;
		}
	}
	
	public Participant getLocalParticipant() {
		return(_localParticipant);
	}
	
	@Override
	public Participant getRemoteParticipant(String role) {
		return (_remoteParticipants.get(role));
	}

	@Override
	public void addRemoteParticipant(String role, Participant p) {
		_remoteParticipants.put(role, p);
	}
	
	@Override
	public void removeRemoteParticipant(String role) {
		_remoteParticipants.remove(role);
	}
	
	public ConversationId getConversationId() {
		return(_conversationId);
	}
	
	public void register(EventHandler handler) {
		_handlers.add(handler);
	}
	
	public void unregister(EventHandler handler) {
		_handlers.remove(handler);
	}
	
	public java.util.List<EventHandler> getEventHandlers() {
		return(_handlers);
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getVariable(Class<T> cls, String name) {
		return((T)_variables.get(name));
	}

	public String toString() {
		return("Conversation["+getConversationId()+"/"+getLocalParticipant()+"]");
	}
}

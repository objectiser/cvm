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

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.scribble.cvm.runtime.Conversation;
import org.scribble.cvm.runtime.ConversationContext;
import org.scribble.cvm.runtime.ConversationId;
import org.scribble.cvm.runtime.ConversationType;
import org.scribble.cvm.runtime.DefaultConversation;
import org.scribble.cvm.runtime.DefaultParticipant;
import org.scribble.cvm.runtime.EventHandler;
import org.scribble.cvm.runtime.Message;
import org.scribble.cvm.runtime.Participant;

public class DefaultConversationEngine implements ConversationEngine {

	private static final Logger LOG=Logger.getLogger(DefaultConversationEngine.class.getName());
	
	private java.security.Principal _principal=null;
	private java.util.Set<ConversationType> _conversationTypes=null;
	private MessagingLayer _messaging=null;
	private ConversationManager _conversationManager=null;
	private ParticipantRegistry _participantRegistry=null;
	private ConversationEngineListener _listener=null;
	private ConversationContext _context=new ConversationEngineContext();
	
	public DefaultConversationEngine() {
	}

	public void setPrincipal(java.security.Principal principal) {
		_principal = principal;
	}
	
	public java.security.Principal getPrincipal() {
		return (_principal);
	}
	
	public void setConversationTypes(java.util.Set<ConversationType> conversationTypes) {
		_conversationTypes = conversationTypes;
	}
	
	public java.util.Set<ConversationType> getConversationTypes() {
		return (_conversationTypes);
	}

	public void setMessagingLayer(MessagingLayer messaging) {
		_messaging = messaging;
	}

	public void setConversationManager(ConversationManager conversationManager) {
		_conversationManager = conversationManager;
	}
	
	public void setParticipantRegistry(ParticipantRegistry registry) {
		_participantRegistry = registry;
	}
	
	public void setListener(ConversationEngineListener l) {
		_listener = l;
	}
	
	public void createConversation(ConversationId cid, ConversationType ctype,
			java.util.Map<String,Object> vars) {
		
		if (!_conversationTypes.contains(ctype)) {
			throw new IllegalArgumentException("Conversation type not registered with engine");
		}
		
		Conversation conversation=_conversationManager.createConversation(cid,
						new DefaultParticipant(ctype.getRole(), getPrincipal()), vars);

		if (_listener != null) {
			_listener.started(conversation);
		}
		
		ctype.initialize(_context, conversation);
	}
	
	public boolean process(Message mesg) {
		boolean handled=false;
		Conversation conversation=null;
		
		// Get session
		conversation = _conversationManager.getConversation(mesg.getConversationId(),
							mesg.getDestination());
		
		/*
		for (ConversationType ctype : _conversationTypes) {
			conversation = _conversationManager.getConversation(mesg.getConversationId(),
										ctype, _principal);
			
			if (conversation != null) {
				break;
			}
		}
		*/
		
		if (conversation == null) {
			// Check if any conversation type can be used to create a session
			for (ConversationType ctype : _conversationTypes) {
				if (ctype.getRole().equals(mesg.getDestination().getRole())) {
					if (ctype.canInitiate(mesg)) {
						conversation = _conversationManager.createConversation(mesg.getConversationId(),
								mesg.getDestination(), null);
						
						if (_listener != null) {
							_listener.started(conversation);
						}
						
						ctype.initialize(_context, conversation);
					}
					break;
				}
			}
		}
		
		if (conversation != null) {
			
			for (EventHandler eh : conversation.getEventHandlers()) {
				if (eh.canHandle(mesg)) {
					((DefaultConversation)conversation).unregister(eh);
					
					if (_listener != null) {
						_listener.received(conversation, mesg);
					}

					eh.onMessage(_context, conversation, mesg);
					
					handled = true;
					
					if (conversation.getEventHandlers().size() == 0
									&& _listener != null) {
						_listener.finished(conversation);
					}
					break;
				}
			}
		} else if (LOG.isLoggable(Level.FINE)) {
			LOG.fine("Message '"+mesg+"' was not handled");
		}
		
		return(handled);
	}
	
	public class ConversationEngineContext implements ConversationContext {

		public void send(Conversation conversation, Message mesg) {
			if (_listener != null) {
				_listener.sending(conversation, mesg);
			}
			_messaging.send(mesg);
		}

		public Participant findParticipant(String role, Properties props) {
			return (_participantRegistry.findParticipant(role, props));
		}

		public Participant findParticipant(String role) {
			return (new DefaultParticipant(role, _principal));
		}
		
	}
}

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

import java.util.logging.Logger;

import org.scribble.cvm.runtime.ConversationType;
import org.scribble.cvm.runtime.DefaultParticipant;
import org.scribble.cvm.runtime.Message;
import org.scribble.cvm.runtime.Participant;

public class DefaultMessagingLayer implements MessagingLayer {

	private static final Logger LOG=Logger.getLogger(DefaultMessagingLayer.class.getName());
	
	//private boolean _asyncMode=false;
	private java.util.Map<Participant,ConversationEngine> _conversationEngines=
					new java.util.HashMap<Participant, ConversationEngine>();
	
	public void init(ConversationEngine ce) {
		for (ConversationType ctype : ce.getConversationTypes()) {
			_conversationEngines.put(new DefaultParticipant(ctype.getRole(), ce.getPrincipal()), ce);
		}
	}
	
	public void send(Message mesg) {
		
		ConversationEngine ce=_conversationEngines.get(mesg.getDestination());
		
		if (ce != null) {
			// TODO: Enable config to determine if dispatch asychronously
			
			boolean result=ce.process(mesg);
			
			if (!result) {
				LOG.severe("Failed to process message: "+mesg);
			}
		} else {
			LOG.severe("Unable to find implementation for destination: "+mesg.getDestination());
		}
	}
	
}

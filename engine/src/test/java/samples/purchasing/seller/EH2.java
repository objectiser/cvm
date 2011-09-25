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
package samples.purchasing.seller;

import org.scribble.cvm.runtime.ConversationContext;
import org.scribble.cvm.runtime.DefaultMessage;
import org.scribble.cvm.runtime.EventHandler;
import org.scribble.cvm.runtime.Message;
import org.scribble.cvm.runtime.Conversation;
import org.scribble.cvm.runtime.Participant;

import samples.purchasing.Confirmation;
import samples.purchasing.CreditOk;
import samples.purchasing.InsufficientCredit;
import samples.purchasing.Rejected;

public class EH2 implements EventHandler {

	private static EH2 INSTANCE=new EH2();
	
	public static EH2 getInstance() {
		return(INSTANCE);
	}

	public boolean canHandle(Message mesg) {
		return (mesg.getContent() instanceof CreditOk
				|| mesg.getContent() instanceof InsufficientCredit);
	}
	
	public void onMessage(ConversationContext context, Conversation conversation, Message mesg) {
		Participant buyer=conversation.getRemoteParticipant("purchasing.Buyer");
		
		if (mesg.getContent() instanceof CreditOk) {
			
			// Return out of stock
			Message resp=new DefaultMessage(conversation.getConversationId(), conversation.getLocalParticipant(),
								buyer, new Confirmation());
			
			context.send(conversation, resp);
			
		} else if (mesg.getContent() instanceof InsufficientCredit) {
			
			// Return rejectedk
			Message resp=new DefaultMessage(conversation.getConversationId(), conversation.getLocalParticipant(),
					buyer, new Rejected());
			
			context.send(conversation, resp);
		}
	}

}

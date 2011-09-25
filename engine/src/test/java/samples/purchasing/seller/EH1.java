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

import samples.purchasing.CreditCheck;
import samples.purchasing.Order;
import samples.purchasing.OutOfStock;

public class EH1 implements EventHandler {

	private static EH1 INSTANCE=new EH1();
	
	public static EH1 getInstance() {
		return(INSTANCE);
	}

	@Override
	public boolean canHandle(Message mesg) {
		return (mesg.getContent() instanceof Order);
	}
	
	@Override
	public void onMessage(ConversationContext context, Conversation conversation, Message mesg) {
		
		// Store message source as buyer role
		conversation.addRemoteParticipant("purchasing.Buyer", mesg.getSource());
		
		Order order=(Order)mesg.getContent();
		
		if (order.getProduct().equals("car")) {
			
			// Return out of stock
			Message resp=new DefaultMessage(conversation.getConversationId(), conversation.getLocalParticipant(),
								mesg.getSource(), new OutOfStock());
			
			context.send(conversation, resp);
			
		} else {
			
			conversation.register(EH2.getInstance());
			
			// Introduces
			Participant creditAgency=context.findParticipant("purchasing.CreditAgency",
									new java.util.Properties());
			
			// Return out of stock
			Message resp=new DefaultMessage(conversation.getConversationId(), conversation.getLocalParticipant(),
								creditAgency, new CreditCheck(order.getPrice()));
			
			context.send(conversation, resp);
		}
	}

}

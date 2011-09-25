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
package samples.purchasing.buyer;

import org.scribble.cvm.runtime.Conversation;
import org.scribble.cvm.runtime.ConversationContext;
import org.scribble.cvm.runtime.ConversationType;
import org.scribble.cvm.runtime.DefaultMessage;
import org.scribble.cvm.runtime.Message;
import org.scribble.cvm.runtime.Participant;

import samples.purchasing.Order;

public class PurchasingBuyer implements ConversationType {
	
	public static final PurchasingBuyer INSTANCE=new PurchasingBuyer();
	
	private PurchasingBuyer() {
	}
	
	public String getRole() {
		return ("purchasing.Buyer");
	}

	public boolean canInitiate(Message mesg) {
		return(false);
	}
	
	public void initialize(ConversationContext context, Conversation conversation) {
		
		Participant seller=context.findParticipant("purchasing.Seller",
							new java.util.Properties());
		
		Order order=new Order();
		order.setPrice(conversation.getVariable(Integer.class, "price"));
		order.setProduct(conversation.getVariable(String.class, "product"));
		
		Message mesg=new DefaultMessage(conversation.getConversationId(),
					conversation.getLocalParticipant(), seller, order);
		
		conversation.register(EH1.getInstance());
		
		context.send(conversation, mesg);
	}

	public int hashCode() {
		return (getRole().hashCode());
	}

	public boolean equals(Object obj) {
		boolean ret=false;
		
		if (obj instanceof ConversationType) {
			ret = ((ConversationType)obj).getRole().equals(getRole());
		}
		
		return (ret);
	}
}

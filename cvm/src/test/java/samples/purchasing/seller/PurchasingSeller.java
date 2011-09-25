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

import org.scribble.cvm.runtime.Conversation;
import org.scribble.cvm.runtime.ConversationContext;
import org.scribble.cvm.runtime.ConversationType;
import org.scribble.cvm.runtime.Message;

import samples.purchasing.Order;

public class PurchasingSeller implements ConversationType {
	
	public static final PurchasingSeller INSTANCE=new PurchasingSeller();
	
	private PurchasingSeller() {
	}
	
	public String getRole() {
		return ("purchasing.Seller");
	}

	public boolean canInitiate(Message mesg) {
		return(mesg.getContent() instanceof Order);
	}
	
	public void initialize(ConversationContext context, Conversation conversation) {
		conversation.register(EH1.getInstance());
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

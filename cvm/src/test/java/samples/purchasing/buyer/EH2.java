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

import org.scribble.cvm.runtime.ConversationContext;
import org.scribble.cvm.runtime.EventHandler;
import org.scribble.cvm.runtime.Message;
import org.scribble.cvm.runtime.Conversation;

import samples.purchasing.Rejected;

public class EH2 implements EventHandler {

	private static EH2 INSTANCE=new EH2();
	
	public static EH2 getInstance() {
		return(INSTANCE);
	}

	public boolean canHandle(Message mesg) {
		return (mesg.getContent() instanceof Rejected);
	}
	
	public void onMessage(ConversationContext context, Conversation session, Message mesg) {
		System.out.println("RECEIVED REJECTED");
	}

}

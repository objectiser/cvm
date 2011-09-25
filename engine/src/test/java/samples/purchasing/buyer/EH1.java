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

public class EH1 implements EventHandler {

	private static EH1 INSTANCE=new EH1();
	
	public static EH1 getInstance() {
		return(INSTANCE);
	}

	@Override
	public boolean canHandle(Message mesg) {
		return (EH2.getInstance().canHandle(mesg)
				|| EH3.getInstance().canHandle(mesg)
				|| EH4.getInstance().canHandle(mesg));
	}
	
	@Override
	public void onMessage(ConversationContext context, Conversation session, Message mesg) {
		
		// How to handle non-directed choice - so we don't necessarily know
		// which path to take without 'testing' them?
		// Could have other handlers for each path and then iterate through them
		// until one handled, but what if activities prior to the choice?
		// or prior to the message entry - possibly need validation to
		// protect against including activities prior to the message receiver.
		if (EH2.getInstance().canHandle(mesg)) {
			EH2.getInstance().onMessage(context, session, mesg);
		} else if (EH3.getInstance().canHandle(mesg)) {
			EH3.getInstance().onMessage(context, session, mesg);
		} else if (EH4.getInstance().canHandle(mesg)) {
			EH4.getInstance().onMessage(context, session, mesg);
		}
	}

}

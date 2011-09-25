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

public class DefaultMessage implements Message {
	
	private ConversationId _conversationId=null;
	private Participant _from=null;
	private Participant _to=null;
	private String _type=null;
	private Object _content=null;

	public DefaultMessage(ConversationId cid, Participant from, Participant to,
			Object content) {
		this(cid, from, to, content.getClass().getName(), content);
	}
	
	public DefaultMessage(ConversationId cid, Participant from, Participant to,
					String type, Object content) {
		_conversationId = cid;
		_from = from;
		_to = to;
		_type = type;
		_content = content;
	}
	
	public ConversationId getConversationId() {
		return(_conversationId);
	}
	
	public Participant getSource() {
		return(_from);
	}
	
	public Participant getDestination() {
		return(_to);
	}
	
	public String getType() {
		return(_type);
	}
	
	public Object getContent() {
		return(_content);
	}
	
	public String toString() {
		return("[type="+getType()+" from="+getSource()+" to="+getDestination()+" cid="+getConversationId()+"]");
	}
}

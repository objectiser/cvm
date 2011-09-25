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

import org.scribble.cvm.runtime.Conversation;
import org.scribble.cvm.runtime.Message;

public class TestListener implements ConversationEngineListener {
	
	private String _name=null;
	private int _startedCount=0;
	private int _receivedCount=0;
	private int _sendingCount=0;
	private int _finishedCount=0;
	
	public TestListener(String name) {
		_name = name;
	}
	
	public void started(Conversation conversation) {
		System.out.println("STARTED("+_name+"): "+conversation);
		_startedCount++;
	}
	
	public int getStartedCount() {
		return (_startedCount);
	}

	public void received(Conversation conversation, Message mesg) {
		System.out.println("RECEIVED("+_name+"): "+conversation+" mesg="+mesg);
		_receivedCount++;
	}
	
	public int getReceivedCount() {
		return (_receivedCount);
	}

	public void sending(Conversation conversation, Message mesg) {
		System.out.println("SENT("+_name+"): "+conversation+" mesg="+mesg);
		_sendingCount++;
	}
	
	public int getSendingCount() {
		return (_sendingCount);
	}

	public void finished(Conversation conversation) {
		System.out.println("FINISHED("+_name+"): "+conversation);
		_finishedCount++;
	}
	
	public int getFinishedCount() {
		return (_finishedCount);
	}

}

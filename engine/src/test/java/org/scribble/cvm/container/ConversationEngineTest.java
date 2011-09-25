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

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import javax.security.auth.kerberos.KerberosPrincipal;

import org.junit.Test;
import org.scribble.cvm.runtime.ConversationId;
import org.scribble.cvm.runtime.ConversationType;
import org.scribble.cvm.runtime.DefaultConversationId;
import org.scribble.cvm.runtime.DefaultParticipant;

import samples.purchasing.buyer.PurchasingBuyer;
import samples.purchasing.creditAgency.PurchasingCreditAgency;
import samples.purchasing.seller.PurchasingSeller;

public class ConversationEngineTest {
	
	@Test
	public void testOutOfStock() {
		// Conversation manager must be sharable, as could be specific to
		// conversation engine, but may also be based on a persistent
		// central repository
		ConversationManager cm=new DefaultConversationManager();
		DefaultParticipantRegistry pr=new DefaultParticipantRegistry();
		DefaultMessagingLayer ml=new DefaultMessagingLayer();
		
		java.security.Principal gary=new KerberosPrincipal("gary@scribble.org");
		
		pr.addParticipant(new DefaultParticipant(PurchasingSeller.INSTANCE.getRole(), gary));
		pr.addParticipant(new DefaultParticipant(PurchasingCreditAgency.INSTANCE.getRole(), gary));
				
		DefaultConversationEngine ce1=new DefaultConversationEngine();
		
		Set<ConversationType> cts1=new HashSet<ConversationType>();
		cts1.add(PurchasingBuyer.INSTANCE);
		cts1.add(PurchasingSeller.INSTANCE);
		ce1.setConversationTypes(cts1);
		
		ce1.setPrincipal(gary);
		ce1.setConversationManager(cm);
		ce1.setParticipantRegistry(pr);
		ce1.setMessagingLayer(ml);
		
		ml.init(ce1);
		
		TestListener tl1=new TestListener("ce1");
		
		ce1.setListener(tl1);

		DefaultConversationEngine ce2=new DefaultConversationEngine();
		
		Set<ConversationType> cts2=new HashSet<ConversationType>();
		cts2.add(PurchasingCreditAgency.INSTANCE);
		ce2.setConversationTypes(cts2);
		
		ce2.setPrincipal(gary);		
		ce2.setConversationManager(cm);
		ce2.setParticipantRegistry(pr);
		ce2.setMessagingLayer(ml);

		ml.init(ce2);
		
		TestListener tl2=new TestListener("ce2");
		
		ce2.setListener(tl2);

		ConversationId cid=new DefaultConversationId("1");
		
		java.util.Map<String, Object> vars=new java.util.HashMap<String, Object>();
		vars.put("product", "car");
		vars.put("price", 150);
		 
		ce1.createConversation(cid, PurchasingBuyer.INSTANCE, vars);
		
		if (tl1.getStartedCount() != 2) {
			fail("Start count should be 2: "+tl1.getStartedCount());
		}
		
		if (tl1.getSendingCount() != 2) {
			fail("Sending count should be 2: "+tl1.getSendingCount());
		}
		
		if (tl1.getReceivedCount() != 2) {
			fail("Received count should be 2: "+tl1.getReceivedCount());
		}
		
		if (tl1.getFinishedCount() != 2) {
			fail("Finished count should be 2: "+tl1.getFinishedCount());
		}
		
		if (tl2.getStartedCount() != 0) {
			fail("Start count should be 0: "+tl2.getStartedCount());
		}
		
		if (tl2.getSendingCount() != 0) {
			fail("Sending count should be 0: "+tl2.getSendingCount());
		}
		
		if (tl2.getReceivedCount() != 0) {
			fail("Received count should be 0: "+tl2.getReceivedCount());
		}
		
		if (tl2.getFinishedCount() != 0) {
			fail("Finished count should be 0: "+tl2.getFinishedCount());
		}
	}

	@Test
	@org.junit.Ignore
	public void testOrderConfirmed() {
		// Conversation manager must be sharable, as could be specific to
		// conversation engine, but may also be based on a persistent
		// central repository
		ConversationManager cm=new DefaultConversationManager();
		DefaultParticipantRegistry pr=new DefaultParticipantRegistry();
		DefaultMessagingLayer ml=new DefaultMessagingLayer();
		
		java.security.Principal gary=new KerberosPrincipal("gary@scribble.org");
		
		pr.addParticipant(new DefaultParticipant(PurchasingSeller.INSTANCE.getRole(), gary));
		pr.addParticipant(new DefaultParticipant(PurchasingCreditAgency.INSTANCE.getRole(), gary));
				
		DefaultConversationEngine ce1=new DefaultConversationEngine();
		
		Set<ConversationType> cts1=new HashSet<ConversationType>();
		cts1.add(PurchasingBuyer.INSTANCE);
		cts1.add(PurchasingSeller.INSTANCE);
		ce1.setConversationTypes(cts1);
		
		ce1.setPrincipal(gary);
		ce1.setConversationManager(cm);
		ce1.setParticipantRegistry(pr);
		ce1.setMessagingLayer(ml);
		
		ml.init(ce1);
		
		TestListener tl1=new TestListener("ce1");
		
		ce1.setListener(tl1);

		DefaultConversationEngine ce2=new DefaultConversationEngine();
		
		Set<ConversationType> cts2=new HashSet<ConversationType>();
		cts2.add(PurchasingCreditAgency.INSTANCE);
		ce2.setConversationTypes(cts2);
		
		ce2.setPrincipal(gary);		
		ce2.setConversationManager(cm);
		ce2.setParticipantRegistry(pr);
		ce2.setMessagingLayer(ml);

		ml.init(ce2);
		
		TestListener tl2=new TestListener("ce2");
		
		ce2.setListener(tl2);

		ConversationId cid=new DefaultConversationId("1");
		
		java.util.Map<String, Object> vars=new java.util.HashMap<String, Object>();
		vars.put("product", "bike");
		vars.put("price", 50);
		 
		ce1.createConversation(cid, PurchasingBuyer.INSTANCE, vars);
		
		if (tl1.getStartedCount() != 2) {
			fail("Start count should be 2: "+tl1.getStartedCount());
		}
		
		if (tl1.getSendingCount() != 3) {
			fail("Sending count should be 3: "+tl1.getSendingCount());
		}
		
		if (tl1.getReceivedCount() != 3) {
			fail("Received count should be 3: "+tl1.getReceivedCount());
		}
		
		if (tl1.getFinishedCount() != 2) {
			fail("Finished count should be 2: "+tl1.getFinishedCount());
		}
		
		if (tl2.getStartedCount() != 1) {
			fail("Start count should be 1: "+tl2.getStartedCount());
		}
		
		if (tl2.getSendingCount() != 1) {
			fail("Sending count should be 1: "+tl2.getSendingCount());
		}
		
		if (tl2.getReceivedCount() != 1) {
			fail("Received count should be 1: "+tl2.getReceivedCount());
		}
		
		if (tl2.getFinishedCount() != 1) {
			fail("Finished count should be 1: "+tl2.getFinishedCount());
		}
	}

}

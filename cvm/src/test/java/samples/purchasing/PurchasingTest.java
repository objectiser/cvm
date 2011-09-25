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
package samples.purchasing;

import static org.junit.Assert.*;

import java.util.Properties;

import org.junit.Test;
import org.scribble.cvm.runtime.Conversation;
import org.scribble.cvm.runtime.ConversationContext;
import org.scribble.cvm.runtime.DefaultConversationId;
import org.scribble.cvm.runtime.DefaultMessage;
import org.scribble.cvm.runtime.DefaultParticipant;
import org.scribble.cvm.runtime.DefaultConversation;
import org.scribble.cvm.runtime.Message;
import org.scribble.cvm.runtime.Participant;

import samples.purchasing.buyer.PurchasingBuyer;
import samples.purchasing.creditAgency.PurchasingCreditAgency;
import samples.purchasing.seller.PurchasingSeller;

public class PurchasingTest {

	@Test
	public void testBuyerOrderConfirmation() {
		
		TestContext context=new TestContext();
		
		java.util.Map<String, Object> vars=new java.util.HashMap<String, Object>();
		vars.put("product", "bike");
		vars.put("price", 50);
		
		DefaultConversation conversation=new DefaultConversation(new DefaultParticipant("purchasing.Buyer"),
				new DefaultConversationId("1"), vars);
		
		PurchasingBuyer.INSTANCE.initialize(context, conversation);
		
		if (context.getSentMessages().size() != 1) {
			fail("Should only be 1 sent message: "+context.getSentMessages().size());
		}
		
		if ((context.getSentMessages().get(0).getContent() instanceof Order) == false) {
			fail("Message should be an Order: "+context.getSentMessages().get(0));
		}
		
		if (conversation.getEventHandlers().size() != 1) {
			fail("Should be one event handler: "+conversation.getEventHandlers().size());
		}
		
		DefaultMessage mesg=new DefaultMessage(new DefaultConversationId("1"),
						new DefaultParticipant("Seller"),
						new DefaultParticipant("Buyer"), new Confirmation());
		
		conversation.getEventHandlers().get(0).onMessage(context, conversation, mesg);
		conversation.unregister(conversation.getEventHandlers().get(0));
		
		if (conversation.getEventHandlers().size() != 0) {
			fail("Should be zero event handlers: "+conversation.getEventHandlers().size());
		}
		
	}

	@Test
	public void testBuyerOrderOutOfStock() {
		
		TestContext context=new TestContext();
		
		java.util.Map<String, Object> vars=new java.util.HashMap<String, Object>();
		vars.put("product", "car");
		vars.put("price", 50);
		
		DefaultConversation conversation=new DefaultConversation(new DefaultParticipant("purchasing.Buyer"),
				new DefaultConversationId("1"), vars);
		
		PurchasingBuyer.INSTANCE.initialize(context, conversation);
		
		if (context.getSentMessages().size() != 1) {
			fail("Should only be 1 sent message: "+context.getSentMessages().size());
		}
		
		if ((context.getSentMessages().get(0).getContent() instanceof Order) == false) {
			fail("Message should be an Order: "+context.getSentMessages().get(0));
		}
		
		if (conversation.getEventHandlers().size() != 1) {
			fail("Should be one event handler: "+conversation.getEventHandlers().size());
		}
		
		DefaultMessage mesg=new DefaultMessage(new DefaultConversationId("1"),
						new DefaultParticipant("Seller"),
						new DefaultParticipant("Buyer"), new OutOfStock());
		
		conversation.getEventHandlers().get(0).onMessage(context, conversation, mesg);		
		conversation.unregister(conversation.getEventHandlers().get(0));
		
		if (conversation.getEventHandlers().size() != 0) {
			fail("Should be zero event handlers: "+conversation.getEventHandlers().size());
		}
		
	}

	@Test
	public void testSellerOrderCCCOkConfirmation() {
		
		TestContext context=new TestContext();
		
		DefaultConversation conversation=new DefaultConversation(new DefaultParticipant("purchasing.Seller"),
					new DefaultConversationId("1"), null);
		
		PurchasingSeller.INSTANCE.initialize(context, conversation);
		
		if (conversation.getEventHandlers().size() != 1) {
			fail("Should be one event handler: "+conversation.getEventHandlers().size());
		}
		
		Order order=new Order();
		order.setProduct("bike");
		order.setPrice(50);
		
		Participant me=new DefaultParticipant("BuyerP");
		
		DefaultMessage mesg=new DefaultMessage(new DefaultConversationId("1"),
				me, new DefaultParticipant("SellerP"), order);

		conversation.getEventHandlers().get(0).onMessage(context, conversation, mesg);
		conversation.unregister(conversation.getEventHandlers().get(0));

		if (context.getSentMessages().size() != 1) {
			fail("Should only be 1 sent message: "+context.getSentMessages().size());
		}
		
		if ((context.getSentMessages().get(0).getContent() instanceof CreditCheck) == false) {
			fail("Message should be a CreditCheck: "+context.getSentMessages().get(0));
		}
		
		if (conversation.getEventHandlers().size() != 1) {
			fail("Should be one event handler: "+conversation.getEventHandlers().size());
		}
		
		DefaultMessage mesg2=new DefaultMessage(new DefaultConversationId("1"),
				new DefaultParticipant("BuyerP"),
				new DefaultParticipant("SellerP"), new CreditOk());

		conversation.getEventHandlers().get(0).onMessage(context, conversation, mesg2);
		conversation.unregister(conversation.getEventHandlers().get(0));

		if (context.getSentMessages().size() != 2) {
			fail("Should only be 2 sent message: "+context.getSentMessages().size());
		}
	
		if (context.getSentMessages().get(1).getDestination() != me) {
			fail("Destination is not my participant");
		}

		if ((context.getSentMessages().get(1).getContent() instanceof Confirmation) == false) {
			fail("Message should be a Confirmation: "+context.getSentMessages().get(1));
		}
		
		if (conversation.getEventHandlers().size() != 0) {
			fail("Should be zero event handlers: "+conversation.getEventHandlers().size());
		}
		
	}

	@Test
	public void testSellerOrderCCInsufficientReject() {
		
		TestContext context=new TestContext();
		
		DefaultConversation conversation=new DefaultConversation(new DefaultParticipant("purchasing.Seller"),
				new DefaultConversationId("1"), null);

		PurchasingSeller.INSTANCE.initialize(context, conversation);
		
		if (conversation.getEventHandlers().size() != 1) {
			fail("Should be one event handler: "+conversation.getEventHandlers().size());
		}
		
		Order order=new Order();
		order.setProduct("bike");
		order.setPrice(150);
		
		Participant me=new DefaultParticipant("BuyerP");
		
		DefaultMessage mesg=new DefaultMessage(new DefaultConversationId("1"),
				me, new DefaultParticipant("SellerP"), order);

		conversation.getEventHandlers().get(0).onMessage(context, conversation, mesg);
		conversation.unregister(conversation.getEventHandlers().get(0));

		if (context.getSentMessages().size() != 1) {
			fail("Should only be 1 sent message: "+context.getSentMessages().size());
		}
		
		if ((context.getSentMessages().get(0).getContent() instanceof CreditCheck) == false) {
			fail("Message should be a CreditCheck: "+context.getSentMessages().get(0));
		}
		
		if (conversation.getEventHandlers().size() != 1) {
			fail("Should be one event handler: "+conversation.getEventHandlers().size());
		}
		
		DefaultMessage mesg2=new DefaultMessage(new DefaultConversationId("1"),
				new DefaultParticipant("BuyerP"),
				new DefaultParticipant("SellerP"), new InsufficientCredit());

		conversation.getEventHandlers().get(0).onMessage(context, conversation, mesg2);
		conversation.unregister(conversation.getEventHandlers().get(0));

		if (context.getSentMessages().size() != 2) {
			fail("Should only be 2 sent message: "+context.getSentMessages().size());
		}
		
		if (context.getSentMessages().get(1).getDestination() != me) {
			fail("Destination is not my participant");
		}
		
		if ((context.getSentMessages().get(1).getContent() instanceof Rejected) == false) {
			fail("Message should be a Rejected: "+context.getSentMessages().get(1));
		}
		
		if (conversation.getEventHandlers().size() != 0) {
			fail("Should be zero event handlers: "+conversation.getEventHandlers().size());
		}
		
	}

	@Test
	public void testSellerOrderOutOfStock() {
		
		TestContext context=new TestContext();
		
		DefaultConversation conversation=new DefaultConversation(new DefaultParticipant("purchasing.Seller"),
				new DefaultConversationId("1"), null);

		PurchasingSeller.INSTANCE.initialize(context, conversation);
		
		if (conversation.getEventHandlers().size() != 1) {
			fail("Should be one event handler: "+conversation.getEventHandlers().size());
		}
		
		Order order=new Order();
		order.setProduct("car");
		
		DefaultMessage mesg=new DefaultMessage(new DefaultConversationId("1"),
				new DefaultParticipant("BuyerP"),
				new DefaultParticipant("SellerP"), order);

		conversation.getEventHandlers().get(0).onMessage(context, conversation, mesg);
		conversation.unregister(conversation.getEventHandlers().get(0));

		if (context.getSentMessages().size() != 1) {
			fail("Should only be 1 sent message: "+context.getSentMessages().size());
		}
		
		if ((context.getSentMessages().get(0).getContent() instanceof OutOfStock) == false) {
			fail("Message should be an OutOfStock: "+context.getSentMessages().get(0));
		}
		
		if (conversation.getEventHandlers().size() != 0) {
			fail("Should be zero event handlers: "+conversation.getEventHandlers().size());
		}
		
	}

	@Test
	public void testCreditAgencyCreditCheckOk() {
		
		TestContext context=new TestContext();
		
		DefaultConversation conversation=new DefaultConversation(new DefaultParticipant("purchasing.CreditAgency"),
				new DefaultConversationId("1"), null);
		
		PurchasingCreditAgency.INSTANCE.initialize(context, conversation);
		
		if (conversation.getEventHandlers().size() != 1) {
			fail("Should be one event handler: "+conversation.getEventHandlers().size());
		}
		
		CreditCheck cc=new CreditCheck(150);
		
		Participant me=new DefaultParticipant("SellerP");
		
		DefaultMessage mesg=new DefaultMessage(new DefaultConversationId("1"),
				me, new DefaultParticipant("CreditAgencyP"), cc);

		conversation.getEventHandlers().get(0).onMessage(context, conversation, mesg);
		conversation.unregister(conversation.getEventHandlers().get(0));

		if (context.getSentMessages().size() != 1) {
			fail("Should only be 1 sent message: "+context.getSentMessages().size());
		}
		
		if ((context.getSentMessages().get(0).getContent() instanceof InsufficientCredit) == false) {
			fail("Message should be an InsufficientCredit: "+context.getSentMessages().get(0));
		}
		
		if (conversation.getEventHandlers().size() != 0) {
			fail("Should be zero event handlers: "+conversation.getEventHandlers().size());
		}
		
	}

	@Test
	public void testCreditAgencyCreditCheckInsufficient() {
		
		TestContext context=new TestContext();
		
		DefaultConversation conversation=new DefaultConversation(new DefaultParticipant("purchasing.CreditAgency"),
				new DefaultConversationId("1"), null);
		
		PurchasingCreditAgency.INSTANCE.initialize(context, conversation);
		
		if (conversation.getEventHandlers().size() != 1) {
			fail("Should be one event handler: "+conversation.getEventHandlers().size());
		}
		
		CreditCheck cc=new CreditCheck(50);
		
		Participant me=new DefaultParticipant("SellerP");
		
		DefaultMessage mesg=new DefaultMessage(new DefaultConversationId("1"),
				me, new DefaultParticipant("CreditAgencyP"), cc);

		conversation.getEventHandlers().get(0).onMessage(context, conversation, mesg);
		conversation.unregister(conversation.getEventHandlers().get(0));

		if (context.getSentMessages().size() != 1) {
			fail("Should only be 1 sent message: "+context.getSentMessages().size());
		}
		
		if ((context.getSentMessages().get(0).getContent() instanceof CreditOk) == false) {
			fail("Message should be a CreditOk: "+context.getSentMessages().get(0));
		}
		
		if (conversation.getEventHandlers().size() != 0) {
			fail("Should be zero event handlers: "+conversation.getEventHandlers().size());
		}
		
	}

	public class TestContext implements ConversationContext {

		private java.util.List<Message> _messages=new java.util.Vector<Message>();
		
		public void send(Conversation conversation, Message mesg) {
			_messages.add(mesg);
		}
		
		public java.util.List<Message> getSentMessages() {
			return(_messages);
		}
		
		public void clearSentMessages() {
			_messages.clear();
		}

		public Participant findParticipant(String name, Properties props) {
			return null;
		}

		public Participant findParticipant(String role) {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
}

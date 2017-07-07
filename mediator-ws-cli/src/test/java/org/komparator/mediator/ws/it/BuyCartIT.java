package org.komparator.mediator.ws.it;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.komparator.mediator.ws.*;
import org.komparator.supplier.ws.BadProductId_Exception;
import org.komparator.supplier.ws.BadProduct_Exception;
import org.komparator.supplier.ws.ProductView;

/**
 * Test suite
 */
public class BuyCartIT extends BaseIT {

	private static ItemIdView idv;
	private static ItemIdView idv2;
	
	// static members
	

	// one-time initialization and clean-up
	@BeforeClass
	public static void oneTimeSetUp() {
		mediatorClient.clear();
		client1.clear();
		client2.clear();
	}

	@AfterClass
	public static void oneTimeTearDown() {
	}

	// members

	// initialization and clean-up for each test
	@Before
	public void setUp() throws BadProductId_Exception, BadProduct_Exception, InvalidCartId_Exception, InvalidItemId_Exception, InvalidQuantity_Exception, NotEnoughItems_Exception {
		idv = new ItemIdView();
		idv.setProductId("X1");
		idv.setSupplierId("A50_Supplier1");
		
		ProductView product = new ProductView();
		product.setId(idv.getProductId());
		product.setDesc("Basketball");
		product.setPrice(10);
		product.setQuantity(10);
		client1.createProduct(product);
		mediatorClient.addToCart("cid", idv , 2);
		
		idv2 = new ItemIdView();
		idv2.setProductId("X1");
		idv2.setSupplierId("A50_Supplier1");
		
		product = new ProductView();
		product.setId(idv.getProductId());
		product.setDesc("Soccer Ball");
		product.setPrice(10);
		product.setQuantity(10);
		client1.createProduct(product);
		mediatorClient.addToCart("cid2", idv2 , 2);
	}

	@After
	public void tearDown() {
		
		mediatorClient.clear();
		client1.clear();
		client2.clear();
	}

	// tests
	// assertEquals(expected, actual);

	// public ShoppingResultView buyCart(String cartId, String creditCardNr) throws
	// EmptyCart_Exception, InvalidCartId_Exception, InvalidCreditCard_Exception

	// bad input tests

	@Test(expected = InvalidCartId_Exception.class)
	public void buyCartNullCartIdTest() throws EmptyCart_Exception, InvalidCartId_Exception, InvalidCreditCard_Exception  {
		mediatorClient.buyCart(null, "4024007102923926");
	}
	
	@Test(expected = InvalidCartId_Exception.class)
	public void buyCartEmptyCartIdTest() throws EmptyCart_Exception, InvalidCartId_Exception, InvalidCreditCard_Exception  {
		mediatorClient.buyCart("", "4024007102923926");
	}
	
	@Test(expected = InvalidCartId_Exception.class)
	public void buyCartWhiteSpaceCartIdTest() throws EmptyCart_Exception, InvalidCartId_Exception, InvalidCreditCard_Exception  {
		mediatorClient.buyCart(" ", "4024007102923926");
	}
	
	@Test(expected = InvalidCartId_Exception.class)
	public void buyCartTabCartIdTest() throws EmptyCart_Exception, InvalidCartId_Exception, InvalidCreditCard_Exception  {
		mediatorClient.buyCart("\t", "4024007102923926");
	}
	
	@Test(expected = InvalidCartId_Exception.class)
	public void buyCartNewLineCartIdTest() throws EmptyCart_Exception, InvalidCartId_Exception, InvalidCreditCard_Exception  {
		mediatorClient.buyCart("\n", "4024007102923926");
	}
	
	
	@Test(expected = InvalidCreditCard_Exception.class)
	public void buyCartNullCreditCardTest() throws EmptyCart_Exception, InvalidCartId_Exception, InvalidCreditCard_Exception  {
		mediatorClient.buyCart("cid", null);
	}
	
	@Test(expected = InvalidCreditCard_Exception.class)
	public void buyCartEmptyCreditCardTest() throws EmptyCart_Exception, InvalidCartId_Exception, InvalidCreditCard_Exception  {
		mediatorClient.buyCart("cid", "");
	}
	
	@Test(expected = InvalidCreditCard_Exception.class)
	public void buyCartIncompleteCreditCardTest() throws EmptyCart_Exception, InvalidCartId_Exception, InvalidCreditCard_Exception  {
		mediatorClient.buyCart("cid", "123");
	}
	
	@Test(expected = InvalidCreditCard_Exception.class)
	public void buyCartInvalidCreditCardTest() throws EmptyCart_Exception, InvalidCartId_Exception, InvalidCreditCard_Exception  {
		mediatorClient.buyCart("cid", "this is an invalid credit card number");
	}
	
	@Test
	public void buyCartValidCreditCardTest() throws EmptyCart_Exception, InvalidCartId_Exception, InvalidCreditCard_Exception  {
		mediatorClient.buyCart("cid", "4024007102923926");
	}
	
	
	// main tests
	
	@Test
	public void buyCartSuccess() throws EmptyCart_Exception, InvalidCartId_Exception, InvalidCreditCard_Exception  {
		ShoppingResultView shopped = mediatorClient.buyCart("cid", "4024007102923926");
		List<ShoppingResultView> shopHistory = mediatorClient.shopHistory();
		ShoppingResultView history = shopHistory.get(0);
		
		assertEquals(shopped.getId(), history.getId());
		assertEquals(shopped.getResult(), history.getResult());
		assertEquals(shopped.getPurchasedItems().size(), history.getPurchasedItems().size());
		assertEquals(shopped.getDroppedItems().size(), history.getDroppedItems().size());
		assertEquals(shopped.getTotalPrice(), history.getTotalPrice());
	}
	
	@Test (expected = InvalidCartId_Exception.class)
	public void buyCartInvalidItem() throws EmptyCart_Exception, InvalidCartId_Exception, InvalidCreditCard_Exception  {
		mediatorClient.buyCart("aid", "4024007102923926");
	}
	
	@Test
	public void buyCartTwoCarts() throws EmptyCart_Exception, InvalidCartId_Exception, InvalidCreditCard_Exception  {
		ShoppingResultView shopped = mediatorClient.buyCart("cid", "4024007102923926");
		ShoppingResultView shopped2 = mediatorClient.buyCart("cid2", "4024007102923926");
		List<ShoppingResultView> shopHistory = mediatorClient.shopHistory();
		
		ShoppingResultView history = shopHistory.get(1);
		assertEquals(shopped.getId(), history.getId());
		assertEquals(shopped.getResult(), history.getResult());
		assertEquals(shopped.getPurchasedItems().size(), history.getPurchasedItems().size());
		assertEquals(shopped.getDroppedItems().size(), history.getDroppedItems().size());
		assertEquals(shopped.getTotalPrice(), history.getTotalPrice());
		
		history = shopHistory.get(0);
		assertEquals(shopped2.getId(), history.getId());
		assertEquals(shopped2.getResult(), history.getResult());
		assertEquals(shopped2.getPurchasedItems().size(), history.getPurchasedItems().size());
		assertEquals(shopped2.getDroppedItems().size(), history.getDroppedItems().size());
		assertEquals(shopped2.getTotalPrice(), history.getTotalPrice());
	}
	
	
}

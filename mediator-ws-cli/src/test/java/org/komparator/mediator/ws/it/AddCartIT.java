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
public class AddCartIT extends BaseIT {
	
	private static ItemIdView idv;
	private List<CartView> cvlist;
	
	// static members

	// one-time initialization and clean-up
	@BeforeClass
	public static void oneTimeSetUp() {
		
		// clear remote service state before all tests
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
	public void setUp() throws BadProductId_Exception, BadProduct_Exception {
		idv = new ItemIdView();
		idv.setProductId("X1");
		idv.setSupplierId("A50_Supplier1");
		
		ProductView product = new ProductView();
		product.setId(idv.getProductId());
		product.setDesc("Basketball");
		product.setPrice(10);
		product.setQuantity(10);
		client1.createProduct(product);
	}

	@After
	public void tearDown() {
		mediatorClient.clear();
		client1.clear();
		client2.clear();	
	}

	// tests
	// assertEquals(expected, actual);

	// public void addToCart(String cartId, ItemIdView itemId, int itemQty) throws
	// InvalidCartId_Exception, InvalidItemId_Exception, InvalidQuantity_Exception, 
	// NotEnoughItems_Exception

	// bad input tests

	@Test(expected = InvalidCartId_Exception.class)
	public void addCartNullCartIdTest() throws InvalidCartId_Exception, InvalidItemId_Exception, InvalidQuantity_Exception, NotEnoughItems_Exception  {
		mediatorClient.addToCart(null, idv , 2);
	}
	
	@Test(expected = InvalidCartId_Exception.class)
	public void addCartEmptyCartIdTest() throws InvalidCartId_Exception, InvalidItemId_Exception, InvalidQuantity_Exception, NotEnoughItems_Exception  {
		mediatorClient.addToCart("", idv , 2);
	}
	
	
	@Test(expected = InvalidCartId_Exception.class)
	public void addCartWhiteSpaceCartIdTest() throws InvalidCartId_Exception, InvalidItemId_Exception, InvalidQuantity_Exception, NotEnoughItems_Exception  {
		mediatorClient.addToCart(" ", idv , 2);
	}
	
	@Test(expected = InvalidCartId_Exception.class)
	public void addCartTabCartIdTest() throws InvalidCartId_Exception, InvalidItemId_Exception, InvalidQuantity_Exception, NotEnoughItems_Exception  {
		mediatorClient.addToCart("\t", idv , 2);
	}
	
	@Test(expected = InvalidCartId_Exception.class)
	public void addCartNewLineCartIdTest() throws InvalidCartId_Exception, InvalidItemId_Exception, InvalidQuantity_Exception, NotEnoughItems_Exception  {
		mediatorClient.addToCart("\n", idv , 2);
	}
	
	@Test(expected = InvalidItemId_Exception.class)
	public void addCartNullItemIdTest() throws InvalidCartId_Exception, InvalidItemId_Exception, InvalidQuantity_Exception, NotEnoughItems_Exception  {
		ItemIdView idv2 = null;
		mediatorClient.addToCart("cart", idv2 , 2);
	}
	
	@Test(expected = InvalidItemId_Exception.class)
	public void addCartNullProductIdTest() throws InvalidCartId_Exception, InvalidItemId_Exception, InvalidQuantity_Exception, NotEnoughItems_Exception  {
		ItemIdView idv2 = new ItemIdView();
		idv2.setProductId(null);
		idv2.setSupplierId("A50_Supplier1");
		mediatorClient.addToCart("cart", idv2 , 2);
	}
	
	
	@Test(expected = InvalidItemId_Exception.class)
	public void addCartEmptyProductIdTest() throws InvalidCartId_Exception, InvalidItemId_Exception, InvalidQuantity_Exception, NotEnoughItems_Exception  {
		ItemIdView idv2 = new ItemIdView();
		idv2.setProductId("");
		idv2.setSupplierId("A50_Supplier1");
		mediatorClient.addToCart("cid", idv2 , 2);
	}
	
	@Test(expected = InvalidItemId_Exception.class)
	public void addCartWhiteSpaceProductIdTest() throws InvalidCartId_Exception, InvalidItemId_Exception, InvalidQuantity_Exception, NotEnoughItems_Exception  {
		ItemIdView idv2 = new ItemIdView();
		idv2.setProductId(" ");
		idv2.setSupplierId("A50_Supplier1");
		mediatorClient.addToCart("cid", idv2 , 2);
	}
	
	@Test(expected = InvalidItemId_Exception.class)
	public void addCartTabProductIdTest() throws InvalidCartId_Exception, InvalidItemId_Exception, InvalidQuantity_Exception, NotEnoughItems_Exception  {
		ItemIdView idv2 = new ItemIdView();
		idv2.setProductId("\t");
		idv2.setSupplierId("A50_Supplier1");
		mediatorClient.addToCart("cid", idv2 , 2);
	}
	
	@Test(expected = InvalidItemId_Exception.class)
	public void addCartNewLineProductIdTest() throws InvalidCartId_Exception, InvalidItemId_Exception, InvalidQuantity_Exception, NotEnoughItems_Exception  {
		ItemIdView idv2 = new ItemIdView();
		idv2.setProductId("\n");
		idv2.setSupplierId("A50_Supplier1");
		mediatorClient.addToCart("cid", idv2 , 2);
	}
	
	@Test(expected = InvalidItemId_Exception.class)
	public void addCartNullSupplierIdTest() throws InvalidCartId_Exception, InvalidItemId_Exception, InvalidQuantity_Exception, NotEnoughItems_Exception  {
		ItemIdView idv2 = new ItemIdView();
		idv2.setProductId("X1");
		idv2.setSupplierId(null);
		mediatorClient.addToCart("cid", idv2 , 2);
	}
	
	@Test(expected = InvalidItemId_Exception.class)
	public void addCartEmptySupplierIdTest() throws InvalidCartId_Exception, InvalidItemId_Exception, InvalidQuantity_Exception, NotEnoughItems_Exception  {
		ItemIdView idv2 = new ItemIdView();
		idv2.setProductId("X1");
		idv2.setSupplierId("");
		mediatorClient.addToCart("cid", idv2 , 2);
	}
	
	@Test(expected = InvalidItemId_Exception.class)
	public void addCartWhiteSpaceSupplierIdTest() throws InvalidCartId_Exception, InvalidItemId_Exception, InvalidQuantity_Exception, NotEnoughItems_Exception  {
		ItemIdView idv2 = new ItemIdView();
		idv2.setProductId("X1");
		idv2.setSupplierId(" ");
		mediatorClient.addToCart("cid", idv2 , 2);
	}
	
	@Test(expected = InvalidItemId_Exception.class)
	public void addCartTabSupplierIdTest() throws InvalidCartId_Exception, InvalidItemId_Exception, InvalidQuantity_Exception, NotEnoughItems_Exception  {
		ItemIdView idv2 = new ItemIdView();
		idv2.setProductId("X1");
		idv2.setSupplierId("\t");
		mediatorClient.addToCart("cid", idv2 , 2);
	}
	
	@Test(expected = InvalidItemId_Exception.class)
	public void addCartNewLineSupplierIdTest() throws InvalidCartId_Exception, InvalidItemId_Exception, InvalidQuantity_Exception, NotEnoughItems_Exception  {
		ItemIdView idv2 = new ItemIdView();
		idv2.setProductId("X1");
		idv2.setSupplierId("\n");
		mediatorClient.addToCart("cid", idv2 , 2);
	} 
	
	@Test(expected = InvalidQuantity_Exception.class)
	public void addCartNegativeQuantityTest() throws InvalidCartId_Exception, InvalidItemId_Exception, InvalidQuantity_Exception, NotEnoughItems_Exception  {
		mediatorClient.addToCart("cid", idv , -1);
	}
	
	@Test(expected = InvalidQuantity_Exception.class)
	public void addCartZeroQuantityTest() throws InvalidCartId_Exception, InvalidItemId_Exception, InvalidQuantity_Exception, NotEnoughItems_Exception  {
		mediatorClient.addToCart("cid", idv , 0);
	}
	
	// main tests
	
	@Test(expected = NotEnoughItems_Exception.class)
	public void addCartNotEnoughItemsTest() throws InvalidCartId_Exception, InvalidItemId_Exception, InvalidQuantity_Exception, NotEnoughItems_Exception  {
		mediatorClient.addToCart("cid", idv , 11);
	}
	
 
	@Test
	public void addCartExactAllItemsTest() throws InvalidCartId_Exception, InvalidItemId_Exception, InvalidQuantity_Exception, NotEnoughItems_Exception  {
		mediatorClient.addToCart("cid", idv , 10);
		cvlist = mediatorClient.listCarts();
		CartView cv = cvlist.get(0);
		assertEquals(cv.getCartId(), "cid");
		CartItemView civ = cv.getItems().get(0);
		assertEquals(civ.getQuantity(), 10);
	}
	
	@Test
	public void addCartExactlyOneItemTest() throws InvalidCartId_Exception, InvalidItemId_Exception, InvalidQuantity_Exception, NotEnoughItems_Exception  {
		mediatorClient.addToCart("cid", idv , 1);
		cvlist = mediatorClient.listCarts();
		CartView cv = cvlist.get(0);
		assertEquals(cv.getCartId(), "cid");
		CartItemView civ = cv.getItems().get(0);
		assertEquals(civ.getQuantity(), 1);
	}
	
	@Test
	public void addCartCartIdAlreadyExistsAddMoreItemsTest() throws InvalidCartId_Exception, InvalidItemId_Exception, InvalidQuantity_Exception, NotEnoughItems_Exception  {
		mediatorClient.addToCart("did", idv , 9);
		mediatorClient.addToCart("did", idv , 1);
		cvlist = mediatorClient.listCarts();
		CartView cv = cvlist.get(0);
		assertEquals(cv.getCartId(), "did");
		CartItemView civ = cv.getItems().get(0);
		assertEquals(civ.getQuantity(), 10);
	}
	
	@Test(expected = NotEnoughItems_Exception.class)
	public void addCartCartIdAlreadyExistsAddOneTooManyItemsTest() throws InvalidCartId_Exception, InvalidItemId_Exception, InvalidQuantity_Exception, NotEnoughItems_Exception  {
		mediatorClient.addToCart("eid", idv , 9);
		mediatorClient.addToCart("eid", idv , 2);
	}
	
	
	@Test(expected = InvalidItemId_Exception.class)
	public void addCartIvalidProductTest() throws InvalidCartId_Exception, InvalidItemId_Exception, InvalidQuantity_Exception, NotEnoughItems_Exception  {
		ItemIdView idv2 = new ItemIdView();
		idv2.setProductId("Y2");
		idv2.setSupplierId("A50_Supplier1");
		
		mediatorClient.addToCart("eid", idv2 , 2);
	} 
	
	@Test(expected = InvalidItemId_Exception.class)
	public void addCartIvalidSupplierTest() throws InvalidCartId_Exception, InvalidItemId_Exception, InvalidQuantity_Exception, NotEnoughItems_Exception  {
		ItemIdView idv2 = new ItemIdView();
		idv2.setProductId("X1");
		idv2.setSupplierId("A50_Supplier1rgre32");
		
		mediatorClient.addToCart("eid", idv2 , 2);
	}
	
	
}

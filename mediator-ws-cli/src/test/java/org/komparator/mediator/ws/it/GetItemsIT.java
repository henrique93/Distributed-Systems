package org.komparator.mediator.ws.it;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
public class GetItemsIT extends BaseIT {

	// static members
	

	// one-time initialization and clean-up
	@BeforeClass
	public static void oneTimeSetUp() {
		// clear remote service state before all tests
		mediatorClient.clear();
		client1.clear();
		client2.clear();
		client3.clear();
	}
	

	@AfterClass
	public static void oneTimeTearDown() {


	}

	// members

	// initialization and clean-up for each test
	@Before
	public void setUp() throws BadProductId_Exception, BadProduct_Exception {

		// fill-in test products
		// (since getProduct is read-only the initialization below
		// can be done once for all tests in this suite)
		{
			ProductView product = new ProductView();
			product.setId("X1");
			product.setDesc("Basketball");
			product.setPrice(10);
			product.setQuantity(10);
			client1.createProduct(product);
		}
		{
			ProductView product = new ProductView();
			product.setId("Y2");
			product.setDesc("Baseball");
			product.setPrice(20);
			product.setQuantity(20);
			client1.createProduct(product);
		}
		{
			ProductView product = new ProductView();
			product.setId("Z3");
			product.setDesc("Soccer ball");
			product.setPrice(30);
			product.setQuantity(30);
			client1.createProduct(product);
		}
		
		{
			ProductView product = new ProductView();
			product.setId("T4");
			product.setDesc("Tennis ball");
			product.setPrice(15);
			product.setQuantity(15);
			client2.createProduct(product);
		}
			
	}

	@After
	public void tearDown() {
		mediatorClient.clear();
		client1.clear();
		client2.clear();
		client3.clear();
	}

	// tests
	// assertEquals(expected, actual);

	// public List<ItemView> getItems(String productId) throws
	// InvalidItemId_Exception

	// bad input tests

	@Test(expected = InvalidItemId_Exception.class)
	public void geItemsNullTest() throws InvalidItemId_Exception {
		mediatorClient.getItems(null);
	}

	@Test(expected = InvalidItemId_Exception.class)
	public void getItemsEmptyTest() throws InvalidItemId_Exception {
		mediatorClient.getItems("");
	}

	@Test(expected = InvalidItemId_Exception.class)
	public void getItemsWhitespaceTest() throws InvalidItemId_Exception {
		mediatorClient.getItems(" ");
	}

	@Test(expected = InvalidItemId_Exception.class)
	public void getItemsTabTest() throws InvalidItemId_Exception {
		mediatorClient.getItems("\t");
	}

	@Test(expected = InvalidItemId_Exception.class)
	public void getItemsNewlineTest() throws InvalidItemId_Exception {
		mediatorClient.getItems("\n");
	} 

	// main tests
	
	@Test
	public void getItemsExistsTest() throws InvalidItemId_Exception {
		List<ItemView>  items = mediatorClient.getItems("X1");
		ItemIdView idItem = new ItemIdView();
		ItemView item = items.get(0);
		
		idItem.setProductId("X1");
		idItem.setSupplierId("A50_Supplier1");
		
		assertEquals(10, item.getPrice());
		assertEquals("Basketball", item.getDesc());
		assertEquals(idItem.getProductId() , item.getItemId().getProductId());
		assertEquals(idItem.getSupplierId() , item.getItemId().getSupplierId());
	}
	
	@Test
	public void getSingleItemExistsTest() throws InvalidItemId_Exception, BadProductId_Exception, BadProduct_Exception {
		client1.clear();
		ProductView product = new ProductView();
		product.setId("X1");
		product.setDesc("Basketball");
		product.setPrice(10);
		product.setQuantity(10);
		client1.createProduct(product);
		
		List<ItemView>  items = mediatorClient.getItems("X1");
		ItemIdView idItem = new ItemIdView();
		ItemView item = items.get(0);
		
		idItem.setProductId("X1");
		idItem.setSupplierId("A50_Supplier1");
		
		assertEquals(10, item.getPrice());
		assertEquals("Basketball", item.getDesc());
		assertEquals(idItem.getProductId() , item.getItemId().getProductId());
		assertEquals(idItem.getSupplierId() , item.getItemId().getSupplierId());
	}
	
	@Test
	public void getItemsAnotherExistsTest() throws InvalidItemId_Exception {
		List<ItemView>  items = mediatorClient.getItems("Y2");
		ItemIdView idItem = new ItemIdView();
		ItemView item = items.get(0);
		
		idItem.setProductId("Y2");
		idItem.setSupplierId("A50_Supplier1");
		
		assertEquals(20, item.getPrice());
		assertEquals("Baseball", item.getDesc());
		assertEquals(idItem.getProductId() , item.getItemId().getProductId());
		assertEquals(idItem.getSupplierId() , item.getItemId().getSupplierId());
	} 
	
	@Test
	public void getItemsYetAnotherExistsTest() throws InvalidItemId_Exception {
		List<ItemView>  items = mediatorClient.getItems("Z3");
		ItemIdView idItem = new ItemIdView();
		ItemView item = items.get(0);
		
		idItem.setProductId("Z3");
		idItem.setSupplierId("A50_Supplier1");
		
		assertEquals(30, item.getPrice());
		assertEquals("Soccer ball", item.getDesc());
		assertEquals(idItem.getProductId() , item.getItemId().getProductId());
		assertEquals(idItem.getSupplierId() , item.getItemId().getSupplierId());
	}
	
	@Test
	public void getItemsExistsAnotherServerTest() throws InvalidItemId_Exception {
		
		List<ItemView>  items = mediatorClient.getItems("T4");
		ItemIdView idItem = new ItemIdView();
		ItemView item = items.get(0);
		
		idItem.setProductId("T4");
		idItem.setSupplierId("A50_Supplier2");
		
		assertEquals(15, item.getPrice());
		assertEquals("Tennis ball", item.getDesc());
		assertEquals(idItem.getProductId() , item.getItemId().getProductId());
		assertEquals(idItem.getSupplierId() , item.getItemId().getSupplierId());
	}
	
	@Test
	public void getItemsExists2DifferentServers() throws InvalidItemId_Exception, BadProductId_Exception, BadProduct_Exception {
		ProductView product = new ProductView();
		product.setId("X1");
		product.setDesc("hewooo");
		product.setPrice(50);
		product.setQuantity(60);
		client2.createProduct(product);
		
		List<ItemView>  items = mediatorClient.getItems("X1");
		
		ItemIdView idItem0 = new ItemIdView();
		idItem0.setProductId("X1");
		idItem0.setSupplierId("A50_Supplier1");
		
		ItemIdView idItem1 = new ItemIdView();
		idItem1.setProductId("X1");
		idItem1.setSupplierId("A50_Supplier2");
		
		ItemView item0 = items.get(0);	
		ItemView item1 = items.get(1);
		
		assertEquals(idItem0.getProductId() , item0.getItemId().getProductId());
		assertEquals(idItem0.getSupplierId() , item0.getItemId().getSupplierId());
		assertEquals(10, item0.getPrice());

		assertEquals(idItem1.getProductId() , item1.getItemId().getProductId());
		assertEquals(idItem1.getSupplierId() , item1.getItemId().getSupplierId());
		assertEquals(50, item1.getPrice()); 
		
	}
	
	@Test
	public void getItemsNotExistsTest() throws InvalidItemId_Exception {
		List<ItemView> items = mediatorClient.getItems("P9");
		assertTrue(items.isEmpty());
	}
	
	@Test
	public void getItemsLowercaseNotExistsTest() throws InvalidItemId_Exception {
		List<ItemView> items = mediatorClient.getItems("x1");
		assertTrue(items.isEmpty());
	}
	
	@Test
	public void getItemsSortedTest() throws InvalidItemId_Exception , BadProductId_Exception, BadProduct_Exception {
		
			ProductView product = new ProductView();
			product.setId("T4");
			product.setDesc("Tennis ball2");
			product.setPrice(40);
			product.setQuantity(40);
			client1.createProduct(product);
			
			product = new ProductView();
			product.setId("T4");
			product.setDesc("Tennis ball3");
			product.setPrice(5);
			product.setQuantity(5);
			client3.createProduct(product);
			
			
		List<ItemView>  items = mediatorClient.getItems("T4");
		ItemView item1 = items.get(0);
		ItemView item2 = items.get(1);
		ItemView item3 = items.get(2);
		
		assertEquals(5, item1.getPrice());
		assertEquals("Tennis ball3", item1.getDesc());
		
		assertEquals(15, item2.getPrice());
		assertEquals("Tennis ball", item2.getDesc());
		
		assertEquals(40, item3.getPrice());
		assertEquals("Tennis ball2", item3.getDesc());

	}
	


}
